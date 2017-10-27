package com.example.huqicheng.service;

import com.example.huqicheng.message.BaseMsg;

public interface OnChatMsgRecievedListener {
    void onChatMsgRecieved(BaseMsg msg);
    long getId(long id);
}