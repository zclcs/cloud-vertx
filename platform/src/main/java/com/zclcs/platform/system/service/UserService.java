package com.zclcs.platform.system.service;

import com.zclcs.platform.system.dao.entity.User;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

@ProxyGen
@VertxGen
public interface UserService {

    @Fluent
    UserService getUser(String username, Handler<AsyncResult<User>> handler);

}
