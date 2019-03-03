package com.example.a26740.todo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> implements View.OnClickListener,View.OnLongClickListener{

    private Context mContext;

    private List<Event> mEventList;

    private RecyclerViewOnItemClickListener onItemClickListener;

    public boolean MUL_tag = false;

    private HashMap<Integer,Boolean> ischecked = new HashMap<Integer,Boolean>();


    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView eventName;
        CheckBox checkBox;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view.findViewById(R.id.cardview);
            eventName = (TextView) view.findViewById(R.id.content);
            checkBox = (CheckBox)view.findViewById(R.id.checkbox);
        }
    }

    public EventAdapter(List<Event> eventList){
        mEventList = eventList;
        initMaps();
    }
    public void initMaps(){
        for (int i=0;i<mEventList.size();i++){
            ischecked.put(i,false);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder( final ViewGroup parent,int viewType){
        if (mContext==null){
            mContext = parent.getContext();
        }
        final View view = LayoutInflater.from(mContext).inflate(R.layout.event_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(this);
        holder.cardView.setOnLongClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        Event event = mEventList.get(position);
        String values = event.getName();
        holder.eventName.setText(values);
       if (MUL_tag){
           holder.checkBox.setVisibility(View.VISIBLE);
       }else {
           holder.checkBox.setVisibility(View.INVISIBLE);
       }
       holder.cardView.setTag(position);
       holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               ischecked.put(position,isChecked);
           }
       });
       if (ischecked.get(position)==null)
           ischecked.put(position,false);
       holder.checkBox.setChecked(ischecked.get(position));
    }
    @Override
    public int getItemCount()
    {
        return mEventList.size();
    }
    //单击传入位置
    @Override
    public void onClick(View v){
        if (onItemClickListener != null){
            onItemClickListener.onItemClickListener(v,(Integer)v.getTag());
        }
    }
    //长按传入position
    public boolean onLongClick(View v){
        initMaps();
        return onItemClickListener != null && onItemClickListener.onLongClickListener(v,(Integer)v.getTag());
    }
    //创建监听事件

    public void setRecycleViewOnItemClickListener(RecyclerViewOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    //设置CheckBox显示状态
    public void setCheckBox(){
        MUL_tag = !MUL_tag;
    }
    public void  setSelection(int position){
        if (ischecked.get(position))
            ischecked.put(position,false);
        else
            ischecked.put(position,true);
        notifyItemChanged(position);
    }

    //MainActivity中获取Map
    public HashMap<Integer,Boolean> getMap(){
        return  ischecked;
    }

    //接口，方便重写
    public interface RecyclerViewOnItemClickListener{
        void onItemClickListener(View view,int position);
        boolean onLongClickListener(View view,int position);
    }
    public void addData(int position, ViewHolder holder) {
        TextView eventName;
        notifyItemInserted(position);
    }
}
