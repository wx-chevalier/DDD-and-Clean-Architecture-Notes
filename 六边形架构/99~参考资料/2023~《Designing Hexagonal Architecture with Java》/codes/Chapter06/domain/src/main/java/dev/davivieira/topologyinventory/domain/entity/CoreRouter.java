package dev.davivieira.topologyinventory.domain.entity;

import dev.davivieira.topologyinventory.domain.specification.EmptyRouterSpec;
import dev.davivieira.topologyinventory.domain.specification.EmptySwitchSpec;
import dev.davivieira.topologyinventory.domain.specification.SameCountrySpec;
import dev.davivieira.topologyinventory.domain.specification.SameIpSpec;
import dev.davivieira.topologyinventory.domain.vo.IP;
import dev.davivieira.topologyinventory.domain.vo.Id;
import dev.davivieira.topologyinventory.domain.vo.Location;
import dev.davivieira.topologyinventory.domain.vo.Model;
import dev.davivieira.topologyinventory.domain.vo.RouterType;
import dev.davivieira.topologyinventory.domain.vo.Vendor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
public final class CoreRouter extends Router {

    @Getter
    private final Map<Id, Router> routers;

    @Builder
    public CoreRouter(Id id, Vendor vendor, Model model, IP ip, Location location, RouterType routerType, Map<Id, Router> routers) {
        super(id, vendor, model, ip, location, routerType);
        this.routers = routers;
    }

    public Router addRouter(Router anyRouter) {
        var sameCountryRouterSpec = new SameCountrySpec(this);
        var sameIpSpec = new SameIpSpec(this);

        sameCountryRouterSpec.check(anyRouter);
        sameIpSpec.check(anyRouter);

        return this.routers.put(anyRouter.id, anyRouter);
    }

    public Router removeRouter(Router anyRouter) {
        var emptyRoutersSpec = new EmptyRouterSpec();
        var emptySwitchSpec = new EmptySwitchSpec();

        switch (anyRouter.routerType) {
            case CORE -> {
                var coreRouter = (CoreRouter)anyRouter;
                emptyRoutersSpec.check(coreRouter);
            }
            case EDGE -> {
                var edgeRouter = (EdgeRouter)anyRouter;
                emptySwitchSpec.check(edgeRouter);
            }
        }
        return this.routers.remove(anyRouter.id);
    }
}
