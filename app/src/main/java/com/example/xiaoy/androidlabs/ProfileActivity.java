package com.example.xiaoy.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.graphics.Bitmap;
import android.widget.EditText;
import android.provider.MediaStore;
import android.widget.ImageButton;


public class ProfileActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "ProfileActivity";
    static final int REQUEST_IMAGE_CAPTURE = 1;

     ImageButton iButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        iButton = (ImageButton) findViewById(R.id.image_clickButton_profile);
        iButton.setOnClickListener(new View.OnClickListener() {
            final int REQUEST_IMAGE_CAPTURE = 1;
            @Override
            public void onClick(View view) {
            }
        private void dispatchTakePictureIntent(){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null){
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
            }
        }
        });

        Log.e(ACTIVITY_NAME,"IN onCREATE()");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap)extras.get("data");
            iButton.setImageBitmap(bitmap);
        }
        Log.e(ACTIVITY_NAME,"IN onActivityRESULT");
    }
}
