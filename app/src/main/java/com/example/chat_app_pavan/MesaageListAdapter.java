package com.example.chat_app_pavan;

import android.content.SharedPreferences;
import android.graphics.Path;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.util.List;


public class MesaageListAdapter extends BaseAdapter
{
    final Message_Activity message_activity;
     List<ccom.example.chat_app_pavan.Message> msgList;
    ccom.example.chat_app_pavan.Message messageObject;


String fromemail;
    public MesaageListAdapter(Message_Activity message_activity, List<ccom.example.chat_app_pavan.Message> msgList) {

    this.message_activity=message_activity;
    this.msgList=msgList;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(message_activity.getApplicationContext());
        fromemail= preferences.getString("user_email", null);

    }



    @Override
    public int getCount() {
        return msgList.size();
    }

    @Override
    public Object getItem(int position) {
        return msgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView= LayoutInflater.from(message_activity).
                inflate(R.layout.msg_row,parent,false);
        LinearLayout ll=(LinearLayout)convertView.findViewById(R.id.llmsg);

        TextView msgTv=(TextView)convertView.findViewById(R.id.msg_tv_id);

        messageObject=msgList.get(position);
        msgTv.setText(messageObject.message);
        System.out.println(fromemail);
        if(messageObject.fromEmail.equalsIgnoreCase(fromemail))
        {

            ll.setGravity(Gravity.RIGHT);
        }


        return convertView;
    }


    public void addAll(List<ccom.example.chat_app_pavan.Message> msgList) {

        this.msgList=msgList;
    }

    public void add(ccom.example.chat_app_pavan.Message message) {

        msgList.add(message);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
