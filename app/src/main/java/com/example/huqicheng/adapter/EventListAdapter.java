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
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jiaxinf on 2017-10-17.
 */

public class EventListAdapter extends BaseAdapter {

    ArrayList<Event> eventList;
    private LayoutInflater inflater;
    public int checkCount;
    LinkedList selectedEvents;
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
        return getItem(i).getEventId();
    }
    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        Log.d("Debug:", "get view at "+i);
        EventListAdapter.ViewHolder holder = null;
        final CheckBox cb;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.event_list_row,null);


            holder = new ViewHolder();
            holder.title = (TextView)convertView.findViewById(R.id.title);
            holder.description = (TextView)convertView.findViewById(R.id.description);
            holder.checkBox = (CheckBox)convertView.findViewById(R.id.chk_box);


            cb = (CheckBox) convertView.findViewById(R.id.chk_box);
            selectedEvents = new LinkedList();
            final ViewHolder finalHolder = holder;

            final CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int pos = (Integer) buttonView.getTag();
                    eventList.get(pos).setSelected(buttonView.isChecked());
                    checkCount += isChecked ? 1 : -1 ;

                    //Log.d("c",checkCount+" of " + eventList.size() + " completed ");
//                if(isChecked){
//                    selectedEvents.add(finalHolder.title.getText().toString());
//                    Log.d("add ",finalHolder.title.getText().toString()+"");
//                }
//                else {selectedEvents.remove(finalHolder.title.getText().toString());
//                 Log.d("remove",finalHolder.title.getText().toString()+"");
//                }
                    //Log.d("size",selectedEvents.size()+"");

                }
            };
            holder.checkBox.setOnCheckedChangeListener(checkListener);
            convertView.setTag(holder);
            convertView.setTag(R.id.chk_box,holder.checkBox);

        }else{
            holder = (EventListAdapter.ViewHolder)convertView.getTag();
        }
        holder.checkBox.setTag(i);

        Event event = getItem(i);
        holder.title.setText(event.getEventTitle());
        holder.description.setText(event.getEventDescription());
        holder.checkBox.setChecked(eventList.get(i).isSelected());




        return convertView;
    }
    private int countCheck(boolean isChecked) {
        checkCount += isChecked ? 1 : -1 ;
        return checkCount;
    }
    public void add(List<Event> events){
        if(events != null)
            this.eventList.addAll(events);
        notifyDataSetChanged();

    }

    class ViewHolder{
        TextView title;
        TextView description;
        CheckBox checkBox;

    }
}
