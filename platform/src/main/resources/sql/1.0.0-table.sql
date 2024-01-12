CREATE TABLE IF NOT EXISTS `system_black_list`
(
    `black_id`       bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '黑名单id',
    `black_ip`       varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '黑名单ip',
    `request_uri`    varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '请求uri（支持通配符）',
    `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '请求方法，如果为ALL则表示对所有方法生效',
    `limit_from`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '00:00:00' COMMENT '限制时间起',
    `limit_to`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '23:59:59' COMMENT '限制时间止',
    `location`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT 'ip对应地址',
    `black_status`   char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci      NULL     DEFAULT '1' COMMENT '黑名单状态 默认 1 @@enable_disable',
    `version`        bigint(20)                                                    NULL     DEFAULT 1 COMMENT '版本',
    `tenant_id`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '租户id',
    `create_at`      datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_at`      datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
    `create_by`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '创建人',
    `update_by`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '编辑人',
    PRIMARY KEY (`black_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '黑名单表'
  ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `system_block_log`
(
    `block_id`       bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '拦截日志id',
    `block_ip`       varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '拦截ip',
    `request_uri`    varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '被拦截请求URI',
    `request_method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '被拦截请求方法',
    `request_time`   datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '拦截时间',
    `location`       varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT 'IP对应地址',
    `version`        bigint(20)                                                    NULL     DEFAULT 1 COMMENT '版本',
    `tenant_id`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '租户id',
    `create_at`      datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_at`      datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
    `create_by`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '创建人',
    `update_by`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '编辑人',
    PRIMARY KEY (`block_id`) USING BTREE,
    INDEX `nk_system_block_log_block_ip` (`block_ip`) USING BTREE,
    INDEX `nk_system_block_log_request_time` (`request_time`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '黑名单拦截日志表'
  ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `system_dept`
(
    `dept_id`     bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '部门id',
    `dept_code`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '部门编码',
    `parent_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '0' COMMENT '上级部门编码',
    `dept_name`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '部门名称',
    `order_num`   double(20, 0)                                                 NULL     DEFAULT 1 COMMENT '排序',
    `version`     bigint(20)                                                    NULL     DEFAULT 1 COMMENT '版本',
    `tenant_id`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '租户id',
    `create_at`   datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_at`   datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
    `create_by`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '创建人',
    `update_by`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '编辑人',
    PRIMARY KEY (`dept_id`) USING BTREE,
    UNIQUE INDEX `uk_system_dept_dept_code` (`dept_code`) USING BTREE COMMENT '部门编码唯一索引',
    INDEX `nk_system_dept_dept_name` (`dept_name`) USING BTREE COMMENT '部门名称索引',
    INDEX `nk_system_dept_parent_code` (`parent_code`) USING BTREE COMMENT '上级部门索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '部门表'
  ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `system_dict_item`
(
    `id`                  bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `dict_name`           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '字典key',
    `parent_value`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '父级字典值',
    `value`               varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '值',
    `title`               varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '标签',
    `type`                char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci      NOT NULL DEFAULT '0' COMMENT '字典类型 @@system_dict_item.type',
    `whether_system_dict` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci      NOT NULL DEFAULT '1' COMMENT '是否系统字典 @@yes_no',
    `description`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '描述',
    `sorted`              int(11)                                                       NOT NULL DEFAULT 0 COMMENT '排序（升序）',
    `is_disabled`         char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci      NULL     DEFAULT '0' COMMENT '是否禁用 @@yes_no',
    `version`             bigint(20)                                                    NULL     DEFAULT 1 COMMENT '版本',
    `tenant_id`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '租户id',
    `create_at`           datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_at`           datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '创建人',
    `update_by`           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_system_dict_item_dict_name_value` (`dict_name`, `value`) USING BTREE COMMENT '字典项表名code唯一索引',
    UNIQUE INDEX `uk_system_dict_item_dict_name_parent_value_title` (`dict_name`, `parent_value`, `title`) USING BTREE COMMENT '字典项表名文本值唯一索引',
    INDEX `nk_system_dict_item_parent_value` (`parent_value`) USING BTREE COMMENT '字典项父value索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '字典项'
  ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `system_generator_config`
(
    `id`                   bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `server_name`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '服务名',
    `author`               varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '作者',
    `base_package`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '基础包名',
    `entity_package`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT 'entity文件存放路径',
    `ao_package`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '入参',
    `vo_package`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '出参',
    `mapper_package`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT 'mapper文件存放路径',
    `mapper_xml_package`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT 'mapper xml文件存放路径',
    `service_package`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT 'service文件存放路径',
    `service_impl_package` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT 'serviceImpl文件存放路径',
    `controller_package`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT 'controller文件存放路径',
    `is_trim`              char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci      NOT NULL DEFAULT '0' COMMENT '是否去除前缀 @@yes_no',
    `trim_value`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '前缀内容',
    `exclude_columns`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '需要排除的字段',
    `version`              bigint(20)                                                    NULL     DEFAULT 1 COMMENT '版本',
    `tenant_id`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '租户id',
    `create_at`            datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_at`            datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
    `create_by`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '创建人',
    `update_by`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '编辑人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_system_generator_config_server_name` (`server_name`) USING BTREE COMMENT '服务名唯一索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '代码生成配置表'
  ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `system_log`
(
    `id`        bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '日志id',
    `username`  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '操作用户',
    `operation` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         NULL COMMENT '操作内容',
    `time`      decimal(11, 0)                                                NULL     DEFAULT 0 COMMENT '耗时',
    `method`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         NULL COMMENT '操作方法',
    `params`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         NULL COMMENT '方法参数',
    `ip`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '操作者ip',
    `location`  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '操作地点',
    `version`   bigint(20)                                                    NULL     DEFAULT 1 COMMENT '版本',
    `tenant_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '租户id',
    `create_at` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_at` datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
    `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '创建人',
    `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '编辑人',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `nk_system_log_username` (`username`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '用户操作日志表'
  ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `system_login_log`
(
    `id`         bigint(11)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `username`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '用户名',
    `login_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
    `location`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '登录地点',
    `ip`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT 'ip地址',
    `system`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '操作系统',
    `login_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '01' COMMENT '登录类型 @@system_login_log.type',
    `browser`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '浏览器',
    `version`    bigint(20)                                                    NULL     DEFAULT 1 COMMENT '版本',
    `tenant_id`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '租户id',
    `create_at`  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_at`  datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
    `create_by`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '创建人',
    `update_by`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '编辑人',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `nk_system_login_log_login_time` (`login_time`) USING BTREE,
    INDEX `nk_system_login_log_username` (`username`) USING BTREE,
    INDEX `nk_system_login_log_ip` (`ip`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '登录日志表'
  ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `system_menu`
(
    `menu_id`               bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '目录/菜单/按钮id',
    `menu_code`             varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '目录/菜单/按钮编码（唯一值）',
    `parent_code`           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '上级目录/菜单编码',
    `menu_name`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '目录/菜单/按钮名称',
    `keep_alive_name`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '页面缓存名称',
    `path`                  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '对应路由path',
    `component`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '对应路由组件component',
    `redirect`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '打开目录重定向的子菜单',
    `perms`                 varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '权限标识',
    `icon`                  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '图标',
    `type`                  char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     NOT NULL DEFAULT '0' COMMENT '类型 @@system_menu.type',
    `hide_menu`             char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     NOT NULL DEFAULT '0' COMMENT '是否隐藏菜单 @@yes_no',
    `ignore_keep_alive`     char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     NOT NULL DEFAULT '0' COMMENT '是否忽略KeepAlive缓存 @@yes_no',
    `hide_breadcrumb`       char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     NOT NULL DEFAULT '0' COMMENT '隐藏该路由在面包屑上面的显示 @@yes_no',
    `hide_children_in_menu` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     NOT NULL DEFAULT '0' COMMENT '隐藏所有子菜单 @@yes_no',
    `current_active_menu`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '当前激活的菜单。用于配置详情页时左侧激活的菜单路径',
    `order_num`             double(20, 0)                                                 NULL     DEFAULT 0 COMMENT '排序',
    `version`               bigint(20)                                                    NULL     DEFAULT 1 COMMENT '版本',
    `tenant_id`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '租户id',
    `create_at`             datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_at`             datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
    `create_by`             varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '创建人',
    `update_by`             varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '编辑人',
    PRIMARY KEY (`menu_id`) USING BTREE,
    UNIQUE INDEX `uk_system_menu_menu_code` (`menu_code`) USING BTREE COMMENT '菜单编码唯一索引',
    INDEX `nk_system_menu_menu_name` (`menu_name`) USING BTREE COMMENT '菜单名称索引',
    INDEX `nk_system_menu_parent_code` (`parent_code`) USING BTREE COMMENT '上级菜单编码唯一索引',
    INDEX `nk_system_menu_perms` (`perms`) USING BTREE COMMENT '菜单权限索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '菜单表'
  ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `system_minio_bucket`
(
    `id`            bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '桶id',
    `bucket_name`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '桶名称',
    `bucket_policy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '桶权限',
    `version`       bigint(20)                                                    NULL     DEFAULT 1 COMMENT '版本',
    `tenant_id`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '租户id',
    `create_at`     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_at`     datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
    `create_by`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '创建人',
    `update_by`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '编辑人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_system_minio_bucket_bucket_name` (`bucket_name`) USING BTREE COMMENT '文件桶名称唯一索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = 'minio桶'
  ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `system_minio_file`
(
    `id`                 varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NOT NULL COMMENT '文件id',
    `bucket_id`          bigint(20)                                                     NOT NULL DEFAULT 0 COMMENT '桶id',
    `file_name`          varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '文件名称',
    `original_file_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '原文件名称',
    `file_path`          varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '文件路径',
    `version`            bigint(20)                                                     NULL     DEFAULT 1 COMMENT '版本',
    `tenant_id`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL     DEFAULT '' COMMENT '租户id',
    `create_at`          datetime                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_at`          datetime                                                       NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
    `create_by`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '创建人',
    `update_by`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '编辑人',
    `content_type`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '内容类型',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `nk_system_minio_file_bucket_id` (`bucket_id`) USING BTREE COMMENT '文件表桶id索引',
    INDEX `nk_system_minio_file_original_file_name` (`original_file_name`) USING BTREE COMMENT '文件表原文件名称索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = 'minio文件'
  ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `system_oauth_client_details`
(
    `client_id`               varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NOT NULL COMMENT '客户端ID',
    `resource_ids`            varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '资源列表',
    `client_secret`           varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '客户端密钥',
    `scope`                   varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '域',
    `authorized_grant_types`  varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '认证类型',
    `web_server_redirect_uri` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '重定向地址',
    `authorities`             varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '角色列表',
    `access_token_validity`   int(11)                                                        NULL     DEFAULT 86400 COMMENT 'token 有效期',
    `refresh_token_validity`  int(11)                                                        NULL     DEFAULT 86400 COMMENT '刷新令牌有效期',
    `additional_information`  varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '令牌扩展字段JSON',
    `autoapprove`             varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT 'true' COMMENT '是否自动放行',
    `version`                 bigint(20)                                                     NULL     DEFAULT 1 COMMENT '版本',
    `tenant_id`               varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL     DEFAULT '' COMMENT '租户id',
    `create_at`               datetime                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_at`               datetime                                                       NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`               varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '创建人',
    `update_by`               varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '更新人',
    PRIMARY KEY (`client_id`) USING BTREE,
    UNIQUE INDEX `uk_system_oauth_client_details_client_id` (`client_id`) USING BTREE COMMENT '客户端id唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '终端信息表'
  ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `system_rate_limit_log`
(
    `rate_limit_log_id` bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '限流日志id',
    `rate_limit_log_ip` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '被拦截请求IP',
    `request_uri`       varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '被拦截请求URI',
    `request_method`    varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '被拦截请求方法',
    `request_time`      datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '拦截时间',
    `location`          varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT 'IP对应地址',
    `version`           bigint(20)                                                    NULL     DEFAULT 1 COMMENT '版本',
    `tenant_id`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '租户id',
    `create_at`         datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_at`         datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
    `create_by`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '创建人',
    `update_by`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '编辑人',
    PRIMARY KEY (`rate_limit_log_id`) USING BTREE,
    INDEX `nk_system_rate_limit_log_rate_limit_log_ip` (`rate_limit_log_ip`) USING BTREE,
    INDEX `nk_system_rate_limit_log_request_time` (`request_time`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '限流拦截日志表'
  ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `system_rate_limit_rule`
(
    `rate_limit_rule_id` bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '限流规则id',
    `request_uri`        varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '请求uri（不支持通配符）',
    `request_method`     varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '请求方法，如果为ALL则表示对所有方法生效',
    `limit_from`         varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '00:00:00' COMMENT '限制时间起',
    `limit_to`           varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '23:59:59' COMMENT '限制时间止',
    `rate_limit_count`   smallint(4)                                                   NOT NULL DEFAULT 1 COMMENT '限制次数',
    `interval_sec`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '1' COMMENT '时间周期（单位秒）',
    `rule_status`        varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '1' COMMENT '规则状态 默认 1 @@enable_disable',
    `version`            bigint(20)                                                    NULL     DEFAULT 1 COMMENT '版本',
    `tenant_id`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '租户id',
    `create_at`          datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_at`          datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
    `create_by`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '创建人',
    `update_by`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '编辑人',
    PRIMARY KEY (`rate_limit_rule_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '限流规则表'
  ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `system_role`
(
    `role_id`   bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '角色id',
    `role_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '角色编码（唯一值）',
    `role_name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '角色名称',
    `remark`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '角色描述',
    `version`   bigint(20)                                                    NULL     DEFAULT 1 COMMENT '版本',
    `tenant_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '租户id',
    `create_at` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_at` datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
    `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '创建人',
    `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '编辑人',
    PRIMARY KEY (`role_id`) USING BTREE,
    UNIQUE INDEX `uk_system_role_role_code` (`role_code`) USING BTREE COMMENT '角色编码唯一索引',
    INDEX `uk_system_role_role_name` (`role_name`) USING BTREE COMMENT '角色名称普通索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '角色表'
  ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `system_role_menu`
(
    `role_id`   bigint(20)                                                    NOT NULL COMMENT '角色id',
    `menu_id`   bigint(20)                                                    NOT NULL COMMENT '菜单id',
    `version`   bigint(20)                                                    NULL     DEFAULT 1 COMMENT '版本',
    `tenant_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '租户id',
    `create_at` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_at` datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
    `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '创建人',
    `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '编辑人',
    PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '角色菜单关联表'
  ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `system_route_log`
(
    `route_id`       bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '网关转发日志id',
    `route_ip`       varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '请求ip',
    `request_uri`    varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '请求uri',
    `target_uri`     varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '目标uri',
    `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '请求方法',
    `request_time`   datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '请求时间',
    `target_server`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '目标服务',
    `code`           varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '响应码',
    `time`           bigint(20)                                                    NULL     DEFAULT 0 COMMENT '响应时间',
    `location`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT 'ip对应地址',
    `version`        bigint(20)                                                    NULL     DEFAULT 1 COMMENT '版本',
    `tenant_id`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '租户id',
    `create_at`      datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_at`      datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
    `create_by`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '创建人',
    `update_by`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '编辑人',
    PRIMARY KEY (`route_id`) USING BTREE,
    INDEX `nk_system_route_log_route_ip` (`route_ip`) USING BTREE,
    INDEX `nk_system_route_log_target_server` (`target_server`) USING BTREE,
    INDEX `nk_system_route_log_request_time` (`request_time`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '网关转发日志表'
  ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `system_user`
(
    `user_id`         bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '用户id',
    `username`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '用户名',
    `real_name`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '用户昵称',
    `password`        varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '密码',
    `dept_id`         bigint(20)                                                    NOT NULL DEFAULT 0 COMMENT '部门id',
    `email`           varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '邮箱',
    `mobile`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '联系电话',
    `status`          char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci      NOT NULL DEFAULT '1' COMMENT '状态 @@system_user.status',
    `last_login_time` datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '最近访问时间',
    `gender`          char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci      NULL     DEFAULT '' COMMENT '性别 @@system_user.gender',
    `is_tab`          char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci      NULL     DEFAULT '' COMMENT '是否开启tab @@yes_no',
    `theme`           varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '主题',
    `avatar`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '头像',
    `description`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '描述',
    `version`         bigint(20)                                                    NULL     DEFAULT 1 COMMENT '版本',
    `tenant_id`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '租户id',
    `create_at`       datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_at`       datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
    `create_by`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '创建人',
    `update_by`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '编辑人',
    PRIMARY KEY (`user_id`) USING BTREE,
    UNIQUE INDEX `uk_system_user_username` (`username`) USING BTREE COMMENT '用户名唯一索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '用户表'
  ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `system_user_data_permission`
(
    `user_id`   bigint(20)                                                    NOT NULL COMMENT '用户id',
    `dept_id`   bigint(20)                                                    NOT NULL COMMENT '部门id',
    `version`   bigint(20)                                                    NULL     DEFAULT 1 COMMENT '版本',
    `tenant_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '租户id',
    `create_at` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_at` datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
    `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '创建人',
    `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '编辑人',
    PRIMARY KEY (`user_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '用户数据权限关联表'
  ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `system_user_role`
(
    `user_id`   bigint(20)                                                    NOT NULL COMMENT '用户id',
    `role_id`   bigint(20)                                                    NOT NULL COMMENT '角色id',
    `version`   bigint(20)                                                    NULL     DEFAULT 1 COMMENT '版本',
    `tenant_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' COMMENT '租户id',
    `create_at` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_at` datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
    `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '创建人',
    `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' COMMENT '编辑人',
    PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '用户角色关联表'
  ROW_FORMAT = DYNAMIC;
