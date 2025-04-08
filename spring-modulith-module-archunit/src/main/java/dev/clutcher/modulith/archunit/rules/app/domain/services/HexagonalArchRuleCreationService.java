package dev.clutcher.modulith.archunit.rules.app.domain.services;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.CompositeArchRule;
import dev.clutcher.modulith.archunit.rules.app.api.ApiForArchRuleCreation;
import dev.clutcher.modulith.archunit.rules.app.domain.services.library.HexagonalArchitectureRulesLibrary;
import dev.clutcher.modulith.archunit.rules.app.spi.HexagonalArchitectureSettings;
import org.springframework.modulith.core.ApplicationModule;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;

public class HexagonalArchRuleCreationService implements ApiForArchRuleCreation {

    private HexagonalArchitectureSettings properties;

    public HexagonalArchRuleCreationService(HexagonalArchitectureSettings properties) {
        this.properties = properties;
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
        String moduleBasePackage = module.getBasePackage().getName();
        return HexagonalArchitectureRulesLibrary.createLayerDefinitionRule(moduleBasePackage, properties);
    }

    @Override
    public ArchRule createPackageStructureRule(ApplicationModule applicationModule) {
        String moduleBasePackage = applicationModule.getBasePackage().getName();
        return CompositeArchRule
                .of(HexagonalArchitectureRulesLibrary.ruleForModuleRootPackageStructure(moduleBasePackage, properties))
                .and(HexagonalArchitectureRulesLibrary.ruleForApplicationPortsPackageStructure(moduleBasePackage, properties))
                .and(HexagonalArchitectureRulesLibrary.ruleForAdaptersPackageStructure(moduleBasePackage, properties));
    }

    @Override
    public ArchRule createDevStandardsRule(ApplicationModule applicationModule) {
        String moduleBasePackage = applicationModule.getBasePackage().getName();
        return CompositeArchRule
                .of(HexagonalArchitectureRulesLibrary.ruleForApplicationServices(moduleBasePackage, properties))
                .and(HexagonalArchitectureRulesLibrary.ruleForDrivingPorts(moduleBasePackage, properties))
                .and(HexagonalArchitectureRulesLibrary.ruleForDrivenPorts(moduleBasePackage, properties))
                .and(HexagonalArchitectureRulesLibrary.ruleForDrivenAdapters(moduleBasePackage, properties));
    }

}
