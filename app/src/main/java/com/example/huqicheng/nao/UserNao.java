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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huqicheng on 2017/10/21.
 */

public class UserNao {

    public UserNao(){

    }

    //placeholder
    public User doLogin(){
        return new User();
    }

    public User doLogin(User user){
        User res = null;
        try{
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("username",user.getUsername()));
            params.add(new BasicNameValuePair("pwd",user.getPassword()));
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

    public static void main(String[] args){
        UserNao nap = new UserNao();
        User user = new User();
        user.setUsername("q45hu");
        user.setPassword("q45hu");
        nap.doLogin(user);
    }


}
