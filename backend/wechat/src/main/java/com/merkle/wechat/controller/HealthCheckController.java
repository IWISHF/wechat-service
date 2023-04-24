package com.merkle.wechat.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.HealthEndpoint;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "健康检查")
public class HealthCheckController extends AbstractController {
    private @Autowired HealthEndpoint healthEndPoint;

    @ApiOperation(value = "健康检查")
    @GetMapping("/wechat/ping")
    public String healthCheck(HttpServletResponse resp) {
        Health health = healthEndPoint.invoke();

        if (health.getStatus().equals(Status.UP)) {
            return "ok";
        } else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return health.toString();
        }
    }
}
