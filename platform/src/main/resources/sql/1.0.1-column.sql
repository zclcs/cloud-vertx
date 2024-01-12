call add_column_if_not_exists(database(), 'system_generator_config', 'gen_version',
                              'char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT "02" COMMENT "版本 @@generate.version"');
call add_column_if_not_exists(database(), 'system_generator_config', 'cache_vo_package',
                              'varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT "" COMMENT "缓存实体"');
call add_column_if_not_exists(database(), 'system_generator_config', 'excel_vo_package',
                              'varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT "" COMMENT "excel实体"');