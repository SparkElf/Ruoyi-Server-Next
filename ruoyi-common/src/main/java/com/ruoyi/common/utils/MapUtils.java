package com.ruoyi.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjUtil;
import org.apache.commons.collections4.KeyValue;


import java.util.*;
import java.util.function.Consumer;

/**
 * Map 工具类
 *
 * @author 芋道源码
 */
public class MapUtils {


    /**
     * 从哈希表查找到 key 对应的 value，然后进一步处理
     * key 为 null 时, 不处理
     * 注意，如果查找到的 value 为 null 时，不进行处理
     *
     * @param map 哈希表
     * @param key key
     * @param consumer 进一步处理的逻辑
     */
    public static <K, V> void findAndThen(Map<K, V> map, K key, Consumer<V> consumer) {
        if (ObjUtil.isNull(key) || CollUtil.isEmpty(map)) {
            return;
        }
        V value = map.get(key);
        if (value == null) {
            return;
        }
        consumer.accept(value);
    }

    public static <K, V> Map<K, V> convertMap(List<KeyValue<K, V>> keyValues) {
        Map<K, V> map = new LinkedHashMap<>(keyValues.size());
        keyValues.forEach(keyValue -> map.put(keyValue.getKey(), keyValue.getValue()));
        return map;
    }

}
