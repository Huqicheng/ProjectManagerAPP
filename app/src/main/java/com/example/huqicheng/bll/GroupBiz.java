package com.example.huqicheng.bll;

import com.example.huqicheng.entity.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huqicheng on 2017/10/14.
 */

public class GroupBiz {

    public List<Group> loadGroups(){
        List<Group> groups = new ArrayList<>();
        for(int i=0;i<10;i++){
            Group group = new Group();
            group.setGroupId(i);
            group.setGroupName("group"+i);
            groups.add(group);
        }
        return groups;
    }
}
