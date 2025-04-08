package dev.clutcher.modulith.archunit.rules.app.domain.services.builder;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import dev.clutcher.modulith.archunit.rules.app.api.ApiForArchRuleCreation;
import dev.clutcher.modulith.archunit.rules.app.api.ApiForCustomizingArchRuleCreation;
import dev.clutcher.modulith.archunit.rules.app.domain.model.RuleApplicabilityChecker;
import dev.clutcher.modulith.archunit.rules.app.domain.services.HexagonalArchRuleCreationService;
import dev.clutcher.modulith.archunit.rules.out.spring.HexagonalPackageSettingsUsingSpringProperties;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.context.annotation.Bean;
import org.springframework.modulith.core.ApplicationModule;
import org.springframework.modulith.core.ApplicationModules;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testable
class DelegatingArchRuleCreationServiceBuilderTest {

    private static final RuleApplicabilityChecker ALWAYS_APPLICABLE_FUNCTION = (module1, allClassesRelatedToModule) -> true;
    private static final HexagonalArchRuleCreationService DEFAULT_HEXAGONAL_ARCH_RULES_INSTANCE = new HexagonalArchRuleCreationService(
            new HexagonalPackageSettingsUsingSpringProperties()
    );

    @Test
    void shouldNotProvideRuleWhenNotSetInBuilder() {
        // given
        ApplicationModule module = getModuleForTest();

        // when
        ApiForArchRuleCreation apiForArchRuleCreation = ApiForCustomizingArchRuleCreation
                .forApplicabilityChecker(ALWAYS_APPLICABLE_FUNCTION)
                .create();

        // then
        assertTrue(apiForArchRuleCreation.isApplicable(module, null));

        ArchRule layerRule = apiForArchRuleCreation.createLayerRule(module);
        assertNull(layerRule);

        ArchRule packageStructureRule = apiForArchRuleCreation.createPackageStructureRule(module);
        assertNull(packageStructureRule);

        ArchRule devStandardsRule = apiForArchRuleCreation.createDevStandardsRule(module);
        assertNull(devStandardsRule);
    }

    @Test
    void shouldProvideRuleWhenDefaultStrategyIsUsed() {
        // given
        ApplicationModule module = getModuleForTest();

        // when
        ApiForArchRuleCreation apiForArchRuleCreation = ApiForCustomizingArchRuleCreation
                .forApplicabilityChecker(ALWAYS_APPLICABLE_FUNCTION)
                .withLayerRule(DEFAULT_HEXAGONAL_ARCH_RULES_INSTANCE)
                .withDevStandardsRule(DEFAULT_HEXAGONAL_ARCH_RULES_INSTANCE)
                .withPackageStructureRule(DEFAULT_HEXAGONAL_ARCH_RULES_INSTANCE)
                .create();

        // then
        assertTrue(apiForArchRuleCreation.isApplicable(module, null));

        ArchRule layerRule = apiForArchRuleCreation.createLayerRule(module);
        assertNotNull(layerRule);

        ArchRule packageStructureRule = apiForArchRuleCreation.createPackageStructureRule(module);
        assertNotNull(packageStructureRule);

        ArchRule devStandardsRule = apiForArchRuleCreation.createDevStandardsRule(module);
        assertNotNull(devStandardsRule);
    }

    @Test
    void shouldProvideRuleWhenRulesProvidedDirectly() {
        // given
        ApplicationModule module = getModuleForTest();

        // when
        ApiForArchRuleCreation apiForArchRuleCreation = ApiForCustomizingArchRuleCreation
                .forApplicabilityChecker(ALWAYS_APPLICABLE_FUNCTION)
                .withLayerRule((m) -> createTestRule())
                .withDevStandardsRule((m) -> createTestRule())
                .withPackageStructureRule((m) -> createTestRule())
                .create();

        // then
        assertTrue(apiForArchRuleCreation.isApplicable(module, null));

        ArchRule layerRule = apiForArchRuleCreation.createLayerRule(module);
        assertNotNull(layerRule);

        ArchRule packageStructureRule = apiForArchRuleCreation.createPackageStructureRule(module);
        assertNotNull(packageStructureRule);

        ArchRule devStandardsRule = apiForArchRuleCreation.createDevStandardsRule(module);
        assertNotNull(devStandardsRule);
    }

    @Test
    void shouldProvideRuleWhenExistingArchRuleCreationServiceProvided() {
        // given
        ApplicationModule module = getModuleForTest();

        // when
        ApiForArchRuleCreation apiForArchRuleCreation = ApiForCustomizingArchRuleCreation
                .forExistingArchRuleCreation(DEFAULT_HEXAGONAL_ARCH_RULES_INSTANCE)
                .withApplicabilityChecker(ALWAYS_APPLICABLE_FUNCTION)
                .withLayerRule((m) -> null)
                .create();

        // then
        assertTrue(apiForArchRuleCreation.isApplicable(module, null));

        ArchRule layerRule = apiForArchRuleCreation.createLayerRule(module);
        assertNull(layerRule);

        ArchRule packageStructureRule = apiForArchRuleCreation.createPackageStructureRule(module);
        assertNotNull(packageStructureRule);

        ArchRule devStandardsRule = apiForArchRuleCreation.createDevStandardsRule(module);
        assertNotNull(devStandardsRule);
    }


    @Bean
    public ApiForArchRuleCreation customArchRuleCreation() {
        return ApiForCustomizingArchRuleCreation
                .forApplicabilityChecker((m, allClassesRelatedToModule) -> true)
                .withPackageStructureRule((m) -> null)
                .create();
    }

    static ArchRule createTestRule() {
        return noClasses()
                .that().resideInAPackage(".app..")
                .should().resideOutsideOfPackage(".app.api..")
                .allowEmptyShould(true);
    }

    private static ApplicationModule getModuleForTest() {
        ApplicationModules applicationModules = ApplicationModules.of(
                "dev.clutcher.modulith.archunit.examples.hexagonal.valid",
                ImportOption.Predefined.ONLY_INCLUDE_TESTS
        );
        return applicationModules.getModuleByName("standard").orElseThrow();
    }

}