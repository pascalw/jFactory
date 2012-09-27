package nl.pwiddershoven.jfactory.persistence.morphia.factories;

import nl.pwiddershoven.jfactory.annotations.AfterFactoryBuild;
import nl.pwiddershoven.jfactory.annotations.AfterFactoryCreate;
import nl.pwiddershoven.jfactory.annotations.BeforeFactoryCreate;
import nl.pwiddershoven.jfactory.persistence.morphia.MorphiaBackedPersistableObjectFactory;
import nl.pwiddershoven.jfactory.persistence.morphia.models.PersistableArticle;
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

    @AfterFactoryBuild
    public void afterBuild(PersistableArticle article) {}

    @BeforeFactoryCreate
    public void beforeCreate(PersistableArticle article) {}

    @AfterFactoryCreate
    public void afterCreate(PersistableArticle article) {}
}