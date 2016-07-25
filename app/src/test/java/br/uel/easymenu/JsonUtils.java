package br.uel.easymenu;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.io.InputStream;
import java.util.HashMap;

import br.uel.easymenu.utils.CalendarUtils;

import static br.uel.easymenu.CalendarFactory.monday;
import static br.uel.easymenu.CalendarFactory.mondayPlusDays;
import static br.uel.easymenu.utils.CalendarUtils.fromCalendarToString;

public class JsonUtils {

    public static String convertJsonToString(String fileName) throws Exception {
        InputStream is = (JsonUtils.class.getResourceAsStream("/" + fileName));
        String rawJson = null;
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        rawJson = s.hasNext() ? s.next() : "";
        is.close();

        HashMap<String, Object> scopes = new HashMap<String, Object>() {{
            put("monday", fromCalendarToString(monday()));
            put("tuesday", fromCalendarToString(mondayPlusDays(1)));
            put("wednesday", fromCalendarToString(mondayPlusDays(2)));
        }};

        Template tmpl = Mustache.compiler().compile(rawJson);
        return tmpl.execute(scopes);
    }
}
