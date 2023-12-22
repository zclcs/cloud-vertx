package com.zclcs.common.web.starter;

import com.zclcs.common.core.service.StarterService;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

import static io.vertx.core.Future.await;

public class WebStarterImpl implements StarterService {

    private final Vertx vertx;
    private final Router router;
    private final String host;
    private final int port;
    private HttpServer httpServer;

    public WebStarterImpl(Vertx vertx, Router router, String host, int port) {
        this.vertx = vertx;
        this.router = router;
        this.host = host;
        this.port = port;
    }

    @Override
    public void setUp() throws Exception {
        httpServer = vertx.createHttpServer().requestHandler(router);
        await(httpServer.listen(port, host));
    }

    public HttpServer getHttpServer() {
        return httpServer;
    }
}
