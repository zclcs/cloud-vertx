package com.zclcs.common.local.filter;

import io.vertx.core.Future;

/**
 * @author zclcs
 */
public class AsyncBloomFilter {

    private final boolean[] bits;
    private final int capacity;
    private final int hashFunctions;
    private int hashSeed;

    public AsyncBloomFilter(int capacity, int hashFunctions) {
        this.capacity = capacity;
        this.hashFunctions = hashFunctions;

        // 初始化布隆过滤器
        bits = new boolean[capacity];
        this.hashSeed = 1;
    }

    /**
     * 添加元素
     *
     * @param element 元素
     * @return 异步结果
     */
    public Future<Void> add(String element) {
        for (int i = 0; i < hashFunctions; i++) {
            int hash = element.hashCode() ^ hashSeed;
            hash = (hash << 15) | (hash >>> 17);
            int index = hash % capacity;
            bits[index] = true;
            hashSeed = 31 * hashSeed + element.length();
        }
        return Future.succeededFuture();
    }

    /**
     * 判断元素是否存在于布隆过滤器中
     *
     * @param element 元素
     * @return 异步结果
     */
    public Future<Boolean> mayContain(String element) {
        for (int i = 0; i < hashFunctions; i++) {
            int hash = element.hashCode() ^ hashSeed;
            hash = (hash << 15) | (hash >>> 17);
            int index = hash % capacity;
            if (!bits[index]) {
                return Future.succeededFuture(false);
            }
            hashSeed = 31 * hashSeed + element.length();
        }
        return Future.succeededFuture(true);
    }
}
