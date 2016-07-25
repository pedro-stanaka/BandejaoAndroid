package br.uel.easymenu.utils;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.io.InputStream;
import java.util.HashMap;

import br.uel.easymenu.CalendarFactory;

import static br.uel.easymenu.utils.CalendarUtils.fromCalendarToString;

public class JsonUtils {

    public static String convertJsonToString(String fileName) throws Exception {
        InputStream is = (JsonUtils.class.getResourceAsStream("/" + fileName));
        String rawJson = null;
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        rawJson = s.hasNext() ? s.next() : "";
        is.close();

        HashMap<String, Object> scopes = new HashMap<String, Object>() {{
            put("monday", fromCalendarToString(CalendarFactory.monday()));
            put("tuesday", fromCalendarToString(CalendarFactory.mondayPlusDays(1)));
            put("wednesday", fromCalendarToString(CalendarFactory.mondayPlusDays(2)));
        }};

        Template tmpl = Mustache.compiler().compile(rawJson);
        return tmpl.execute(scopes);
    }
}
