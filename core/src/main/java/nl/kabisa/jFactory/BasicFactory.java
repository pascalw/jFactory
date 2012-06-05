package nl.kabisa.jFactory;

import java.util.Map;
import java.util.Random;

import static com.google.common.collect.Maps.newHashMap;

abstract class BasicFactory {

    protected Map<String, Object> defaultPropertyValues = newHashMap();
    protected Map<String, Object> defaultFieldValues = newHashMap();

    protected void field(String name, Object value) {
        defaultFieldValues.put(name, value);
    }

    protected void property(String name, Object value) {
        defaultPropertyValues.put(name, value);
    }

    protected Map<String, Object> getDefaultPropertyValues() {
        return defaultPropertyValues;
    }

    protected Map<String, Object> getDefaultFieldValues() {
        return defaultFieldValues;
    }

    protected static int rand(int max) {
        return new Random().nextInt(max);
    }
}
