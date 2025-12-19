package com.company.jsonlib.instrospectors;

import com.company.jsonlib.annotations.Ignore;
import com.company.jsonlib.fields.CollectionTypeFieldInfo;
import com.company.jsonlib.fields.FieldInfo;
import com.company.jsonlib.fields.ObjectTypeFieldInfo;
import com.company.jsonlib.fields.SimpleTypeFieldInfo;

import java.lang.reflect.Field;
import java.util.*;

public class DesrialisationIntrospector extends Introspector {

    public DesrialisationIntrospector(Class<?> dtoClass){
        dtoType = dtoClass;
        simpleFiels = new ArrayList<>();
        objectFields = new ArrayList<>();
        collectionFields = new ArrayList<>();
        analyseFields();
    }

    private void analyseFields() {
        Field[] fields = dtoType.getDeclaredFields();

        for (Field f : fields) {
            f.setAccessible(true);

            if (f.getAnnotation(Ignore.class) != null) continue;

            Class<?> t = f.getType();

            if (isSimpleOrString(t)) {
                simpleFiels.add(new SimpleTypeFieldInfo(f));
            } else if (isCollectionOrArray(t)) {
                collectionFields.add(new CollectionTypeFieldInfo(f));
            } else {
                objectFields.add(new ObjectTypeFieldInfo(f));
            }
        }
    }

    private boolean isCollectionOrArray(Class<?> t) {
        return t.isArray() || Collection.class.isAssignableFrom(t);
    }

    private boolean isSimpleOrString(Class<?> t) {
        if (t.isPrimitive()) return true;
        if (t == String.class) return true;

        return t == Integer.class || t == Long.class || t == Double.class || t == Float.class ||
                t == Short.class || t == Byte.class || t == Boolean.class || t == Character.class ||
                Number.class.isAssignableFrom(t);
    }

    public <T> T toDTO(String json, Class<T> dtoType) {
        try {
            T instance = dtoType.getDeclaredConstructor().newInstance();
            Map<String, String> data = parseFlatObject(json);
            applySimpleFields(instance, dtoType, data);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Deserialization failed for type " + dtoType.getName(), e);
        }
    }

    private Map<String, String> parseFlatObject(String json) {
        String s = json.trim();
        if (s.startsWith("{")) s = s.substring(1);
        if (s.endsWith("}")) s = s.substring(0, s.length() - 1);

        Map<String, String> map = new HashMap<>();
        if (s.isBlank()) return map;

        String[] pairs = s.split(",");

        for (String pair : pairs) {
            int colon = pair.indexOf(':');
            if (colon < 0) continue;

            String key = pair.substring(0, colon).trim();
            String val = pair.substring(colon + 1).trim();

            map.put(unquote(key), val);
        }

        return map;
    }

    private void applySimpleFields(Object instance, Class<?> dtoType, Map<String, String> data) throws Exception {

        Field[] fields = dtoType.getDeclaredFields();

        for (Field f : fields) {
            f.setAccessible(true);

            String key = f.getName();
            if (!data.containsKey(key)) continue;

            String rawVal = data.get(key);

            if (rawVal == null || rawVal.equals("null")) {
                if (!f.getType().isPrimitive()) {
                    f.set(instance, null);
                }
                continue;
            }

            if (f.getType() == int.class || f.getType() == Integer.class) {
                f.setInt(instance, Integer.parseInt(rawVal));
            } else if (f.getType() == String.class) {
                f.set(instance, unquote(rawVal));
            }
        }
    }

    private String unquote(String s) {
        String t = s.trim();
        if (t.startsWith("\"") && t.endsWith("\"") && t.length() >= 2) {
            t = t.substring(1, t.length() - 1);
        }
        t = t.replace("\\\"", "\"").replace("\\\\", "\\");
        return t;
    }
}
