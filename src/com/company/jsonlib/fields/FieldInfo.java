package com.company.jsonlib.fields;

import com.company.jsonlib.annotations.FieldName;
import com.company.jsonlib.annotations.Ignore;
import java.lang.reflect.Field;


public abstract class FieldInfo {

    private String name;
    private FieldName overrideAnnotation;
    private Ignore ignoredAnnotation;

    private Field field;

    public FieldInfo(Field field){
        this.field = field;
        this.field.setAccessible(true);

        this.overrideAnnotation = field.getAnnotation(FieldName.class);
        this.ignoredAnnotation  = field.getAnnotation(Ignore.class);

        // nom utilisé dans le JSON
        if (overrideAnnotation != null) {
            this.name = overrideAnnotation.override();
        } else {
            this.name = field.getName();
        }
    }

    public boolean isIgnored() {
        return ignoredAnnotation != null;
    }

    public String getName() {
        return name;
    }

    protected Field getField() {
        return field;
    }

    public final String asJson(Object instance) {
        if (isIgnored()) return "";
        return toJson(instance);
    }

    // Modifiable
    public   abstract void fillField(Object instance, Object value) ;

    protected abstract  String toJson(Object instance);


}
