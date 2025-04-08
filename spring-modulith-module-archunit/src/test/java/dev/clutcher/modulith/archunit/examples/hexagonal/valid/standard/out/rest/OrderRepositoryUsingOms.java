package dev.clutcher.modulith.archunit.examples.hexagonal.valid.standard.out.rest;


import dev.clutcher.modulith.archunit.examples.hexagonal.valid.standard.app.domain.model.Order;
import dev.clutcher.modulith.archunit.examples.hexagonal.valid.standard.app.spi.OrderRepository;

public class OrderRepositoryUsingOms implements OrderRepository {

    @Override
    public Order searchOrders(String id) {
        return new Order();
    }

}
