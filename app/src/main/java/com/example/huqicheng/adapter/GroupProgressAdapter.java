package com.example.huqicheng.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.huqicheng.entity.Event;
import com.example.huqicheng.entity.Group;
import com.example.huqicheng.pm.R;
import com.example.huqicheng.utils.AsyncImageLoader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jiaxinf on 2017-11-19.
 */

public class GroupProgressAdapter  extends BaseAdapter {

    private List<Group> groupList = null;
    private LayoutInflater inflater;

    public GroupProgressAdapter(Context context, List<Group> groupList) {
        if(groupList != null)
            this.groupList = groupList;
        else
            this.groupList = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return groupList.size();
    }

    @Override
    public Group getItem(int i) {
        return groupList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).getGroupId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        GroupProgressAdapter.ViewHolder holder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.group_progress_list_row,null);
            holder = new GroupProgressAdapter.ViewHolder();
            holder.groupName = (TextView) convertView.findViewById(R.id.groupName);
            holder.groupProgressText = (TextView)convertView.findViewById(R.id.group_progress_text);
            holder.groupProgressBar = (ProgressBar) convertView.findViewById(R.id.group_bar) ;
            holder.getGroupDeadline = (TextView) convertView.findViewById(R.id.group_deadline) ;
            convertView.setTag(holder);
            convertView.setTag(R.id.group_bar,holder.groupProgressBar);
            convertView.setTag(R.id.group_progress_text,holder.groupProgressText);
        }else{
            holder = (GroupProgressAdapter.ViewHolder)convertView.getTag();
        }

        Group group = getItem(i);
        if (group.getGroupId() == 1){
            holder.getGroupDeadline.setText("");
        }
        else {
            holder.getGroupDeadline.setText(""+new Date(group.getDeadline()));

        }
        holder.groupName.setText(group.getGroupName());
        holder.groupProgressText.setText("");
        holder.groupProgressBar.setMax(100);
        holder.groupProgressBar.setProgress(50);
        holder.groupProgressBar.setTag(getItemId(i));
        holder.groupProgressText.setTag(getItemId(i));

        return convertView;
    }


    public void add(List<Group> groups){
        if(groups != null)
            this.groupList.addAll(groups);
        notifyDataSetChanged();
    }



    class ViewHolder{
        TextView groupName;
        TextView groupProgressText;
        TextView getGroupDeadline;
        ProgressBar groupProgressBar;

    }




}
