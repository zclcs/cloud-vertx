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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zclcs
 */
public class WebStarter {

    private static final Logger log = LoggerFactory.getLogger(WebStarter.class);
    private final Vertx vertx;
    private HttpServer httpServer;
    private final Router router;
    private final JsonObject env;
    private final JsonObject config;


    public WebStarter(Vertx vertx, JsonObject env) {
        this.vertx = vertx;
        router = Router.router(vertx);
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
        httpServer.requestHandler(router);
        router.route().failureHandler(failureHandler);
        return httpServer.listen(port, host);
    }

    public HttpServer getHttpServer() {
        return httpServer;
    }

    public void addRoute(HttpMethod method, String path, Handler<RoutingContext> requestHandler) {
        this.router.route(method, path).handler(requestHandler);
    }

    public void addRoute(String path, Handler<RoutingContext> requestHandler) {
        this.router.route(path).handler(requestHandler);
    }

}
