package com.example.huqicheng.message;


public enum  MsgType {
	/*
	 * heart beat
	 * params : None
	 */
    PING,

    /*
     * Authentication
     * params: cookie.clientId -- String
     *         clientVersion -- Long(optional)
     */
    LOGIN,
    /*
     * Group Chat
     * params: body -- String
     * params: username -- String
     */
    ChatMsg,
    /*
     * Reply for ChatMsg
     * params: body -- String (exactly same as ChatMsg)
     *         status -- Integer    
     */
    ReplyForChatMsg
    
}
