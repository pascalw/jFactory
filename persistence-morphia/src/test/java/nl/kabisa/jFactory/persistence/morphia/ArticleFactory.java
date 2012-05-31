package nl.kabisa.jFactory.persistence.morphia;

import org.bson.types.ObjectId;

public class ArticleFactory extends MorphiaBackedPersistableObjectFactory<PersistableArticle> {

    public ArticleFactory(Object... attributes) {
        super(PersistableArticle.class, attributes);
    }

    @Override
    protected void define() {
        property("id", new ObjectId());
        property("guid", "http://kabisa.nl");
        property("title", "Test");
    }
}