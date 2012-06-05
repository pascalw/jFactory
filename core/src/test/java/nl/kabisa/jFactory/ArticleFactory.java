package nl.kabisa.jFactory;

public class ArticleFactory extends ObjectFactory<Article> {

    public ArticleFactory() {
        super(Article.class);
    }

    @Override
    protected void define() {
        property("guid", "http://kabisa.nl");

        trait(new Trait("read") {
            void define() {
                field("read", true);
            }
        });

        trait(new Trait("unread") {
            void define() {
                field("read", false);
            }
        });

        sequence("id", new Sequence() {
            public Object apply(int n) {
                return n;
            }
        });

        sequence("title", new Sequence() {
            public Object apply(int n) {
                return String.format("Article %d", n);
            }
        });
    }

    protected void foo() {
        System.out.println("Hai");
    }
}