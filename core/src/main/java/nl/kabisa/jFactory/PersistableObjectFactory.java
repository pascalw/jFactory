package nl.kabisa.jFactory;

import nl.kabisa.jFactory.ObjectFactory;
import nl.kabisa.jFactory.annotations.AfterFactoryCreate;

public abstract class PersistableObjectFactory<T> extends ObjectFactory<T> {

    protected abstract void persist(T object);

    public PersistableObjectFactory(Class<T> factoryClass, Object... attributes) {
        super(factoryClass, attributes);
    }

    public T create() {
        // build
        T object = build();

        // persist
        persist(object);

        // execute callbacks
        executeCallbacks(AfterFactoryCreate.class, object);

        // return
        return object;
    }
}
