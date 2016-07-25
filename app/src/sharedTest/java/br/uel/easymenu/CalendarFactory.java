package br.uel.easymenu;

import java.util.Calendar;

public class CalendarFactory {

    public static Calendar monday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar;
    }

    public static Calendar mondayPlusDays(int numberOfDays) {
        Calendar calendar = monday();
        calendar.add(Calendar.DAY_OF_MONTH, numberOfDays);
        return calendar;
    }

}
