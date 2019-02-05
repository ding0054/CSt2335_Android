package com.example.xiaoy.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
                editor.putString("DefaultEmail",loginName.getText().toString());
                editor.commit();
                Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        Log.e(ACTIVITY_NAME,"IN onCREATE()");
    }



    public void onStart(){
        super.onStart();
        Log.e(ACTIVITY_NAME,"IN onSTART()");
    }


  public void onResume(){
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(
                "LoginFile",Context.MODE_PRIVATE);
        int timeRun = sharedPreferences.getInt("TIME_RUN",0);
        EditText loginName = (EditText) findViewById(R.id.editText_email_login);
        loginName.setText(sharedPreferences.getString("DefaultEmail",""));
        Log.e(ACTIVITY_NAME,"IN onRESUME()");
  }


  public void onPause(){
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences(
                "LoginFile",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        EditText loginName = (EditText) findViewById(R.id.editText_email_login);
        editor.putString("DefaultEmail",loginName.getText().toString());
        editor.commit();
        Log.e(ACTIVITY_NAME,"IN onPAUSE()");
  }

  protected  void  onStop(){
        super.onStop();
        Log.e(ACTIVITY_NAME,"IN onSTOP()");
  }

  protected  void onDestroy(){
        super.onDestroy();
        Log.e(ACTIVITY_NAME,"IN onDESTROY()");
  }
}
