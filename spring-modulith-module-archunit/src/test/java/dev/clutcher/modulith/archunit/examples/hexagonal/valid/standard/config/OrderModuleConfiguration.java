package dev.clutcher.modulith.archunit.examples.hexagonal.valid.standard.config;

import dev.clutcher.modulith.archunit.examples.hexagonal.valid.standard.app.domain.services.OrderSearchService;
import dev.clutcher.modulith.archunit.examples.hexagonal.valid.standard.in.rest.OrderSearchController;
import dev.clutcher.modulith.archunit.examples.hexagonal.valid.standard.out.rest.OrderRepositoryUsingOms;

public class OrderModuleConfiguration {

    public OrderSearchController createOrderSearchController() {
        return new OrderSearchController();
    }

    public OrderSearchService createOrderSearchService() {
        return new OrderSearchService(createOrderRepositoryUsingOms());
    }

    public OrderRepositoryUsingOms createOrderRepositoryUsingOms() {
        return new OrderRepositoryUsingOms();
    }

}
