package nl.pwiddershoven.jfactory.factories;

import nl.pwiddershoven.jfactory.ObjectFactory;
import nl.pwiddershoven.jfactory.models.Book;
import nl.pwiddershoven.jfactory.types.Trait;

public class BookFactory extends ObjectFactory<Book> {

    public BookFactory() {
        super(Book.class);
    }

    @Override
    protected void define() {
        constructWith("Enterprise Integration Patterns", "Gregor Hohpe");

        trait("java", new Trait() {
            @Override
            public void apply() {
                constructWith("Effective Java", "Joshua Bloch");
            }
        });

        trait("camel", new Trait() {
            @Override
            public void apply() {
                constructWith("Camel In Action", "Claus Ibsen");
            }
        });

        trait("cheap", new Trait() {
            @Override
            public void apply() {
                field("price", 5);
            }
        });
    }
}
