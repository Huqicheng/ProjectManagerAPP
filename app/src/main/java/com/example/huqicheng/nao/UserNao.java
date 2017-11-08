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

    public static void main(String[] args){

    }


}
