package nl.kabisa.jFactory;

import com.google.common.collect.Maps;
import nl.kabisa.jFactory.annotations.AfterFactoryBuild;
import nl.kabisa.jFactory.types.LazyValue;
import nl.kabisa.jFactory.types.Sequence;
import nl.kabisa.jFactory.types.Trait;
import nl.kabisa.jFactory.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public abstract class ObjectFactory<T> extends BasicFactory {

    private Map<String, Trait> traits = newHashMap();
    private Class<T> factoryClass;

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
        T object = ReflectionUtils.createObject(factoryClass);

        String trait = null;

        if(attributes.length > 0 && traits.containsKey((String)attributes[0])) {
            trait = (String)attributes[0];
            attributes = Arrays.copyOfRange(attributes, 1, attributes.length);
        }

        if(trait != null) {
            // a trait was defined, apply it
            Trait t = traits.get(trait);
            t.apply();
        }

        // merge default properties with supplied attributes
        Map<String, Object> propertyValues = createObjectPropertyValues(this.propertyValues, attributes);

        setProperties(object, propertyValues);
        setFields(object, fieldValues);

        executeCallbacks(AfterFactoryBuild.class, object);
        return object;
    }

    /** DSL methods **/

    protected abstract void define();

    protected void trait(Trait trait) {
        traits.put(trait.getName(), trait);
    }

    /** Protected methods **/

    protected Class<T> getFactoryClass() {
        return factoryClass;
    }

    protected void executeCallbacks(Class<? extends Annotation> annotationType, T object) {
        //first gather a list of all methods down the inheritance chain which applied the given annotation
        List<Method> annotatedMethods = newArrayList();

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
                value = ((Sequence) value).apply(currentSequence(property)); //lazy evaluate sequence. TODO: move to parent + see how to make this nicer/more general
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

    private Map<String, Object> createObjectPropertyValues(Map<String, Object> defaultPropertyValues, Object... attributes) {
        Map<String, Object> propertyValues = newHashMap(defaultPropertyValues);

        if(attributes != null) {
            Iterator<Object> iterator = newArrayList(attributes).iterator();
            Map<String, Object> propertyOverideMap = new HashMap<String, Object>();

            while(iterator.hasNext()) {
                String name = (String)iterator.next();
                Object object;

                if(iterator.hasNext()) {
                    object = iterator.next();
                    propertyOverideMap.put(name, object);
                }
            }

            propertyValues.putAll(propertyOverideMap);
        }

        return propertyValues;
    }
}