package com.example.xiaoy.androidlabs;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME="LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = (Button) findViewById(R.id.loginButton_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(
                        "LoginFile",Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                EditText loginName= (EditText)findViewById(R.id.editText_email_login);
            }
        });
            //SharedPreferences sharedPref = getSharedPreferences(
                //"LoginFile", Context.MODE_PRIVATE);
        //int numTimeRun = sharedPref.getInt("TIMES_RUN", 0);
        //EditText loginName = (EditText) findViewById(R.id.editTextLogin);
        //loginName.setText( sharedPref.getString("DefaultEmail",""));

        //Log.i(ACTIVITY_NAME, "In onCreate()");
    }
}
