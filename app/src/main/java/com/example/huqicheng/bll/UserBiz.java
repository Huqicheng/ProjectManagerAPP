package com.example.huqicheng.bll;

import android.content.Context;

import com.example.huqicheng.dao.UserDao;
import com.example.huqicheng.entity.User;
import com.example.huqicheng.nao.UserNao;

/**
 * Created by huqicheng on 2017/11/7.
 */

public class UserBiz {

    private UserNao userNao = null;
    private UserDao userDao = null;

    public UserBiz(Context context){
        userNao = new UserNao();
        userDao = new UserDao(context);
    }

    public User doLogin(User user,String type){
        User res = userNao.doLoginByUsername(user);
        if(res == null) return null;
        userDao.saveUser(res);
        return res;
    }

    public User readUser(){
        return userDao.readUser();
    }
}
