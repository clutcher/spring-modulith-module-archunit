package dev.clutcher.modulith.archunit.examples.hexagonal.valid.standard.app.domain.services;


import dev.clutcher.modulith.archunit.examples.hexagonal.valid.standard.app.api.ApiForOrderSearch;
import dev.clutcher.modulith.archunit.examples.hexagonal.valid.standard.app.domain.model.Order;
import dev.clutcher.modulith.archunit.examples.hexagonal.valid.standard.app.spi.OrderRepository;

public class OrderSearchService implements ApiForOrderSearch {

    private final OrderRepository orderRepository;

    public OrderSearchService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order findOrder(String id) {
        return orderRepository.searchOrders(id);
    }
}
