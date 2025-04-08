package dev.clutcher.modulith.archunit.examples.hexagonal.invalid.applicationServicesUsedInAdapters.out;

import dev.clutcher.modulith.archunit.examples.hexagonal.invalid.applicationServicesUsedInAdapters.app.domain.services.SimpleService;

public class UsingAdapter {

    public String execute() {
        return new SimpleService().execute();
    }

}
