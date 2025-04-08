package dev.clutcher.modulith.archunit.rules.app.domain.model;

import com.tngtech.archunit.lang.ArchRule;
import org.springframework.modulith.core.ApplicationModule;

@FunctionalInterface
public interface RuleProvider {
    ArchRule provide(ApplicationModule module);
}
