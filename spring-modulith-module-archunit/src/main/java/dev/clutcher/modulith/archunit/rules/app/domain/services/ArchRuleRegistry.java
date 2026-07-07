package dev.clutcher.modulith.archunit.rules.app.domain.services;

import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.CompositeArchRule;
import dev.clutcher.modulith.archunit.rules.app.domain.model.RuleGroup;
import dev.clutcher.modulith.archunit.rules.app.spi.ArchRuleToggleSettings;
import dev.clutcher.modulith.archunit.rules.app.spi.HexagonalArchitectureSettings;
import dev.clutcher.modulith.archunit.rules.app.spi.NamedArchRule;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ArchRuleRegistry {

    private final Map<String, NamedArchRule> rules = new LinkedHashMap<>();
    private final ArchRuleToggleSettings toggleSettings;

    public ArchRuleRegistry(ArchRuleToggleSettings toggleSettings) {
        this.toggleSettings = toggleSettings;
    }

    public void register(NamedArchRule rule) {
        rules.put(rule.getId(), rule);
    }

    public ArchRule buildGroupRule(RuleGroup group, String moduleBasePackage, HexagonalArchitectureSettings settings) {
        List<ArchRule> enabledRules = rules.values().stream()
                .filter(r -> r.getGroup() == group)
                .filter(r -> toggleSettings.isRuleEnabled(r.getId()))
                .map(r -> r.create(moduleBasePackage, settings))
                .filter(Objects::nonNull)
                .toList();

        if (enabledRules.isEmpty()) {
            return null;
        }

        CompositeArchRule composite = CompositeArchRule.of(enabledRules.get(0));
        for (int i = 1; i < enabledRules.size(); i++) {
            composite = composite.and(enabledRules.get(i));
        }
        return composite;
    }
}
