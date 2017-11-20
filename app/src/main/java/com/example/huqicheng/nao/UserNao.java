package com.example.huqicheng.nao;

import android.util.Log;

import com.example.huqicheng.config.Config;
import com.example.huqicheng.entity.User;
import com.example.huqicheng.utils.HttpUtils;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huqicheng on 2017/10/21.
 */

public class UserNao {

    public UserNao(){

    }

    //fake data
    public User doLogin(){
        return new User();
    }

    public User doLoginByUsername(User user){
        User res = null;
        try{
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("username",user.getUsername()));
            params.add(new BasicNameValuePair("pwd",user.getPassword()));
            params.add(new BasicNameValuePair("type","username"));
            HttpEntity entity = HttpUtils.execute(Config.SERVER_IP+"/doLogin.do",params,HttpUtils.POST);

            String json = EntityUtils.toString(entity);

            Log.d("debug: ",json);

            if(json.trim().equalsIgnoreCase("failed")) return null;

            res = new Gson().fromJson(json,User.class);


        }catch (Exception e){
            e.printStackTrace();

        }

        return res;
    }

    //register
    public User registerUser(String user_email, String username, String pwd, String type) {
        User res = null;
        try{
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("email",user_email));
            params.add(new BasicNameValuePair("username",username));
            params.add(new BasicNameValuePair("pwd",pwd));
            params.add(new BasicNameValuePair("type",type));
            HttpEntity entity = HttpUtils.execute(Config.SERVER_IP+"/doRegister.do",params,HttpUtils.POST);

            String json = EntityUtils.toString(entity);

            Log.d("debug: ",json);

            if(json.trim().equalsIgnoreCase("failed")) return null;

            res = new Gson().fromJson(json,User.class);


        }catch (Exception e){
            e.printStackTrace();

        }
        return res;
    }

    //update user information
    public String updateUserInformation(User user) {
        //List<Event> res = null;
        try{
            // decoding here
            // Type:   simple objects: ObjectName.class
            //         complex objects such as List, Map: TypeToken<ArrayList<ObjectName>>(){}.getType();
            Type type = User.class;
            String json = new Gson().toJson(user,type);
            Log.d("In EventNao: ",json);

            // add your parameters here
            List<NameValuePair> params = new ArrayList<>();

            params.add(new BasicNameValuePair("jsonUser",json));

            //modify url according to interface doc
            HttpEntity entity = HttpUtils.execute(Config.SERVER_IP+"/updateUserInformation.do",params,HttpUtils.POST);

            if(entity == null){
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        String res = "success";
        return res;
    }

    public static void main(String[] args){

    }


}
