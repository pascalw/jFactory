package org.jfactory;

import junit.framework.Assert;
import org.jfactory.factories.ItemFactory;
import org.jfactory.factories.OrderFactory;
import org.jfactory.models.Article;
import org.jfactory.models.Item;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static org.jfactory.Factory.build;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class FactoryTest {

    @Before
    public void setup() {
        // add package to scan for factory classes, so jFactory can find the factories
        Factory.addFactoryScanPackage("org.jfactory.factories");
    }

    @Test
    public void buildObject() {
        // build a default Article, as defined in our factory
        Article article = build(Article.class);
        assertEquals("http://pwiddershoven.nl/blog/2011/01/05/airplayer.html", article.getGuid());

        // now build another article, but override some properties
        article = build(Article.class, "title", "foobar", "guid", "test");
        assertEquals("foobar", article.getTitle());
        assertEquals("test", article.getGuid());
    }

    @Test
    public void traits() {
        // you can also specify and use traits. jFactory evaluates the first passed attribute to see if it matches
        // the name of a registered trait
        Article article = build(Article.class, "read", "title", "test");
        assertEquals(true, article.isRead());
        assertEquals("test", article.getTitle());

        // apply the unread trait
        article = build(Article.class, "unread");
        assertEquals(false, article.isRead());

        // if the first attribute matches both the name of a trait and the name of a property, the trait should not be
        // applied if there's also a value given
        article = build(Article.class, "read", false);
        assertEquals(false, article.isRead());
    }

    @Test
    public void lazyValues() throws InterruptedException {
        OrderFactory factory = new OrderFactory();

        // amount property is non lazy, initialized once at factory creation time
        Assert.assertEquals(1, factory.build().getAmount());
        Assert.assertEquals(1, factory.build().getAmount());

        // totalAmount is lazy, so evaluated each time you call build
        Assert.assertEquals(3, factory.build().getTotalAmount());
        Assert.assertEquals(4, factory.build().getTotalAmount());
        Assert.assertEquals(5, factory.build().getTotalAmount());
    }

    @Test
    public void sequences() throws Exception {
        // reset all sequences, so we can assume the first built object in this test has a sequence value of 1
        Field field = ObjectFactory.class.getDeclaredField("sequences");
        field.setAccessible(true);
        field.set(null, new HashMap());

        assertEquals("Article 1", build(Article.class).getTitle());
        assertEquals("Article 2", build(Article.class).getTitle());
        assertEquals("Article 3", build(Article.class).getTitle());

        assertEquals(1, build(Item.class).getQuantity());
        assertEquals(2, build(Item.class).getQuantity());
        assertEquals(3, build(Item.class).getQuantity());
    }

    @Test
    public void callbacks() {
        ItemFactory factory = spy(new ItemFactory());
        Item item = factory.build();

        verify(factory).afterBuild(eq(item));
        assertEquals("callback", item.getName());
    }
}