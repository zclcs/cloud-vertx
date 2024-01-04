package com.zclcs.common.web.starter;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.openapi.router.RequestExtractor;
import io.vertx.ext.web.openapi.router.RouterBuilder;
import io.vertx.openapi.contract.OpenAPIContract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zclcs
 */
public class WebStarter {

    private static final Logger log = LoggerFactory.getLogger(WebStarter.class);
    private final Vertx vertx;
    private HttpServer httpServer;
    private Router router;
    private final RouterBuilder routerBuilder;
    private final JsonObject env;
    private final JsonObject config;


    public WebStarter(Vertx vertx, JsonObject env, OpenAPIContract contract) {
        this.vertx = vertx;
        this.router = Router.router(vertx);
        if (contract != null) {
            this.routerBuilder = RouterBuilder.create(vertx, contract, RequestExtractor.withBodyHandler());
        } else {
            this.routerBuilder = null;
        }
        this.env = env;
        this.config = vertx.getOrCreateContext().config();
    }

    public Future<HttpServer> setUpHttpServer(int port, String host, Handler<RoutingContext> failureHandler) {
        httpServer = vertx.createHttpServer(new HttpServerOptions()
                .setTcpFastOpen(true)
                .setTcpCork(true)
                .setTcpQuickAck(true)
                .setReusePort(true)
        );
        httpServer.exceptionHandler((e) -> log.error("http server exception", e));
        router.route().handler(BodyHandler.create());
        if (routerBuilder != null) {
            router.route().subRouter(routerBuilder.createRouter());
        }
        httpServer.requestHandler(router);
        router.route().failureHandler(failureHandler);
        return httpServer.listen(port, host);
    }

    public HttpServer getHttpServer() {
        return httpServer;
    }

    public void addOpenApiRoute(String operationId, Handler<RoutingContext> requestHandler) {
        routerBuilder.getRoute(operationId).addHandler(requestHandler);
    }

    public void addRoute(HttpMethod method, String path, Handler<RoutingContext> requestHandler) {
        this.router.route(method, path).handler(requestHandler);
    }

    public void addRoute(String path, Handler<RoutingContext> requestHandler) {
        this.router.route(path).handler(requestHandler);
    }

}
