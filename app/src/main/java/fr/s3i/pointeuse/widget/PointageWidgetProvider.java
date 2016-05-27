package fr.s3i.pointeuse.widget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import fr.s3i.pointeuse.R;
import fr.s3i.pointeuse.persistance.DatabaseHelper;
import fr.s3i.pointeuse.service.Rafraichissement;

public class PointageWidgetProvider extends AppWidgetProvider {
    public static String ACTION_WIDGET_CONFIGURE = "ConfigureWidget";
    public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";
    public static String ACTION_START_REFRESH_WIDGET = "ACTION_START_REFRESH_WIDGET";

    String message;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Cursor dernierEnregistrement;


    private static PendingIntent pendingIntent;
    private static AlarmManager alarmManager;
    final static public int PERIODE = 60;
    Intent monService;

    private boolean pointageEnCours = false;
    int[] appWidgetIds;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        this.appWidgetIds = appWidgetIds;
        int N = appWidgetIds.length;
        //android.util.Log.w("onUpdate=true","onUpdate N=");
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        monService = new Intent(context, Rafraichissement.class);
        pendingIntent = PendingIntent.getService(context, 0, monService, 0);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, PointageWidgetProvider.class);
        intent.setAction(ACTION_START_REFRESH_WIDGET);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pi);
        //alarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, pi);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, pi);
        } else {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, pi);
        }

        // alarmManager.set(AlarmManager.RTC_WAKEUP,  Calendar.getInstance().getTimeInMillis(), pendingIntent);//Toutes les minutes
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), PERIODE*1000, pendingIntent);//Toutes les minutes
        pointageEnCours = false;

    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Prepare widget views
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widgetlayout);
        Intent active = new Intent(context, PointageWidgetProvider.class);
        active.setAction(ACTION_WIDGET_RECEIVER);

        active.putExtra("msg", "Pointage");
        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
        remoteViews.setOnClickPendingIntent(R.id.monbouttonwidget, actionPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        // v1.5 fix that doesn't call onDelete Action
        final String action = intent.getAction();
        //android.util.Log.w("action",action);

        if (alarmManager == null) {
            monService = new Intent(context, Rafraichissement.class);
            pendingIntent = PendingIntent.getService(context, 0, monService, 0);
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }

        if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
            final int appWidgetId = intent.getExtras().getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                this.onDeleted(context, new int[]{appWidgetId});
            }
        } else {
            if (intent.getAction().equals(ACTION_WIDGET_RECEIVER)) {
                if (pointageEnCours) {
                    return;
                }
                pointageEnCours = true;
                pointe(context);

                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);

                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
              /*  Notification noty = new Notification(R.drawable.icon, message,
			    		System.currentTimeMillis());
			    
			    noty.setLatestEventInfo(context, "Notice", message, contentIntent);
			    
			    notificationManager.notify(1, noty);*/

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                Notification notification = builder.setContentIntent(contentIntent)
                        .setSmallIcon(R.drawable.icon).setTicker(message).setWhen(System.currentTimeMillis())
                        .setAutoCancel(true).setContentTitle("Notice")
                        .setContentText(message).build();
                notificationManager.notify(1, notification);

                pointageEnCours = false;

                // alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtTime, pendingIntent);//Toutes les minutes
                //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), PERIODE* 1000, pendingIntent);//Toutes les minutes
               /* alarmManager.cancel(pendingIntent);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis()+1000, pendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis()+1000, pendingIntent);
                }*/
            } else if (intent.getAction().equals(ACTION_START_REFRESH_WIDGET)) {
		    	/* //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), PERIODE* 1000, pendingIntent);//Toutes les minutes
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis()+1000, pendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis()+1000, pendingIntent);
                }*/
            }
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                //e.printStackTrace();
            }
        }
    }


    public void pointe(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widgetlayout);
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        dernierEnregistrement = dbHelper.getLastEnregistrementPointage(db);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Date fin = null;
        String date1 = dateFormat.format(date);

        try {
            fin = dateFormat.parse(date1);
        } catch (ParseException e1) {
            fin = null;
            //android.util.Log.w("Exception e1", e1.getMessage());
            db.close();
            dbHelper.close();
            return;
        }

        try {
            if (dernierEnregistrement.getString(1).length() == 0) {
                dbHelper.updateEnregistrementPointage(db, dernierEnregistrement.getLong(0), dbHelper.DATE_DEBUT, dateFormat.format(date));
                dateFormat = new SimpleDateFormat("HH:mm");
                message = context.getString(R.string.debutpointage) + " " + dateFormat.format(date);
            } else {
                dbHelper.updateEnregistrementPointage(db, dernierEnregistrement.getLong(0), dbHelper.DATE_FIN, dateFormat.format(date));
                dbHelper.insereNouveauPointage(db, "", "");
                dateFormat = new SimpleDateFormat("HH:mm");
                message = context.getString(R.string.finpointage) + " " + dateFormat.format(date);
            }
        } catch (Exception e) {
            android.util.Log.w("Exception Pointage=", "message = " + e.getMessage());
//    		dbHelper.insereNouveauPointage(db, "", "");
//    		dateFormat = new SimpleDateFormat("HH:mm");
//    		message =  context.getString(R.string.debutpointage) + " " + dateFormat.format(date);
        }

        if (!dernierEnregistrement.isClosed()) {
            dernierEnregistrement.close();
        }

        db.close();
        dbHelper.close();

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        context.stopService(new Intent(context, Rafraichissement.class));
        alarmManager.cancel(pendingIntent);
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {

        context.stopService(new Intent(context, Rafraichissement.class));
        super.onDisabled(context);
    }


}
