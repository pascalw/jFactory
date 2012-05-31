package nl.kabisa.jFactory;

public class ArticleFactory extends ObjectFactory<Article> {

    public ArticleFactory(Object... attributes) {
        super(Article.class, attributes);
    }

    @Override
    protected void define() {
        property("guid", "http://kabisa.nl");
        property("title", "Test");
    }
}