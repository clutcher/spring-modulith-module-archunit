package dev.clutcher.modulith.archunit.rules.out.spring;

import dev.clutcher.modulith.archunit.rules.app.spi.ArchRuleToggleSettings;

import java.util.HashMap;
import java.util.Map;

public class ArchRuleToggleSettingsUsingSpringProperties implements ArchRuleToggleSettings {

    private Map<String, Boolean> toggle = new HashMap<>();

    @Override
    public boolean isRuleEnabled(String ruleId) {
        return toggle.getOrDefault(ruleId, true);
    }

    public Map<String, Boolean> getToggle() {
        return toggle;
    }

    public void setToggle(Map<String, Boolean> toggle) {
        this.toggle = toggle;
    }
}
