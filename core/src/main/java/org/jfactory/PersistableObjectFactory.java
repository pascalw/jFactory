package org.jfactory;

import org.jfactory.annotations.AfterFactoryCreate;
import org.jfactory.annotations.BeforeFactoryCreate;

/**
 * Abstract class that provides functionality for building objects that can be persisted through some persistence layer.
 * The actual persisting of objects is to be implemented by subclasses.
 * @param <T> factory class, see {@link ObjectFactory}
 */
public abstract class PersistableObjectFactory<T> extends ObjectFactory<T> {

    protected abstract void persist(T object);

    public PersistableObjectFactory(Class<T> factoryClass) {
        super(factoryClass);
    }

    /**
     * Create an object with the given attributes.
     * First builds the object, than persists it.
     * @param attributes
     * @return
     */
    public T create(Object... attributes) {
        // build
        T object = build(attributes);

        // excute beforeCreate callback
        executeCallbacks(BeforeFactoryCreate.class, object);

        // persist
        persist(object);

        // execute after create callback
        executeCallbacks(AfterFactoryCreate.class, object);

        // return
        return object;
    }
}
