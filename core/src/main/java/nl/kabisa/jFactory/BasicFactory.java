package nl.kabisa.jFactory;

import nl.kabisa.jFactory.types.Sequence;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.google.common.collect.Maps.newHashMap;

abstract class BasicFactory {

    protected Map<String, Object> propertyValues = newHashMap();
    protected Map<String, Object> fieldValues = newHashMap();

    protected static Map<Class, Map<String, Integer>> sequences = newHashMap();

    protected void field(String name, Object value) {
        fieldValues.put(name, value);
    }

    protected void property(String name, Object value) {
        propertyValues.put(name, value);
    }

    protected void sequence(String name, Sequence seq) {
        propertyValues.put(name, seq);
    }

    protected static int rand(int max) {
        return new Random().nextInt(max);
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
