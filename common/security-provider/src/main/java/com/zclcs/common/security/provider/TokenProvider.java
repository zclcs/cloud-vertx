package com.zclcs.common.security.provider;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

public interface TokenProvider {

    Future<String> generateToken();

    void verifyToken(String token, Handler<AsyncResult<String>> handler);

}
