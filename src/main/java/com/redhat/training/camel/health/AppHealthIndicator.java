package com.redhat.training.camel.health;

import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class AppHealthIndicator implements HealthIndicator {
    @Autowired
    private CamelContext ctx;

    @Override
    public Health health() {
        Route myRoute = ctx.getRoute("restClient");
        if (myRoute != null && myRoute.isAutoStartup()) {
            return Health.up().build();
        }
        return Health.down().withDetail("Route missing", ctx.getStatus().toString()).build();
    }
}
