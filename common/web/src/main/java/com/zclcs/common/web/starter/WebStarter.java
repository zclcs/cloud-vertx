package com.zclcs.common.web.starter;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

public class WebStarter {

    private final Vertx vertx;
    private final Router router;
    private final String host;
    private final int port;

    public WebStarter(Vertx vertx, Router router, String host, int port) {
        this.vertx = vertx;
        this.router = router;
        this.host = host;
        this.port = port;
    }

    public HttpServer setUpWebServer() throws Exception {
        HttpServer httpServer = vertx.createHttpServer().requestHandler(router);
        Future.await(httpServer.listen(port, host));
        return httpServer;
    }
}
