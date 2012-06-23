package nl.pwiddershoven.jfactory.factories;

import nl.pwiddershoven.jfactory.ObjectFactory;
import nl.pwiddershoven.jfactory.models.Item;
import nl.pwiddershoven.jfactory.types.Sequence;

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
