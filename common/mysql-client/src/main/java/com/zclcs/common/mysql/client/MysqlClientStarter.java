package com.zclcs.common.mysql.client;

import com.zclcs.common.core.utils.StringsUtil;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.mysqlclient.MySQLBuilder;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.sqlclient.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;

/**
 * @author zhouc
 */
public class MysqlClientStarter {

    private final Logger log = LoggerFactory.getLogger(MysqlClientStarter.class);

    private final Vertx vertx;
    private final JsonObject env;
    private final JsonObject config;

    private static final String[] procedures = new String[]{
            """
            drop procedure if exists add_column_if_not_exists
            """,
            """
            create procedure add_column_if_not_exists(
                    IN dbName tinytext,
                    IN tableName tinytext,
                    IN fieldName tinytext,
                    IN fieldDef text)
                BEGIN
                    IF
                        (NOT EXISTS(
                                SELECT *
                                FROM information_schema.COLUMNS
                                WHERE column_name = fieldName
                                  and table_name = tableName
                                  and table_schema = dbName
                            )) THEN
                        set @ddl = CONCAT('ALTER TABLE ', dbName, '.', tableName, ' ADD COLUMN ', fieldName, ' ', fieldDef);
                        prepare stmt from @ddl;
                        execute stmt;
                    END IF;
                END
            """,
            """
            drop procedure if exists change_column_if_exists
            """,
            """
            create procedure change_column_if_exists(IN dbName tinytext,
                                                         IN tableName tinytext,
                                                         IN oldFieldName tinytext,
                                                         IN fieldName tinytext,
                                                         IN fieldDef text)
                begin
                    IF
                        EXISTS(
                                SELECT *
                                FROM information_schema.COLUMNS
                                WHERE column_name = oldFieldName
                                  and table_name = tableName
                                  and table_schema = dbName
                            )
                    THEN
                        set @ddl = CONCAT('ALTER TABLE ', dbName, '.', tableName, ' CHANGE COLUMN ', oldFieldName, ' ', fieldName, ' ',
                                          fieldDef);
                        prepare stmt from @ddl;
                        execute stmt;
                    END IF;
                END
            """,
            """
            drop procedure if exists drop_column_if_exists
            """,
            """
            create procedure drop_column_if_exists(IN dbName tinytext,
                                                       IN tableName tinytext,
                                                       IN fieldName tinytext)
                begin
                    IF
                        EXISTS(
                                SELECT *
                                FROM information_schema.COLUMNS
                                WHERE column_name = fieldName
                                  and table_name = tableName
                                  and table_schema = dbName
                            )
                    THEN
                        set @ddl = CONCAT('ALTER TABLE ', dbName, '.', tableName, ' DROP ', fieldName);
                        prepare stmt from @ddl;
                        execute stmt;
                    END IF;
                END
            """,
            """
            drop procedure if exists add_index_if_not_exists
            """,
            """
            CREATE PROCEDURE add_index_if_not_exists(IN dbName tinytext,
                                                         IN tableName tinytext,
                                                         IN indexType int,
                                                         IN indexName tinytext,
                                                         IN indexField text)
                BEGIN
                    SET @index_type = ' INDEX ';
                    IF (indexType = 2) THEN
                        SET @index_type = ' UNIQUE ';
                    END IF;
                    IF (indexType = 3) THEN
                        SET @index_type = ' FULLTEXT ';
                    END IF;
                    IF (indexType = 4) THEN
                        SET @index_type = ' SPATIAL ';
                    END IF;
                    SET @str = concat(
                            ' ALTER TABLE ',
                            dbName,
                            '.',
                            tableName,
                            ' ADD ',
                            @index_type,
                            indexName,
                            ' ( ',
                            indexField,
                            ' ) '
                        );
                    SELECT count(*)
                    INTO @cnt
                    FROM information_schema.statistics
                    WHERE TABLE_SCHEMA = dbName
                      AND table_name = tableName
                      AND index_name = indexName;
                    IF (@cnt <= 0) THEN
                        PREPARE stmt FROM @str;
                        EXECUTE stmt;
                    END IF;
                END
            """,
            """
            DROP PROCEDURE IF EXISTS change_index_if_exists
            """,
            """
            CREATE PROCEDURE change_index_if_exists(IN dbName tinytext,
                                                        IN tableName tinytext,
                                                        IN indexType tinytext,
                                                        IN indexName tinytext,
                                                        IN indexField tinytext,
                                                        IN oldIndexName text)
                BEGIN
                    SET @str = concat(
                            ' DROP INDEX ',
                            oldIndexName,
                            ' ON ',
                            dbName,
                            '.',
                            tableName
                        );
                    SELECT count(*)
                    INTO @cnt
                    FROM information_schema.statistics
                    WHERE TABLE_SCHEMA = dbName
                      AND table_name = tableName
                      AND index_name = oldIndexName;
                    IF (@cnt > 0) THEN
                        PREPARE stmt FROM @str;
                        EXECUTE stmt;
                    END IF;
                    SET @index_type = ' INDEX ';
                    IF (indexType = 2) THEN
                        SET @index_type = ' UNIQUE ';
                    END IF;
                    IF (indexType = 3) THEN
                        SET @index_type = ' FULLTEXT ';
                    END IF;
                    IF (indexType = 4) THEN
                        SET @index_type = ' SPATIAL ';
                    END IF;
                    SET @str = concat(
                            ' ALTER TABLE ',
                            dbName,
                            '.',
                            tableName,
                            ' ADD ',
                            @index_type,
                            indexName,
                            ' ( ',
                            indexField,
                            ' ) '
                        );
                    SELECT count(*)
                    INTO @cnt
                    FROM information_schema.statistics
                    WHERE TABLE_SCHEMA = dbName
                      AND table_name = tableName
                      AND index_name = indexName;
                    IF (@cnt <= 0) THEN
                        PREPARE stmt FROM @str;
                        EXECUTE stmt;
                    END IF;
                END
            """,
            """
            DROP PROCEDURE IF EXISTS drop_index_if_exists
            """,
            """
            CREATE PROCEDURE drop_index_if_exists(IN dbName tinytext,
                                                      IN tableName tinytext,
                                                      IN indexName tinytext)
                BEGIN
                    SET @str = concat(
                            ' DROP INDEX ',
                            indexName,
                            ' ON ',
                            dbName,
                            '.',
                            tableName
                        );
                    SELECT count(*)
                    INTO @cnt
                    FROM information_schema.statistics
                    WHERE TABLE_SCHEMA = dbName
                      AND table_name = tableName
                      AND index_name = indexName;
                    IF (@cnt > 0) THEN
                        PREPARE stmt FROM @str;
                        EXECUTE stmt;
                    END IF;
                END
            """,
            """
            DROP PROCEDURE IF EXISTS insert_if_not_exists
            """,
            """
            CREATE PROCEDURE insert_if_not_exists(IN dbName tinytext,
                                                      IN tableName tinytext,
                                                      IN columnName text,
                                                      IN columnData text,
                                                      IN unique_sql text)
                BEGIN
                    SET @str = concat(
                            ' insert into ',
                            dbName,
                            '.',
                            tableName,
                            ' ( ',
                            columnName,
                            ' ) ',
                            ' select ',
                            columnData,
                            ' from dual where not exists (select * from ',
                            dbName,
                            '.',
                            tableName,
                            ' where ',
                            unique_sql,
                            ' ) '
                        );
                    PREPARE stmt FROM @str;
                    EXECUTE stmt;
                END
            """,
    };

    public MysqlClientStarter(Vertx vertx, JsonObject env) {
        this.vertx = vertx;
        this.env = env;
        this.config = vertx.getOrCreateContext().config();
    }

    public Pool createMysqlPool() {
        String mysqlPort = StringsUtil.chooseOneIsNotBlank(env.getString("MYSQL_PORT"), config.getString("mysql.port"), "3306");
        String mysqlHost = StringsUtil.chooseOneIsNotBlank(env.getString("MYSQL_HOST"), config.getString("mysql.host"), "127.0.0.1");
        String mysqlDatabase = StringsUtil.chooseOneIsNotBlank(env.getString("MYSQL_DATABASE"), config.getString("mysql.database"), "default");
        String mysqlUser = StringsUtil.chooseOneIsNotBlank(env.getString("MYSQL_USER"), config.getString("mysql.user"), "root");
        String mysqlPassword = StringsUtil.chooseOneIsNotBlank(env.getString("MYSQL_PASSWORD"), config.getString("mysql.password"), "123456");
        String mysqlCharset = StringsUtil.chooseOneIsNotBlank(env.getString("MYSQL_CHARSET"), config.getString("mysql.charset"), "utf8mb4");
        String mysqlCollation = StringsUtil.chooseOneIsNotBlank(env.getString("MYSQL_COLLATION"), config.getString("mysql.collation"), "utf8mb4_unicode_ci");

        MySQLConnectOptions connectOptions = new MySQLConnectOptions()
                .setPort(Integer.parseInt(mysqlPort))
                .setHost(mysqlHost)
                .setDatabase(mysqlDatabase)
                .setUser(mysqlUser)
                .setPassword(mysqlPassword)
                .setCharset(mysqlCharset)
                .setCollation(mysqlCollation);

        String mysqlPoolMaxSize = StringsUtil.chooseOneIsNotBlank(env.getString("MYSQL_POOL_MAX_SIZE"), config.getString("mysql.pool.maxSize"), "20");
        String mysqlPoolMaxWaitQueueSize = StringsUtil.chooseOneIsNotBlank(env.getString("MYSQL_POOL_MAX_WAIT_QUEUE_SIZE"), config.getString("mysql.pool.maxWaitQueueSize"), "1000");
        String mysqlPoolConnectionTimeout = StringsUtil.chooseOneIsNotBlank(env.getString("MYSQL_POOL_CONNECTION_TIMEOUT"), config.getString("mysql.pool.connectionTimeout"), "60000");
        String mysqlPoolIdleTimeout = StringsUtil.chooseOneIsNotBlank(env.getString("MYSQL_POOL_IDLE_TIMEOUT"), config.getString("mysql.pool.idleTimeout"), "60000");
        String mysqlPoolCleanerPeriod = StringsUtil.chooseOneIsNotBlank(env.getString("MYSQL_POOL_CLEANER_PERIOD"), config.getString("mysql.pool.cleanerPeriod"), "120000");

        PoolOptions poolOptions = new PoolOptions()
                .setMaxSize(Integer.parseInt(mysqlPoolMaxSize))
                .setMaxWaitQueueSize(Integer.parseInt(mysqlPoolMaxWaitQueueSize))
                .setConnectionTimeout(Integer.parseInt(mysqlPoolConnectionTimeout))
                .setIdleTimeout(Integer.parseInt(mysqlPoolIdleTimeout))
                .setPoolCleanerPeriod(Integer.parseInt(mysqlPoolCleanerPeriod));
        log.info("mysql connectOptions: {}", connectOptions.toJson());

        return MySQLBuilder.pool()
                .with(poolOptions)
                .connectingTo(connectOptions)
                .using(vertx)
                .build();
    }

    private Future<Properties> loadDbQueries(String path) {
        return vertx.executeBlocking(new Callable<>() {
            @Override
            public Properties call() throws Exception {
                var properties = new Properties();
                try (var is = getClass().getClassLoader().getResourceAsStream("db/queries.xml")) {
                    properties.loadFromXML(is);
                } catch (IOException e) {
                    throw new IOException("resource not found");
                }
                return properties;
            }
        });
    }

    public CompositeFuture initDb(SqlConnection connection) {
        List<Future<RowSet<Row>>> futures = new ArrayList<>();
        for (String procedure : procedures) {
            futures.add(connection.query(procedure).execute());
        }
        List<String> dirs = vertx.fileSystem().readDirBlocking("sql");
        for (String dir : dirs) {
            if (dir.endsWith(".sql")) {
                String statements = vertx.fileSystem().readFileBlocking(dir).toString();
                List<String> statement = Arrays.stream(statements.split(";"))
                        .filter(StringsUtil::isNotBlank)
                        .toList();
                for (String sql : statement) {
                    futures.add(connection.query(sql).execute());
                }
            }
        }
        return Future.all(futures);
    }

}
