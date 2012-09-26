package org.jfactory.utils;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

@SuppressWarnings("unchecked")
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
        List<Method> annotatedMethods = new ArrayList<Method>();

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
     * @param clazz
     * @param constructorArgs arguments to pass to the matching constructor
     * @param <T>
     * @return
     */
    public static <T> T createObject(Class<T> clazz, Object... constructorArgs) {
        if(constructorArgs == null) return createObject(clazz);

        T object;

        Class[] parameterTypes = new Class[constructorArgs.length];

        for (int i = 0; i < constructorArgs.length; i++) {
            parameterTypes[i] = constructorArgs[i].getClass();
        }

        try {
            Constructor ctor = clazz.getConstructor(parameterTypes);
            object = (T)ctor.newInstance(constructorArgs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return object;
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

    public static <T> Set<Class<? extends T>> getSubclassesOf(Class<T> targetClazz, List<String> packages) {
        Set<Class<? extends T>> classes = new HashSet<Class<? extends T>>();

        Set<Class<?>> classesInPackages;
        try {
            classesInPackages = getClasses(packages);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for(Class<?> clazz : classesInPackages) {
            if(targetClazz.isAssignableFrom(clazz)) {
                classes.add((Class<?extends T>)clazz);
            }
        }

        return classes;
    }

    public static Set<Class<?>> getClasses(List<String> packages) throws ClassNotFoundException, IOException {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        for(String pkg : packages) {
            classes.addAll(getClasses(pkg));
        }

        return classes;
    }

    /**
     * @source http://code.google.com/p/morphia/source/browse/trunk/morphia/src/main/java/com/google/code/morphia/utils/ReflectionUtils.java?r=1751
     * @param packageName
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Set<Class<?>> getClasses(final String packageName) throws IOException, ClassNotFoundException
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return getClasses(loader, packageName);
    }

    /**
     * @source http://code.google.com/p/morphia/source/browse/trunk/morphia/src/main/java/com/google/code/morphia/utils/ReflectionUtils.java?r=1751
     * @param loader
     * @param packageName
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Set<Class<?>> getClasses(final ClassLoader loader, final String packageName) throws IOException,
            ClassNotFoundException
    {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = loader.getResources(path);
        if (resources != null)
        {
            while (resources.hasMoreElements())
            {
                String filePath = resources.nextElement().getFile();
                // WINDOWS HACK
                if (filePath.indexOf("%20") > 0)
                    filePath = filePath.replaceAll("%20", " ");
                // # in the jar name
                if (filePath.indexOf("%23") > 0)
                    filePath = filePath.replaceAll("%23", "#");

                if (filePath != null)
                {
                    if ((filePath.indexOf("!") > 0) & (filePath.indexOf(".jar") > 0))
                    {
                        String jarPath = filePath.substring(0, filePath.indexOf("!")).substring(
                                filePath.indexOf(":") + 1);
                        // WINDOWS HACK
                        if (jarPath.indexOf(":") >= 0)
                        {
                            jarPath = jarPath.substring(1);
                        }
                        classes.addAll(getFromJARFile(jarPath, path));
                    }
                    else
                    {
                        classes.addAll(getFromDirectory(new File(filePath), packageName));
                    }
                }
            }
        }
        return classes;
    }

    /**
     * @source http://code.google.com/p/morphia/source/browse/trunk/morphia/src/main/java/com/google/code/morphia/utils/ReflectionUtils.java?r=1751
     * @param filename
     * @return
     */
    private static String stripFilenameExtension(final String filename)
    {
        if (filename.indexOf('.') != -1)
        {
            return filename.substring(0, filename.lastIndexOf('.'));
        }
        else
        {
            return filename;
        }
    }

    /**
     * @source http://code.google.com/p/morphia/source/browse/trunk/morphia/src/main/java/com/google/code/morphia/utils/ReflectionUtils.java?r=1751
     * @param directory
     * @param packageName
     * @return
     * @throws ClassNotFoundException
     */
    public static Set<Class<?>> getFromDirectory(final File directory, final String packageName)
            throws ClassNotFoundException
    {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        if (directory.exists())
        {
            for (String file : directory.list())
            {
                if (file.endsWith(".class"))
                {
                    String name = packageName + '.' + stripFilenameExtension(file);
                    Class<?> clazz = Class.forName(name);
                    classes.add(clazz);
                }
            }
        }
        return classes;
    }

    /**
     * @source http://code.google.com/p/morphia/source/browse/trunk/morphia/src/main/java/com/google/code/morphia/utils/ReflectionUtils.java?r=1751
     * @param jar
     * @param packageName
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ClassNotFoundException
     */
    public static Set<Class<?>> getFromJARFile(final String jar, final String packageName) throws IOException,
            FileNotFoundException, ClassNotFoundException
    {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        JarInputStream jarFile = new JarInputStream(new FileInputStream(jar));
        JarEntry jarEntry;
        do
        {
            jarEntry = jarFile.getNextJarEntry();
            if (jarEntry != null)
            {
                String className = jarEntry.getName();
                if (className.endsWith(".class"))
                {
                    className = stripFilenameExtension(className);
                    if (className.startsWith(packageName))
                    {
                        classes.add(Class.forName(className.replace('/', '.')));
                    }
                }
            }
        }
        while (jarEntry != null);
        return classes;
    }
}
