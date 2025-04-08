package dev.clutcher.modulith.archunit.examples.hexagonal.valid.standard.app.api;

import dev.clutcher.modulith.archunit.examples.hexagonal.valid.standard.app.domain.model.Order;

public interface ApiForOrderSearch {

    Order findOrder(String id);

}
