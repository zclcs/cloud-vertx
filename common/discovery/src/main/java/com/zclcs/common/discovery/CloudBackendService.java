package com.zclcs.common.discovery;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisOptions;
import io.vertx.redis.client.Response;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.spi.ServiceDiscoveryBackend;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.vertx.redis.client.Command.*;
import static io.vertx.redis.client.Request.cmd;

/**
 * An implementation of the discovery backend based on Redis.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class CloudBackendService implements ServiceDiscoveryBackend {

    private Redis redis;
    private String key;
    private Duration duration;

    @Override

    public void init(Vertx vertx, JsonObject configuration) {
        key = configuration.getString("key", "records");
        redis = Redis.createClient(vertx, new RedisOptions(configuration));
    }

    @Override
    public void store(Record record, Handler<AsyncResult<Record>> resultHandler) {
        if (record.getRegistration() != null) {
            resultHandler.handle(Future.failedFuture("The record has already been registered"));
            return;
        }
        String uuid = UUID.randomUUID().toString();
        record.setRegistration(uuid);

        redis.send(cmd(HSET).arg(key).arg(uuid).arg(record.toJson().encode())).onComplete(ar -> {
            if (ar.succeeded()) {
                resultHandler.handle(Future.succeededFuture(record));
            } else {
                resultHandler.handle(Future.failedFuture(ar.cause()));
            }
        });
    }

    @Override
    public void remove(Record record, Handler<AsyncResult<Record>> resultHandler) {
        Objects.requireNonNull(record.getRegistration(), "No registration id in the record");
        remove(record.getRegistration(), resultHandler);
    }

    @Override
    public void remove(String uuid, Handler<AsyncResult<Record>> resultHandler) {
        Objects.requireNonNull(uuid, "No registration id in the record");

        redis.send(cmd(HGET).arg(key).arg(uuid)).onComplete(ar -> {
            if (ar.succeeded()) {
                if (ar.result() != null) {
                    redis.send(cmd(HDEL).arg(key).arg(uuid)).onComplete(deletion -> {
                        if (deletion.succeeded()) {
                            resultHandler.handle(Future.succeededFuture(new Record(new JsonObject(ar.result().toBuffer()))));
                        } else {
                            resultHandler.handle(Future.failedFuture(deletion.cause()));
                        }
                    });
                } else {
                    resultHandler.handle(Future.failedFuture("Record '" + uuid + "' not found"));
                }
            } else {
                resultHandler.handle(Future.failedFuture(ar.cause()));
            }
        });
    }

    @Override
    public void update(Record record, Handler<AsyncResult<Void>> resultHandler) {
        Objects.requireNonNull(record.getRegistration(), "No registration id in the record");
        redis.send(cmd(HSET).arg(key).arg(record.getRegistration()).arg(record.toJson().encode())).onComplete(ar -> {
            if (ar.succeeded()) {
                resultHandler.handle(Future.succeededFuture());
            } else {
                resultHandler.handle(Future.failedFuture(ar.cause()));
            }
        });
    }

    @Override
    public void getRecords(Handler<AsyncResult<List<Record>>> resultHandler) {
        redis.send(cmd(HGETALL).arg(key)).onComplete(ar -> {
            if (ar.succeeded()) {
                Response entries = ar.result();
                resultHandler.handle(Future.succeededFuture(entries.getKeys().stream()
                        .map(key -> new Record(new JsonObject(entries.get(key).toBuffer())))
                        .collect(Collectors.toList())));
            } else {
                resultHandler.handle(Future.failedFuture(ar.cause()));
            }
        });
    }

    @Override
    public void getRecord(String uuid, Handler<AsyncResult<Record>> resultHandler) {
        redis.send(cmd(HGET).arg(key).arg(uuid)).onComplete(ar -> {
            if (ar.succeeded()) {
                if (ar.result() != null) {
                    resultHandler.handle(Future.succeededFuture(new Record(new JsonObject(ar.result().toBuffer()))));
                } else {
                    resultHandler.handle(Future.succeededFuture(null));
                }
            } else {
                resultHandler.handle(Future.failedFuture(ar.cause()));
            }
        });
    }

}