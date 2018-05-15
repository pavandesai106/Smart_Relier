package com.example.chat_app_pavan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import java.util.regex.Pattern;

public class Register_Activity extends AppCompatActivity
{


    private static final Context Second = null;
    private static final Pattern EMAIL_PATTERN = Pattern
            .compile("[a-zA-Z0-9+._%-+]{1,100}" + "@"
                    + "[a-zA-Z0-9][a-zA-Z0-9-]{0,10}" + "(" + "."
                    + "[a-zA-Z0-9][a-zA-Z0-9-]{0,20}"+
                    ")+");
    private static final Pattern USERNAME_PATTERN = Pattern
            .compile("[a-zA-Z0-9]{1,250}");
    private static final Pattern PASSWORD_PATTERN = Pattern
            .compile("[a-zA-Z0-9+_.]{4,16}");
    private static final Pattern NUMBER_PATTERN = Pattern
            .compile("[0-9]{10}");


    Button btnSubmit;


    static public EditText reg_et_nm;
    static  public EditText reg_et_mobile;
    static   public  EditText reg_et_password;
    static  public EditText reg_et_email;
    static   public EditText reg_et_cpassword;
    private static ProgressDialog dialog;
    private static String url_all_data = "http://"+R.string.mip+"/chat_app/register.php";
    private static final String TAG_SUCCESS = "success";
    private static final JSONParser parser=new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);

         btnSubmit=(Button)findViewById(R.id.reg_btn_submit);
        reg_et_nm= (EditText) findViewById(R.id.reg_et_nm);
        reg_et_email= (EditText) findViewById(R.id.reg_et_email);
        reg_et_mobile= (EditText) findViewById(R.id.reg_et_mobile);
        reg_et_password= (EditText) findViewById(R.id.reg_et_password);
      reg_et_cpassword= (EditText) findViewById(R.id.reg_et_cpassword);

        btnSubmit.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                String nm=reg_et_nm.getText().toString();
                String email=reg_et_email.getText().toString();
                String mobile=reg_et_mobile.getText().toString();
                String pass=reg_et_password.getText().toString();
                String cpass=reg_et_cpassword.getText().toString();

                if (nm.equals("") ||email.equals("")
                        || mobile.equals("") || pass.equals("") ||cpass.equals(""))
                {

                    Toast.makeText(Register_Activity.this, "ENTER ALL DETAILS",
                            Toast.LENGTH_LONG).show();
                }

                else if (!Checkname(nm) || !CheckPassword(pass) || !CheckEmail(email)|| !CheckNumber(mobile)
                        || !pass.equals(cpass))
                {

                    if (!Checkname(nm)) {
                        Toast.makeText(Register_Activity.this, "ENTER VALID NAME",
                                Toast.LENGTH_LONG).show();
                    }
                    if (!CheckNumber(mobile))
                    {
                        Toast.makeText(Register_Activity.this, "ENTER 10-DIGIT NUMBER",
                                Toast.LENGTH_LONG).show();
                    }
                    if (!CheckPassword(pass)) {
                        Toast.makeText(Register_Activity.this, "ENTER VALID PASSWORD",
                                Toast.LENGTH_LONG).show();
                    }
                    if (!CheckEmail(email)) {
                        Toast.makeText(Register_Activity.this, "ENTER VALID EMAIL ID",
                                Toast.LENGTH_LONG).show();
                    }
                    if(!reg_et_password.equals(cpass))
                    {
                        Toast.makeText(Register_Activity.this, "Password doesn't match.",
                                Toast.LENGTH_LONG).show();
                    }
                }
                new CreateUser().execute();



            }

            private boolean CheckNumber(String number) {

                return NUMBER_PATTERN.matcher(number).matches();
            }

            private boolean CheckEmail(String email) {

                return EMAIL_PATTERN.matcher(email).matches();
            }

            private boolean CheckPassword(String password) {

                return PASSWORD_PATTERN.matcher(password).matches();
            }

            private boolean Checkname(String name) {

                return USERNAME_PATTERN.matcher(name).matches();
            }
        });
    }

    class CreateUser extends AsyncTask<String,String,String>{

        @Override


        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(Register_Activity.this);
            dialog.setMessage("Creating User....");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected String doInBackground(String... params)
        {

            String nm=reg_et_nm.getText().toString();
            String email=reg_et_email.getText().toString();
            //String email=reg_et_email.getText().toString();
                String mobile=reg_et_mobile.getText().toString();
            String pass=reg_et_password.getText().toString();

            List<NameValuePair> param=new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("action","register"));
            param.add(new BasicNameValuePair("name",nm));
            param.add(new BasicNameValuePair("email",email));
            param.add(new BasicNameValuePair("mobile",mobile));
            param.add(new BasicNameValuePair("password", pass));

            JSONObject object=parser.makeHttpRequest(url_all_data,"POST",param);
            Log.d("Create Success", object.toString());



            try {
                int success = object.getInt(TAG_SUCCESS);

                if (success == 1)  {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), Login_Activity.class);
                    startActivity(i);
                    finish();
                }
                else
                {

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
