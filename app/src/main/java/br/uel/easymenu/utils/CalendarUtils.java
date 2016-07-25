package br.uel.easymenu.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class CalendarUtils {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public static java.util.Calendar fromStringToCalendar(String calendarString) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();

        try {
            SDF.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
            Date date = SDF.parse(calendarString);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }

    public static String fromCalendarToString(java.util.Calendar calendar) {
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        SDF.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
        return SDF.format(calendar.getTime());
    }

    public static String simpleLocaleFormat(Calendar calendar) {
        DateFormat dateFormatSimple = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
        dateFormatSimple.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
        return dateFormatSimple.format(calendar.getTime());
    }

    public static String dayOfWeekName(Calendar calendar) {
        DateFormat dateFormatDayOfWeek = new SimpleDateFormat("E", Locale.getDefault());
        dateFormatDayOfWeek.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
        return dateFormatDayOfWeek.format(calendar.getTime());
    }
}
