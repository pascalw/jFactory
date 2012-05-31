package nl.kabisa.jFactory;

import org.junit.Before;
import org.junit.Test;

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
        System.out.println(article);
    }

    @Test
    public void factory() {
        ArticleFactory factory = getFactory(Article.class);
    }
}