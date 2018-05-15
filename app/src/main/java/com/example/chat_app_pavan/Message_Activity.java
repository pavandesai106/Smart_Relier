package com.example.chat_app_pavan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class  Message_Activity extends AppCompatActivity {

    static EditText msg_et_msg;
    private ProgressDialog dialog;
    List<Message> msgList;
    String msgtosend;
    JSONParser parser=new JSONParser();
    private static String url_all_data = "http://"+R.string.mip+"/chat_app/register.php";
    private static final String TAG_SUCCESS = "success";
    ListView listView;
    String email,toemail,fromemail;
    JSONArray msg = null;
    MesaageListAdapter msgListAdapter;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_);
        Intent in=getIntent();
        toemail=in.getStringExtra("email_id");

        listView=(ListView)findViewById(R.id.msg_listview);
        msg_et_msg=(EditText)findViewById(R.id.msg_et_msg);
        btnSend=(Button)findViewById(R.id.msg_btn_send);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        fromemail= preferences.getString("user_email", null);
        msgList=new ArrayList<>();
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        new ViewMessage().execute();
        msg_et_msg.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                btnSend.setClickable(true);
                return false;
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 msgtosend=msg_et_msg.getText().toString();
                if(!msgtosend.equalsIgnoreCase("")){

                   Message message=new Message(msgtosend,toemail,fromemail,true);

                    new Message_Send().execute();
                    msgListAdapter.add(message);
                    msg_et_msg.setText("");
                    btnSend.setClickable(false);
                    msgListAdapter.notifyDataSetChanged();
                }
                else
                {btnSend.setClickable(false);}

            }
        });
        if(!msg_et_msg.getText().toString().equalsIgnoreCase("")){}
        else
        {btnSend.setClickable(false);}




    }
    class ViewMessage extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("action","viewMessage"));
            param.add(new BasicNameValuePair("toemail",toemail));
            param.add(new BasicNameValuePair("fromemail",fromemail));

            JSONObject json = parser.makeHttpRequest(url_all_data, "POST", param);
            try {
                int success=json.getInt(TAG_SUCCESS);

                if(success==1){
                    msg=json.getJSONArray("data");
                    for (int i=0;i<msg.length();i++)
                    {
                        JSONObject c=msg.getJSONObject(i);
                        String msg=c.getString("message");
                        String toemail=c.getString("toemail");
                        String fromemail=c.getString("fromemail");
                        boolean ismine=Boolean.parseBoolean(c.getString("ismine"));
                        msgList.add(new Message(msg,toemail,fromemail,ismine));
                        //System.out.println(msgList.toString());
                    }
                    }
                else
                {
                    Intent intent=new Intent(Message_Activity.this,MainActivity.class);
                    startActivity(intent);
                }

            }catch (Exception e){
                e.printStackTrace();

            }

            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            msgListAdapter=new MesaageListAdapter(Message_Activity.this,msgList);
            listView.setAdapter(msgListAdapter);


        }
    }
    class Message_Send extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {



            List<NameValuePair> par = new ArrayList<NameValuePair>();
            par.add(new BasicNameValuePair("action","msgSend"));
            par.add(new BasicNameValuePair("toemail",toemail));
            par.add(new BasicNameValuePair("fromemail",fromemail));
            par.add(new BasicNameValuePair("message",msgtosend));
            par.add(new BasicNameValuePair("ismine", String.valueOf(true)));
            JSONObject object=parser.makeHttpRequest(url_all_data,"POST",par);
            try {
                int success = object.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product


                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
