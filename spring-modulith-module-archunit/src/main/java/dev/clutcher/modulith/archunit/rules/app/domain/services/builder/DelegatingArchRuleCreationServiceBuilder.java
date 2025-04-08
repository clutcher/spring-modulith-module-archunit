package dev.clutcher.modulith.archunit.rules.app.domain.services.builder;

import dev.clutcher.modulith.archunit.rules.app.api.ApiForArchRuleCreation;
import dev.clutcher.modulith.archunit.rules.app.api.ApiForCustomizingArchRuleCreation;
import dev.clutcher.modulith.archunit.rules.app.domain.model.RuleApplicabilityChecker;
import dev.clutcher.modulith.archunit.rules.app.domain.model.RuleProvider;
import dev.clutcher.modulith.archunit.rules.app.domain.services.DelegatingArchRuleCreationService;

public class DelegatingArchRuleCreationServiceBuilder implements ApiForCustomizingArchRuleCreation {

    private RuleApplicabilityChecker ruleApplicabilityChecker;
    private RuleProvider layerRuleProvider;
    private RuleProvider packageRuleProvider;
    private RuleProvider devStandardsRuleProvider;

    public DelegatingArchRuleCreationServiceBuilder(ApiForArchRuleCreation apiForArchRuleCreation) {
        this.ruleApplicabilityChecker = apiForArchRuleCreation::isApplicable;
        this.layerRuleProvider = apiForArchRuleCreation::createLayerRule;
        this.packageRuleProvider = apiForArchRuleCreation::createPackageStructureRule;
        this.devStandardsRuleProvider = apiForArchRuleCreation::createDevStandardsRule;
    }

    public DelegatingArchRuleCreationServiceBuilder(RuleApplicabilityChecker ruleApplicabilityChecker) {
        this.ruleApplicabilityChecker = ruleApplicabilityChecker;
        this.layerRuleProvider = (module) -> null;
        this.packageRuleProvider = (module) -> null;
        this.devStandardsRuleProvider = (module) -> null;
    }

    @Override
    public ApiForCustomizingArchRuleCreation withApplicabilityChecker(ApiForArchRuleCreation apiForArchRuleCreation) {
        this.ruleApplicabilityChecker = apiForArchRuleCreation::isApplicable;
        return this;
    }

    @Override
    public ApiForCustomizingArchRuleCreation withApplicabilityChecker(RuleApplicabilityChecker checker) {
        this.ruleApplicabilityChecker = checker;
        return this;
    }

    @Override
    public ApiForCustomizingArchRuleCreation withLayerRule(ApiForArchRuleCreation apiForArchRuleCreation) {
        this.layerRuleProvider = apiForArchRuleCreation::createLayerRule;
        return this;
    }

    @Override
    public ApiForCustomizingArchRuleCreation withLayerRule(RuleProvider provider) {
        this.layerRuleProvider = provider;
        return this;
    }

    @Override
    public ApiForCustomizingArchRuleCreation withPackageStructureRule(ApiForArchRuleCreation apiForArchRuleCreation) {
        this.packageRuleProvider = apiForArchRuleCreation::createPackageStructureRule;
        return this;
    }

    @Override
    public ApiForCustomizingArchRuleCreation withPackageStructureRule(RuleProvider provider) {
        this.packageRuleProvider = provider;
        return this;
    }

    @Override
    public ApiForCustomizingArchRuleCreation withDevStandardsRule(ApiForArchRuleCreation apiForArchRuleCreation) {
        this.devStandardsRuleProvider = apiForArchRuleCreation::createDevStandardsRule;
        return this;
    }

    @Override
    public ApiForCustomizingArchRuleCreation withDevStandardsRule(RuleProvider provider) {
        this.devStandardsRuleProvider = provider;
        return this;
    }

    @Override
    public ApiForArchRuleCreation create() {
        return new DelegatingArchRuleCreationService(
                this.ruleApplicabilityChecker,
                this.layerRuleProvider,
                this.packageRuleProvider,
                this.devStandardsRuleProvider
        );
    }
}