package com.example.huqicheng.utils;

import com.example.huqicheng.config.Config;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import  org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.BasicHttpContext;


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieStore;
import java.util.List;


/**
 * Created by huqicheng on 2017/10/21.
 */

public class HttpUtils {

    public static final int GET = 1;
    public static final int POST = 2;

    public static InputStream getStream(HttpEntity entity){
        if(entity != null){
            try {
                return entity.getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    /*
    * method: execute
    * params: url  String
    *         params Map<NameValuePair>
    *         method int (HttpUtils.GET/HttpUtils.POST)
    *
    * example: com.example.huqicheng.nao.UserNao
    */

    public static HttpEntity execute(String url, List<NameValuePair> params,int method){
        HttpEntity entity = null;
        HttpClient client = new DefaultHttpClient();
        client.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT,10*1000);

        HttpUriRequest request = null;
        CookieStore store = PersistentCookieStore.getInstance(Config.SERVER_IP).getStore();

        switch(method){
            case GET:
                StringBuffer sb = new StringBuffer(url);
                if(params!=null && !params.isEmpty()){
                    sb.append('?');
                    for(NameValuePair p : params){
                        sb.append(p.getName()).append('=').append(p.getValue()).append('&');
                    }
                    sb.deleteCharAt(sb.length()-1);
                }
                request = new HttpGet(sb.toString());
                break;

            case POST:
                request = new HttpPost(url);
                if(params!=null && !params.isEmpty()){
                    try {
                        UrlEncodedFormEntity reqEntity = new UrlEncodedFormEntity(params);
                        ((HttpPost)request).setEntity(reqEntity);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
        }

        try {


            if(request == null) return null;


            HttpResponse response = client.execute(request);

            int statusCode = response.getStatusLine().getStatusCode();

            if(statusCode == 200) {
                entity = response.getEntity();
            }
            else
                return null;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return entity;

    }


}
