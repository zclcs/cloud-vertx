package com.zclcs.common.core.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zclcs
 */
public class BeanToMapUtil {

    /**
     * 将Java Bean转换为Map对象。
     *
     * @param bean 需要转换的Java Bean对象
     * @return 转换后的Map对象
     */
    public static Map<String, Object> beanToMap(Object bean) {
        if (bean == null) {
            return null;
        }

        Field[] fields = bean.getClass().getDeclaredFields();
        Map<String, Object> map = new HashMap<>(fields.length + 1);
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(bean));
            } catch (IllegalAccessException e) {
                // ignore 暂时忽略
            }
        }
        return map;
    }
}

