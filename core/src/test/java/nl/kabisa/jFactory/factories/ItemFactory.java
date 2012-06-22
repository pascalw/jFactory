package nl.kabisa.jFactory.factories;

import nl.kabisa.jFactory.ObjectFactory;
import nl.kabisa.jFactory.models.Item;
import nl.kabisa.jFactory.types.Sequence;

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
