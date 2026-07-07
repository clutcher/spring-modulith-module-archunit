package dev.clutcher.modulith.archunit.examples.hexagonal.invalid.wrongParameterTypeNaming.app.api;

import dev.clutcher.modulith.archunit.examples.hexagonal.invalid.wrongParameterTypeNaming.app.domain.model.OrderFilter;

public interface ApiForOrderSearch {

    void findOrders(OrderFilter filter);
}
