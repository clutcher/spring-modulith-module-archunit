package dev.clutcher.modulith.archunit.examples.hexagonal.valid.standard.in.rest;

import dev.clutcher.modulith.archunit.examples.hexagonal.valid.standard.app.api.ApiForOrderSearch;
import dev.clutcher.modulith.archunit.examples.hexagonal.valid.standard.app.domain.model.Order;

public class OrderSearchController {

    private ApiForOrderSearch apiForOrderSearch;

    public Order searchOrders(String requestParameter) {
        return apiForOrderSearch.findOrder(requestParameter);
    }

}
