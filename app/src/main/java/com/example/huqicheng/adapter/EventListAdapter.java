package com.example.huqicheng.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huqicheng.entity.Event;
import com.example.huqicheng.pm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiaxinf on 2017-10-17.
 */

public class EventListAdapter extends BaseAdapter {

    public ArrayList<Event> getEventList() {
        return eventList;
    }

    ArrayList<Event> eventList;
    private LayoutInflater inflater;
    public int checkCount;
    public ArrayList<Integer> selectedEvents = new ArrayList<>();

    public EventListAdapter(Context context, ArrayList<Event> eventList) {

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
        EventListAdapter.ViewHolder holder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.single_group_event_list_row,null);

            holder = new ViewHolder();
            holder.title = (TextView)convertView.findViewById(R.id.title);
            holder.description = (TextView)convertView.findViewById(R.id.description);
            holder.checkBox = (CheckBox)convertView.findViewById(R.id.chk_box);
            //holder.checkView = (ImageView)convertView.findViewById(R.id.check_view);
            //holder.creator = (TextView)convertView.findViewById(R.id.creator);


            final CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    long eid =  (long) buttonView.getTag();
                    getEventById(eid).setSelected(buttonView.isChecked());
                    countCheck(isChecked,eid);
                }
            };
            holder.checkBox.setOnCheckedChangeListener(checkListener);
            convertView.setTag(holder);
            convertView.setTag(R.id.chk_box,holder.checkBox);
            //convertView.setTag(R.id.check_view,holder.checkView);

        }else{
            holder = (EventListAdapter.ViewHolder)convertView.getTag();
        }
        holder.checkBox.setTag(eventList.get(i).getEventID());
        //holder.checkView.setTag(eventList.get(i).getEventID());

        Event event = getItem(i);
        //holder.title.setText(event.getEventTitle());
        holder.description.setText(event.getDescription());
        holder.checkBox.setChecked(eventList.get(i).isSelected());

        if(event.getEventStatus().equals("finished")){
            holder.title.setText(event.getEventTitle()+"  --- finished ");

            //holder.checkView.setVisibility(View.VISIBLE);
        }
        else if(event.getEventStatus().equals("dropped")){
            holder.title.setText(event.getEventTitle()+"  --- dropped ");
        }
        else{
            holder.title.setText(event.getEventTitle());

        }


            //holder.checkView.setVisibility(View.INVISIBLE);


        return convertView;
    }

    public void add(ArrayList<Event> events){
        //Log.d("size",""+events.size());
        if(events != null){
            this.eventList.addAll(events);
        }
        notifyDataSetChanged();

    }
    private void countCheck(boolean isChecked, long eid) {
        Long iid = (Long) eid;
        Integer id = Integer.valueOf(iid.intValue());
//        checkCount += isChecked ? 1 : -1 ;
        if (isChecked && !selectedEvents.contains(id)){
            selectedEvents.add(id);
        }
        else{
            selectedEvents.remove(id);
        }
        //Log.d("checked size", ""+selectedEvents.size());
    }
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
        //TextView creator;
        //ImageView checkView;
        CheckBox checkBox;

    }

}
