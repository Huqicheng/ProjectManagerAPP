package com.example.huqicheng.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;


import com.example.huqicheng.entity.User;

/**
 * Created by huqicheng on 2017/11/7.
 */

public class UserDao {
    private SharedPreferences pref;

    public UserDao(Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveUser(User user) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("userid", String.valueOf(user.getUserId()));
        editor.putString("username", user.getUsername());
        editor.putString("avatar",user.getAvatar());
        editor.commit();
    }

    public void removeUser() {
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("userid");
        editor.remove("username");
        editor.remove("avatar");
        editor.commit();
    }

    public User readUser() {
        User user = null;
        String id = pref.getString("userid", null);
        String name = pref.getString("username", null);
        String avatar = pref.getString("avatar",null);
        if (id != null && name != null) {
            user = new User();
            user.setUserId(Long.parseLong(id));
            user.setUsername(name);
            user.setAvatar(avatar);
        }
        return user;
    }
}
