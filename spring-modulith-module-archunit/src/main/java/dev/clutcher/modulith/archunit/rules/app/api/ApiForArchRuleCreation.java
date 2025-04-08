package dev.clutcher.modulith.archunit.rules.app.api;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.modulith.core.ApplicationModule;

public interface ApiForArchRuleCreation {

    boolean isApplicable(ApplicationModule module, JavaClasses allClassesRelatedToModule);

    ArchRule createLayerRule(ApplicationModule module);

    ArchRule createPackageStructureRule(ApplicationModule module);

    ArchRule createDevStandardsRule(ApplicationModule applicationModule);

}
