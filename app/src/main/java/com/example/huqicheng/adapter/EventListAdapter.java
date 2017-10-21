package com.example.huqicheng.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huqicheng.entity.Event;
import com.example.huqicheng.entity.Group;
import com.example.huqicheng.pm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiaxinf on 2017-10-17.
 */

public class EventListAdapter extends BaseAdapter {

    ArrayList<Event> eventList;
    private LayoutInflater inflater;
    public EventListAdapter(Context context, ArrayList<Event> eventList) {
        if(eventList != null)
            this.eventList = eventList;
        else
            this.eventList = new ArrayList<>();




    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Event getItem(int i) {
        return eventList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).getEventId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        Log.d("Debug:", "get view at "+i);
        EventListAdapter.ViewHolder holder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.event_list_row,null);

            holder = new ViewHolder();
            holder.title = (TextView)convertView.findViewById(R.id.title);
            holder.description = (TextView)convertView.findViewById(R.id.description);
            convertView.setTag(holder);
        }else{
            holder = (EventListAdapter.ViewHolder)convertView.getTag();
        }

        Event event = getItem(i);
        holder.title.setText(event.getEventTitle());
        holder.description.setText(event.geteventDescription());


        return convertView;
    }

    public void add(List<Event> events){
        if(events != null)
            this.eventList.addAll(events);
        notifyDataSetChanged();

    }

    class ViewHolder{
        TextView title;
        TextView description;

    }
}
