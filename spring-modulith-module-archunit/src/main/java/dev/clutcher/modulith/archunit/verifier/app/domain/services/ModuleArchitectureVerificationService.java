package dev.clutcher.modulith.archunit.verifier.app.domain.services;


import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import dev.clutcher.modulith.archunit.rules.app.api.ApiForArchRuleCreation;
import dev.clutcher.modulith.archunit.verifier.app.api.ApiForModuleArchitectureVerification;
import org.springframework.modulith.core.ApplicationModule;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.core.DependencyDepth;
import org.springframework.modulith.core.JavaPackage;

import java.util.List;

public class ModuleArchitectureVerificationService implements ApiForModuleArchitectureVerification {

    private final List<ApiForArchRuleCreation> apiForArchRuleCreationList;
    private final ImportOption importOption;

    public ModuleArchitectureVerificationService(
            List<ApiForArchRuleCreation> apiForArchRuleCreationList
    ) {
        this(apiForArchRuleCreationList, ImportOption.Predefined.DO_NOT_INCLUDE_TESTS);
    }

    public ModuleArchitectureVerificationService(
            List<ApiForArchRuleCreation> apiForArchRuleCreationList,
            ImportOption importOption
    ) {
        this.apiForArchRuleCreationList = apiForArchRuleCreationList;
        this.importOption = importOption;
    }

    @Override
    public void verifyAllModules(ApplicationModules applicationModules) {
        for (ApplicationModule module : applicationModules) {
            verifySingleModule(module, applicationModules);
        }
    }

    @Override
    public void verifySingleModule(ApplicationModule module, ApplicationModules applicationModules) {
        JavaClasses allClassesRelatedToModule = getModuleRelatedJavaClasses(
                module,
                applicationModules,
                importOption
        );

        for (ApiForArchRuleCreation apiForArchRuleCreation : apiForArchRuleCreationList) {
            if (!apiForArchRuleCreation.isApplicable(module, allClassesRelatedToModule)) {
                continue;
            }

            checkAllRules(apiForArchRuleCreation, module, allClassesRelatedToModule);
        }
    }

    protected void checkAllRules(
            ApiForArchRuleCreation apiForArchRuleCreation,
            ApplicationModule module,
            JavaClasses allClassesRelatedToModule
    ) {
        ArchRule packageStructureRule = apiForArchRuleCreation.createPackageStructureRule(module);
        if (packageStructureRule != null) {
            packageStructureRule.check(allClassesRelatedToModule);
        }

        ArchRule layerRule = apiForArchRuleCreation.createLayerRule(module);
        if (layerRule != null) {
            layerRule.check(allClassesRelatedToModule);
        }

        ArchRule devStandardsRule = apiForArchRuleCreation.createDevStandardsRule(module);
        if (devStandardsRule != null) {
            devStandardsRule.check(allClassesRelatedToModule);
        }
    }

    protected JavaClasses getModuleRelatedJavaClasses(
            ApplicationModule module,
            ApplicationModules applicationModules,
            ImportOption importOption
    ) {
        List<String> list = module.getBootstrapBasePackages(applicationModules, DependencyDepth.ALL)
                .map(JavaPackage::getName)
                .toList();
        return new ClassFileImporter(List.of(importOption)).importPackages(list);
    }

}
