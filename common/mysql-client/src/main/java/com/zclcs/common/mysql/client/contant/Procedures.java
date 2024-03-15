package com.zclcs.common.mysql.client.contant;

/**
 * @author zclcs
 */
public class Procedures {

    public static final String[] PROCEDURES = new String[]{
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
}
