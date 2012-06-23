package nl.pwiddershoven.jfactory.persistence.morphia;

import com.google.code.morphia.Datastore;
import nl.pwiddershoven.jfactory.PersistableObjectFactory;
import nl.pwiddershoven.jfactory.PersistableObjectFactory;

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