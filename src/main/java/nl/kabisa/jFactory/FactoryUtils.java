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

public class FactoryUtils {

    private static List<String> factoryPackages = newArrayList();
    private static Map<Class<?>, Class<?extends ObjectFactory>> factoryClasses;

    public static void addFactoryScanPackage(String factoriesPackage) {
        FactoryUtils.factoryPackages.add(factoriesPackage);
    }

    public static <T> T build(Class<T> objectClass, Object... attributes) {
        ObjectFactory<T> objectFactory = getFactory(objectClass, attributes);
        return objectFactory.build();
    }

    @SuppressWarnings("unchecked")
    public static <T,E> T getFactory(final Class<E> factoryClass, Object... attributes) {
        Class<?extends ObjectFactory> factory = getFactoryClass(factoryClass);

        try {
            Constructor constructor = factory.getConstructor(Object[].class);
            return (T)constructor.newInstance(new Object[]{ attributes} );
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
                        Constructor constructor = clazz.getConstructor(Object[].class);
                        ObjectFactory factory = (ObjectFactory)constructor.newInstance(new Object[]{null});
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
