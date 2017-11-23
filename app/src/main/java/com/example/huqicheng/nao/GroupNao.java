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
    static final String TAG="GroupNao";
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

            Log.e(TAG,json);

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

    public String createGroup(String name,String description,long timestamp,long user_id) {
        String res = "";
        try {
            // add your parameters here
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("creator_id", user_id + ""));
            params.add(new BasicNameValuePair("projectName", name + ""));
            params.add(new BasicNameValuePair("projectDeadline", timestamp + ""));
            params.add(new BasicNameValuePair("projectDescription", description + ""));
            //modify url according to interface doc
            HttpEntity entity = HttpUtils.execute(Config.SERVER_IP + "/addGroup.do", params, HttpUtils.POST);

            if (entity == null) {
                return null;
            }

            //convert stream to json String
            String json = EntityUtils.toString(entity);

            Log.d(TAG, json);

            // check if failed, you should return null
            if (json.trim().equalsIgnoreCase("failed")) return null;

            // decoding here
            // Type:   simple objects: ObjectName.class
            //         complex objects such as List, Map: TypeToken<ArrayList<ObjectName>>(){}.getType()
            res = json;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return res;
    }

    public String dropGroups(long user_id,long group_id) {
        String res = "";
        try {
            // add your parameters here
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("user_id", user_id + ""));
            params.add(new BasicNameValuePair("group_id", group_id + ""));

            //modify url according to interface doc
            HttpEntity entity = HttpUtils.execute(Config.SERVER_IP + "/dropFromGroup.do", params, HttpUtils.POST);

            if (entity == null) {
                return null;
            }

            //convert stream to json String
            String json = EntityUtils.toString(entity);

            Log.d("debug: ", json);

            // check if failed, you should return null
            if (json.trim().equalsIgnoreCase("failed")) return null;

            // decoding here
            // Type:   simple objects: ObjectName.class
            //         complex objects such as List, Map: TypeToken<ArrayList<ObjectName>>(){}.getType()
            res = json;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return res;
    }
    public String joinGroup(long user_id,long group_id) {
        String res = "";
        try {
            // add your parameters here
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("user_id", user_id + ""));
            params.add(new BasicNameValuePair("group_id", group_id + ""));

            //modify url according to interface doc
            HttpEntity entity = HttpUtils.execute(Config.SERVER_IP + "/joinGroup.do", params, HttpUtils.POST);

            if (entity == null) {
                return null;
            }

            //convert stream to json String
            String json = EntityUtils.toString(entity);

            Log.d("debug: ", json);

            // check if failed, you should return null
            if (json.trim().equalsIgnoreCase("failed")) return null;

            // decoding here
            // Type:   simple objects: ObjectName.class
            //         complex objects such as List, Map: TypeToken<ArrayList<ObjectName>>(){}.getType()
            res = json;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return res;
    }
    public List<User> getUsersOfSpecificGroup(long group_id){
        List<User> res = null;
        try{
            // add your parameters here
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("group_id",group_id+""));
            //modify url according to interface doc
            HttpEntity entity = HttpUtils.execute(Config.SERVER_IP+"/getUsersOfGroup.do",params,HttpUtils.GET);
            //convert stream to json String
            String json = EntityUtils.toString(entity);
            Log.d("debug: ",json);
            // check if failed, you should return null
            if(json.trim().equalsIgnoreCase("failed")) return null;
            Type type = new TypeToken<ArrayList<User>>(){}.getType();
            res = new Gson().fromJson(json,type);
        }catch (Exception e){
            e.printStackTrace();

        }
        return res;
    }
    public List<User> getAllUser(String username){
        List<User> res = null;
        try{
            // add your parameters here
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("username",username+""));
            //modify url according to interface doc
            HttpEntity entity = HttpUtils.execute(Config.SERVER_IP+"/getUsers.do",params,HttpUtils.GET);
            //convert stream to json String
            String json = EntityUtils.toString(entity);
            Log.d("debug: ",json);
            // check if failed, you should return null
            if(json.trim().equalsIgnoreCase("failed")) return null;
            Type type = new TypeToken<ArrayList<User>>(){}.getType();
            res = new Gson().fromJson(json,type);
        }catch (Exception e){
            e.printStackTrace();

        }
        return res;
    }

    public List<Group> getGroupsInProgress(long user_id){
        List<Group> res = null;
        try{
            // add your parameters here
            List<NameValuePair> params = new ArrayList<>();

            params.add(new BasicNameValuePair("user_id",user_id+""));

            //modify url according to interface doc
            HttpEntity entity = HttpUtils.execute(Config.SERVER_IP+"/getGroupsIncludingPersonal.do",params,HttpUtils.GET);

            //convert stream to json String
            String json = EntityUtils.toString(entity);

            Log.e(TAG,json);

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
