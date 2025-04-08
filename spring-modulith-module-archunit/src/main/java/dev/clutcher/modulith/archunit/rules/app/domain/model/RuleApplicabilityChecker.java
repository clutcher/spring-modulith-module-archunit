package dev.clutcher.modulith.archunit.rules.app.domain.model;

import com.tngtech.archunit.core.domain.JavaClasses;
import org.springframework.modulith.core.ApplicationModule;

@FunctionalInterface
public interface RuleApplicabilityChecker {
    boolean check(ApplicationModule module, JavaClasses allClassesRelatedToModule);
}
