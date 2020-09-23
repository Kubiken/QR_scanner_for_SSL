package com.example.qrreader.Parser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Url parser for QR scan results
 */

public class LinkParser {

    public static ArrayList<String> getLinks(String str)
    {
        ArrayList<String> result = new ArrayList<String>();

        String regex = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        while (matcher.find()) {
            result.add( str.substring(matcher.start(), matcher.end()));
        }

        return result;

    }
}
