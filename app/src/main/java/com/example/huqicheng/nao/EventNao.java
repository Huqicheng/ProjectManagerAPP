package com.example.huqicheng.nao;

import android.util.Log;

import com.example.huqicheng.config.Config;
import com.example.huqicheng.entity.Event;
import com.example.huqicheng.entity.Group;
import com.example.huqicheng.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by huqicheng on 2017/11/7.
 */

public class EventNao {
    public List<Long> getDatesHavingEvents(long user_id, String status){
        List<Long> res = null;
        try{
            // add your parameters here
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("user_id",user_id+""));
            params.add(new BasicNameValuePair("status",status));
            //modify url according to interface doc
            HttpEntity entity = HttpUtils.execute(Config.SERVER_IP+"/getDatesHavingEvents.do",params,HttpUtils.GET);
            if(entity == null){
                return null;
            }
            //convert stream to json String
            String json = EntityUtils.toString(entity);
            Log.d("debug: ",json);
            // check if failed, you should return null
            if(json.trim().equalsIgnoreCase("failed")) return null;
            // decoding here
            // Type:   simple objects: ObjectName.class
            //         complex objects such as List, Map: TypeToken<ArrayList<ObjectName>>(){}.getType();
            Type type = new TypeToken<ArrayList<Long>>(){}.getType();
            res = new Gson().fromJson(json,type);

        }catch (Exception e){
            e.printStackTrace();

        }
        return res;
    }

    public List<Event> getEventsOfOneDate(long user_id, long time_stamp){
        List<Event> res = null;
        try{
            // add your parameters here
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("user_id",user_id+""));
            params.add(new BasicNameValuePair("timestamp",time_stamp+""));
            Log.d("dates http",""+ new Date(time_stamp).toString());

            //modify url according to interface doc
            HttpEntity entity = HttpUtils.execute(Config.SERVER_IP+"/getEventsByDate.do",params,HttpUtils.GET);

            if(entity == null){
                return null;
            }

            //convert stream to json String
            String json = EntityUtils.toString(entity);

            Log.d("debug: ",json);

            // check if failed, you should return null
            if(json.trim().equalsIgnoreCase("failed")) return null;

            // decoding here
            // Type:   simple objects: ObjectName.class
            //         complex objects such as List, Map: TypeToken<ArrayList<ObjectName>>(){}.getType();
            Type type = new TypeToken<ArrayList<Event>>(){}.getType();
            res = new Gson().fromJson(json,type);
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }
    public String deleteEvent(long event_id) {
        String res = "";
        try {
            // add your parameters here
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("event_id", event_id + ""));

            //modify url according to interface doc
            HttpEntity entity = HttpUtils.execute(Config.SERVER_IP + "/deleteEvent.do", params, HttpUtils.GET);

            if (entity == null) {
                return null;
            }

            //convert stream to json String
            String json = EntityUtils.toString(entity);

            Log.d("debug: ", json);

            // check if failed, you should return null
            if (json.trim().equalsIgnoreCase("failed")) return null;

            // decoding here
            // Type:   simple objects: ObjectName.class
            //         complex objects such as List, Map: TypeToken<ArrayList<ObjectName>>(){}.getType()
            res = json;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return res;
    }
    public String assignEvent(Event event){
        List<Event> res = null;
        try{
            // decoding here
            // Type:   simple objects: ObjectName.class
            //         complex objects such as List, Map: TypeToken<ArrayList<ObjectName>>(){}.getType();
            Type type = Event.class;
            String json = new Gson().toJson(event,type);
            Log.d("In EventNao: ",json);

            // add your parameters here
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("jsonEvent",json));

            //modify url according to interface doc
            HttpEntity entity = HttpUtils.execute(Config.SERVER_IP+"/assignEvent.do",params,HttpUtils.POST);

            if(entity == null){
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        String str = "success";
        return str;
    }
    public String updateEvent(long event_id, long assignTo, String newtitle, String newdescription, long newdeadline){
        List<Event> res = null;
        try{
            // decoding here
            // Type:   simple objects: ObjectName.class
            //         complex objects such as List, Map: TypeToken<ArrayList<ObjectName>>(){}.getType();


            // add your parameters here
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("deadline",newdeadline+""));
            params.add(new BasicNameValuePair("title", newtitle));
            params.add(new BasicNameValuePair("description",newdescription));
            params.add(new BasicNameValuePair("assignTo",assignTo+""));
            params.add(new BasicNameValuePair("event_id",event_id+""));

            //modify url according to interface doc
            HttpEntity entity = HttpUtils.execute(Config.SERVER_IP+"/updateEvent.do",params,HttpUtils.POST);

            if(entity == null){
                return null;
            }

            //convert stream to json String
            String json = EntityUtils.toString(entity);

        }catch (Exception e){
            e.printStackTrace();
        }
        String str = "success";
        return str;
    }

    public List<Event> getEventsByGroup(long group_id, long user_id, String status){
        List<Event> res = null;
        try{
            // add your parameters here
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("group_id",group_id+""));
            params.add(new BasicNameValuePair("user_id",user_id+""));
            params.add(new BasicNameValuePair("status",status));

            //modify url according to interface doc
            HttpEntity entity = HttpUtils.execute(Config.SERVER_IP+"/getEventsByGroup.do",params,HttpUtils.GET);
            if(entity == null){
                return null;
            }
            //convert stream to json String
            String json = EntityUtils.toString(entity);
            Log.d("getEventsByGroup: ",json);
            // check if failed, you should return null
            if(json.trim().equalsIgnoreCase("failed")) return null;
            // decoding here
            Type type = new TypeToken<ArrayList<Event>>(){}.getType();
            res = new Gson().fromJson(json,type);

        }catch (Exception e){
            e.printStackTrace();

        }

        return res;
    }

}
