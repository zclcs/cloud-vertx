package com.zclcs.platform.system.service;

import com.zclcs.platform.system.dao.entity.User;
import io.vertx.core.Future;

public interface UserService {

    Future<User> getUser(String username);

}
