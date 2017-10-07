package com.example.huqicheng.message;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



public class BaseMsg  implements Serializable {
    private static final long serialVersionUID = 1L;
    private MsgType type;
    private String clientId;
    private String groupId;
    private Date date;
    private Map<String,Object> params;
    
    public BaseMsg() {
        this.clientId = Constants.getClientId();
        params = new HashMap<String,Object>();
    }
    
    public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public MsgType getType() {
        return type;
    }

    public void setType(MsgType type) {
        this.type = type;
    }
    
    public Map<String,Object> getParams(){
    	return params;
    }
    
    public void putParams(String key, Object val){
    	params.put(key, val);
    }

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
    
}
