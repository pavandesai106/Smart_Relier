package com.example.chat_app_pavan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CreateChatActivity extends AppCompatActivity {

    List<ChatModel> chatList;
    ProgressDialog dialog;

    JSONParser parser = new JSONParser();
    private static String url_all_data = "http://"+R.string.mip+"/chat_app/register.php";
    private static final String TAG_SUCCESS = "success";
    ListView listView;
    JSONArray contact = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat);
        listView = (ListView) findViewById(R.id.chat_create_list);
        new CreateChatDetail().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChatModel dt = chatList.get(position);
                Toast.makeText(getApplicationContext(), "Name" + dt.nm, Toast.LENGTH_LONG).show();
                Intent in = new Intent(CreateChatActivity.this, Message_Activity.class);
                in.putExtra("email_id", dt.email);
                startActivity(in);
            }
        });


    }

    class CreateChatDetail extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(CreateChatActivity.this);
            dialog.setMessage("Loading Users....");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("action", "viewContact"));
            JSONObject json = parser.makeHttpRequest(url_all_data, "POST", param);
            //Log.d("All Products: ", json.toString());
            try {
                int success = json.getInt(TAG_SUCCESS);
                chatList = new ArrayList<>();
                if (success == 1) {
                    contact = json.getJSONArray("data");
                    for (int i = 0; i < contact.length(); i++) {
                        JSONObject c = contact.getJSONObject(i);
                        String nm = c.getString("name");
                        String email = c.getString("email");
                        chatList.add(new ChatModel(nm, email));
                        System.out.println(chatList.toString());
                    }
                } else {
                    Intent intent = new Intent(CreateChatActivity.this,ChatModel.class);
                    startActivity(intent);
                }

            } catch (Exception e) {
                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            CustomChatAdapter adapter = new CustomChatAdapter(CreateChatActivity.this, chatList);
            listView.setAdapter(adapter);
        }

        class CustomChatAdapter extends BaseAdapter {
            public final AppCompatActivity createChatActivity;
            public final List<ChatModel> chatList;

            public CustomChatAdapter(CreateChatActivity createChatActivity, List<ChatModel> chatList) {
                this.createChatActivity = createChatActivity;
                this.chatList = chatList;
            }

            @Override
            public int getCount()
            {
                return chatList.size();
            }

            @Override
            public Object getItem(int position) {
                return chatList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View view, ViewGroup parent)
            {
                view = LayoutInflater.from(CreateChatActivity.this).inflate(R.layout.custom_row_chat, parent, false);
                TextView tv_nm = (TextView) view.findViewById(R.id.chat_user_name);
                TextView tv_email = (TextView) view.findViewById(R.id.chat_user_email);
                ChatModel model = chatList.get(position);
                tv_nm.setText(model.nm);
                tv_email.setText(model.email);
                return view;
            }
        }
    }
}