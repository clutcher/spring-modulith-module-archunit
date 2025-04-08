package dev.clutcher.modulith.archunit.rules.app.domain.services;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.lang.ArchRule;
import dev.clutcher.modulith.archunit.rules.app.domain.model.RuleProvider;
import dev.clutcher.modulith.archunit.rules.app.domain.model.RuleApplicabilityChecker;
import dev.clutcher.modulith.archunit.rules.app.api.ApiForArchRuleCreation;
import org.springframework.modulith.core.ApplicationModule;

public class DelegatingArchRuleCreationService implements ApiForArchRuleCreation {

    private final RuleApplicabilityChecker ruleApplicabilityChecker;
    private final RuleProvider layerRuleProvider;
    private final RuleProvider packageRuleProvider;
    private final RuleProvider devStandardsRuleProvider;

    public DelegatingArchRuleCreationService(
            RuleApplicabilityChecker ruleApplicabilityChecker,
            RuleProvider layerRuleProvider,
            RuleProvider packageRuleProvider,
            RuleProvider devStandardsRuleProvider
    ) {
        this.ruleApplicabilityChecker = ruleApplicabilityChecker;
        this.layerRuleProvider = layerRuleProvider;
        this.packageRuleProvider = packageRuleProvider;
        this.devStandardsRuleProvider = devStandardsRuleProvider;
    }

    @Override
    public boolean isApplicable(ApplicationModule module, JavaClasses allClassesRelatedToModule) {
        return ruleApplicabilityChecker.check(module, allClassesRelatedToModule);
    }

    @Override
    public ArchRule createLayerRule(ApplicationModule module) {
        return layerRuleProvider.provide(module);
    }

    @Override
    public ArchRule createPackageStructureRule(ApplicationModule module) {
        return packageRuleProvider.provide(module);
    }

    @Override
    public ArchRule createDevStandardsRule(ApplicationModule applicationModule) {
        return devStandardsRuleProvider.provide(applicationModule);
    }

}