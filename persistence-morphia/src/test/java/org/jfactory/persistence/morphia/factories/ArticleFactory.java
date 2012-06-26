package org.jfactory.persistence.morphia.factories;

import org.jfactory.persistence.morphia.MorphiaBackedPersistableObjectFactory;
import org.jfactory.persistence.morphia.models.PersistableArticle;
import org.bson.types.ObjectId;

public class ArticleFactory extends MorphiaBackedPersistableObjectFactory<PersistableArticle> {

    public ArticleFactory() {
        super(PersistableArticle.class);
    }

    @Override
    protected void define() {
        property("id", new ObjectId());
        property("guid", "http://pwiddershoven.nl");
        property("title", "Test");
    }
}