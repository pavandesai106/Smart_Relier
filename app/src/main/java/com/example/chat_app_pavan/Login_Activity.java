package com.example.chat_app_pavan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Login_Activity extends AppCompatActivity
{

    static EditText log_et_nm,log_et_pass;
    private static ProgressDialog dialog;
    private static String url_all_data = "http://"+R.string.mip+"/chat_app/register.php";
    private static final String TAG_SUCCESS = "success";
    private static final JSONParser parser=new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        Button btnLogin,btnRegister;
        btnLogin=(Button)findViewById(R.id.login_btn_login);
        btnRegister=(Button)findViewById(R.id.login_btn_register);
        log_et_nm= (EditText) findViewById(R.id.login_et_email);
        log_et_pass= (EditText) findViewById(R.id.login_et_pass);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String email= preferences.getString("user_email", "not_ok");
                 if(!email.equals("not_ok"))
                      {
                          Intent intent=new Intent(Login_Activity.this,CreateChatActivity.class);
                          startActivity(intent);
                      }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new LoginAct().execute();

            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(Login_Activity.this,Register_Activity.class);
                startActivity(intent);
            }
        });
    }


    class LoginAct extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(Login_Activity.this);
            dialog.setMessage("Logining User....");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String email=log_et_nm.getText().toString();
            String pass=log_et_pass.getText().toString();

            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("action","login"));
            list.add(new BasicNameValuePair("email", email));
            list.add(new BasicNameValuePair("password", pass));

            JSONObject object=parser.makeHttpRequest(url_all_data,"POST",list);
            Log.d("Login Success", object.toString());

            try {
                int success = object.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("user_email", email);
                    editor.commit();
                    Intent i = new Intent(getApplicationContext(),CreateChatActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                {


                    Toast.makeText(Login_Activity.this, "ENTER ALL DETAILS",
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
        }

    }
}
