package com.example.huqicheng.entity;

/**
 * 一个消息的JavaBean
 * 
 * @author way
 * 
 */
public class ChatMsgEntity {
	private String name;//消息来自
	private String date;//消息日期
	private String message;//消息内容
	private String avatar;
	private boolean isComMeg = true;// 是否为收到的消息

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean getMsgType() {
		return isComMeg;
	}

	public void setMsgType(boolean isComMsg) {
		isComMeg = isComMsg;
	}

	public ChatMsgEntity() {
	}

	public boolean isComMeg() {
		return isComMeg;
	}

	public void setComMeg(boolean comMeg) {
		isComMeg = comMeg;
	}

	public ChatMsgEntity(String name, String date, String text, boolean isComMsg) {
		super();
		this.name = name;
		this.date = date;
		this.message = text;
		this.isComMeg = isComMsg;
	}

}
