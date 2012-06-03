package nl.kabisa.jFactory;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static nl.kabisa.jFactory.Factory.build;
import static nl.kabisa.jFactory.Factory.getFactory;

public class FactoryTest {

    @Before
    public void setup() {
        Factory.addFactoryScanPackage("nl.kabisa.jFactory");
    }

    @Test
    public void buildObject() {
        Article article = build(Article.class, "title", "foobar", "guid", "test");

        assertEquals("foobar", article.getTitle());
        assertEquals("test", article.getGuid());
    }

    @Test
    public void factory() {
        ArticleFactory factory = getFactory(Article.class);

        Article article = factory.build("title", "test");
        assertEquals("test", article.getTitle());

        article = factory.build("title", "test2");
        assertEquals("test2", article.getTitle());
    }
}