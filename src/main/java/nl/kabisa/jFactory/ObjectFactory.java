package nl.kabisa.jFactory;

import nl.kabisa.jFactory.annotations.AfterFactoryBuild;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import static com.google.common.collect.Lists.newArrayList;

public abstract class ObjectFactory<T> {

    private Map<String, Object> propertyValues = new HashMap<String, Object>();
    private Map<String, Object> fieldValues = new HashMap<String, Object>();
    private Class<T> factoryClass;

    /** Public **/

    public ObjectFactory(Class<T> factoryClass, Object... attributes) {
        this.factoryClass = factoryClass;

        // define factory, calls subclasses
        define();

        // merge passed attributes, if any
        mergeAttributes(attributes);
    }

    /**
     * Build object.
     * @return
     */
    public T build() {
        T object = ReflectionUtils.createObject(factoryClass);

        setProperties(object);
        setFields(object);

        executeCallbacks(AfterFactoryBuild.class, object);
        return object;
    }

    /** DSL methods **/

    protected abstract void define();

    protected void field(String name, Object value) {
        fieldValues.put(name, value);
    }

    protected void property(String name, Object value) {
        propertyValues.put(name, value);
    }

    protected static int rand(int max) {
        return new Random().nextInt(max);
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
        if(! ReflectionUtils.setProperty(target, name, value)) {
            // no property was found, try to set the field directly
            setField(target, name, value);
        }
    }

    private void setField(Object target, String name, Object value) {
        ReflectionUtils.setField(target, name, value);
    }

    private void setProperties(T object) {
        for(String property: propertyValues.keySet()) {
            Object value = propertyValues.get(property);
            setProperty(object, property, value);
        }
    }

    private void setFields(T object) {
        for(String field : fieldValues.keySet()) {
            Object value = fieldValues.get(field);
            setField(object, field, value);
        }
    }

    private void mergeAttributes(Object... attributes) {
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
    }
}