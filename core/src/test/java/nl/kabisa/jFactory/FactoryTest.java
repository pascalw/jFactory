package nl.kabisa.jFactory;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static nl.kabisa.jFactory.Factory.build;

public class FactoryTest {

    @Before
    public void setup() {
        Factory.addFactoryScanPackage("nl.kabisa.jFactory");
    }

    @Test
    public void sequences() {
        assertEquals("Article 1", build(Article.class).getTitle());
        assertEquals("Article 2", build(Article.class).getTitle());
        assertEquals("Article 3", build(Article.class).getTitle());

        assertEquals(1, build(Item.class).getQuantity());
        assertEquals(2, build(Item.class).getQuantity());
        assertEquals(3, build(Item.class).getQuantity());
    }

    @Test
    public void buildObject() {
        Article article = build(Article.class, "title", "foobar", "guid", "test");

        assertEquals("foobar", article.getTitle());
        assertEquals("test", article.getGuid());
    }

    @Test
    public void factory() {
        Article article = build(Article.class, "title", "test");
        assertEquals("test", article.getTitle());

        article = build(Article.class, "title", "test2");
        assertEquals("test2", article.getTitle());
    }

    @Test
    public void traits() {
        Article article = build(Article.class, "read", "title", "test");
        assertEquals(true, article.isRead());
        assertEquals("test", article.getTitle());

        article = build(Article.class, "unread");
        assertEquals(false, article.isRead());
    }
}