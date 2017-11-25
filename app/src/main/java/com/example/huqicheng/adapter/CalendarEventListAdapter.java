package com.example.huqicheng.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.huqicheng.entity.Event;
import com.example.huqicheng.entity.Group;
import com.example.huqicheng.pm.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jiaxinf on 2017-10-17.
 */

public class CalendarEventListAdapter extends BaseAdapter {

    public ArrayList<Event> getEventList() {
        return eventList;
    }

    ArrayList<Event> eventList;
    private LayoutInflater inflater;
    public int checkCount;
    //public List<Long> selectedEvents = new ArrayList<>();

    public CalendarEventListAdapter(Context context, ArrayList<Event> eventList) {

        if(eventList != null)
            this.eventList = eventList;
        else
            this.eventList = new ArrayList<>();

        checkCount = 0;
        inflater = this.inflater = LayoutInflater.from(context);

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
        return getItem(i).getEventID();
    }

    public void remove(Long i){
        long id = (long) i;
        ArrayList<Event> tmp = new ArrayList<>(eventList);
        for (Event e : tmp){
            if (e.getEventID() == id){
                eventList.remove(e);
            }
        }
    }


    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        //Log.d("Debug:", "get view at "+i);
        CalendarEventListAdapter.ViewHolder holder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.calendar_event_list,null);

            holder = new ViewHolder();
            holder.title = (TextView)convertView.findViewById(R.id.title);
            holder.description = (TextView)convertView.findViewById(R.id.description);
            holder.deadline = (TextView)convertView.findViewById(R.id.deadline);

            /*
            holder.checkBox = (CheckBox)convertView.findViewById(R.id.chk_box);
            final CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    long eid =  (long) buttonView.getTag();
                    //getEventById(eid).setSelected(buttonView.isChecked());
                    countCheck(isChecked,eid);
                }
            };
            holder.checkBox.setOnCheckedChangeListener(checkListener);
            */
            convertView.setTag(holder);
            //convertView.setTag(R.id.chk_box,holder.checkBox);

        }else{
            holder = (CalendarEventListAdapter.ViewHolder)convertView.getTag();
        }
        //holder.checkBox.setTag(eventList.get(i).getEventID());

        Event event = getItem(i);
        holder.title.setText(event.getEventTitle());
        holder.description.setText(event.getDescription());
        holder.deadline.setText(new Date(event.getDeadLine()).toString());
        //holder.checkBox.setChecked(eventList.get(i).isSelected());

        return convertView;
    }

    public void add(List<Event> events){
        if(events != null)
            this.eventList.addAll(events);
        notifyDataSetChanged();

    }
    /*
    private void countCheck(boolean isChecked, long eid) {
        Long iid = (Long) eid;
//        checkCount += isChecked ? 1 : -1 ;
        if (isChecked && !selectedEvents.contains(iid)){
            selectedEvents.add(iid);
        }
        else{
            selectedEvents.remove(iid);
        }
        Log.d("checked size", ""+selectedEvents.size());
    }
    */
    private Event getEventById(long id){
        Event returnE = new Event();
        for (Event e : eventList){
            if (e.getEventID() == id){
                returnE = e;
            }
        }
        return  returnE;

    }

    class ViewHolder{
        TextView title;
        TextView description;
        TextView deadline;
        //CheckBox checkBox;

    }

}
