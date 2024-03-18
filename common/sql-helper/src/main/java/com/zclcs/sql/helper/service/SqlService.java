package com.zclcs.sql.helper.service;

import com.zclcs.sql.helper.statement.SQLStatement;
import com.zclcs.sql.helper.statement.bean.Page;
import com.zclcs.sql.helper.statement.bean.SqlAndParams;
import com.zclcs.sql.helper.statement.bean.SqlAssist;
import com.zclcs.sql.helper.statement.bean.SqlWhereCondition;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.mysqlclient.MySQLClient;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.templates.RowMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zclcs
 */
public interface SqlService<T> {

    SQLStatement sqlStatement();

    Pool pool();

    RowMapper<T> rowMapper();

    default <R> R rowSetToOne(RowSet<R> rowSet) {
        R row = null;
        if (rowSet.iterator().hasNext()) {
            row = rowSet.iterator().next();
        }
        return row;
    }

    default <R> List<R> rowSetToList(RowSet<R> rowSet) {
        List<R> list = new ArrayList<>();
        if (rowSet != null && rowSet.size() > 0) {
            rowSet.forEach(list::add);
        }
        return list;
    }

    default void execute(SqlAndParams qp, Handler<AsyncResult<RowSet<Row>>> handler) {
        if (qp.succeeded()) {
            if (qp.getParams() == null) {
                pool().query(qp.getSql()).execute(handler);
            } else {
                pool().preparedQuery(qp.getSql()).execute(qp.getParams(), handler);
            }
        } else {
            handler.handle(Future.failedFuture(String.format("Error generating sql sql = %s", qp.getSql())));
        }
    }

    default Future<RowSet<Row>> execute(SqlAndParams qp) {
        Promise<RowSet<Row>> promise = Promise.promise();
        execute(qp, promise);
        return promise.future();
    }

    default <R> void execute(SqlAndParams qp, RowMapper<R> mapping, Handler<AsyncResult<RowSet<R>>> handler) {
        if (qp.succeeded()) {
            if (qp.getParams() == null) {
                pool().query(qp.getSql()).mapping(mapping::map).execute(handler);
            } else {
                pool().preparedQuery(qp.getSql()).mapping(mapping::map).execute(qp.getParams(), handler);
            }
        } else {
            handler.handle(Future.failedFuture(String.format("Error generating sql sql = %s", qp.getSql())));
        }
    }

    default <R> Future<RowSet<R>> execute(SqlAndParams qp, RowMapper<R> mapping) {
        Promise<RowSet<R>> promise = Promise.promise();
        execute(qp, mapping, promise);
        return promise.future();
    }

    default void executeBatch(SqlAndParams qp, Handler<AsyncResult<RowSet<Row>>> handler) {
        if (qp.succeeded()) {
            if (qp.getBatchParams() == null) {
                handler.handle(Future.failedFuture("Sql params get null = " + qp.getSql()));
            } else {
                pool().preparedQuery(qp.getSql()).executeBatch(qp.getBatchParams(), handler);
            }
        } else {
            handler.handle(Future.failedFuture(String.format("Error generating sql sql = %s", qp.getSql())));
        }
    }

    default Future<RowSet<Row>> executeBatch(SqlAndParams qp) {
        Promise<RowSet<Row>> promise = Promise.promise();
        executeBatch(qp, promise);
        return promise.future();
    }

    default void save(T entity, Handler<AsyncResult<T>> handler) {
        SqlAndParams sqlAndParams = sqlStatement().insertNonEmptySQL(entity);
        execute(sqlAndParams, ar -> {
            if (ar.succeeded()) {
                RowSet<Row> rows = ar.result();
                if (rows.rowCount() > 0) {
                    try {
                        long lastInsertId = rows.property(MySQLClient.LAST_INSERTED_ID);
                        sqlStatement().setPrimaryId(entity, lastInsertId);
                    } catch (Exception ignored) {

                    }
                    handler.handle(Future.succeededFuture(entity));
                }
            }
            handler.handle(Future.failedFuture(ar.cause()));
        });
    }

    /**
     * <p>保存实体类对象数据。
     *
     * @param entity 实体类对象
     * @return {@code true} 保存成功，{@code false} 保存失败。
     */
    default Future<T> save(T entity) {
        Promise<T> promise = Promise.promise();
        save(entity, promise);
        return promise.future();
    }

    /**
     * <p>批量保存实体类对象数据。
     *
     * @param entities 实体类对象
     */
    default void saveBatch(List<T> entities, Handler<AsyncResult<Boolean>> handler) {
        SqlAndParams sqlAndParams = sqlStatement().insertBatchSQL(entities);
        executeBatch(sqlAndParams, ar -> {
            if (ar.succeeded()) {
                if (ar.result().rowCount() > 0) {
                    handler.handle(Future.succeededFuture(true));
                }
            }
            handler.handle(Future.failedFuture(ar.cause()));
        });
    }

    default Future<Boolean> saveBatch(List<T> entities) {
        Promise<Boolean> promise = Promise.promise();
        saveBatch(entities, promise);
        return promise.future();
    }

    default <S> void removeById(S primaryValue, Handler<AsyncResult<Boolean>> handler) {
        SqlAndParams sqlAndParams = sqlStatement().deleteByIdSQL(primaryValue);
        execute(sqlAndParams, ar -> {
            if (ar.succeeded()) {
                if (ar.result().rowCount() > 0) {
                    handler.handle(Future.succeededFuture(true));
                }
            }
            handler.handle(Future.failedFuture(ar.cause()));
        });
    }

    default <S> Future<Boolean> removeById(S primaryValue) {
        Promise<Boolean> promise = Promise.promise();
        removeById(primaryValue, promise);
        return promise.future();
    }

    default <S> void removeByIds(List<S> primaryValues, Handler<AsyncResult<Boolean>> handler) {
        SqlAndParams sqlAndParams = sqlStatement().deleteByIdsSQL(primaryValues);
        executeBatch(sqlAndParams, ar -> {
            if (ar.succeeded()) {
                if (ar.result().rowCount() > 0) {
                    handler.handle(Future.succeededFuture(true));
                }
            }
            handler.handle(Future.failedFuture(ar.cause()));
        });
    }

    default <S> Future<Boolean> removeByIds(List<S> primaryValues) {
        Promise<Boolean> promise = Promise.promise();
        removeByIds(primaryValues, promise);
        return promise.future();
    }

    default void remove(SqlAssist assist, Handler<AsyncResult<Boolean>> handler) {
        SqlAndParams sqlAndParams = sqlStatement().deleteByAssistSQL(assist);
        execute(sqlAndParams, ar -> {
            if (ar.succeeded()) {
                if (ar.result().rowCount() > 0) {
                    handler.handle(Future.succeededFuture(true));
                }
            }
            handler.handle(Future.failedFuture(ar.cause()));
        });
    }

    default Future<Boolean> remove(SqlAssist assist) {
        Promise<Boolean> promise = Promise.promise();
        remove(assist, promise);
        return promise.future();
    }

    default void updateById(T entity, Handler<AsyncResult<T>> handler) {
        SqlAndParams sqlAndParams = sqlStatement().updateNonEmptyByIdSQL(entity);
        execute(sqlAndParams, ar -> {
            if (ar.succeeded()) {
                RowSet<Row> rows = ar.result();
                if (rows.rowCount() > 0) {
                    handler.handle(Future.succeededFuture(entity));
                }
            }
            handler.handle(Future.failedFuture(ar.cause()));
        });
    }

    default Future<T> updateById(T entity) {
        Promise<T> promise = Promise.promise();
        updateById(entity, promise);
        return promise.future();
    }

    default void update(SqlAssist assist, T entity, Handler<AsyncResult<Boolean>> handler) {
        SqlAndParams sqlAndParams = sqlStatement().updateNonEmptyByAssistSQL(entity, assist);
        execute(sqlAndParams, ar -> {
            if (ar.succeeded()) {
                if (ar.result().rowCount() > 0) {
                    handler.handle(Future.succeededFuture(true));
                }
            }
            handler.handle(Future.failedFuture(ar.cause()));
        });
    }

    default Future<Boolean> update(SqlAssist assist, T entity) {
        Promise<Boolean> promise = Promise.promise();
        update(assist, entity, promise);
        return promise.future();
    }

    default void saveOrUpdate(T entity, Handler<AsyncResult<T>> handler) throws Exception {
        Object primaryKeyValue = sqlStatement().primaryKeyValue(sqlStatement().getPropertyValue(entity));
        if (sqlStatement().isPrimaryHasValue(primaryKeyValue)) {
            updateById(entity, handler);
        } else {
            save(entity, handler);
        }
    }

    default Future<T> saveOrUpdate(T entity) throws Exception {
        Promise<T> promise = Promise.promise();
        saveOrUpdate(entity, promise);
        return promise.future();
    }

    default <S> void getById(S primaryKeyValue, Handler<AsyncResult<T>> handler) {
        getByIdAs(primaryKeyValue, rowMapper(), handler);
    }

    default <S> Future<T> getById(S primaryKeyValue) {
        Promise<T> promise = Promise.promise();
        getById(primaryKeyValue, promise);
        return promise.future();
    }

    default <S, R> void getByIdAs(S primaryKeyValue, RowMapper<R> mapping, Handler<AsyncResult<R>> handler) {
        SqlAndParams sqlAndParams = sqlStatement().selectAllSQL(new SqlAssist(SqlWhereCondition.andEq(sqlStatement().primaryId(), primaryKeyValue)));
        execute(sqlAndParams, mapping).flatMap(rowSet ->
                Future.succeededFuture(rowSetToOne(rowSet))).onComplete(handler);
    }

    default <S, R> Future<R> getByIdAs(S primaryKeyValue, RowMapper<R> mapping) {
        Promise<R> promise = Promise.promise();
        getByIdAs(primaryKeyValue, mapping, promise);
        return promise.future();
    }

    default void getOne(SqlAssist sqlAssist, Handler<AsyncResult<T>> handler) {
        getOneAs(sqlAssist, rowMapper(), handler);
    }

    default Future<T> getOne(SqlAssist sqlAssist) {
        Promise<T> promise = Promise.promise();
        getOneAs(sqlAssist, rowMapper(), promise);
        return promise.future();
    }

    default <R> void getOneAs(SqlAssist sqlAssist, RowMapper<R> mapping, Handler<AsyncResult<R>> handler) {
        SqlAndParams sqlAndParams = sqlStatement().selectAllSQL(sqlAssist);
        execute(sqlAndParams, mapping).flatMap(rowSet ->
                Future.succeededFuture(rowSetToOne(rowSet))).onComplete(handler);
    }

    default <R> Future<R> getOneAs(SqlAssist sqlAssist, RowMapper<R> mapping) {
        Promise<R> promise = Promise.promise();
        getOneAs(sqlAssist, mapping, promise);
        return promise.future();
    }

    default void list(SqlAssist sqlAssist, Handler<AsyncResult<List<T>>> handler) {
        listAs(sqlAssist, rowMapper(), handler);
    }

    default Future<List<T>> list(SqlAssist sqlAssist) {
        Promise<List<T>> promise = Promise.promise();
        list(sqlAssist, promise);
        return promise.future();
    }

    default <R> void listAs(SqlAssist sqlAssist, RowMapper<R> mapping, Handler<AsyncResult<List<R>>> handler) {
        SqlAndParams sqlAndParams = sqlStatement().selectAllSQL(sqlAssist);
        execute(sqlAndParams, mapping).flatMap(rowSet ->
                Future.succeededFuture(rowSetToList(rowSet))).onComplete(handler);
    }

    default <R> Future<List<R>> listAs(SqlAssist sqlAssist, RowMapper<R> mapping) {
        Promise<List<R>> promise = Promise.promise();
        listAs(sqlAssist, mapping, promise);
        return promise.future();
    }

    default <S> void listByIds(List<S> primaryValues, Handler<AsyncResult<List<T>>> handler) {
        listByIdsAs(primaryValues, rowMapper(), handler);
    }

    default <S> Future<List<T>> listByIds(List<S> primaryValues) {
        Promise<List<T>> promise = Promise.promise();
        listByIds(primaryValues, promise);
        return promise.future();
    }

    default <S, R> void listByIdsAs(List<S> primaryValues, RowMapper<R> mapping, Handler<AsyncResult<List<R>>> handler) {
        SqlAndParams sqlAndParams = sqlStatement().selectAllSQL(new SqlAssist(SqlWhereCondition.andIn(sqlStatement().primaryId(), primaryValues)));
        execute(sqlAndParams, mapping).flatMap(rowSet ->
                Future.succeededFuture(rowSetToList(rowSet))).onComplete(handler);
    }

    default <S, R> Future<List<R>> listByIdsAs(List<S> primaryValues, RowMapper<R> mapping) {
        Promise<List<R>> promise = Promise.promise();
        listByIdsAs(primaryValues, mapping, promise);
        return promise.future();
    }

    default void count(SqlAssist sqlAssist, Handler<AsyncResult<Long>> handler) {
        SqlAndParams countSQL = sqlStatement().getCountSQL(sqlAssist);
        execute(countSQL, ar -> {
            if (ar.succeeded()) {
                handler.handle(Future.succeededFuture(ar.result().iterator().next().getLong(0)));
            } else {
                handler.handle(Future.failedFuture(ar.cause()));
            }
        });
    }

    default Future<Long> count(SqlAssist sqlAssist) {
        Promise<Long> promise = Promise.promise();
        count(sqlAssist, promise);
        return promise.future();
    }

    default void countByCustomSql(String sqlString, SqlAssist sqlAssist, Handler<AsyncResult<Long>> handler) {
        SqlAndParams countSQL = sqlStatement().countByCustomSql(sqlString, sqlAssist);
        execute(countSQL, ar -> {
            if (ar.succeeded()) {
                handler.handle(Future.succeededFuture(ar.result().iterator().next().getLong(0)));
            } else {
                handler.handle(Future.failedFuture(ar.cause()));
            }
        });
    }

    default Future<Long> countByCustomSql(String sqlString, SqlAssist sqlAssist) {
        Promise<Long> promise = Promise.promise();
        countByCustomSql(sqlString, sqlAssist, promise);
        return promise.future();
    }

    default void page(SqlAssist sqlAssist, Handler<AsyncResult<Page<T>>> handler) {
        pageAs(sqlAssist, rowMapper(), handler);
    }


    default Future<Page<T>> page(SqlAssist sqlAssist) {
        Promise<Page<T>> promise = Promise.promise();
        page(sqlAssist, promise);
        return promise.future();
    }

    default <R> void pageAs(SqlAssist sqlAssist, RowMapper<R> mapping, Handler<AsyncResult<Page<R>>> handler) {
        if (sqlAssist == null) {
            handler.handle(Future.failedFuture("The SqlAssist cannot be null , you can pass in new SqlAssist()"));
            return;
        }
        if (sqlAssist.getPage() == null || sqlAssist.getPage() < 1L) {
            sqlAssist.setPage(1L);
        }
        if (sqlAssist.getRowSize() == null || sqlAssist.getRowSize() < 1L) {
            sqlAssist.setRowSize(15L);
        }
        if (sqlAssist.getPage() == 1L) {
            sqlAssist.setStartRow(0L);
        } else {
            sqlAssist.setStartRow((sqlAssist.getPage() - 1L) * sqlAssist.getRowSize());
        }
        count(sqlAssist, countAr -> {
            if (countAr.succeeded()) {
                Long count = countAr.result();
                Page<R> result = new Page<>(count, sqlAssist.getPage(), sqlAssist.getRowSize());
                if (count == 0 || sqlAssist.getPage() > result.getPages()) {
                    handler.handle(Future.succeededFuture(new Page<>()));
                } else {
                    listAs(sqlAssist, mapping, ar -> {
                        if (ar.succeeded()) {
                            result.setList(ar.result());
                            handler.handle(Future.succeededFuture(result));
                        } else {
                            handler.handle(Future.failedFuture(ar.cause()));
                        }
                    });
                }
            } else {
                handler.handle(Future.failedFuture(countAr.cause()));
            }
        });
    }

    default <R> Future<Page<R>> pageAs(SqlAssist sqlAssist, RowMapper<R> mapping) {
        Promise<Page<R>> promise = Promise.promise();
        pageAs(sqlAssist, mapping, promise);
        return promise.future();
    }

    default void getOneByCustomSql(String sqlString, SqlAssist sqlAssist, Handler<AsyncResult<T>> handler) {
        getOneByCustomSqlAs(sqlString, sqlAssist, rowMapper(), handler);
    }

    default Future<T> getOneByCustomSql(String sqlString, SqlAssist sqlAssist) {
        Promise<T> promise = Promise.promise();
        getOneByCustomSql(sqlString, sqlAssist, promise);
        return promise.future();
    }


    /**
     * 自定义sql 获取一条数据
     *
     * @param sqlString sql
     * @param sqlAssist 条件
     * @param mapping   映射
     * @param handler   处理器
     * @param <R>       返回类型
     */
    default <R> void getOneByCustomSqlAs(String sqlString, SqlAssist sqlAssist, RowMapper<R> mapping, Handler<AsyncResult<R>> handler) {
        SqlAndParams sqlAndParams = sqlStatement().selectByCustomSql(sqlString, sqlAssist);
        execute(sqlAndParams, mapping).flatMap(rowSet ->
                Future.succeededFuture(rowSetToOne(rowSet))).onComplete(handler);
    }

    default <R> Future<R> getOneByCustomSqlAs(String sqlString, SqlAssist sqlAssist, RowMapper<R> mapping) {
        Promise<R> promise = Promise.promise();
        getOneByCustomSqlAs(sqlString, sqlAssist, mapping, promise);
        return promise.future();
    }

    default void listByCustomSql(String sqlString, SqlAssist sqlAssist, Handler<AsyncResult<List<T>>> handler) {
        listByCustomSqlAs(sqlString, sqlAssist, rowMapper(), handler);
    }

    default Future<List<T>> listByCustomSql(String sqlString, SqlAssist sqlAssist) {
        Promise<List<T>> promise = Promise.promise();
        listByCustomSql(sqlString, sqlAssist, promise);
        return promise.future();
    }

    default <R> void listByCustomSqlAs(String sqlString, SqlAssist sqlAssist, RowMapper<R> mapping, Handler<AsyncResult<List<R>>> handler) {
        SqlAndParams sqlAndParams = sqlStatement().selectByCustomSql(sqlString, sqlAssist);
        execute(sqlAndParams, mapping).flatMap(rowSet ->
                Future.succeededFuture(rowSetToList(rowSet))).onComplete(handler);
    }

    default <R> Future<List<R>> listByCustomSqlAs(String sqlString, SqlAssist sqlAssist, RowMapper<R> mapping) {
        Promise<List<R>> promise = Promise.promise();
        listByCustomSqlAs(sqlString, sqlAssist, mapping, promise);
        return promise.future();
    }

    default void pageByCustomSql(String sqlString, SqlAssist sqlAssist, Handler<AsyncResult<Page<T>>> handler) {
        pageByCustomSqlAs(sqlString, sqlAssist, rowMapper(), handler);
    }

    default Future<Page<T>> pageByCustomSql(String sqlString, SqlAssist sqlAssist) {
        Promise<Page<T>> promise = Promise.promise();
        pageByCustomSql(sqlString, sqlAssist, promise);
        return promise.future();
    }

    default <R> void pageByCustomSqlAs(String sqlString, SqlAssist sqlAssist, RowMapper<R> mapping, Handler<AsyncResult<Page<R>>> handler) {
        if (sqlAssist == null) {
            handler.handle(Future.failedFuture("The SqlAssist cannot be null , you can pass in new SqlAssist()"));
            return;
        }
        if (sqlAssist.getPage() == null || sqlAssist.getPage() < 1L) {
            sqlAssist.setPage(1L);
        }
        if (sqlAssist.getRowSize() == null || sqlAssist.getRowSize() < 1L) {
            sqlAssist.setRowSize(15L);
        }
        if (sqlAssist.getPage() == 1L) {
            sqlAssist.setStartRow(0L);
        } else {
            sqlAssist.setStartRow((sqlAssist.getPage() - 1L) * sqlAssist.getRowSize());
        }
        countByCustomSql(sqlString, sqlAssist, countAr -> {
            if (countAr.succeeded()) {
                Long count = countAr.result();
                Page<R> result = new Page<>(count, sqlAssist.getPage(), sqlAssist.getRowSize());
                if (count == 0 || sqlAssist.getPage() > result.getPages()) {
                    handler.handle(Future.succeededFuture(new Page<>()));
                } else {
                    listByCustomSqlAs(sqlString, sqlAssist, mapping, ar -> {
                        if (ar.succeeded()) {
                            result.setList(ar.result());
                            handler.handle(Future.succeededFuture(result));
                        } else {
                            handler.handle(Future.failedFuture(ar.cause()));
                        }
                    });
                }
            } else {
                handler.handle(Future.failedFuture(countAr.cause()));
            }
        });
    }

    default <R> Future<Page<R>> pageByCustomSqlAs(String sqlString, SqlAssist sqlAssist, RowMapper<R> mapping) {
        Promise<Page<R>> promise = Promise.promise();
        pageByCustomSqlAs(sqlString, sqlAssist, mapping, promise);
        return promise.future();
    }
}
