package nl.kabisa.jFactory;

public class ArticleFactory extends ObjectFactory<Article> {

    public ArticleFactory() {
        super(Article.class);
    }

    @Override
    protected void define() {
        property("guid", "http://kabisa.nl");
        property("title", "Test");

        trait(new Trait("read") {
            @Override
            public void define() {
                field("read", true);
            }
        });

        trait(new Trait("unread") {
            @Override
            protected void define() {
                field("read", false);
            }
        });
    }
}