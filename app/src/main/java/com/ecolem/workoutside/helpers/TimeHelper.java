package com.ecolem.workoutside.helpers;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by snabou on 05/11/2015.
 */
public class TimeHelper {

    public static String getEventShortDateStr(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR);
    }

    public static String getEventDateStr(Date date, boolean showYear) {
        String dateStr = "";

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        //dateStr  += getDayForInt(c.get(Calendar.DAY_OF_WEEK)) + " ";


        int day = c.get(Calendar.DAY_OF_MONTH);
        if (day < 10) {
            dateStr += "0";
        }
        dateStr += day;

        dateStr += " " + getMonthForInt(c.get(Calendar.MONTH));

        if (showYear)
            dateStr += " " + c.get(Calendar.YEAR);

        return dateStr;
    }

    public static String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }

    public static String getDayForInt(int num) {
        String day = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] days = dfs.getWeekdays();
        if (num >= 0 && num <= 6) {
            day = days[num];
        }
        return day;
    }

    public static String getEventHourStr(Date date) {
        String hourStr = "";

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        int hour = c.get(Calendar.HOUR_OF_DAY);

        if (hour < 10) {
            hourStr += "0";
        }

        hourStr += hour + ":";


        int min = c.get(Calendar.MINUTE);

        if (min < 10) {
            hourStr += "0";
        }

        hourStr += min;

        return hourStr;

    }

}
