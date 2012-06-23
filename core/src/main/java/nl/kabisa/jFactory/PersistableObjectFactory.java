package nl.kabisa.jFactory;

import nl.kabisa.jFactory.annotations.AfterFactoryCreate;

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

        // persist
        persist(object);

        // execute callbacks
        executeCallbacks(AfterFactoryCreate.class, object);

        // return
        return object;
    }
}
