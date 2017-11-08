package com.example.huqicheng.bll;

import com.example.huqicheng.entity.Group;
import com.example.huqicheng.nao.GroupNao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huqicheng on 2017/10/14.
 */

public class GroupBiz {

    private GroupNao groupNao = null;
    public GroupBiz(){
        groupNao = new GroupNao();
    }
    public List<Group> loadGroups(long user_id){
        return groupNao.getGroups(user_id);
    }
}
