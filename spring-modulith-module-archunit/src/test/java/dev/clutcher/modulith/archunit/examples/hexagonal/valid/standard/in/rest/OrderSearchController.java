package dev.clutcher.modulith.archunit.examples.hexagonal.valid.standard.in.rest;

import dev.clutcher.modulith.archunit.examples.hexagonal.valid.standard.app.api.ApiForOrderSearch;
import dev.clutcher.modulith.archunit.examples.hexagonal.valid.standard.app.domain.model.Order;

public class OrderSearchController {

    private ApiForOrderSearch apiForOrderSearch;

    public OrderSearchResponse searchOrders(String requestParameter) {
        Order order = apiForOrderSearch.findOrder(requestParameter);
        return new OrderSearchResponse(order.id);
    }

}
