package nl.pwiddershoven.jfactory.factories;

import nl.pwiddershoven.jfactory.ObjectFactory;
import nl.pwiddershoven.jfactory.annotations.AfterFactoryBuild;
import nl.pwiddershoven.jfactory.models.Item;
import nl.pwiddershoven.jfactory.types.Sequence;

public class ItemFactory extends ObjectFactory<Item> {

    public ItemFactory() {
        super(Item.class);
    }

    @Override
    protected void define() {
        property("quantity", new Sequence() {
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
