package com.company.jsonlib.fields;

import com.company.jsonlib.instrospectors.SerialisationIntrospector;

import java.lang.reflect.Field;

public class ObjectTypeFieldInfo extends FieldInfo {

    public ObjectTypeFieldInfo(Field field) {
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
            Object nested = getField().get(instance);

            if (nested == null) {
                return "\"" + getName() + "\":null";
            }

            // On délègue à l’introspector pour sérialiser l’objet imbriqué
            SerialisationIntrospector reflector = new SerialisationIntrospector(nested);
            String nestedJson = reflector.toJson(nested);

            return "\"" + getName() + "\":" + nestedJson;

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

