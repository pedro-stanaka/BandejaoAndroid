package br.uel.easymenu.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class CalendarUtils {

    public static final String CALENDAR_FORMAT = "yyyy-MM-dd";
    public static final SimpleDateFormat SDF = new SimpleDateFormat(CALENDAR_FORMAT, Locale.getDefault());

    public static DateTime fromStringToCalendar(String calendarString) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(CALENDAR_FORMAT);
        return formatter.parseDateTime(calendarString);
    }

    public static String fromCalendarToString(DateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(CALENDAR_FORMAT);
        return formatter.print(dateTime);
    }

    public static String simpleLocaleFormat(DateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormat.shortDate().withLocale(Locale.getDefault());
        return formatter.print(dateTime);
    }

    public static String dayOfWeekName(DateTime dateTime) {
        return dayOfWeekName(dateTime, Locale.getDefault());
    }

    public static String dayOfWeekName(DateTime dateTime, Locale locale) {
        return dateTime.dayOfWeek().getAsShortText(locale);
    }

    public static DateTime today() {
        return DateTime.now();
    }
}
