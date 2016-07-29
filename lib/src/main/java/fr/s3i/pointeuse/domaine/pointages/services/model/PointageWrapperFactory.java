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

package fr.s3i.pointeuse.domaine.pointages.services.model;

import java.util.Collection;

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.communs.services.Service;
import fr.s3i.pointeuse.domaine.pointages.entities.Pointage;
import fr.s3i.pointeuse.domaine.pointages.services.Calculateur;
import fr.s3i.pointeuse.domaine.pointages.services.Formateur;

/**
 * Created by Adrien on 26/07/2016.
 */
public class PointageWrapperFactory implements Service {

    private final Calculateur calculateur;

    private final Formateur formateur;

    public PointageWrapperFactory(Contexte contexte) {
        calculateur = contexte.getService(Calculateur.class);
        formateur = contexte.getService(Formateur.class);
    }

    public PointageWrapper getPointageWrapper(Pointage pointage) {
        return new PointageWrapper(calculateur, formateur, pointage);
    }

    public PointageWrapperListe getPointageWrapper(Collection<Pointage> pointages) {
        return new PointageWrapperListe(this, calculateur, formateur, pointages);
    }

}