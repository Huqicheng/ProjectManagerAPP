package com.example.huqicheng.dao;

import com.example.huqicheng.config.Config;
import com.example.huqicheng.utils.PersistentCookieStore;

import java.net.HttpCookie;
import java.net.URI;

/**
 * Created by huqicheng on 2017/10/21.
 */

public class CookieDao {
    PersistentCookieStore store = null;

    public CookieDao(){
        store = PersistentCookieStore.getInstance(Config.SERVER_IP);
    }

    //new URI(Config.SERVER_IP)
    public void add(URI uri, String key, String value){
        store.add(uri, new HttpCookie(key,value));
    }

}
