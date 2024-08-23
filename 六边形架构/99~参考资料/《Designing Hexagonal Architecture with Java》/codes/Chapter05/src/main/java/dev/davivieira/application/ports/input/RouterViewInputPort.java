package dev.davivieira.application.ports.input;

import dev.davivieira.application.ports.output.RouterViewOutputPort;
import dev.davivieira.application.usecases.RouterViewUseCase;
import dev.davivieira.domain.entity.Router;
import dev.davivieira.domain.service.RouterSearch;
import dev.davivieira.domain.vo.RouterType;

import java.util.List;

public class RouterViewInputPort implements RouterViewUseCase {

    private RouterViewOutputPort routerListOutputPort;

    public RouterViewInputPort(RouterViewOutputPort routerGraphOutputPort) {
        this.routerListOutputPort = routerGraphOutputPort;
    }

    @Override
    public List<Router> getRelatedRouters(RelatedRoutersCommand relatedRoutersCommand) {
        var type = relatedRoutersCommand.getType();
        var routers = routerListOutputPort.fetchRelatedRouters();
        return fetchRelatedEdgeRouters(type, routers);
    }

    private List<Router> fetchRelatedEdgeRouters(RouterType type, List<Router> routers){
        return RouterSearch.getRouters(type, routers);
    }
}
