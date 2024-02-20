package com.zclcs.common.mysql.client;

import com.zclcs.common.core.utils.StringsUtil;
import com.zclcs.common.mysql.client.contant.Procedures;
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

    public MysqlClientStarter(Vertx vertx, JsonObject env) {
        this.vertx = vertx;
        this.env = env;
        this.config = vertx.getOrCreateContext().config();
    }

    public SqlClient createSqlClient() {
        return MySQLBuilder.client()
                .with(createPoolOptions())
                .connectingTo(createConnectOptions())
                .using(vertx)
                .build();
    }

    public Pool createPool() {
        return MySQLBuilder.pool()
                .with(createPoolOptions())
                .connectingTo(createConnectOptions())
                .using(vertx)
                .build();
    }

    private MySQLConnectOptions createConnectOptions() {
        String mysqlPort = StringsUtil.chooseOneIsNotBlank(env.getString("MYSQL_PORT"), config.getString("mysql.port"), "3306");
        String mysqlHost = StringsUtil.chooseOneIsNotBlank(env.getString("MYSQL_HOST"), config.getString("mysql.host"), "127.0.0.1");
        String mysqlDatabase = StringsUtil.chooseOneIsNotBlank(env.getString("MYSQL_DATABASE"), config.getString("mysql.database"), "default");
        String mysqlUser = StringsUtil.chooseOneIsNotBlank(env.getString("MYSQL_USER"), config.getString("mysql.user"), "root");
        String mysqlPassword = StringsUtil.chooseOneIsNotBlank(env.getString("MYSQL_PASSWORD"), config.getString("mysql.password"), "123456");
        String mysqlCharset = StringsUtil.chooseOneIsNotBlank(env.getString("MYSQL_CHARSET"), config.getString("mysql.charset"), "utf8mb4");
        String mysqlCollation = StringsUtil.chooseOneIsNotBlank(env.getString("MYSQL_COLLATION"), config.getString("mysql.collation"), "utf8mb4_unicode_ci");

        return new MySQLConnectOptions()
                .setPort(Integer.parseInt(mysqlPort))
                .setHost(mysqlHost)
                .setDatabase(mysqlDatabase)
                .setUser(mysqlUser)
                .setPassword(mysqlPassword)
                .setCharset(mysqlCharset)
                .setCollation(mysqlCollation);
    }

    private PoolOptions createPoolOptions() {
        String mysqlPoolMaxSize = StringsUtil.chooseOneIsNotBlank(env.getString("MYSQL_POOL_MAX_SIZE"), config.getString("mysql.pool.maxSize"), "20");
        String mysqlPoolMaxWaitQueueSize = StringsUtil.chooseOneIsNotBlank(env.getString("MYSQL_POOL_MAX_WAIT_QUEUE_SIZE"), config.getString("mysql.pool.maxWaitQueueSize"), "1000");
        String mysqlPoolConnectionTimeout = StringsUtil.chooseOneIsNotBlank(env.getString("MYSQL_POOL_CONNECTION_TIMEOUT"), config.getString("mysql.pool.connectionTimeout"), "60000");
        String mysqlPoolIdleTimeout = StringsUtil.chooseOneIsNotBlank(env.getString("MYSQL_POOL_IDLE_TIMEOUT"), config.getString("mysql.pool.idleTimeout"), "60000");
        String mysqlPoolCleanerPeriod = StringsUtil.chooseOneIsNotBlank(env.getString("MYSQL_POOL_CLEANER_PERIOD"), config.getString("mysql.pool.cleanerPeriod"), "120000");

        return new PoolOptions()
                .setMaxSize(Integer.parseInt(mysqlPoolMaxSize))
                .setMaxWaitQueueSize(Integer.parseInt(mysqlPoolMaxWaitQueueSize))
                .setConnectionTimeout(Integer.parseInt(mysqlPoolConnectionTimeout))
                .setIdleTimeout(Integer.parseInt(mysqlPoolIdleTimeout))
                .setPoolCleanerPeriod(Integer.parseInt(mysqlPoolCleanerPeriod));
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

    public Future<CompositeFuture> initDb(SqlConnection sqlConnection) {
        List<Future<RowSet<Row>>> futures = new ArrayList<>();
        for (String procedure : Procedures.PROCEDURES) {
            futures.add(sqlConnection.query(procedure).execute());
        }
        List<String> dirs = vertx.fileSystem().readDirBlocking("sql").stream().sorted().toList();
        for (String dir : dirs) {
            if (dir.endsWith(".sql")) {
                String statements = vertx.fileSystem().readFileBlocking(dir).toString();
                List<String> statement = Arrays.stream(statements.split(";"))
                        .filter(StringsUtil::isNotBlank)
                        .toList();
                for (String sql : statement) {
                    futures.add(sqlConnection.query(sql).execute());
                }
            }
        }
        return Future.all(futures).eventually(() -> sqlConnection.close());
    }

}
