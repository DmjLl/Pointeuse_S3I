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

import fr.s3i.pointeuse.domaine.pointages.entities.Pointage;
import fr.s3i.pointeuse.domaine.pointages.services.Calculateur;
import fr.s3i.pointeuse.domaine.pointages.services.Formateur;

/**
 * Created by Adrien on 26/07/2016.
 */
public class PointageWrapper {

    private final Pointage pointage;

    private final Calculateur calculateur;

    private final Formateur formateur;

    PointageWrapper(Calculateur calculateur, Formateur formateur, Pointage pointage) {
        this.calculateur = calculateur;
        this.formateur = formateur;
        this.pointage = pointage != null ? pointage : new Pointage();
    }

    public Pointage getPointage() {
        return pointage;
    }

    public Long getId() {
        return pointage.getId();
    }

    public String getDateDebut() {
        return formateur.formatDate(pointage.getDebut());
    }

    public String getDateFin() {
        return formateur.formatDate(pointage.getFin());
    }

    public String getHeureDebut() {
        return formateur.formatHeure(pointage.getDebut());
    }

    public String getHeureFin() {
        return formateur.formatHeure(pointage.getFin());
    }

    public String getDuree() {
        return formateur.formatDuree(calculateur.calculDuree(pointage));
    }

    public boolean isDemarre() {
        return pointage.getDebut() != null;
    }

    public boolean isArrete() {
        return pointage.getFin() != null;
    }

    public boolean isVide() {
        return !isDemarre() && !isArrete();
    }

    public boolean isEnCours() {
        return isDemarre() && !isArrete();
    }

    public boolean isTermine() {
        return isDemarre() && isArrete();
    }

    public boolean isValide() {
        return pointage.getErrorMessage() == null;
    }

}
