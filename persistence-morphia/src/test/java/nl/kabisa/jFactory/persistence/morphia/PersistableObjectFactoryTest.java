package nl.kabisa.jFactory.persistence.morphia;

import com.google.code.morphia.Datastore;
import org.junit.Before;
import org.junit.Test;

import static nl.kabisa.jFactory.Factory.addFactoryScanPackage;
import static nl.kabisa.jFactory.Factory.create;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PersistableObjectFactoryTest {

    private Datastore datastore = mock(Datastore.class);

    @Before
    public void setup() {
        addFactoryScanPackage("nl.kabisa.jFactory");
        MorphiaBackedPersistableObjectFactory.setDatastore(datastore);
    }

    @Test
    public void createObject() {
        PersistableArticle article = create(PersistableArticle.class);
        System.out.println(article);

        verify(datastore).save(article);
    }
}
