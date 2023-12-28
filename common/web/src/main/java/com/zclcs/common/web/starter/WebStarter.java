package com.zclcs.common.web.starter;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

public class WebStarter {

    private final Vertx vertx;
    private final Router router;

    public WebStarter(Vertx vertx, Router router) {
        this.vertx = vertx;
        this.router = router;
    }

    public HttpServer setUpWebServer(Integer port, String host) throws Exception {
        HttpServer httpServer = vertx.createHttpServer().requestHandler(router);
        Future.await(httpServer.listen(port, host));
        return httpServer;
    }
}
