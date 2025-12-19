package com.company.jsonlib;

import com.company.jsonlib.instrospectors.DesrialisationIntrospector;
import com.company.jsonlib.instrospectors.Introspector;
import com.company.jsonlib.instrospectors.SerialisationIntrospector;

import java.util.HashMap;

public class JsonTool {

    private HashMap<Class<?>, Introspector> introspectorHashMap;

    public JsonTool(){
        introspectorHashMap = new HashMap<>();
    }

    public String toJson(Object o){
        if (o == null) return "null";

        Class<?> type = o.getClass();

        Introspector existing = introspectorHashMap.get(type);
        SerialisationIntrospector si;

        if (existing instanceof SerialisationIntrospector) {
            si = (SerialisationIntrospector) existing;
        } else {
            si = new SerialisationIntrospector(o);
            introspectorHashMap.put(type, si);
        }

        return si.toJson(o);
    }

    public <T> T toDTO(String json, Class<T> dtoType) {
        DesrialisationIntrospector di = new DesrialisationIntrospector(dtoType);
        return di.toDTO(json, dtoType);
    }
}
