package com.zclcs;

import com.zclcs.common.discovery.BaseDiscoveryVerticle;
import com.zclcs.common.web.starter.WebStarterImpl;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.vertx.core.Future.await;

/**
 * @author zclcs
 */
public class PlatformSystem extends BaseDiscoveryVerticle {

    private static final int DEFAULT_PORT = 8788;
    private static final Logger log = LoggerFactory.getLogger(PlatformSystem.class);
    private WebStarterImpl webStart;

    @Override
    public void start() throws Exception {
        super.start();
        ConfigStoreOptions store = new ConfigStoreOptions()
                .setType("env");
        ConfigRetriever retriever = ConfigRetriever.create(vertx,
                new ConfigRetrieverOptions().addStore(store));
        JsonObject config = await(retriever.getConfig());
        String host = config.getString("SYSTEM_HTTP_ADDRESS", "0.0.0.0");
        int port = config.getInteger("SYSTEM_HTTP_PORT", DEFAULT_PORT);

        Router router = Router.router(vertx);

        // body handler
        router.route().handler(BodyHandler.create());

        // api dispatcher
        router.route("/user").handler(context -> {
            context.response()
                    .end(new JsonObject().put("version", "v1").encodePrettily());
        });
        webStart = new WebStarterImpl(vertx, router, host, port);
        webStart.setUp();
        log.info("http server started on " + host + ":" + port);
        publishHttpEndpoint("platform-system", host, port, "/system");
        log.info("Register succeeded");
    }

}
