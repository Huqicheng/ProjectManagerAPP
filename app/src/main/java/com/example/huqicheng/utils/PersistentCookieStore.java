package com.example.huqicheng.utils;

import android.util.Log;

import com.example.huqicheng.config.Config;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huqicheng on 2017/10/21.
 */

public class PersistentCookieStore implements CookieStore,Runnable{
    CookieStore store;

    private static PersistentCookieStore cookieStore;
    private static String IP_ADDRESS;

    public static PersistentCookieStore getInstance(String ip){

        if(cookieStore == null){
            cookieStore = new PersistentCookieStore();
            IP_ADDRESS = ip;

        }


        return cookieStore;
    }

    public PersistentCookieStore(){
        store = new CookieManager().getCookieStore();
        try {
            String jsonCookies = FileUtils.readFromFile("cookie_new");
            if(jsonCookies.equals("")){
                Runtime.getRuntime().addShutdownHook(new Thread(this));
                return;
            }

            Map<String,Object> cookies = new Gson().fromJson(jsonCookies,
                    new TypeToken<Map<String,Object>>() {}.getType());

            for(Map.Entry cookie : cookies.entrySet()){
                try {
                    this.add(new URI(Config.SERVER_IP),new HttpCookie((String)cookie.getKey(),cookie.getValue().toString()));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(this));


    }

    @Override
    public void run() {
        try {
            //printCookies();
            Map<String,String> map = new HashMap<>();
            try {
                List<HttpCookie> cookies = store.get(new URI(Config.SERVER_IP));
                for(int i=0;i<cookies.size();i++){
                    Log.d("debug: ",cookies.get(i).toString());
                    map.put(cookies.get(i).getName(),cookies.get(i).getValue());
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            String jsonCookies = new Gson().toJson(map);

            FileUtils.writeToFile("cookie_new", jsonCookies, null);

        }catch (Exception e){

        }






    }

    @Override
    public void add(URI uri, HttpCookie httpCookie) {
        store.add(uri,httpCookie);
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        return store.get(uri);
    }

    @Override
    public List<HttpCookie> getCookies() {
        return store.getCookies();
    }

    @Override
    public List<URI> getURIs() {
        return store.getURIs();
    }

    @Override
    public boolean remove(URI uri, HttpCookie httpCookie) {
        return store.remove(uri,httpCookie);
    }

    @Override
    public boolean removeAll() {
        return store.removeAll();
    }

    public HttpCookie getCookie(URI uri , String key){

        List<HttpCookie> cookies = store.get(uri);
        for(HttpCookie cookie : cookies){
            if(cookie.getName().equals(key)){
                return cookie;
            }
        }

        return null;
    }

    public CookieStore getStore(){
        return store;
    }

    public void printCookies(){
        try {
            List<HttpCookie> cookies = store.get(new URI(Config.SERVER_IP));
            for(int i=0;i<cookies.size();i++){
                Log.d("debug: ",cookies.toString());
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
