package org.jfactory.factories;

import org.jfactory.ObjectFactory;
import org.jfactory.models.Order;
import org.jfactory.types.LazyValue;

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
