package dev.clutcher.modulith.archunit.rules.app.api;

import dev.clutcher.modulith.archunit.rules.app.domain.model.RuleApplicabilityChecker;
import dev.clutcher.modulith.archunit.rules.app.domain.model.RuleProvider;
import dev.clutcher.modulith.archunit.rules.app.domain.services.builder.DelegatingArchRuleCreationServiceBuilder;

public interface ApiForCustomizingArchRuleCreation {

    static ApiForCustomizingArchRuleCreation forExistingArchRuleCreation(ApiForArchRuleCreation apiForArchRuleCreation) {
        return new DelegatingArchRuleCreationServiceBuilder(apiForArchRuleCreation);
    }

    static ApiForCustomizingArchRuleCreation forApplicabilityChecker(RuleApplicabilityChecker checker) {
        return new DelegatingArchRuleCreationServiceBuilder(checker);
    }

    ApiForCustomizingArchRuleCreation withApplicabilityChecker(ApiForArchRuleCreation apiForArchRuleCreation);

    ApiForCustomizingArchRuleCreation withApplicabilityChecker(RuleApplicabilityChecker checker);

    ApiForCustomizingArchRuleCreation withLayerRule(ApiForArchRuleCreation apiForArchRuleCreation);

    ApiForCustomizingArchRuleCreation withLayerRule(RuleProvider provider);

    ApiForCustomizingArchRuleCreation withPackageStructureRule(ApiForArchRuleCreation apiForArchRuleCreation);

    ApiForCustomizingArchRuleCreation withPackageStructureRule(RuleProvider provider);

    ApiForCustomizingArchRuleCreation withDevStandardsRule(ApiForArchRuleCreation apiForArchRuleCreation);

    ApiForCustomizingArchRuleCreation withDevStandardsRule(RuleProvider provider);

    ApiForArchRuleCreation create();

}