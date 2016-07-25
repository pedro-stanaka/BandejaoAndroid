package br.uel.easymenu.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CalendarUtils {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public static java.util.Calendar fromStringToCalendar(String calendarString) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();

        try {
            Date date = SDF.parse(calendarString);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }

    public static String fromCalendarToString(java.util.Calendar calendar) {
        return SDF.format(calendar.getTime());
    }

}
