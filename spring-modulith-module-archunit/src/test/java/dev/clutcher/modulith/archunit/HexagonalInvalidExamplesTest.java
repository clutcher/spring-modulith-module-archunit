package dev.clutcher.modulith.archunit;

import com.tngtech.archunit.core.importer.ImportOption;
import dev.clutcher.modulith.archunit.rules.app.domain.services.ArchRuleRegistry;
import dev.clutcher.modulith.archunit.rules.app.domain.services.HexagonalArchRuleCreationService;
import dev.clutcher.modulith.archunit.rules.app.domain.services.library.CodeConventionsRulesLibrary;
import dev.clutcher.modulith.archunit.rules.app.domain.services.library.HexagonalArchitectureRulesLibrary;
import dev.clutcher.modulith.archunit.rules.out.spring.HexagonalPackageSettingsUsingSpringProperties;
import dev.clutcher.modulith.archunit.verifier.app.domain.services.ModuleArchitectureVerificationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.modulith.core.ApplicationModule;
import org.springframework.modulith.core.ApplicationModules;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Testable
public class HexagonalInvalidExamplesTest {

    private static final String SERVICE_USED_IN_ADAPTERS_VIOLATION_MESSAGE = "Method <dev.clutcher.modulith.archunit.examples.hexagonal.invalid.applicationServicesUsedInAdapters.out.UsingAdapter.execute()> calls method <dev.clutcher.modulith.archunit.examples.hexagonal.invalid.applicationServicesUsedInAdapters.app.domain.services.SimpleService.execute()>";
    private static final String DRIVEN_PORT_USED_OUTSIDE_DRIVEN_ADAPTER_MESSAGE = "Class <dev.clutcher.modulith.archunit.examples.hexagonal.invalid.drivenPortUsedOutsideDrivenAdapter.in.DrivingAdapter> implements interface <dev.clutcher.modulith.archunit.examples.hexagonal.invalid.drivenPortUsedOutsideDrivenAdapter.app.spi.DrivenPort>";
    private static final String WRONG_DRIVEN_PORT_CLASS_TYPE_USED_MESSAGE = "Class <dev.clutcher.modulith.archunit.examples.hexagonal.invalid.wrongImplementationOfDrivenPorts.app.spi.WrongClassDefinitionForDrivenPort> is no interface";
    private static final String BAD_MODULE_ROOT_PACKAGE_STRUCTURE_MESSAGE = "Class <dev.clutcher.modulith.archunit.examples.hexagonal.invalid.undefinedPackagesInModuleRoot.test.SomeInterface> does reside outside of packages";
    private static final String BAD_DOMAIN_ROOT_PACKAGE_STRUCTURE_MESSAGE = "Class <dev.clutcher.modulith.archunit.examples.hexagonal.invalid.undefinedPackagesInDomainRoot.domain.test.SomeService> does reside outside of packages";
    private static final String BAD_APP_ROOT_PACKAGE_STRUCTURE_MESSAGE = "Class <dev.clutcher.modulith.archunit.examples.hexagonal.invalid.undefinedPackagesInAppRoot.app.test.SomeInterface> does reside outside of packages";
    private static final String WRONG_DRIVEN_ADAPTER_CLASS_NAME_MESSAGE = "Class <dev.clutcher.modulith.archunit.examples.hexagonal.invalid.wrongImplementationOfDrivenAdapter.out.WrongDrivenAdapterName> does not have simple name containing 'Using'";
    private static final String WRONG_APPLICATION_SERVICE_CLASS_NAME_MESSAGE = "Class <dev.clutcher.modulith.archunit.examples.hexagonal.invalid.wrongImplementationOfApplicationServices.app.domain.services.WrongServiceName> does not have simple name ending with 'Service'";
    private static final String DOMAIN_MODEL_EXPOSED_IN_CONTROLLER_MESSAGE = "returns domain model type";
    private static final String DTO_IN_CLASS_NAME_MESSAGE = "contains 'dto' (case-insensitive) in its name";
    private static final String DATA_IN_CLASS_NAME_MESSAGE = "has name ending with 'Data'";
    private static final String IMPL_POSTFIX_MESSAGE = "has simple name ending with 'Impl'";
    private static final String NON_RECORD_IN_DOMAIN_MODEL_MESSAGE = "is an interface, expected record, enum, or POJO";
    private static final String ABSTRACT_CLASS_IN_DOMAIN_MODEL_MESSAGE = "is abstract, expected record, enum, or POJO";
    private static final String EMPTY_CLASS_IN_DOMAIN_MODEL_MESSAGE = "has no instance fields, not a valid POJO";
    private static final String WRONG_LOGGER_FIELD_NAME_MESSAGE = "does not have name 'LOGGER'";
    private static final String DOMAIN_MODEL_DEPENDENCY_VIOLATION_MESSAGE = "should only depend on classes that reside in any package";
    private static final String CROSS_MODULE_DOMAIN_DEPENDENCY_MESSAGE = "has type <dev.clutcher.modulith.archunit.examples.hexagonal.valid.standard.app.domain.model.Order>";
    private static final String WRONG_SPRING_ADAPTER_NAMING_MESSAGE = "does not have simple name ending with 'Adapter'";
    private static final String MAPPER_WITHOUT_GENERATED_MESSAGE = "missing @AnnotateWith annotation";
    private static final String WRONG_PARAMETER_TYPE_NAMING_MESSAGE = "does not end with 'Criteria'";

    private static final ImportOption IMPORT_OPTION = ImportOption.Predefined.ONLY_INCLUDE_TESTS;
    private static final ApplicationModules APPLICATION_MODULES = ApplicationModules.of(
            "dev.clutcher.modulith.archunit.examples.hexagonal.invalid",
            IMPORT_OPTION
    );
    private static final ModuleArchitectureVerificationService MODULES_ARCHITECTURE_VERIFIER;

    static {
        HexagonalPackageSettingsUsingSpringProperties settings = new HexagonalPackageSettingsUsingSpringProperties();
        settings.setGeneratedClassAnnotations(Arrays.asList(
                "javax.annotation.processing.Generated",
                "jakarta.annotation.Generated"
        ));

        ArchRuleRegistry registry = new ArchRuleRegistry(ruleId -> true);
        HexagonalArchitectureRulesLibrary.allRules().forEach(registry::register);
        CodeConventionsRulesLibrary.allRules().forEach(registry::register);

        MODULES_ARCHITECTURE_VERIFIER = new ModuleArchitectureVerificationService(
                List.of(new HexagonalArchRuleCreationService(settings, registry)),
                IMPORT_OPTION
        );
    }

    @Test
    void shouldFailHexagonalRuleWhenApplicationServicesAreUsedInAdapters() throws NoSuchElementException {
        assertViolationThrown("applicationServicesUsedInAdapters", SERVICE_USED_IN_ADAPTERS_VIOLATION_MESSAGE);
    }

    @Test
    void shouldFailHexagonalRuleWhenDrivenPortUsedOutsideDrivenAdapter() throws NoSuchElementException {
        assertViolationThrown("drivenPortUsedOutsideDrivenAdapter", DRIVEN_PORT_USED_OUTSIDE_DRIVEN_ADAPTER_MESSAGE);
    }

    @Test
    void shouldFailHexagonalRuleWhenUndefinedPackageIsPresentInAppRoot() throws NoSuchElementException {
        assertViolationThrown("undefinedPackagesInAppRoot", BAD_APP_ROOT_PACKAGE_STRUCTURE_MESSAGE);
    }

    @Test
    void shouldFailHexagonalRuleWhenUndefinedPackageIsPresentInDomainRoot() throws NoSuchElementException {
        assertViolationThrown("undefinedPackagesInDomainRoot", BAD_DOMAIN_ROOT_PACKAGE_STRUCTURE_MESSAGE);
    }

    @Test
    void shouldFailHexagonalRuleWhenUndefinedPackageIsPresentInModuleRoot() throws NoSuchElementException {
        assertViolationThrown("undefinedPackagesInModuleRoot", BAD_MODULE_ROOT_PACKAGE_STRUCTURE_MESSAGE);
    }

    @Test
    void shouldFollowApplicationServiceNamingRule() throws NoSuchElementException {
        assertViolationThrown("wrongImplementationOfApplicationServices", WRONG_APPLICATION_SERVICE_CLASS_NAME_MESSAGE);
    }

    @Test
    void shouldFollowDrivenAdapterNameRule() throws NoSuchElementException {
        assertViolationThrown("wrongImplementationOfDrivenAdapter", WRONG_DRIVEN_ADAPTER_CLASS_NAME_MESSAGE);
    }

    @Test
    void shouldFollowDrivenPortClassTypeRule() throws NoSuchElementException {
        assertViolationThrown("wrongImplementationOfDrivenPorts", WRONG_DRIVEN_PORT_CLASS_TYPE_USED_MESSAGE);
    }

    @Test
    void shouldFailWhenDomainModelExposedInController() throws NoSuchElementException {
        assertViolationThrown("domainModelExposedInController", DOMAIN_MODEL_EXPOSED_IN_CONTROLLER_MESSAGE);
    }

    @Test
    void shouldFailWhenClassNameContainsDto() throws NoSuchElementException {
        assertViolationThrown("dtoInClassName", DTO_IN_CLASS_NAME_MESSAGE);
    }

    @Test
    void shouldFailWhenClassNameContainsUppercaseDTO() throws NoSuchElementException {
        assertViolationThrown("uppercaseDtoInClassName", DTO_IN_CLASS_NAME_MESSAGE);
    }

    @Test
    void shouldFailWhenClassNameEndsWithData() throws NoSuchElementException {
        assertViolationThrown("dataInClassName", DATA_IN_CLASS_NAME_MESSAGE);
    }

    @Test
    void shouldFailWhenClassNameEndsWithImpl() throws NoSuchElementException {
        assertViolationThrown("implPostfixInClassName", IMPL_POSTFIX_MESSAGE);
    }

    @Test
    void shouldFailWhenInterfaceUsedInDomainModel() throws NoSuchElementException {
        assertViolationThrown("nonRecordInDomainModel", NON_RECORD_IN_DOMAIN_MODEL_MESSAGE);
    }

    @Test
    void shouldFailWhenAbstractClassUsedInDomainModel() throws NoSuchElementException {
        assertViolationThrown("abstractClassInDomainModel", ABSTRACT_CLASS_IN_DOMAIN_MODEL_MESSAGE);
    }

    @Test
    void shouldFailWhenEmptyClassUsedInDomainModel() throws NoSuchElementException {
        assertViolationThrown("emptyClassInDomainModel", EMPTY_CLASS_IN_DOMAIN_MODEL_MESSAGE);
    }

    @Test
    void shouldFailWhenLoggerFieldNotNamedLogger() throws NoSuchElementException {
        assertViolationThrown("wrongLoggerFieldName", WRONG_LOGGER_FIELD_NAME_MESSAGE);
    }

    @Test
    void shouldFailWhenDomainModelDependsOnExternalLibraries() throws NoSuchElementException {
        assertViolationThrown("domainModelDependencyViolation", DOMAIN_MODEL_DEPENDENCY_VIOLATION_MESSAGE);
    }

    @Test
    void shouldFailWhenDomainDependsOnAnotherModuleDomain() throws NoSuchElementException {
        assertViolationThrown("crossModuleDomainDependency", CROSS_MODULE_DOMAIN_DEPENDENCY_MESSAGE);
    }

    @Test
    void shouldFailWhenSpringAdapterDoesNotEndWithAdapter() throws NoSuchElementException {
        assertViolationThrown("wrongSpringAdapterNaming", WRONG_SPRING_ADAPTER_NAMING_MESSAGE);
    }

    @Test
    void shouldFailWhenMapperMissingAnnotateWithGenerated() throws NoSuchElementException {
        assertViolationThrown("mapperWithoutGenerated", MAPPER_WITHOUT_GENERATED_MESSAGE);
    }

    @Test
    void shouldFailWhenMethodParameterTypeDoesNotFollowNamingConvention() throws NoSuchElementException {
        assertViolationThrown("wrongParameterTypeNaming", WRONG_PARAMETER_TYPE_NAMING_MESSAGE);
    }


    private void assertViolationThrown(String moduleName, String expectedViolationMessage) {
        // given
        ApplicationModule module = APPLICATION_MODULES.getModuleByName(moduleName).orElseThrow();

        // when
        AssertionError assertionError = Assertions.assertThrows(
                AssertionError.class,
                () -> MODULES_ARCHITECTURE_VERIFIER.verifySingleModule(module, APPLICATION_MODULES)
        );

        // then
        String ruleViolationMessage = assertionError.getMessage();
        Assertions.assertTrue(
                ruleViolationMessage.contains(expectedViolationMessage),
                "Violation message does not contain expected message: " + ruleViolationMessage
        );
    }

}
