package com.example.huqicheng.adapter;

import com.example.huqicheng.entity.ChatMsgEntity;
import com.example.huqicheng.message.BaseMsg;
import com.example.huqicheng.utils.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import java.lang.System.*;

/**
 * Created by huqicheng on 2017/10/25.
 */

public class MsgAdapter {
    public BaseMsg ChatMsgEntity2BaseMsg(ChatMsgEntity msgEntity) {

        BaseMsg baseMsg = new BaseMsg();
        baseMsg.putParams("username", msgEntity.getName());
        baseMsg.putParams("body", msgEntity.getMessage());

        String pattern = "yyyy-MM-dd hh:mm:ss";
        Date date = DateUtils.parseStrToDate(msgEntity.getDate(), pattern);
        baseMsg.setDate(date);
        return baseMsg;
    }


    public ChatMsgEntity BaseMsg2ChatMsgEntity(BaseMsg msg) {
        ChatMsgEntity msgEntity = new ChatMsgEntity();
        Map<String, Object> params = new HashMap<String, Object>();
        params = msg.getParams();
        msgEntity.setName((String) params.get("username"));
        msgEntity.setMessage((String) params.get("body"));
        msgEntity.setDate( msg.getDate().toString() );

        return msgEntity;
    }

    public static void main(String str[]) {
        //test
        /*
        BaseMsg baseMsg = new BaseMsg();
        baseMsg.setDate(new Date(2017, 10, 25, 15, 38, 30));
        baseMsg.putParams("username","Mark1212132321");
        baseMsg.putParams("body", "blablablabla");

        ChatMsgEntity msgEntity = new ChatMsgEntity();
        msgEntity.setMessage("blabla");
        msgEntity.setName("Mark");
        msgEntity.setDate("2000-10-25 13:34:43");

        MsgAdapter adapter = new MsgAdapter();
        BaseMsg targetBaseMsg = adapter.ChatMsgEntity2BaseMsg(msgEntity);
        ChatMsgEntity targetMsgEntity = adapter.BaseMsg2ChatMsgEntity(baseMsg);

        System.out.print("targetBaseMsg\n");
        System.out.print (targetBaseMsg.getDate().toString() + "\n");
        System.out.print(targetBaseMsg.getParams() + "\n");

        System.out.print("targetMsgEntity\n");
        System.out.print (targetMsgEntity.getDate() + "\n");
        System.out.print(targetMsgEntity.getMessage() + "\n");
        System.out.print(targetMsgEntity.getName() + "\n");
    }
*/
}

