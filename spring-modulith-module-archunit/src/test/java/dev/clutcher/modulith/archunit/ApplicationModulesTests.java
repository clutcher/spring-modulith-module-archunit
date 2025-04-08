package dev.clutcher.modulith.archunit;

import dev.clutcher.modulith.archunit.rules.app.domain.services.HexagonalArchRuleCreationService;
import dev.clutcher.modulith.archunit.rules.out.spring.HexagonalPackageSettingsUsingSpringProperties;
import dev.clutcher.modulith.archunit.verifier.app.domain.services.ModuleArchitectureVerificationService;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.modulith.core.ApplicationModules;

import java.util.List;

@Testable
class ApplicationModulesTests {

    @Test
    void shouldPassSpringModulithArchitectureRules() {
        // given
        ApplicationModules applicationModules = ApplicationModules.of("dev.clutcher.modulith.archunit");
        applicationModules.forEach(System.out::println);

        ModuleArchitectureVerificationService verificationService = createInstanceOfVerificationService();

        // when
        applicationModules.verify();
        verificationService.verifyAllModules(applicationModules);

        // then
        // No violation exceptions should be thrown.
    }

    private static ModuleArchitectureVerificationService createInstanceOfVerificationService() {
        return new ModuleArchitectureVerificationService(
                List.of(
                        new HexagonalArchRuleCreationService(new HexagonalPackageSettingsUsingSpringProperties())
                )
        );
    }

}
