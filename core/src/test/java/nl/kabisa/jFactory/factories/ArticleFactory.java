package nl.kabisa.jFactory.factories;

import nl.kabisa.jFactory.ObjectFactory;
import nl.kabisa.jFactory.models.Article;
import nl.kabisa.jFactory.types.Sequence;
import nl.kabisa.jFactory.types.Trait;

public class ArticleFactory extends ObjectFactory<Article> {

    public ArticleFactory() {
        super(Article.class);
    }

    @Override
    protected void define() {
        property("guid", "http://kabisa.nl");

        trait(new Trait("read") {
            public void apply() {
                property("read", true);
            }
        });

        trait(new Trait("unread") {
            public void apply() {
                property("read", false);
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
}