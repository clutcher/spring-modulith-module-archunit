package dev.clutcher.modulith.archunit.rules.app.domain.model;

public enum RuleGroup {

    LAYER("layer"),
    PACKAGE_STRUCTURE("package-structure"),
    DEV_STANDARDS("dev-standards"),
    CODE_CONVENTIONS("code-conventions");

    private final String value;

    RuleGroup(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
