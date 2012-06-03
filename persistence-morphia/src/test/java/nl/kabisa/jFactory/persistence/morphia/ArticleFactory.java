package nl.kabisa.jFactory.persistence.morphia;

import org.bson.types.ObjectId;

public class ArticleFactory extends MorphiaBackedPersistableObjectFactory<PersistableArticle> {

    public ArticleFactory() {
        super(PersistableArticle.class);
    }

    @Override
    protected void define() {
        property("id", new ObjectId());
        property("guid", "http://kabisa.nl");
        property("title", "Test");
    }
}