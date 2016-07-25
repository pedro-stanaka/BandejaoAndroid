package br.uel.easymenu;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

public class CalendarFactory {

    public static DateTime monday() {
        return new DateTime().withDayOfWeek(DateTimeConstants.MONDAY);
    }

    public static DateTime mondayPlusDays(int numberOfDays) {
        return monday().plusDays(numberOfDays);
    }

}
