package nl.pwiddershoven.jfactory.factories;

import nl.pwiddershoven.jfactory.ObjectFactory;
import nl.pwiddershoven.jfactory.models.Order;
import nl.pwiddershoven.jfactory.types.LazyValue;

public class OrderFactory extends ObjectFactory<Order> {

    private int amount;
    private int totalAmount;

    public OrderFactory() {
        super(Order.class);
    }

    @Override
    protected void define() {
        property("amount", ++amount);
        property("totalAmount", new LazyValue() {
            @Override
            public Object evaluate() {
                return ++totalAmount;
            }
        });
    }
}
