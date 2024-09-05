module framework {
    requires domain;
    requires application;
    requires static lombok;
    requires org.eclipse.persistence.core;
    requires java.sql;
    requires jakarta.persistence;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires jakarta.cdi;
    requires jakarta.inject;

    exports dev.davivieira.topologyinventory.framework.adapters.output.h2.data;
    opens dev.davivieira.topologyinventory.framework.adapters.output.h2.data;

    provides dev.davivieira.topologyinventory.application.ports.output.RouterManagementOutputPort
            with dev.davivieira.topologyinventory.framework.adapters.output.h2.RouterManagementH2Adapter;
    provides dev.davivieira.topologyinventory.application.ports.output.SwitchManagementOutputPort
            with dev.davivieira.topologyinventory.framework.adapters.output.h2.SwitchManagementH2Adapter;

    uses dev.davivieira.topologyinventory.application.usecases.RouterManagementUseCase;
    uses dev.davivieira.topologyinventory.application.usecases.SwitchManagementUseCase;
    uses dev.davivieira.topologyinventory.application.usecases.NetworkManagementUseCase;
    uses dev.davivieira.topologyinventory.application.ports.output.RouterManagementOutputPort;
    uses dev.davivieira.topologyinventory.application.ports.output.SwitchManagementOutputPort;
}