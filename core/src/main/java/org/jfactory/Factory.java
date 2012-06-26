package org.jfactory;

import org.jfactory.utils.ReflectionUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.*;

public class Factory {

    private static List<String> factoryPackages = new ArrayList<String>();
    private static Map<Class<?>, Class<?extends ObjectFactory>> factoryClasses;

    public static void addFactoryScanPackage(String factoriesPackage) {
        Factory.factoryPackages.add(factoriesPackage);
    }

    public static <T> T build(Class<T> objectClass, Object... attributes) {
        ObjectFactory<T> objectFactory = getFactory(objectClass);
        return objectFactory.build(attributes);
    }

    public static <T> T create(Class<T> objectClass, Object... attributes) {
        PersistableObjectFactory<T> objectFactory = getFactory(objectClass);
        return objectFactory.create(attributes);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFactory(final Class<?> factoryClass) {
        Class<?extends ObjectFactory> factory = getFactoryClass(factoryClass);

        try {
            Constructor constructor = factory.getConstructor();
            return (T)constructor.newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the class of the Factory for the given factoryClass.
     * @param factoryClass
     * @return
     */
    private static Class<?extends ObjectFactory> getFactoryClass(Class<?> factoryClass) {
        if(factoryPackages.size() == 0) throw new IllegalArgumentException("No package provide to look for factories.");

        if(factoryClasses == null) {
            factoryClasses = new HashMap<Class<?>, Class<? extends ObjectFactory>>();
            Set<Class<?extends ObjectFactory>> classes = ReflectionUtils.getSubclassesOf(ObjectFactory.class, factoryPackages);

            for(Class<?extends ObjectFactory> clazz : classes) {
                if(! Modifier.isAbstract(clazz.getModifiers())) {
                    try {
                        Constructor constructor = clazz.getConstructor();
                        ObjectFactory factory = (ObjectFactory)constructor.newInstance();
                        factoryClasses.put(factory.getFactoryClass(), factory.getClass());
                    } catch (Exception e) {
                        throw new RuntimeException(e); //should not happen, compiler forces factory classes to implement correct constructor.
                    }
                }
            }
        }

        return factoryClasses.get(factoryClass);
    }
}
