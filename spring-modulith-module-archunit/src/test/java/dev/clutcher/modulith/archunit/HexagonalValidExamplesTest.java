package dev.clutcher.modulith.archunit;

import com.tngtech.archunit.core.importer.ImportOption;
import dev.clutcher.modulith.archunit.rules.app.domain.services.HexagonalArchRuleCreationService;
import dev.clutcher.modulith.archunit.rules.out.spring.HexagonalPackageSettingsUsingSpringProperties;
import dev.clutcher.modulith.archunit.verifier.app.domain.services.ModuleArchitectureVerificationService;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.modulith.core.ApplicationModules;

import java.util.List;

@Testable
public class HexagonalValidExamplesTest {

    @Test
    void shouldPassHexagonalArchitectureRulesForValidCases() {
        // given
        ImportOption importOption = ImportOption.Predefined.ONLY_INCLUDE_TESTS;

        ApplicationModules applicationModules = ApplicationModules.of(
                "dev.clutcher.modulith.archunit.examples.hexagonal.valid",
                importOption
        );

        ModuleArchitectureVerificationService modulesArchitectureVerifier = new ModuleArchitectureVerificationService(
                List.of(new HexagonalArchRuleCreationService(new HexagonalPackageSettingsUsingSpringProperties())),
                importOption
        );

        // when
        modulesArchitectureVerifier.verifyAllModules(applicationModules);

        // then
        // No violation exceptions should be thrown.
    }

}
