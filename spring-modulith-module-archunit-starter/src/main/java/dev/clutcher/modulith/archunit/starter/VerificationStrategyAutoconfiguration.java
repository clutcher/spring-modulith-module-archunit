package dev.clutcher.modulith.archunit.starter;

import dev.clutcher.modulith.archunit.rules.app.api.ApiForArchRuleCreation;
import dev.clutcher.modulith.archunit.rules.app.domain.services.HexagonalArchRuleCreationService;
import dev.clutcher.modulith.archunit.rules.app.spi.HexagonalArchitectureSettings;
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
    @ConditionalOnMissingBean
    public ApiForArchRuleCreation hexagonalArchRuleCreationService(HexagonalArchitectureSettings properties) {
        return new HexagonalArchRuleCreationService(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public ApiForModuleArchitectureVerification applicationModulesArchitectureVerifier(
            List<ApiForArchRuleCreation> apiForArchRuleCreationList
    ) {
        return new ModuleArchitectureVerificationService(apiForArchRuleCreationList);
    }

}
