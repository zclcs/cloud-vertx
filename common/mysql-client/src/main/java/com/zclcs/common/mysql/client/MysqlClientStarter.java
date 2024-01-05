package com.zclcs.common.mysql.client;

import com.zclcs.common.core.utils.StringsUtil;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.mysqlclient.MySQLBuilder;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
        String mysqlCollation = StringsUtil.chooseOneIsNotBlank(env.getString("MYSQL_COLLATION"), config.getString("mysql.collation"), "utf8mb4_general_ci");

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

}
