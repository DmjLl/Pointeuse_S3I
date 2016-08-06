/*
 * Oburo.O est un programme destinée à saisir son temps de travail sur un support Android.
 *
 *     This file is part of Oburo.O
 *     Oburo.O is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package fr.s3i.pointeuse.domaine.pointages.services;

import fr.s3i.pointeuse.domaine.communs.services.BusService;
import fr.s3i.pointeuse.domaine.pointages.entities.Pointage;

/**
 * Created by Adrien on 31/07/2016.
 */
public class BusPointage extends BusService {

    public static abstract class PointageEvent extends BaseEvent<Pointage> {

        public PointageEvent(Object originator, Pointage data) {
            super(originator, PointageEvent.class.getName(), data);
        }

    }

    public static class PointageDemarreEvent extends PointageEvent {

        public PointageDemarreEvent(Object originator, Pointage data) {
            super(originator, data);
        }

    }

    public static class PointageTermineEvent extends PointageEvent {

        public PointageTermineEvent(Object originator, Pointage data) {
            super(originator, data);
        }

    }

    public static class PointageModifieEvent extends PointageEvent {

        public PointageModifieEvent(Object originator, Pointage data) {
            super(originator, data);
        }

    }

    public static class PointageSupprimeEvent extends PointageEvent {

        public PointageSupprimeEvent(Object originator, Pointage data) {
            super(originator, data);
        }

    }

    public static class PointageInsereEvent extends PointageEvent {

        public PointageInsereEvent(Object originator, Pointage data) {
            super(originator, data);
        }

    }

}
