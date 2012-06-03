package nl.kabisa.jFactory;

import nl.kabisa.jFactory.annotations.AfterFactoryCreate;

public abstract class PersistableObjectFactory<T> extends ObjectFactory<T> {

    protected abstract void persist(T object);

    public PersistableObjectFactory(Class<T> factoryClass) {
        super(factoryClass);
    }

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
