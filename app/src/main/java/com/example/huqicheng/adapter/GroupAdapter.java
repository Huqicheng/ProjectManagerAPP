package com.example.huqicheng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huqicheng.entity.Group;
import com.example.huqicheng.pm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huqicheng on 2017/10/14.
 */

public class GroupAdapter extends BaseAdapter{
    private List<Group> groupList = null;
    private LayoutInflater inflater;

    public GroupAdapter(Context context,List<Group> groupList) {
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
        ViewHolder holder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_groups,null);
            holder = new ViewHolder();
            holder.ivGroup = (ImageView)convertView.findViewById(R.id.ivGroup);
            holder.tvGroupName = (TextView)convertView.findViewById(R.id.tvGroupName);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        Group group = getItem(i);
        holder.tvGroupName.setText(group.getGroupName());


        return convertView;
    }

    public void add(List<Group> groups){
        if(groups != null)
            this.groupList.addAll(groups);
        notifyDataSetChanged();

    }
    class ViewHolder{
        ImageView ivGroup;
        TextView tvGroupName;

    }


}
