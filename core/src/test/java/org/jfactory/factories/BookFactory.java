package org.jfactory.factories;

import org.jfactory.ObjectFactory;
import org.jfactory.models.Book;
import org.jfactory.types.Trait;

public class BookFactory extends ObjectFactory<Book> {

    public BookFactory() {
        super(Book.class);
    }

    @Override
    protected void define() {
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
    }
}
