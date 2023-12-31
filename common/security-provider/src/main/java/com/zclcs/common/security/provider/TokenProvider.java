package com.zclcs.common.security.provider;

import io.reactivex.rxjava3.core.Single;
import io.vertx.core.Future;

public interface TokenProvider {

    Future<String> generateToken();

    Single<String> verifyToken(String token);

}
