package dev.clutcher.modulith.archunit.rules.app.domain.services;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.lang.ArchRule;
import dev.clutcher.modulith.archunit.rules.app.api.ApiForArchRuleCreation;
import dev.clutcher.modulith.archunit.rules.app.domain.model.RuleGroup;
import dev.clutcher.modulith.archunit.rules.app.spi.HexagonalArchitectureSettings;
import org.springframework.modulith.core.ApplicationModule;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;

public class HexagonalArchRuleCreationService implements ApiForArchRuleCreation {

    private final HexagonalArchitectureSettings properties;
    private final ArchRuleRegistry registry;

    public HexagonalArchRuleCreationService(HexagonalArchitectureSettings properties, ArchRuleRegistry registry) {
        this.properties = properties;
        this.registry = registry;
    }

    @Override
    public boolean isApplicable(ApplicationModule module, JavaClasses allClassesRelatedToModule) {
        String moduleBasePackage = module.getBasePackage().getName();
        return !allClassesRelatedToModule.that(resideInAPackage(moduleBasePackage + properties.getDrivingPortPackageMatcher())).isEmpty()
                || !allClassesRelatedToModule.that(resideInAPackage(moduleBasePackage + properties.getDrivenPortPackageMatcher())).isEmpty()
                || !allClassesRelatedToModule.that(resideInAPackage(moduleBasePackage + properties.getDrivingAdapterPackageMatcher())).isEmpty()
                || !allClassesRelatedToModule.that(resideInAPackage(moduleBasePackage + properties.getDrivenAdapterPackageMatcher())).isEmpty();
    }

    @Override
    public ArchRule createLayerRule(ApplicationModule module) {
        return registry.buildGroupRule(RuleGroup.LAYER, module.getBasePackage().getName(), properties);
    }

    @Override
    public ArchRule createPackageStructureRule(ApplicationModule applicationModule) {
        return registry.buildGroupRule(RuleGroup.PACKAGE_STRUCTURE, applicationModule.getBasePackage().getName(), properties);
    }

    @Override
    public ArchRule createDevStandardsRule(ApplicationModule applicationModule) {
        return registry.buildGroupRule(RuleGroup.DEV_STANDARDS, applicationModule.getBasePackage().getName(), properties);
    }

    @Override
    public ArchRule createCodeConventionsRule(ApplicationModule applicationModule) {
        return registry.buildGroupRule(RuleGroup.CODE_CONVENTIONS, applicationModule.getBasePackage().getName(), properties);
    }

}
