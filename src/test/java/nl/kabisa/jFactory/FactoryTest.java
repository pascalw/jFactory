package nl.kabisa.jFactory;

import com.google.code.morphia.Datastore;
import nl.kabisa.jFactory.persistence.MorphiaBackedPersistableObjectFactory;
import org.junit.Before;
import org.junit.Test;

import static nl.kabisa.jFactory.FactoryUtils.build;
import static nl.kabisa.jFactory.persistence.FactoryUtils.create;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FactoryTest {

    private Datastore datastore = mock(Datastore.class);

    @Before
    public void setup() {
        FactoryUtils.addFactoryScanPackage("nl.kabisa.jFactory");
        MorphiaBackedPersistableObjectFactory.setDatastore(datastore);
    }

    @Test
    public void buildObject() {
        Article article = build(Article.class, "title", "foobar", "guid", "test");
        System.out.println(article);
    }

    @Test
    public void createObject() {
        Article article = create(Article.class);
        System.out.println(article);

        verify(datastore).save(article);
    }
}