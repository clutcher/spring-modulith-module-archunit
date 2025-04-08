package dev.clutcher.modulith.archunit.rules.out.spring;

import dev.clutcher.modulith.archunit.rules.app.spi.HexagonalArchitectureSettings;

public class HexagonalPackageSettingsUsingSpringProperties implements HexagonalArchitectureSettings {

    private Port port = new Port();
    private Adapter adapter = new Adapter();
    private Application application = new Application();

    public static class Port {
        private String driving = ".api..";
        private String driven = ".spi..";

        public String getDriving() {
            return driving;
        }

        public void setDriving(String driving) {
            this.driving = driving;
        }

        public String getDriven() {
            return driven;
        }

        public void setDriven(String driven) {
            this.driven = driven;
        }
    }

    public static class Adapter {
        private String driving = ".in..";
        private String driven = ".out..";

        public String getDriving() {
            return driving;
        }

        public void setDriving(String driving) {
            this.driving = driving;
        }

        public String getDriven() {
            return driven;
        }

        public void setDriven(String driven) {
            this.driven = driven;
        }
    }

    public static class Application {
        private String root = ".app";
        private String services = ".domain.services..";
        private String configuration = ".config..";

        public String getRoot() {
            return root;
        }

        public void setRoot(String root) {
            this.root = root;
        }

        public String getServices() {
            return services;
        }

        public void setServices(String services) {
            this.services = services;
        }

        public String getConfiguration() {
            return configuration;
        }

        public void setConfiguration(String configuration) {
            this.configuration = configuration;
        }
    }

    public Port getPort() {
        return port;
    }

    public Adapter getAdapter() {
        return adapter;
    }

    public Application getApplication() {
        return application;
    }

    public void setPort(Port port) {
        this.port = port;
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    @Override
    public String getDrivingPortPackageMatcher() {
        return application.root + port.driving;
    }

    @Override
    public String getDrivenPortPackageMatcher() {
        return application.root + port.driven;
    }

    @Override
    public String getDrivingAdapterPackageMatcher() {
        return adapter.driving;
    }

    @Override
    public String getDrivenAdapterPackageMatcher() {
        return adapter.driven;
    }

    @Override
    public String getApplicationRoot() {
        return application.root;
    }

    @Override
    public String getApplicationServicesPackageMatcher() {
        return application.root + application.services;
    }

    @Override
    public String getApplicationConfigurationPackageMatcher() {
        return application.configuration;
    }
}