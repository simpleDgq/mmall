package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class TokenCache {

    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);
    //maximumSize设置最大缓存量，当超过这个值的时候，会使用LRU算法清除缓存
    private static LoadingCache<String, String> localCache = CacheBuilder.newBuilder().initialCapacity(1000)
            .maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS).build(new CacheLoader<String, String>() {
                @Override
                // 默认的数据加载实现。当调用get取值的时候，如果key没有对应的值，就调用这个方法进行加载
                public String load(String s) throws Exception {
                    return "null"; // 没有key命中，返回"null"
                }
            });

    /**
     * 网guava缓存中放值
     * @param key
     * @param value
     */
    public static void setKey(String key, String value) {
        localCache.put(key,value);
    }

    /**
     * 取缓存中的值
     * @param key
     */
    public static String getKey(String key) {
        String value = null;
        try {
            value = localCache.get(key);
            if("null".equals(value)) { // 如果key没有命中，get方法会返回"null"
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            logger.error("localCache get error", e);
        }
        return null;
    }

}
