package com.zclcs.sql.helper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * id列关联表的注解
 *
 * @author <a href="https://zclcstools.org">zclcs</a>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableId {
    String name() default "";

    String alias() default "";
}
