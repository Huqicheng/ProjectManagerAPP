package com.example.huqicheng.nao;

import android.util.Log;

import com.example.huqicheng.config.Config;
import com.example.huqicheng.entity.Group;
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
 * Created by huqicheng on 2017/11/7.
 */

public class EventNao {
    public List<Date> getDatesHavingEvents(int user_id, String status){
        List<Date> res = null;
        List<Long> tmp = null;
        try{
            // add your parameters here
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("user_id",user_id+""));
            params.add(new BasicNameValuePair("status",status));

            //modify url according to interface doc
            HttpEntity entity = HttpUtils.execute(Config.SERVER_IP+"/getDatesHavingEvents.do",params,HttpUtils.GET);

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
            Type type = new TypeToken<ArrayList<Long>>(){}.getType();
            tmp = new Gson().fromJson(json,type);

            // convert Long to Date Object
            for(Long timestamp:tmp){
                res.add(new Date(timestamp));
            }


        }catch (Exception e){
            e.printStackTrace();

        }

        return res;
    }
}
