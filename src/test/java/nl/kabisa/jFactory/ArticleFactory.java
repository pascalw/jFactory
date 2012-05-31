package nl.kabisa.jFactory;

import nl.kabisa.jFactory.persistence.MorphiaBackedPersistableObjectFactory;

public class ArticleFactory extends MorphiaBackedPersistableObjectFactory<Article> {

    public ArticleFactory(Object... attributes) {
        super(Article.class, attributes);
    }

    @Override
    protected void define() {
        property("guid", "http://kabisa.nl");
        property("title", "Test");
    }
}