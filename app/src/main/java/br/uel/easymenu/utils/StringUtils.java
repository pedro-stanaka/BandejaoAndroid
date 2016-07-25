package br.uel.easymenu.utils;

public class StringUtils {

    public static String filterHtml(String rawString) {
        return rawString.replace("u0026", "&");
    }
}
