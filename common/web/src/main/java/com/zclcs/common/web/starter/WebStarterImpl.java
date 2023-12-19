package com.zclcs.common.web.starter;

import com.zclcs.common.core.service.StarterService;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

import static io.vertx.core.Future.await;

/**
 * @author zclcs
 */
public class WebStarterImpl implements StarterService {

    private final Vertx vertx;
    private final Router router;
    private final String host;
    private final Integer port;

    public WebStarterImpl(Vertx vertx, Router router, String host, Integer port) {
        this.vertx = vertx;
        this.router = router;
        this.host = host;
        this.port = port;
    }

    @Override
    public void setUp() throws Exception {
        createHttpServer(router, host, port);
    }

    private void createHttpServer(Router router, String host, int port) {
        HttpServer httpServer = vertx.createHttpServer()
                .requestHandler(router);
        await(httpServer.listen(port, host));
    }
}
