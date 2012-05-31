package nl.kabisa.jFactory.persistence;

import com.google.code.morphia.Datastore;

public abstract class MorphiaBackedPersistableObjectFactory<T> extends PersistableObjectFactory<T> {

    private static Datastore datastore;

    public static void setDatastore(Datastore datastore) {
        MorphiaBackedPersistableObjectFactory.datastore = datastore;
    }

    public MorphiaBackedPersistableObjectFactory(Class<T> factoryClass, Object... attributes) {
        super(factoryClass, attributes);
    }

    @Override
    protected void persist(T object) {
        datastore.save(object);
    }
}