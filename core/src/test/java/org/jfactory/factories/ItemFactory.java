package org.jfactory.factories;

import org.jfactory.ObjectFactory;
import org.jfactory.models.Item;
import org.jfactory.types.Sequence;

public class ItemFactory extends ObjectFactory<Item> {

    public ItemFactory() {
        super(Item.class);
    }

    @Override
    protected void define() {
        sequence("quantity", new Sequence() {
            @Override
            public Object apply(int n) {
                return n;
            }
        });
    }
}
