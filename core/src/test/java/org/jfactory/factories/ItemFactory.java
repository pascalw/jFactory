package org.jfactory.factories;

import org.jfactory.ObjectFactory;
import org.jfactory.annotations.AfterFactoryBuild;
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

    /**
     * Annotated callbacks method don't have to be public, can also be private or protected.
     * @param item
     */
    @AfterFactoryBuild
    public void afterBuild(Item item) {
        item.setName("callback");
    }

    @AfterFactoryBuild
    public void afterBuild2(Item item) {}
}
