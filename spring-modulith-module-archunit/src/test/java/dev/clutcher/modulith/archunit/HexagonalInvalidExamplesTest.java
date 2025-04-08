package dev.clutcher.modulith.archunit;

import com.tngtech.archunit.core.importer.ImportOption;
import dev.clutcher.modulith.archunit.rules.app.domain.services.HexagonalArchRuleCreationService;
import dev.clutcher.modulith.archunit.rules.out.spring.HexagonalPackageSettingsUsingSpringProperties;
import dev.clutcher.modulith.archunit.verifier.app.domain.services.ModuleArchitectureVerificationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.modulith.core.ApplicationModule;
import org.springframework.modulith.core.ApplicationModules;

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

    private ApplicationModules applicationModules;
    private ModuleArchitectureVerificationService modulesArchitectureVerifier;

    @BeforeEach
    public void init() {
        ImportOption importOption = ImportOption.Predefined.ONLY_INCLUDE_TESTS;

        modulesArchitectureVerifier = new ModuleArchitectureVerificationService(
                List.of(new HexagonalArchRuleCreationService(new HexagonalPackageSettingsUsingSpringProperties())),
                importOption
        );

        applicationModules = ApplicationModules.of(
                "dev.clutcher.modulith.archunit.examples.hexagonal.invalid",
                importOption
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


    private void assertViolationThrown(String moduleName, String expectedViolationMessage) {
        // given
        ApplicationModule module = getModule(applicationModules, moduleName);

        // when
        AssertionError assertionError = Assertions.assertThrows(
                AssertionError.class,
                () -> modulesArchitectureVerifier.verifySingleModule(module, applicationModules)
        );

        // then
        String ruleViolationMessage = assertionError.getMessage();
        Assertions.assertTrue(
                ruleViolationMessage.contains(expectedViolationMessage),
                "Violation message does not contain expected message: " + ruleViolationMessage
        );
    }

    private static ApplicationModule getModule(ApplicationModules applicationModules, String moduleName) {
        return applicationModules.getModuleByName(moduleName).orElseThrow();
    }

}
