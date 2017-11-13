package com.example.huqicheng.entity;

import com.example.huqicheng.message.BaseMsg;

import java.util.List;

public class MsgList {

	List<BaseMsg> msgs;
	boolean hasMore;
	public List<BaseMsg> getMsgs() {
		return msgs;
	}
	public void setMsgs(List<BaseMsg> msgs) {
		this.msgs = msgs;
	}
	public boolean isHasMore() {
		return hasMore;
	}
	public void setHasMore(boolean hasMore) {
		this.hasMore = hasMore;
	}
	
	
}
