package nl.kabisa.jFactory;

public class ArticleFactory extends ObjectFactory<Article> {

    public ArticleFactory() {
        super(Article.class);
    }

    @Override
    protected void define() {
        property("guid", "http://kabisa.nl");
        property("title", "Test");
    }
}