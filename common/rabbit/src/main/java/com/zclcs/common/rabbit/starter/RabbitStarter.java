package com.zclcs.common.rabbit.starter;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.rabbitmq.RabbitMQClient;
import io.vertx.rabbitmq.RabbitMQOptions;

/**
 * @author zclcs
 */
public class RabbitStarter {

    private final Logger log = LoggerFactory.getLogger(RabbitStarter.class);

    private final Vertx vertx;
    private final JsonObject config;
    private RabbitMQClient rabbit;

    public RabbitStarter(Vertx vertx, JsonObject config) {
        this.vertx = vertx;
        this.config = config;
    }

    public Future<Void> connectRabbitMQ() {
        RabbitMQOptions options = new RabbitMQOptions();
        options.setUser(config.getString("RABBITMQ_USER", "guest"));
        options.setPassword(config.getString("RABBITMQ_PASSWORD", "guest"));
        options.setHost(config.getString("RABBITMQ_HOST", "127.0.0.1"));
        options.setPort(config.getInteger("RABBITMQ_PORT", 5672));
        options.setVirtualHost(config.getString("RABBITMQ_VIRTUAL_HOST", "/"));
        options.setConnectionTimeout(6000);
        options.setRequestedHeartbeat(60);
        options.setHandshakeTimeout(6000);
        options.setRequestedChannelMax(5);
        options.setNetworkRecoveryInterval(500);
        options.setAutomaticRecoveryEnabled(true);
        rabbit = RabbitMQClient.create(vertx, options);
        createExchange(config);
        return rabbit.start();
    }

    public void createExchange(JsonObject config) {
        String nacosNamespace = config.getString("NAMESPACE", "dev");
        String exchangeName = nacosNamespace + ".test.exchange";
        String queueName = nacosNamespace + ".test.queue";
        String routingKey = nacosNamespace + ".test.routingKey";
        rabbit.addConnectionEstablishedCallback(promise -> {
            rabbit.exchangeDeclare(exchangeName, "topic", true, false)
                    .compose(v -> rabbit.queueDeclare(queueName, true, false, false))
                    .compose(v -> rabbit.queueBind(queueName, exchangeName, routingKey))
                    .onComplete(promise);
        });
    }
}
