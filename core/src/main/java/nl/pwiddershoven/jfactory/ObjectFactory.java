package nl.pwiddershoven.jfactory;

import nl.pwiddershoven.jfactory.annotations.AfterFactoryBuild;
import nl.pwiddershoven.jfactory.types.LazyValue;
import nl.pwiddershoven.jfactory.types.Sequence;
import nl.pwiddershoven.jfactory.types.Trait;
import nl.pwiddershoven.jfactory.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public abstract class ObjectFactory<T> {

    private Class<T> factoryClass;
    private Map<String, Trait> traits = new HashMap<String, Trait>();
    private Map<String, String[]> innerFactories = new HashMap<String, String[]>();
    private Map<String, Object> propertyValues = new HashMap<String, Object>();
    private Map<String, Object> fieldValues = new HashMap<String, Object>();
    private Object[] constructorArgs;

    // map of sequences by name for factory classes
    private static Map<Class, Map<String, Integer>> sequences = new HashMap<Class, Map<String, Integer>>();

    /** Public **/

    public ObjectFactory(Class<T> factoryClass) {
        this.factoryClass = factoryClass;

        define(); //define the factory, calls subclasses.
    }

    /**
     * Build object.
     * @return
     */
    public T build(Object... attributes) {
        List<Object> attributeList = new ArrayList<Object>(Arrays.asList(attributes)); //kinda wacky but Arrays.asList returns a unmodifiable list

        String[] traitNames = getTraitNames(attributeList);
        applyTraits(traitNames);

        T object = ReflectionUtils.createObject(factoryClass, constructorArgs);

        // merge default properties with supplied attributes
        Map<String, Object> propertyValues = createObjectPropertyValues(this.propertyValues, attributeList);

        // now set properties and fields to the created object
        setProperties(object, propertyValues);
        setFields(object, fieldValues);

        executeCallbacks(AfterFactoryBuild.class, object);
        return object;
    }

    /** DSL methods **/

    /**
     * Method that defines the factory. Should be implemented by subclasses.
     * The implementation of this method should call dsl methods below, to populate the factory.
     */
    protected abstract void define();

    /**
     * Register a trait for this factory
     * @param trait
     */
    protected void trait(String name, Trait trait) {
        traits.put(name, trait);
    }

    /**
     * Register a static value for the given field, can be private.
     * @param name name of the field
     * @param value value that should be assigned to each built object for the given field
     */
    protected void field(String name, Object value) {
        fieldValues.put(name, value);
    }

    /**
     * Register a sequence value for a given field
     * @param name name of the field
     * @param seq Sequence object, to be evaluated later
     */
    protected void field(String name, Sequence seq) {
        field(name, (Object)seq);
    }

    /**
     * Register a lazy value for a given field
     * @param name name of the field
     * @param value Lazy value, to be evaluated later
     */
    protected void field(String name, LazyValue value) {
        field(name, (Object)value);
    }

    /**
     * Register a static value for the given property
     * @param name name of the property
     * @param value value that should be assigned to each built object for the given property
     */
    protected void property(String name, Object value) {
        propertyValues.put(name, value);
    }

    /**
     * Register a sequence value for a given property
     * @param name name of the property
     * @param seq Sequence object, to be evaluated later
     */
    protected void property(String name, Sequence seq) {
        property(name, (Object)seq);
    }

    /**
     * Register a lazy value for a given property
     * @param name name of the property
     * @param value Lazy value, to be evaluated later
     */
    protected void property(String name, LazyValue value) {
        property(name, (Object)value);
    }

    protected void factory(String name, String[] traits) {
        innerFactories.put(name, traits);
    }

    public static String[] traits(String... traits) {
        return traits;
    }

    protected void constructWith(Object... args) {
        this.constructorArgs = args;
    }

    /** Protected methods **/

    protected Class<T> getFactoryClass() {
        return factoryClass;
    }

    protected void executeCallbacks(Class<? extends Annotation> annotationType, T object) {
        //first gather a list of all methods down the inheritance chain which applied the given annotation
        List<Method> annotatedMethods = new ArrayList<Method>();

        Class clz = getClass();
        while(ObjectFactory.class.isAssignableFrom(clz)) {
            // add the methods to the beginning of the method list, so we get a list ordered by position in the inheritance chain
            annotatedMethods.addAll(0, ReflectionUtils.getAnnotatedMethods(clz, annotationType));
            clz = clz.getSuperclass();
        }

        // now call all methods
        for(Method method : annotatedMethods) {
            ReflectionUtils.invokeMethod(this, method, object);
        }
    }

    /** Private methods **/

    private void setProperty(Object target, String name, Object value) {
        if(! ReflectionUtils.setProperty(target, name, getValue(value))) {
            // no property was found, try to set the field directly
            setField(target, name, value);
        }
    }

    private void setField(Object target, String name, Object value) {
        ReflectionUtils.setField(target, name, getValue(value));
    }

    private void setProperties(T object, Map<String, Object> propertyValues) {
        for(String property: propertyValues.keySet()) {
            Object value = propertyValues.get(property);
            if(value instanceof Sequence) {
                value = ((Sequence) value).apply(currentSequence(property)); //lazy evaluate sequence. TODO: see how to make this nicer/more generic
            }

            setProperty(object, property, value);
        }
    }

    private void setFields(T object, Map<String, Object> fieldValues) {
        for(String field : fieldValues.keySet()) {
            Object value = fieldValues.get(field);
            setField(object, field, value);
        }
    }

    private Object getValue(Object value) {
        return value instanceof LazyValue ? ((LazyValue) value).evaluate() : value;
    }

    /**
     * Merge passed attributes with the supplied property values.
     * @param defaultPropertyValues
     * @param attributes
     * @return
     */
    private Map<String, Object> createObjectPropertyValues(Map<String, Object> defaultPropertyValues, List<Object> attributes) {
        Map<String, Object> propertyValues = new HashMap<String, Object>(defaultPropertyValues);

        if(attributes != null) {
            Iterator<Object> iterator = attributes.iterator();
            Map<String, Object> propertyOverrideMap = new HashMap<String, Object>();

            while(iterator.hasNext()) {
                String name = (String)iterator.next();

                // we can only create a map entry if we have both a key and value, so make sure there's a value left
                if(iterator.hasNext()) {
                    Object object = iterator.next();
                    propertyOverrideMap.put(name, object);
                }
            }

            propertyValues.putAll(propertyOverrideMap);
        }

        return propertyValues;
    }

    private String[] getTraitNames(List<Object> attributes) {
        String[] traitNames = null;

        // check if the first attribute matches the name of a defined trait.
        // Also check if the number of attributes is odd. If it's even, we'll assume it's a number of key/value pairs
        // for properties and not meant to apply a trait.
        if(attributes.size() > 0 && attributes.size() %2 != 0) {
            String firstAttribute = (String)attributes.get(0);

            if(traits.containsKey(firstAttribute)) {
                // the first attribute matched the name of a defined trait, assume the trait was meant to be applied
                traitNames = new String[] {firstAttribute};
            }else if(innerFactories.containsKey(firstAttribute)) {
                // first attribute matched the name of a defined factory, apply it's traits
                traitNames = innerFactories.get(firstAttribute);
            }

            if(traitNames != null) {
                // first attribute was a valid trait name or factory, remove it from the list of attributes
                attributes.remove(0);
            }
        }

        return traitNames;
    }

    private void applyTraits(String[] traitNames) {
        if(traitNames != null) {
            // traits were defined, apply them
            for(String traitName : traitNames) {
                Trait t = traits.get(traitName);
                t.apply();
            }
        }
    }

    /**
     * Retrieve the current sequence value for the given property.
     * @param name name of the property that should be assigned the sequence value.
     * @return The current value of the referenced sequence.
     */
    private int currentSequence(String name) {
        Map<String, Integer> sequencesForClass = sequences.get(getClass());
        if(sequencesForClass == null) {
            sequencesForClass = new HashMap<String, Integer>();
            sequences.put(getClass(), sequencesForClass);
        }

        Integer seq = sequencesForClass.get(name);
        seq = seq == null ? 1 : seq + 1;

        sequencesForClass.put(name, seq);
        return seq;
    }
}