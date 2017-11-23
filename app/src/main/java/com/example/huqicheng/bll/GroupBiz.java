package com.example.huqicheng.bll;

import com.example.huqicheng.entity.Event;
import com.example.huqicheng.entity.EventStat;
import com.example.huqicheng.entity.Group;
import com.example.huqicheng.entity.User;
import com.example.huqicheng.nao.GroupNao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by huqicheng on 2017/10/14.
 */

public class GroupBiz {

    private static GroupNao groupNao = null;
    public GroupBiz(){
        groupNao = new GroupNao();
    }
    public List<Group> loadGroups(long user_id){
        return groupNao.getGroups(user_id);
    }

    public String dropGroup(long user_id, long group_id) {
        return groupNao.dropGroups(user_id, group_id);
    }
    public List<User> getAllUser(String username) {
        return groupNao.getAllUser(username);
    }
    public String joinGroup(long user_id, long group_id) {
        return groupNao.joinGroup(user_id, group_id);
    }
    public List<User> loadUsersofSpecificGroup(long group_id){
        return groupNao.getUsersOfSpecificGroup(group_id);

    }
    public String createGroup(String name,String description,long deadline,long user_id){
        return groupNao.createGroup(name,description,deadline,user_id);
    }

    public List<Group> loadGroupinProgress(long user_id){
        return groupNao.getGroupsInProgress(user_id);
    }

    public Map<Integer,EventStat> loadGropStats(long user_id){
        return  groupNao.getGroupStats(user_id);
    }

}
