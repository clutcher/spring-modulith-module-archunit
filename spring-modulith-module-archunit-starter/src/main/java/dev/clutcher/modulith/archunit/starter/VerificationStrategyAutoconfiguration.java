package dev.clutcher.modulith.archunit.starter;

import dev.clutcher.modulith.archunit.rules.app.api.ApiForArchRuleCreation;
import dev.clutcher.modulith.archunit.rules.app.domain.services.ArchRuleRegistry;
import dev.clutcher.modulith.archunit.rules.app.domain.services.HexagonalArchRuleCreationService;
import dev.clutcher.modulith.archunit.rules.app.domain.services.library.CodeConventionsRulesLibrary;
import dev.clutcher.modulith.archunit.rules.app.domain.services.library.HexagonalArchitectureRulesLibrary;
import dev.clutcher.modulith.archunit.rules.app.spi.ArchRuleToggleSettings;
import dev.clutcher.modulith.archunit.rules.app.spi.HexagonalArchitectureSettings;
import dev.clutcher.modulith.archunit.rules.app.spi.NamedArchRule;
import dev.clutcher.modulith.archunit.rules.out.spring.ArchRuleToggleSettingsUsingSpringProperties;
import dev.clutcher.modulith.archunit.rules.out.spring.HexagonalPackageSettingsUsingSpringProperties;
import dev.clutcher.modulith.archunit.verifier.app.api.ApiForModuleArchitectureVerification;
import dev.clutcher.modulith.archunit.verifier.app.domain.services.ModuleArchitectureVerificationService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

@AutoConfiguration
@EnableConfigurationProperties
public class VerificationStrategyAutoconfiguration {

    @Bean
    @ConfigurationProperties(prefix = "dev.clutcher.modulith.archunit.rules.hexagonal.package")
    public HexagonalArchitectureSettings hexagonalArchitectureVerificationProperties() {
        return new HexagonalPackageSettingsUsingSpringProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "dev.clutcher.modulith.archunit.rules")
    @ConditionalOnMissingBean
    public ArchRuleToggleSettings archRuleToggleSettings() {
        return new ArchRuleToggleSettingsUsingSpringProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public ArchRuleRegistry archRuleRegistry(ArchRuleToggleSettings toggleSettings,
                                             List<NamedArchRule> customRules) {
        ArchRuleRegistry registry = new ArchRuleRegistry(toggleSettings);
        HexagonalArchitectureRulesLibrary.allRules().forEach(registry::register);
        CodeConventionsRulesLibrary.allRules().forEach(registry::register);
        customRules.forEach(registry::register);
        return registry;
    }

    @Bean
    @ConditionalOnMissingBean
    public ApiForArchRuleCreation hexagonalArchRuleCreationService(HexagonalArchitectureSettings properties,
                                                                    ArchRuleRegistry registry) {
        return new HexagonalArchRuleCreationService(properties, registry);
    }

    @Bean
    @ConditionalOnMissingBean
    public ApiForModuleArchitectureVerification applicationModulesArchitectureVerifier(
            List<ApiForArchRuleCreation> apiForArchRuleCreationList
    ) {
        return new ModuleArchitectureVerificationService(apiForArchRuleCreationList);
    }

}
