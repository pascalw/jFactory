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

        trait(new Trait("java") {
            @Override
            public void apply() {
                constructWith("Effective Java", "Joshua Bloch");
            }
        });

        trait(new Trait("camel") {
            @Override
            public void apply() {
                constructWith("Camel In Action", "Claus Ibsen");
            }
        });

        trait(new Trait("cheap") {
            @Override
            public void apply() {
                field("price", 5);
            }
        });
    }
}
