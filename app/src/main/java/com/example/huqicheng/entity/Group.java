package com.example.huqicheng.entity;

import java.util.Date;

/**
 * Created by huqicheng on 2017/10/14.
 */

public class Group implements java.io.Serializable{
    long groupId;
    String groupName;
    String groupDescription;
    String groupNew="";
    long createdAt;
    long updatedAt;
    int projectId;
    String cover;
    long deadLine;
    public String getCover() {
        return cover;
    }
    public String getGroupNew() {
        return groupNew;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
    public void setGroupNew(String gn) {
        this.groupNew = gn;
    }

    public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public long getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setDeadLine(long deadLine) {
        this.deadLine = deadLine;
    }

    public long getDeadLine() {
        return deadLine;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
