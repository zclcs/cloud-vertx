package com.zclcs.test;

import com.zclcs.test.example.ExampleVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;

/**
 * @author zclcs
 */
public class TestApplication extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        vertx.deployVerticle(() -> new ExampleVerticle(),
                new DeploymentOptions().setConfig(config()).setInstances(16))
        ;
    }

}
