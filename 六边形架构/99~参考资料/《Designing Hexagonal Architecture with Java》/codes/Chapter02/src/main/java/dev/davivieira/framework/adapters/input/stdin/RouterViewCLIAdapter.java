package dev.davivieira.framework.adapters.input.stdin;

import dev.davivieira.application.ports.input.RouterViewInputPort;
import dev.davivieira.application.usecases.RouterViewUseCase;
import dev.davivieira.domain.entity.Router;
import dev.davivieira.domain.vo.RouterType;
import dev.davivieira.framework.adapters.output.file.RouterViewFileAdapter;

import java.util.List;

public class RouterViewCLIAdapter {

    private RouterViewUseCase routerViewUseCase;

    public RouterViewCLIAdapter() {
        setAdapters();
    }

    public List<Router> obtainRelatedRouters(String type) {
        return routerViewUseCase.getRouters(
                Router.filterRouterByType(RouterType.valueOf(type)));
    }

    private void setAdapters() {
        this.routerViewUseCase = new RouterViewInputPort(RouterViewFileAdapter.getInstance());
    }
}