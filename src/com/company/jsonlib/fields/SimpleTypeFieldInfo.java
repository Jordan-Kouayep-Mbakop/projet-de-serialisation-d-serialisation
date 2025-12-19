package com.company.jsonlib.fields;

import java.lang.reflect.Field;

public class SimpleTypeFieldInfo extends FieldInfo {


    public SimpleTypeFieldInfo(Field field){
        super(field);
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
        try {
            Object v = getField().get(instance);

            if (v == null) {
                return "\"" + getName() + "\":null";
            }

            if (v instanceof String) {
                return "\"" + getName() + "\":\"" + escape((String) v) + "\"";
            }

            return "\"" + getName() + "\":" + v;

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

}
