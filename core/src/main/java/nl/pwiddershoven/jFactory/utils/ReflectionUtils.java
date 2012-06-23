package nl.pwiddershoven.jfactory.utils;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class ReflectionUtils {

    /**
     * Set the property identified by name to the provided value.
     * @param target
     * @param name
     * @param value
     * @return true on success, false if property wasn't found.
     */
    public static boolean setProperty(Object target, String name, Object value) {
        try {
            for (PropertyDescriptor pd : Introspector.getBeanInfo(target.getClass()).getPropertyDescriptors()) {
                if (pd.getWriteMethod() != null && pd.getName().equals(name)) {
                    pd.getWriteMethod().invoke(target, value);
                    return true;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    /**
     * Set the field identified by name to the given value.
     * @param target
     * @param name
     * @param value
     * @return true on success, false if field wasn't found.
     */
    public static boolean setField(Object target, String name, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(target, value);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get all methods on the given class that are annotated with the given annotation type.
     * @param targetClass
     * @param annotationType
     * @return
     */
    public static List<Method> getAnnotatedMethods(Class targetClass, Class<? extends Annotation> annotationType) {
        List<Method> annotatedMethods = newArrayList();

        for(Method method : targetClass.getDeclaredMethods()) {
            if(method.isAnnotationPresent(annotationType)) {
                annotatedMethods.add(method);
            }
        }

        return annotatedMethods;
    }

    /**
     * Invoke the given method on the given target using the given arguments.
     * Allows to call private and protected methods.
     * @throws RuntimeException if method could not be invoked.
     * @param target
     * @param method
     * @param arguments
     */
    public static void invokeMethod(Object target, Method method, Object... arguments) {
        method.setAccessible(true); // so we can call private and protected methods too

        try {
            method.invoke(target, arguments);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create an empty object of the given class.
     * @throws RuntimeException if object could not be created.
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T createObject(Class<T> clazz) {
        T object;

        try {
            object = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return object;
    }
}
