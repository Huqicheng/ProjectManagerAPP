package com.example.huqicheng.entity;

import java.util.Date;



/**
 * Created by jiaxinf on 2017-10-17.
 */

public class Event implements java.io.Serializable{
	long eventID;
	String title;
	String description;
	long deadLine;
	long assignedBy;
	long assignedTo;
	String eventStatus;
	long createdAt;
	long updatedAt;
	long groupId;
	String groupName;
	boolean isSelected;



	String assignByName;

	public String getAssignByName() {
		return assignByName;
	}

	public void setAssignByName(String assignByName) {
		this.assignByName = assignByName;
	}


	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public long getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(long assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}



	public long getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(long deadLine) {
		this.deadLine = deadLine;
	}

	public long getAssignedBy() {
		return assignedBy;
	}

	public void setAssignedBy(long assignedBy) {
		this.assignedBy = assignedBy;
	}

	public String getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}



	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(long updatedAt) {
		this.updatedAt = updatedAt;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}



	public long getEventID() {
		return eventID;
	}

	public String getEventTitle() {
		return title;
	}

	public void setEventID(long eventID) {
		this.eventID = eventID;
	}


	public void setEventTitle(String eventTitle) {
		this.title = eventTitle;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public  void setSelected(boolean isSelected){this.isSelected = isSelected;}


}
