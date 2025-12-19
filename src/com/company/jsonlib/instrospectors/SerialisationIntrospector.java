package com.company.jsonlib.instrospectors;

import com.company.jsonlib.annotations.Ignore;
import com.company.jsonlib.fields.CollectionTypeFieldInfo;
import com.company.jsonlib.fields.FieldInfo;
import com.company.jsonlib.fields.ObjectTypeFieldInfo;
import com.company.jsonlib.fields.SimpleTypeFieldInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class SerialisationIntrospector extends Introspector {

    public SerialisationIntrospector(Object instance) {
        // fill different inherited fields
        dtoType = instance.getClass();

        simpleFiels = new ArrayList<>();
        objectFields = new ArrayList<>();
        collectionFields = new ArrayList<>();

        analyseFields();
    }

    private void analyseFields() {
        Field[] fields = dtoType.getDeclaredFields();

        for (Field f : fields) {
            f.setAccessible(true);

            // 5) champs ignorés
            if (f.getAnnotation(Ignore.class) != null) {
                continue;
            }

            Class<?> t = f.getType();

            if (isSimpleOrString(t)) {
                simpleFiels.add(new SimpleTypeFieldInfo(f));      // constructor doit être public
            } else if (isCollectionOrArray(t)) {
                collectionFields.add(new CollectionTypeFieldInfo(f));
            } else {
                objectFields.add(new ObjectTypeFieldInfo(f));     // constructor doit être public
            }
        }
    }

    private boolean isCollectionOrArray(Class<?> t) {
        return t.isArray() || Collection.class.isAssignableFrom(t);
    }

    private boolean isSimpleOrString(Class<?> t) {
        if (t.isPrimitive()) return true;
        if (t == String.class) return true;

        // wrappers + nombres + bool
        return t == Integer.class || t == Long.class || t == Double.class || t == Float.class ||
                t == Short.class || t == Byte.class || t == Boolean.class || t == Character.class ||
                Number.class.isAssignableFrom(t);
    }

    public String toJson(Object instance) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        boolean first = true;

        // on sérialise dans un ordre stable
        first = appendAll(sb, simpleFiels, instance, first);
        first = appendAll(sb, objectFields, instance, first);
        first = appendAll(sb, collectionFields, instance, first);

        sb.append("}");
        return sb.toString();
    }

    private boolean appendAll(StringBuilder sb, java.util.List<FieldInfo> list, Object instance, boolean first) {
        for (FieldInfo fi : list) {
            String piece = fi.asJson(instance); // si toJson est protected: crée une méthode publique (ex: asJson)
            if (piece == null || piece.isBlank()) continue;

            if (!first) sb.append(",");
            sb.append(piece);
            first = false;
        }
        return first;
    }
}
