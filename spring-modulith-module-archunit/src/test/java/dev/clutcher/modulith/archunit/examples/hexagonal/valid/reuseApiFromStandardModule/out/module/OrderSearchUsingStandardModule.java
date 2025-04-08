package dev.clutcher.modulith.archunit.examples.hexagonal.valid.reuseApiFromStandardModule.out.module;

import dev.clutcher.modulith.archunit.examples.hexagonal.valid.standard.app.api.ApiForOrderSearch;
import dev.clutcher.modulith.archunit.examples.hexagonal.valid.standard.app.domain.model.Order;

public class OrderSearchUsingStandardModule {

    private ApiForOrderSearch apiForOrderSearch;

    public Order searchOrder(String id) {
        return apiForOrderSearch.findOrder(id);
    }

}
