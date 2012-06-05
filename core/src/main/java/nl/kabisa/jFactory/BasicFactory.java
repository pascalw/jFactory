package nl.kabisa.jFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.google.common.collect.Maps.newHashMap;

abstract class BasicFactory {

    protected Map<String, Object> defaultPropertyValues = newHashMap();
    protected Map<String, Object> defaultFieldValues = newHashMap();

    protected static Map<Class, Map<String, Integer>> sequences = newHashMap();

    protected void field(String name, Object value) {
        defaultFieldValues.put(name, value);
    }

    protected void property(String name, Object value) {
        defaultPropertyValues.put(name, value);
    }

    protected void sequence(String name, Sequence seq) {
        defaultPropertyValues.put(name, seq);
    }

    protected static int rand(int max) {
        return new Random().nextInt(max);
    }

    protected Map<String, Object> getDefaultPropertyValues() {
        return defaultPropertyValues;
    }

    protected Map<String, Object> getDefaultFieldValues() {
        return defaultFieldValues;
    }

    protected int currentSequence(String name) {
        Map<String, Integer> sequencesForClass = sequences.get(getClass());
        if(sequencesForClass == null) {
            sequencesForClass = new HashMap<String, Integer>();
            sequences.put(getClass(), sequencesForClass);
        }

        Integer seq = sequencesForClass.get(name);
        if(seq == null) {
            seq = 1;
        }
        else {
            ++seq;
        }

        sequencesForClass.put(name, seq);
        return seq;
    }
}
