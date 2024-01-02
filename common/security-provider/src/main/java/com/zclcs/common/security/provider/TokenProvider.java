package com.zclcs.common.security.provider;

import io.vertx.core.Future;

public interface TokenProvider {

    Future<String> generateToken();

    Future<String> verifyToken(String token);

}
