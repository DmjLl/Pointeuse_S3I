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

package fr.s3i.pointeuse.business.pointages.interactors.communs.boundaries.out.translator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.s3i.pointeuse.business.communs.Contexte;
import fr.s3i.pointeuse.business.communs.interactors.boundaries.out.model.OutTranslator;
import fr.s3i.pointeuse.business.pointages.entities.Pointage;
import fr.s3i.pointeuse.business.pointages.interactors.communs.boundaries.out.model.PointageInfo;
import fr.s3i.pointeuse.business.pointages.interactors.communs.boundaries.out.utils.Calculs;

/**
 * Created by Adrien on 19/07/2016.
 */

public class PointageInfoTranslator implements OutTranslator<Pointage, PointageInfo> {

    private final Calculs calculs;

    public PointageInfoTranslator(Contexte contexte) {
        this.calculs = contexte.getService(Calculs.class);
    }

    @Override
    public PointageInfo translate(Pointage pointage) {
        long id = pointage.getId();
        String debut = calculs.formatDate(pointage.getDebut());
        String fin = calculs.formatDate(pointage.getFin());
        String duree = calculs.formatDuree(calculs.calculDuree(pointage));
        return new PointageInfo(id, debut, fin, duree);
    }

    @Override
    public List<PointageInfo> translate(Collection<Pointage> entities) {
        List<PointageInfo> result = new ArrayList<>();
        for (Pointage entity : entities) {
            result.add(translate(entity));
        }
        return result;
    }

}