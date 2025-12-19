package com.company.jsonlib.fields;

import com.company.jsonlib.instrospectors.SerialisationIntrospector;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;

public class CollectionTypeFieldInfo extends FieldInfo {

    public CollectionTypeFieldInfo(Field field) {
        super(field);
    }

    Object getValue(Object instance) {
        try {
            return getField().get(instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void fillField(Object instance, Object value) {
        try {
            getField().set(instance, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String toJson(Object instance) {
        Object container = getValue(instance);

        if (container == null) {
            return "\"" + getName() + "\":null";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\"").append(getName()).append("\":[");

        boolean first = true;

        if (container.getClass().isArray()) {
            int len = Array.getLength(container);
            for (int i = 0; i < len; i++) {
                Object element = Array.get(container, i);
                if (!first) sb.append(",");
                sb.append(elementToJson(element));
                first = false;
            }
        } else if (container instanceof Collection<?> col) {
            for (Object element : col) {
                if (!first) sb.append(",");
                sb.append(elementToJson(element));
                first = false;
            }
        } else {
            // au cas où un champ a été classé collection par erreur
            throw new IllegalArgumentException("Field '" + getName() + "' is not a Collection or Array.");
        }

        sb.append("]");
        return sb.toString();
    }

    private String elementToJson(Object element) {
        if (element == null) return "null";

        if (element instanceof String s) {
            return "\"" + escape(s) + "\"";
        }

        if (element instanceof Character c) {
            return "\"" + c + "\"";
        }

        // types simples
        Class<?> t = element.getClass();
        if (isSimpleOrWrapper(t)) {
            return String.valueOf(element);
        }

        // DTO imbriqué
        SerialisationIntrospector reflector = new SerialisationIntrospector(element);
        return reflector.toJson(element);
    }

    private boolean isSimpleOrWrapper(Class<?> t) {
        if (t.isPrimitive()) return true;

        return t == Integer.class || t == Long.class || t == Double.class || t == Float.class ||
                t == Short.class || t == Byte.class || t == Boolean.class || t == Character.class ||
                Number.class.isAssignableFrom(t);
    }

    private String escape(String s) {
        return s
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
