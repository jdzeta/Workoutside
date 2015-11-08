package com.ecolem.workoutside.helpers;

/**
 * Created by akawa_000 on 08/11/2015.
 */
public class EventHelper {

    public static String convertMinLevelToString(int level){
        switch (level) {
            case 0:
                return "Débutant";
            case 1:
                return "Intermédiaire";
            case 2:
                return "Avancé";
            case 3:
                return "Expert";
            default:
                return "Inconnu";
        }
    }
}
