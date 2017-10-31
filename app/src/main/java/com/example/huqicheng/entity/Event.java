package entity;

import java.util.Date;



/**
 * Created by jiaxinf on 2017-10-17.
 */

public class Event implements java.io.Serializable{
    long eventID;
    String title;
    String description;
    String deadLine;
    int assignedBy;
    String eventStatus;
    Date createdAt;
    Date updatedAt;
    int groupId;
    String groupName;

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

	public String getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(String deadLine) {
		this.deadLine = deadLine;
	}

	public int getAssignedBy() {
		return assignedBy;
	}

	public void setAssignedBy(int assignedBy) {
		this.assignedBy = assignedBy;
	}

	public String getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public long getEventID() {
		return eventID;
	}

	public String getEventTitle() {
        return title;
    }

    public String geteventDescription() {
        return description;
    }

    public long getEventId() {
        return eventID;
    }

    public void setEventID(long groupId) {
        this.eventID = groupId;
    }

    public void setEventTitle(String eventTitle) {
        this.title = eventTitle;
    }

    public void setEventDescription(String eventDescription) {
        this.description = eventDescription;
    }
}
