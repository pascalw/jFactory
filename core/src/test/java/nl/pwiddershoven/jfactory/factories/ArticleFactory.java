package nl.pwiddershoven.jfactory.factories;

import nl.pwiddershoven.jfactory.ObjectFactory;
import nl.pwiddershoven.jfactory.models.Article;
import nl.pwiddershoven.jfactory.types.Sequence;
import nl.pwiddershoven.jfactory.types.Trait;

public class ArticleFactory extends ObjectFactory<Article> {

    public ArticleFactory() {
        super(Article.class);
    }

    @Override
    protected void define() {
        property("guid", "http://pwiddershoven.nl/blog/2011/01/05/airplayer.html");

        trait("read", new Trait() {
            public void apply() {
                property("read", true);
            }
        });

        trait("unread", new Trait() {
            public void apply() {
                property("read", false);
            }
        });

        trait("published", new Trait() {
            @Override
            public void apply() {
                property("state", Article.State.PUBLISHED);
            }
        });

        trait("unpublished", new Trait() {
            @Override
            public void apply() {
                property("state", Article.State.UNPUBLISHED);
            }
        });

        property("id", new Sequence() {
            public Object apply(int n) {
                return n;
            }
        });

        property("title", new Sequence() {
            public Object apply(int n) {
                return String.format("Article %d", n);
            }
        });

        factory("publishedRead", traits("published", "read"));
    }
}