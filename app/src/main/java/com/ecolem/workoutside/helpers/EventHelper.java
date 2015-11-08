package com.ecolem.workoutside.helpers;

import android.content.Context;

import com.ecolem.workoutside.R;

/**
 * Created by Sandra on 08/11/15.
 */
public class EventHelper {


    public static String getLevelStr(Context context, int level) {
        switch (level) {
            case 0:
                return context.getResources().getString(R.string.level_0);
            case 1:
                return context.getResources().getString(R.string.level_1);
            case 2:
                return context.getResources().getString(R.string.level_2);
            case 3:
                return context.getResources().getString(R.string.level_3);
            default:
                return "";
        }
    }


}
