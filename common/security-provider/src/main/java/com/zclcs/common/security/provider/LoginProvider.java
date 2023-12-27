package com.zclcs.common.security.provider;

import io.vertx.core.Future;

/**
 * @author zhouc
 */
public interface LoginProvider {

    Future<String> login(String loginId);

}
