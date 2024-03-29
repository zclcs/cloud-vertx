package com.zclcs.cloud.core.constant;

/**
 * @author zclcs
 */
public class RedisPrefix {

    public static final String TOKEN_PREFIX = "token:%s";

    public static final String VERIFY_CODE_PREFIX = "verify:code:%s";

    public static final String USER_PREFIX = "user:%s";

    public static final String USER_MOBILE_PREFIX = "user:m:%s";

    public static final String USER_PERMISSION_PREFIX = "user:permission:%s";

    public static final String USER_ROUTER_PREFIX = "user:router:%s";

    public static final String RATE_LIMIT_PREFIX = "rate:limit:%s:%s:%s";

    public static final String HTTP_RATE_LIMIT_LIST_PREFIX = "http:rate:limit:list";

    public static final String HTTP_BLACK_LIST_PREFIX = "http:black:list";

    public static final String DICT_PREFIX = "dict:n:%s";

    public static final String DICT_VALUE_PREFIX = "dict:n:%s:v:%s";

    public static final String DICT_PARENT_VALUE_AND_VALUE_PREFIX = "dict:n:%s:p:%s:v:%s";

    public static final String DICT_PARENT_VALUE_PREFIX = "dict:n:%s:p:%s";

    
}
