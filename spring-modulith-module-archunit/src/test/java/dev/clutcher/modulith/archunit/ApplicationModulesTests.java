package dev.clutcher.modulith.archunit;

import com.tngtech.archunit.lang.CompositeArchRule;
import dev.clutcher.modulith.archunit.rules.app.api.ApiForArchRuleCreation;
import dev.clutcher.modulith.archunit.rules.app.api.ApiForCustomizingArchRuleCreation;
import dev.clutcher.modulith.archunit.rules.app.domain.services.ArchRuleRegistry;
import dev.clutcher.modulith.archunit.rules.app.domain.services.HexagonalArchRuleCreationService;
import dev.clutcher.modulith.archunit.rules.app.domain.services.library.CodeConventionsRulesLibrary;
import dev.clutcher.modulith.archunit.rules.app.domain.services.library.HexagonalArchitectureRulesLibrary;
import dev.clutcher.modulith.archunit.rules.app.spi.HexagonalArchitectureSettings;
import dev.clutcher.modulith.archunit.rules.out.spring.HexagonalPackageSettingsUsingSpringProperties;
import dev.clutcher.modulith.archunit.verifier.app.domain.services.ModuleArchitectureVerificationService;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.modulith.core.ApplicationModules;

import java.util.Arrays;
import java.util.List;

@Testable
class ApplicationModulesTests {

    @Test
    void shouldPassSpringModulithArchitectureRules() {
        // Given
        ApplicationModules applicationModules = ApplicationModules.of("dev.clutcher.modulith.archunit");
        applicationModules.forEach(System.out::println);

        ModuleArchitectureVerificationService verificationService = createInstanceOfVerificationService();

        // When
        applicationModules.verify();
        verificationService.verifyAllModules(applicationModules);

        // Then
        // No violation exceptions should be thrown.
    }

    private static ModuleArchitectureVerificationService createInstanceOfVerificationService() {
        HexagonalPackageSettingsUsingSpringProperties springProperties = new HexagonalPackageSettingsUsingSpringProperties();
        springProperties.setGeneratedClassAnnotations(Arrays.asList(
                "javax.annotation.processing.Generated",
                "jakarta.annotation.Generated"
        ));

        HexagonalArchitectureSettings settings = new HexagonalPackageSettingsUsingSpringProperties() {
            @Override
            public String[] getAdditionalDomainModelAllowedPackages() {
                return new String[]{
                        "com.tngtech.archunit..",
                        "org.springframework.modulith.."
                };
            }

            @Override
            public String[] getGeneratedClassAnnotations() {
                return springProperties.getGeneratedClassAnnotations();
            }
        };

        ArchRuleRegistry registry = new ArchRuleRegistry(ruleId -> true);
        HexagonalArchitectureRulesLibrary.allRules().forEach(registry::register);
        CodeConventionsRulesLibrary.allRules().forEach(registry::register);

        ApiForArchRuleCreation ruleCreation = ApiForCustomizingArchRuleCreation
                .forExistingArchRuleCreation(new HexagonalArchRuleCreationService(settings, registry))
                .withDevStandardsRule(module -> {
                    String base = module.getBasePackage().getName();
                    return CompositeArchRule
                            .of(HexagonalArchitectureRulesLibrary.ruleForApplicationServices(base, settings))
                            .and(HexagonalArchitectureRulesLibrary.ruleForDrivingPorts(base, settings))
                            .and(HexagonalArchitectureRulesLibrary.ruleForDrivenPorts(base, settings))
                            .and(HexagonalArchitectureRulesLibrary.ruleForDrivenAdapters(base, settings))
                            .and(HexagonalArchitectureRulesLibrary.ruleForDomainModelNotExposedInControllers(base, settings));
                })
                .withCodeConventionsRule(module -> {
                    String base = module.getBasePackage().getName();
                    String springAdapterPkg = base + settings.getSpringDrivingAdapterPackageMatcher();
                    String[] generatedAnnotations = settings.getGeneratedClassAnnotations();
                    return CompositeArchRule
                            .of(CodeConventionsRulesLibrary.ruleForNoDtoInClassNames(base))
                            .and(CodeConventionsRulesLibrary.ruleForNoImplPostfix(base, generatedAnnotations))
                            .and(CodeConventionsRulesLibrary.ruleForLoggerFieldNaming(base))
                            .and(CodeConventionsRulesLibrary.ruleForSpringAdapterPublicParameterTypes(springAdapterPkg))
                            .and(CodeConventionsRulesLibrary.ruleForMapperAnnotatedWithGenerated(base));
                })
                .create();

        return new ModuleArchitectureVerificationService(List.of(ruleCreation));
    }

}
