package com.zclcs.sql.helper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类属性关联列的注解
 *
 * @author <a href="https://zclcstools.org">zclcs</a>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableColumn {
    String name() default "";

    String alias() default "";
}
