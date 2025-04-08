package dev.clutcher.modulith.archunit.rules.app.domain.services.library;

import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;
import dev.clutcher.modulith.archunit.rules.app.spi.HexagonalArchitectureSettings;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideOutsideOfPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class HexagonalArchitectureRulesLibrary {

    private static final String HEXAGONAL_DRIVING_PORTS_LAYER_NAME = "Driving Ports";
    private static final String HEXAGONAL_DRIVEN_PORTS_LAYER_NAME = "Driven Ports";
    private static final String HEXAGONAL_DRIVING_ADAPTERS_LAYER_NAME = "Driving Adapters";
    private static final String HEXAGONAL_DRIVEN_ADAPTERS_LAYER_NAME = "Driven Adapters";
    private static final String HEXAGONAL_APPLICATION_SERVICES_LAYER_NAME = "Application Services";
    private static final String HEXAGONAL_APPLICATION_CONFIGURATION_LAYER_NAME = "Application Configuration";

    public static Architectures.LayeredArchitecture createLayerDefinitionRule(String moduleBasePackage, HexagonalArchitectureSettings properties) {
        return Architectures.layeredArchitecture()
                .consideringAllDependencies()
                .withOptionalLayers(true)

                .layer(HEXAGONAL_DRIVING_PORTS_LAYER_NAME).definedBy(moduleBasePackage + properties.getDrivingPortPackageMatcher())
                .layer(HEXAGONAL_DRIVEN_PORTS_LAYER_NAME).definedBy(moduleBasePackage + properties.getDrivenPortPackageMatcher())
                .layer(HEXAGONAL_DRIVING_ADAPTERS_LAYER_NAME).definedBy(moduleBasePackage + properties.getDrivingAdapterPackageMatcher())
                .layer(HEXAGONAL_DRIVEN_ADAPTERS_LAYER_NAME).definedBy(moduleBasePackage + properties.getDrivenAdapterPackageMatcher())
                .layer(HEXAGONAL_APPLICATION_SERVICES_LAYER_NAME).definedBy(moduleBasePackage + properties.getApplicationServicesPackageMatcher())
                .layer(HEXAGONAL_APPLICATION_CONFIGURATION_LAYER_NAME).definedBy(moduleBasePackage + properties.getApplicationConfigurationPackageMatcher())

                .whereLayer(HEXAGONAL_DRIVING_PORTS_LAYER_NAME)
                .mayOnlyBeAccessedByLayers(
                        HEXAGONAL_DRIVING_ADAPTERS_LAYER_NAME,
                        HEXAGONAL_APPLICATION_SERVICES_LAYER_NAME,
                        HEXAGONAL_APPLICATION_CONFIGURATION_LAYER_NAME)
                .ignoreDependency(
                        resideOutsideOfPackage(moduleBasePackage + ".."),
                        resideInAPackage(moduleBasePackage + "..")
                )

                .whereLayer(HEXAGONAL_DRIVEN_PORTS_LAYER_NAME)
                .mayOnlyBeAccessedByLayers(
                        HEXAGONAL_DRIVEN_ADAPTERS_LAYER_NAME,
                        HEXAGONAL_APPLICATION_SERVICES_LAYER_NAME,
                        HEXAGONAL_APPLICATION_CONFIGURATION_LAYER_NAME)

                .whereLayer(HEXAGONAL_APPLICATION_SERVICES_LAYER_NAME)
                .mayOnlyBeAccessedByLayers(
                        HEXAGONAL_DRIVING_PORTS_LAYER_NAME,
                        HEXAGONAL_DRIVEN_PORTS_LAYER_NAME,
                        HEXAGONAL_APPLICATION_CONFIGURATION_LAYER_NAME)

                .whereLayer(HEXAGONAL_DRIVING_ADAPTERS_LAYER_NAME)
                .mayOnlyBeAccessedByLayers(HEXAGONAL_APPLICATION_CONFIGURATION_LAYER_NAME)

                .whereLayer(HEXAGONAL_DRIVEN_ADAPTERS_LAYER_NAME)
                .mayOnlyBeAccessedByLayers(HEXAGONAL_APPLICATION_CONFIGURATION_LAYER_NAME);
    }

    public static ArchRule ruleForModuleRootPackageStructure(String moduleBasePackage, HexagonalArchitectureSettings properties) {
        return noClasses()
                .that().resideInAPackage(moduleBasePackage + "..")
                .should().resideOutsideOfPackages(
                        moduleBasePackage + properties.getApplicationRoot() + "..",
                        moduleBasePackage + properties.getApplicationConfigurationPackageMatcher(),
                        moduleBasePackage + properties.getDrivingAdapterPackageMatcher(),
                        moduleBasePackage + properties.getDrivenAdapterPackageMatcher()
                )
                .allowEmptyShould(true);
    }

    public static ArchRule ruleForApplicationPortsPackageStructure(String moduleBasePackage, HexagonalArchitectureSettings properties) {
        return noClasses()
                .that().resideInAPackage(moduleBasePackage + properties.getApplicationRoot() + "..")
                .should().resideOutsideOfPackages(
                        moduleBasePackage + properties.getDrivingPortPackageMatcher(),
                        moduleBasePackage + properties.getDrivenPortPackageMatcher(),
                        moduleBasePackage + properties.getApplicationServicesPackageMatcher(),
                        moduleBasePackage + properties.getApplicationRoot() + ".domain.model.."
                )
                .allowEmptyShould(true);
    }

    public static ArchRule ruleForAdaptersPackageStructure(String moduleBasePackage, HexagonalArchitectureSettings properties) {
        return noClasses()
                .that().resideInAnyPackage(
                        moduleBasePackage + properties.getDrivingAdapterPackageMatcher(),
                        moduleBasePackage + properties.getDrivenAdapterPackageMatcher()
                )
                .should().resideInAPackage(
                        moduleBasePackage + properties.getApplicationRoot() + ".."
                )
                .allowEmptyShould(true);
    }


    public static ArchRule ruleForDrivingPorts(String moduleBasePackage, HexagonalArchitectureSettings properties) {
        return classes()
                .that().resideInAPackage(moduleBasePackage + properties.getDrivingPortPackageMatcher())
                .and().doNotHaveSimpleName("package-info")
                .should().beInterfaces()
                .andShould().haveSimpleNameStartingWith("ApiFor")
                .allowEmptyShould(true);
    }

    public static ArchRule ruleForDrivenPorts(String moduleBasePackage, HexagonalArchitectureSettings properties) {
        return classes()
                .that().resideInAPackage(moduleBasePackage + properties.getDrivenPortPackageMatcher())
                .should().beInterfaces()
                .allowEmptyShould(true);
    }

    public static ArchRule ruleForApplicationServices(String moduleBasePackage, HexagonalArchitectureSettings properties) {
        return classes()
                .that().resideInAPackage(moduleBasePackage + properties.getApplicationServicesPackageMatcher())
                .and().implement(resideInAPackage(moduleBasePackage + properties.getDrivingPortPackageMatcher()))
                .should().haveSimpleNameEndingWith("Service")
                .orShould().haveSimpleNameEndingWith("ServiceBuilder")
                .orShould().haveSimpleNameEndingWith("ServiceFactory")
                .andShould().notBeInterfaces()
                .allowEmptyShould(true);
    }

    public static ArchRule ruleForDrivenAdapters(String moduleBasePackage, HexagonalArchitectureSettings properties) {
        return classes()
                .that()
                .resideInAPackage(moduleBasePackage + properties.getDrivenAdapterPackageMatcher())
                .and()
                .implement(resideInAPackage(moduleBasePackage + properties.getDrivenPortPackageMatcher()))
                .should()
                .haveSimpleNameContaining("Using")
                .allowEmptyShould(true);
    }
}
