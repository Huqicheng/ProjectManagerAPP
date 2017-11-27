package com.example.huqicheng.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huqicheng.entity.Event;
import com.example.huqicheng.entity.User;
import com.example.huqicheng.pm.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jiaxinf on 2017-10-17.
 */

public class EventListAdapter extends BaseAdapter implements Filterable{

    public ArrayList<Event> getEventList() {
        return eventList;
    }

    ArrayList<Event> eventList = new ArrayList<>();
    User user;
    CustomFilter filter;
    ArrayList<Event> filterList = new ArrayList<>();
    private LayoutInflater inflater;
    public int checkCount;
    public ArrayList<Integer> selectedEvents = new ArrayList<>();

    public EventListAdapter(Context context, ArrayList<Event> eventList, User user) {

        if(eventList != null){
            this.eventList = eventList;
        }

        else{
            this.eventList = new ArrayList<>();

        }


        if (user != null)
            this.user = user;
        else
            this.user = new User();
        this.filterList = this.eventList;

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

            holder.deadline = (TextView) convertView.findViewById(R.id.deadline);
            holder.notes = (ImageView) convertView.findViewById(R.id.notes);

            holder.checkBox = (CheckBox)convertView.findViewById(R.id.chk_box);


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

        //fail to make checkbox invisible
        /*if(!eventList.get(i).getEventStatus().equals("started")){
            holder.checkBox.setVisibility(View.INVISIBLE);
        }*/


        Event event = getItem(i);
        //holder.title.setText(event.getEventTitle());
        holder.description.setText(event.getDescription());
        holder.checkBox.setChecked(eventList.get(i).isSelected());



        switch (event.getEventStatus()){
            case "started":
                holder.title.setText(event.getEventTitle());
                holder.title.setTextColor(Color.parseColor("#FF8A65"));
                if(user.getUserId() == event.getAssignedTo()){
                    holder.notes.setBackgroundResource(R.drawable.you);
                }
                else {
                    holder.notes.setBackgroundResource(0);
                }
                String[] deadline_str = new Date(event.getDeadLine()).toString().split("\\s");
                String res = deadline_str[0] + " " + deadline_str[1] + " " + deadline_str[2] + " "+ deadline_str[5] ;
                holder.deadline.setText(res);
                break;
            case "finished":
                holder.title.setText(event.getEventTitle());
                //holder.notes.setText("Done");
                holder.title.setTextColor(Color.parseColor("#4CAF50"));
                holder.notes.setBackgroundResource(R.drawable.done);
                holder.deadline.setText("");
                break;
            case "dropped":
                holder.title.setText(event.getEventTitle());
                //holder.notes.setText("DROPPED");
                holder.title.setTextColor(Color.parseColor("#BDBDBD"));
                holder.description.setTextColor(Color.parseColor("#BDBDBD"));
                holder.notes.setBackgroundResource(R.drawable.dropped);
                holder.deadline.setText("");
                break;

        }

        return convertView;
    }

    public void add(ArrayList<Event> events,User user){
        //Log.d("size",""+events.size());
        if(events != null){
            this.eventList.addAll(events);
        }
        this.user = user;
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

    public Filter getFilter() {
        if (filter == null)
            filter = new CustomFilter();


        return filter;
    }

    class ViewHolder{
        TextView title;
        TextView description;
        ImageView notes;
        TextView deadline;
        //ImageView checkView;
        CheckBox checkBox;

    }


    private class CustomFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            if(constraint != null && constraint.length()>0)
            {
                //CONSTARINT TO UPPER
                String cons =constraint.toString().toUpperCase();
                ArrayList<Event> filters=new ArrayList<Event>();
                //get specific items
                Log.d(filterList.size()+"","fillist");

                for(int i=0;i<filterList.size();i++)
                {
                    if(filterList.get(i).getEventTitle().toUpperCase().contains(cons))
                    {
                        Event e = filterList.get(i);
                        filters.add(e);
                        Log.d("filter","fail");
                    }


                }
                results.count=filters.size();
                results.values=filters;
            }else
            {
                results.count=filterList.size();
                results.values=filterList;
        }
        return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            eventList = (ArrayList<Event>)results.values;
            notifyDataSetChanged();

        }
    }
}
