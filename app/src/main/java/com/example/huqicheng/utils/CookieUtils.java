package com.example.huqicheng.utils;

import java.net.HttpCookie;

/**
 * Created by huqicheng on 2017/10/20.
 */

public class CookieUtils {
    private static HttpCookie cookies = null;

    public synchronized static HttpCookie getCookie(){
        if(cookies == null) {
            //load here
        }

        return cookies;
    }

    public static void put(String key, String value){

    }
}
