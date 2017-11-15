package com.example.huqicheng.message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by huqicheng on 2017/11/15.
 */

public class MsgCache {
    private static Map<String,Long> cache = new ConcurrentHashMap<String,Long>();

    public static void putPair(String key, Long value){
        cache.put(key,value);

    }

    public static Long getPair(String key){
        if(cache.containsKey(key)){
            long l = (Long)cache.get(key);

            //remove from cache
            cache.remove(key);

            return l;
        }
        return Long.valueOf(0);
    }

}
