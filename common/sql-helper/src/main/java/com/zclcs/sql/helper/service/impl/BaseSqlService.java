package com.zclcs.sql.helper.service.impl;

import com.zclcs.sql.helper.service.SqlService;
import com.zclcs.sql.helper.statement.SQLStatement;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.templates.RowMapper;

public class BaseSqlService<T> implements SqlService<T> {

    private final SQLStatement sqlStatement;
    private final Pool pool;
    private final RowMapper<T> rowMapper;
    private final Class<T> entityClass;

    public BaseSqlService(Pool pool, RowMapper<T> rowMapper, Class<T> entityClass) {
        this.sqlStatement = SQLStatement.create(entityClass);
        this.rowMapper = rowMapper;
        this.pool = pool;
        this.entityClass = entityClass;
    }

    @Override
    public SQLStatement sqlStatement() {
        return sqlStatement;
    }

    @Override
    public Pool pool() {
        return pool;
    }

    @Override
    public RowMapper<T> rowMapper() {
        return rowMapper;
    }
}
