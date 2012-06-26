package org.jfactory.persistence.morphia;

import com.google.code.morphia.Datastore;
import org.jfactory.Factory;
import org.jfactory.persistence.morphia.factories.ArticleFactory;
import org.jfactory.persistence.morphia.models.PersistableArticle;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static junit.framework.Assert.assertEquals;
import static org.jfactory.Factory.create;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class PersistableObjectFactoryTest {

    private Datastore datastore = mock(Datastore.class);

    @Before
    public void setup() {
        Factory.addFactoryScanPackage("org.jfactory.persistence.morphia.factories");
        MorphiaBackedPersistableObjectFactory.setDatastore(datastore);
    }

    @Test
    public void createObject() {
        PersistableArticle article = create(PersistableArticle.class, "title", "test");

        verify(datastore).save(article);

        assertEquals("test", article.getTitle());
    }

    @Test
    public void callbacks() {
        ArticleFactory factory = spy(new ArticleFactory());
        PersistableArticle article = factory.create();

        InOrder inOrder = inOrder(factory, datastore);

        // after build callback should be called first
        inOrder.verify(factory).afterBuild(eq(article));

        // after create callback should be called after saving the object
        inOrder.verify(factory).beforeCreate(article);
        inOrder.verify(datastore).save(eq(article));
        inOrder.verify(factory).afterCreate(eq(article));
    }
}
