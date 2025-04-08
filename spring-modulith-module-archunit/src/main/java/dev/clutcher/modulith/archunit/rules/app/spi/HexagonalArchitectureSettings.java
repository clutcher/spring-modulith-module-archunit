package dev.clutcher.modulith.archunit.rules.app.spi;

public interface HexagonalArchitectureSettings {
    String getDrivingPortPackageMatcher();

    String getDrivenPortPackageMatcher();

    String getDrivingAdapterPackageMatcher();

    String getDrivenAdapterPackageMatcher();

    String getApplicationServicesPackageMatcher();

    String getApplicationConfigurationPackageMatcher();

    String getApplicationRoot();
}