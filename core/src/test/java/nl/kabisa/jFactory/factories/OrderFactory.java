package nl.kabisa.jFactory.factories;

import nl.kabisa.jFactory.ObjectFactory;
import nl.kabisa.jFactory.models.Order;
import nl.kabisa.jFactory.types.LazyValue;

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
