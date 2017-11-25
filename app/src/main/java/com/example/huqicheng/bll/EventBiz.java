package com.example.huqicheng.bll;

import com.example.huqicheng.entity.Event;
import com.example.huqicheng.nao.EventNao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 2017/11/11.
 */

public class EventBiz {
    private EventNao eventNao = null;
    public EventBiz(){
        eventNao = new EventNao();
    }
    public List<Long> loadDatesHavingEvents(long user_id,String status){
        return eventNao.getDatesHavingEvents(user_id, status);
    }
    public List<Event> loadEventsOfOneDate(long user_id, long time_stamp){
        return eventNao.getEventsOfOneDate(user_id, time_stamp);
    }
    public String deleteEventBiz(long event_id){
        return eventNao.deleteEvent(event_id);
    }
    public String assignEventBiz(Event event){
        return eventNao.assignEvent(event);
    }
    public String updateEvent(long event_id, long assignTo, String newtitle, String newdescription, long newdeadline){
        return eventNao.updateEvent(event_id, assignTo, newtitle, newdescription, newdeadline);
    }
    public List<Event> loadEventsByGroup(long group_id, long user_id){
        return eventNao.getEventsByGroup(group_id, user_id);
    }
    public String updateEventStatus(ArrayList<Integer> arrayList,String staus){
        return eventNao.markEvents(arrayList,staus);
    }
}

