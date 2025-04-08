package dev.clutcher.modulith.archunit.examples.hexagonal.valid.standard.app.spi;

import dev.clutcher.modulith.archunit.examples.hexagonal.valid.standard.app.domain.model.Order;

public interface OrderRepository {
    Order searchOrders(String id);
}
