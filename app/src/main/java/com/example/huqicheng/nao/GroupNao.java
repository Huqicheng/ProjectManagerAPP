package com.example.huqicheng.nao;

import android.util.Log;

import com.example.huqicheng.config.Config;
import com.example.huqicheng.entity.Group;
import com.example.huqicheng.entity.User;
import com.example.huqicheng.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huqicheng on 2017/11/7.
 */

public class GroupNao {
    public List<Group> getGroups(long user_id){
        List<Group> res = null;
        try{
            // add your parameters here
            List<NameValuePair> params = new ArrayList<>();

            params.add(new BasicNameValuePair("user_id",user_id+""));

            //modify url according to interface doc
            HttpEntity entity = HttpUtils.execute(Config.SERVER_IP+"/getGroupOfUser.do",params,HttpUtils.GET);

            //convert stream to json String
            String json = EntityUtils.toString(entity);

            Log.d("debug: ",json);

            // check if failed, you should return null
            if(json.trim().equalsIgnoreCase("failed")) return null;

            // decoding here
            // Type:   simple objects: ObjectName.class
            //         complex objects such as List, Map: TypeToken<ArrayList<ObjectName>>(){}.getType();
            Type type = new TypeToken<ArrayList<Group>>(){}.getType();
            res = new Gson().fromJson(json,type);


        }catch (Exception e){
            e.printStackTrace();

        }

        return res;
    }
}
