package com.example.chat_app_pavan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
public class MainActivity extends AppCompatActivity
{
    SharedPreferences preferences;

    SharedPreferences.Editor editor;

    boolean isFirst;
    private Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable()
        {

            @Override

            public void run()
            {

                // TODO Auto-generated method stub

                isFirst=getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirst", true);

                if(isFirst)

                {

                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putBoolean("isFirst", false).commit();

                    Intent i=new Intent(MainActivity.this,Register_Activity.class);

                    startActivity(i);

                    finish();

                }


                else

                {
                    Intent i=new Intent(MainActivity.this,Login_Activity.class);

                    startActivity(i);

                    finish();

                }

            }

        }, 1000);


    }
}




