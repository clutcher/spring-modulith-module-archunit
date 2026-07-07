package dev.clutcher.modulith.archunit.rules.app.spi;

import com.tngtech.archunit.lang.ArchRule;
import dev.clutcher.modulith.archunit.rules.app.domain.model.RuleGroup;

import java.util.function.BiFunction;

public interface NamedArchRule {

    String getId();

    RuleGroup getGroup();

    ArchRule create(String moduleBasePackage, HexagonalArchitectureSettings settings);

    static NamedArchRule of(String id, RuleGroup group,
                            BiFunction<String, HexagonalArchitectureSettings, ArchRule> factory) {
        return new NamedArchRule() {
            @Override
            public String getId() { return id; }
            @Override
            public RuleGroup getGroup() { return group; }
            @Override
            public ArchRule create(String moduleBasePackage, HexagonalArchitectureSettings settings) {
                return factory.apply(moduleBasePackage, settings);
            }
        };
    }
}
