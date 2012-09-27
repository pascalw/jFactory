package nl.pwiddershoven.jfactory.factories;

import nl.pwiddershoven.jfactory.ObjectFactory;
import nl.pwiddershoven.jfactory.models.Article;
import nl.pwiddershoven.jfactory.types.Sequence;
import nl.pwiddershoven.jfactory.types.Trait;

import static nl.pwiddershoven.jfactory.Factory.traits;

public class ArticleFactory extends ObjectFactory<Article> {

    public ArticleFactory() {
        super(Article.class);
    }

    @Override
    protected void define() {
        property("guid", "http://pwiddershoven.nl/blog/2011/01/05/airplayer.html");

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

        trait(new Trait("published") {
            @Override
            public void apply() {
                property("state", Article.State.PUBLISHED);
            }
        });

        trait(new Trait("unpublished") {
            @Override
            public void apply() {
                property("state", Article.State.UNPUBLISHED);
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

        factory("publishedRead", traits("published", "read"));
    }
}