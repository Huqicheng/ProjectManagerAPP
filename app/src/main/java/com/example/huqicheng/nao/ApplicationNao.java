package com.example.huqicheng.nao;

import android.util.Log;

import com.example.huqicheng.config.Config;
import com.example.huqicheng.entity.Application;
import com.example.huqicheng.entity.Event;
import com.example.huqicheng.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by huqicheng on 2017/11/16.
 */

public class ApplicationNao {

    public List<Application> getApplications(long userId){
        List<Application> res = null;
        try{
            // add your parameters here
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("user_id",userId+""));

            //modify url according to interface doc
            HttpEntity entity = HttpUtils.execute(Config.SERVER_IP+"/getApplications.do",params,HttpUtils.GET);

            if(entity == null){
                return null;
            }

            //convert stream to json String
            String json = EntityUtils.toString(entity);

            Log.d("debug: ",json);

            // check if failed, you should return null
            if(json.trim().equalsIgnoreCase("failed")) return null;

            // decoding here
            // Type:   simple objects: ObjectName.class
            //         complex objects such as List, Map: TypeToken<ArrayList<ObjectName>>(){}.getType();
            Type type = new TypeToken<ArrayList<Application>>(){}.getType();
            res = new Gson().fromJson(json,type);
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    public Application addAplication(long fromId, long toId, long groupId){
        Application res = null;
        try{
            // add your parameters here
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("fromId",fromId+""));
            params.add(new BasicNameValuePair("toId",toId+""));
            params.add(new BasicNameValuePair("groupId",groupId+""));

            //modify url according to interface doc
            HttpEntity entity = HttpUtils.execute(Config.SERVER_IP+"/addApplication.do",params,HttpUtils.GET);

            if(entity == null){
                return null;
            }

            //convert stream to json String
            String json = EntityUtils.toString(entity);

            Log.d("debug: ",json);

            // check if failed, you should return null
            if(json.trim().equalsIgnoreCase("failed")) return null;

            // decoding here
            // Type:   simple objects: ObjectName.class
            //         complex objects such as List, Map: TypeToken<ArrayList<ObjectName>>(){}.getType()
            res = new Gson().fromJson(json,Application.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    public boolean finishApplication(long appId, int isAccept){
        boolean res = false;
        try{
            // add your parameters here
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("appId",appId+""));
            params.add(new BasicNameValuePair("isAccept",isAccept+""));


            //modify url according to interface doc
            HttpEntity entity = HttpUtils.execute(Config.SERVER_IP+"/finishApplication.do",params,HttpUtils.GET);

            if(entity == null){
                return false;
            }

            //convert stream to json String
            String json = EntityUtils.toString(entity);

            Log.d("debug: ",json);

            // check if failed, you should return null
            if(json.trim().equalsIgnoreCase("failed")) return false;

            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }
}
