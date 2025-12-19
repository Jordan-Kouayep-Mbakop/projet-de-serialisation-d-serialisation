package com.company.jsonlib.instrospectors;

import com.company.jsonlib.fields.FieldInfo;

import java.util.ArrayList;
import java.util.List;

public class Introspector  {
    protected Class<?> dtoType;
    protected List<FieldInfo> simpleFiels;
    protected List<FieldInfo> objectFields;
    protected List<FieldInfo> collectionFields;

    private java.util.List<String> parseArrayOfObjects(String rawArray) {
        String s = rawArray.trim();
        if (s.equals("null")) return null;

        if (s.startsWith("[")) s = s.substring(1);
        if (s.endsWith("]")) s = s.substring(0, s.length() - 1);

        // chaque élément est un objet { ... } (top-level split sur virgule)
        return splitTopLevel(s, ','); // attention: ici, splitTopLevel doit voir { } donc ok
    }

    private List<String> splitTopLevel(String s, char sep) {
        List<String> parts = new ArrayList<>();
        StringBuilder cur = new StringBuilder();

        int depthObj = 0;
        boolean inString = false;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == '"' && (i == 0 || s.charAt(i - 1) != '\\')) {
                inString = !inString;
            }

            if (!inString) {
                if (c == '{') depthObj++;
                else if (c == '}') depthObj--;
            }

            if (c == sep && !inString && depthObj == 0) {
                parts.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }

        if (cur.length() > 0) parts.add(cur.toString());
        return parts;
    }
}
