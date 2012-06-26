package org.jfactory.persistence.morphia;

import com.google.code.morphia.Datastore;
import org.jfactory.PersistableObjectFactory;

public abstract class MorphiaBackedPersistableObjectFactory<T> extends PersistableObjectFactory<T> {

    private static Datastore datastore;

    public static void setDatastore(Datastore datastore) {
        MorphiaBackedPersistableObjectFactory.datastore = datastore;
    }

    public MorphiaBackedPersistableObjectFactory(Class<T> factoryClass) {
        super(factoryClass);
    }

    @Override
    protected void persist(T object) {
        datastore.save(object);
    }
}