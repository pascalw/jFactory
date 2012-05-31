package nl.kabisa.jFactory.persistence;

import static nl.kabisa.jFactory.FactoryUtils.getFactory;

public class FactoryUtils {

    public static <T> T create(Class<T> objectClass, Object... attributes) {
        PersistableObjectFactory<T> objectFactory = getFactory(objectClass, attributes);
        return objectFactory.create();
    }
}
