package nl.kabisa.jFactory;

import com.google.common.base.Preconditions;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class Factory {

    private static List<String> factoryPackages = newArrayList();
    private static Map<Class<?>, Class<?extends ObjectFactory>> factoryClasses;

    public static void addFactoryScanPackage(String factoriesPackage) {
        Factory.factoryPackages.add(factoriesPackage);
    }

    public static <T> T build(Class<T> objectClass, Object... attributes) {
        ObjectFactory<T> objectFactory = getFactory(objectClass, attributes);
        return objectFactory.build(attributes);
    }

    public static <T> T create(Class<T> objectClass, Object... attributes) {
        PersistableObjectFactory<T> objectFactory = getFactory(objectClass, attributes);
        return objectFactory.create(attributes);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFactory(final Class<?> factoryClass, Object... attributes) {
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
        Preconditions.checkArgument(factoryPackages.size() != 0, "No package provide to look for factories.");

        if(factoryClasses == null) {
            factoryClasses = newHashMap();

            Reflections reflections = new Reflections(factoryPackages);
            Set<Class<?extends ObjectFactory>> classes = reflections.getSubTypesOf(ObjectFactory.class);

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
