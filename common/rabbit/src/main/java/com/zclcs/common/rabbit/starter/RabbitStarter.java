package com.zclcs.common.rabbit.starter;

import io.reactivex.rxjava3.core.Completable;
import io.vertx.core.json.JsonObject;
import io.vertx.rabbitmq.RabbitMQOptions;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.rabbitmq.RabbitMQClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public Completable connectRabbitMQ() {
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
        return rabbit.rxStart();
    }

    @Deprecated
    public Completable createExchange() {
        String nacosNamespace = config.getString("NAMESPACE", "dev");
        String exchangeName = nacosNamespace + ".test.exchange";
        String queueName = nacosNamespace + ".test.queue";
        String routingKey = nacosNamespace + ".test.routingKey";
        return rabbit.rxExchangeDelete(exchangeName)
                .andThen(rabbit.rxQueueDeclare(queueName, true, false, false)).ignoreElement()
                .andThen(rabbit.rxQueueBind(queueName, exchangeName, routingKey));
    }
}
