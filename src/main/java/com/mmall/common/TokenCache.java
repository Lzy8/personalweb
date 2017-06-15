package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.oracle.tools.packager.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by Liuzy on 2017/6/13.
 */
public class TokenCache {

    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    private static LoadingCache<String,String> localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS).build(new CacheLoader<String, String>() {

        //key没有值时,调用此方法;
        @Override
        public String load(String s) throws Exception {
            return "null";
        }
    });
    public static void setKey(String key,String value){
        localCache.put(key,value);
    }
    public static String getValueForKey(String key){

        String value = null;

        try {
            value = localCache.get(key);

            if ("null".equals(value)){
                return null;
            }
            return value;
        }catch (Exception e) {
            logger.error("expection");
        }
        return null;
    }

}
