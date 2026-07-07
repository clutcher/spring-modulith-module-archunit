package dev.clutcher.modulith.archunit.examples.hexagonal.invalid.domainModelExposedInController.in.rest;

import dev.clutcher.modulith.archunit.examples.hexagonal.invalid.domainModelExposedInController.app.domain.model.DomainEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerExposingDomainModel {

    public DomainEntity getDomainEntity() {
        return new DomainEntity();
    }
}
