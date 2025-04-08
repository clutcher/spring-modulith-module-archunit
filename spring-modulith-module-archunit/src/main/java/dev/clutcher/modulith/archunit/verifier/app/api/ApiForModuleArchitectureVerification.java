package dev.clutcher.modulith.archunit.verifier.app.api;

import org.springframework.modulith.core.ApplicationModule;
import org.springframework.modulith.core.ApplicationModules;

public interface ApiForModuleArchitectureVerification {
    void verifyAllModules(ApplicationModules applicationModules);

    void verifySingleModule(ApplicationModule module, ApplicationModules applicationModules);
}
