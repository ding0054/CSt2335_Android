package com.example.xiaoy.androidlabs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //SharedPreferences sharedPref = getSharedPreferences(
                //"LoginFile", Context.MODE_PRIVATE);
        //int numTimeRun = sharedPref.getInt("TIMES_RUN", 0);
        //EditText loginName = (EditText) findViewById(R.id.editTextLogin);
        //loginName.setText( sharedPref.getString("DefaultEmail",""));

        //Log.i(ACTIVITY_NAME, "In onCreate()");
    }
}
