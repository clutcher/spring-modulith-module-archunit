package dev.clutcher.modulith.archunit.examples.hexagonal.invalid.crossModuleDomainDependency.app.domain.model;

import dev.clutcher.modulith.archunit.examples.hexagonal.valid.standard.app.domain.model.Order;

public class ModelDependingOnExternalDomain {

    public Order externalOrder;
}
