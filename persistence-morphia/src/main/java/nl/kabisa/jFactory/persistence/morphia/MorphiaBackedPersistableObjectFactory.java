package nl.kabisa.jFactory.persistence.morphia;

import com.google.code.morphia.Datastore;
import nl.kabisa.jFactory.PersistableObjectFactory;

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