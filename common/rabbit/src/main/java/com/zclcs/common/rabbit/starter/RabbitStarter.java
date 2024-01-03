package com.zclcs.common.rabbit.starter;

import com.zclcs.common.core.utils.StringsUtil;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.rabbitmq.RabbitMQClient;
import io.vertx.rabbitmq.RabbitMQOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zclcs
 */
public class RabbitStarter {

    private final Logger log = LoggerFactory.getLogger(RabbitStarter.class);

    private final Vertx vertx;
    private final JsonObject env;
    private final JsonObject config;
    private RabbitMQClient rabbit;

    public RabbitStarter(Vertx vertx, JsonObject env) {
        this.vertx = vertx;
        this.env = env;
        this.config = vertx.getOrCreateContext().config();
    }

    public Future<Void> connectRabbitMQ() {
        RabbitMQOptions options = new RabbitMQOptions();
        String rabbitmqHost = StringsUtil.chooseOneIsNotBlank(env.getString("RABBITMQ_HOST"), config.getString("rabbitmq.host"), "127.0.0.1");
        String rabbitmqPort = StringsUtil.chooseOneIsNotBlank(env.getString("RABBITMQ_PORT"), config.getString("rabbitmq.port"), "5672");
        String rabbitmqUser = StringsUtil.chooseOneIsNotBlank(env.getString("RABBITMQ_USER"), config.getString("rabbitmq.user"), "guest");
        String rabbitmqPassword = StringsUtil.chooseOneIsNotBlank(env.getString("RABBITMQ_PASSWORD"), config.getString("rabbitmq.password"), "guest");
        String rabbitmqVirtualHost = StringsUtil.chooseOneIsNotBlank(env.getString("RABBITMQ_VIRTUAL_HOST"), config.getString("rabbitmq.virtualHost"), "/");
        String rabbitmqConnectionTimeOut = StringsUtil.chooseOneIsNotBlank(env.getString("RABBITMQ_CONNECTION_TIME_OUT", config.getString("rabbitmq.connectionTimeOut", "6000")));
        String rabbitmqRequestedHeartBeat = StringsUtil.chooseOneIsNotBlank(env.getString("RABBITMQ_REQUESTED_HEART_BEAT"), config.getString("rabbitmq.requestedHeartBeat"), "60");
        String rabbitmqHandshakeTimeOut = StringsUtil.chooseOneIsNotBlank(env.getString("RABBITMQ_HANDSHAKE_TIME_OUT"), config.getString("rabbitmq.handshakeTimeOut"), "6000");
        String rabbitmqChannelMax = StringsUtil.chooseOneIsNotBlank(env.getString("RABBITMQ_CHANNEL_MAX"), config.getString("rabbitmq.channelMax"), "5");
        String rabbitmqNetworkRecoveryInterval = StringsUtil.chooseOneIsNotBlank(env.getString("RABBITMQ_NETWORK_RECOVERY_INTERVAL"), config.getString("rabbitmq.networkRecoveryInterval"), "500");
        String rabbitmqAutomaticRecoveryEnabled = StringsUtil.chooseOneIsNotBlank(env.getString("RABBITMQ_AUTOMATIC_RECOVERY_ENABLED"), config.getString("rabbitmq.automaticRecoveryEnabled"), "true");
        options.setHost(rabbitmqHost);
        options.setPort(Integer.parseInt(rabbitmqPort));
        options.setUser(rabbitmqUser);
        options.setPassword(rabbitmqPassword);
        options.setVirtualHost(rabbitmqVirtualHost);
        options.setConnectionTimeout(Integer.parseInt(rabbitmqConnectionTimeOut));
        options.setRequestedHeartbeat(Integer.parseInt(rabbitmqRequestedHeartBeat));
        options.setHandshakeTimeout(Integer.parseInt(rabbitmqHandshakeTimeOut));
        options.setRequestedChannelMax(Integer.parseInt(rabbitmqChannelMax));
        options.setNetworkRecoveryInterval(Long.parseLong(rabbitmqNetworkRecoveryInterval));
        options.setAutomaticRecoveryEnabled(Boolean.parseBoolean(rabbitmqAutomaticRecoveryEnabled));
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
