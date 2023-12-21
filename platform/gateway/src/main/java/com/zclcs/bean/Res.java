package com.zclcs.bean;

import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.json.JsonObject;

import static io.vertx.core.Future.await;

/**
 * @author zclcs
 */
public class Res {

    private int statusCode;

    private MultiMap head;

    private Buffer body;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public MultiMap getHead() {
        return head;
    }

    public void setHead(MultiMap head) {
        this.head = head;
    }

    public Buffer getBody() {
        return body;
    }

    public void setBody(Buffer body) {
        this.body = body;
    }

    public Res(int statusCode, MultiMap head, Buffer body) {
        this.statusCode = statusCode;
        this.head = head;
        this.body = body;
    }

    public Res(HttpClientResponse response) {
        this.statusCode = response.statusCode();
        this.head = response.headers();
        this.body = await(response.body());
    }

    public static Res notFound() {
        return new Res(404, jsonHead(), notFoundJson());
    }

    public static Res serviceUnavailable() {
        return new Res(503, jsonHead(), serviceUnavailableJson());
    }

    private static Buffer notFoundJson() {
        return new JsonObject().put("message", "Not Found").toBuffer();
    }

    private static Buffer serviceUnavailableJson() {
        return new JsonObject().put("message", "Service Unavailable").toBuffer();
    }

    private static MultiMap jsonHead() {
        MultiMap head = MultiMap.caseInsensitiveMultiMap();
        head.set("Content-Type", "application/json; charset=utf-8");
        return head;
    }
}
