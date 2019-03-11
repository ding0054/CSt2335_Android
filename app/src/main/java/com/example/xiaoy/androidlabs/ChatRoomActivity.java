package com.example.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ChatWindow extends AppCompatActivity {
    protected EditText chatEditText;
    private ArrayList<Message> chatMessage = new ArrayList<>();
    protected static final String ACTIVITY_NAME = "ChatWindow";
    protected static final int SEND_MESSAGE = 1;
    protected static final int RECEIVE_MESSAGE = 2;

    ChatAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);


        ListView listView = (ListView) findViewById(R.id.listView);
        chatEditText = (EditText) findViewById(R.id.editChat);

        messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);

        Button btSend = (Button) findViewById(R.id.buttonSend);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chatMessage.add(new Message(chatEditText.getText().toString(),SEND_MESSAGE));
                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()

                ContentValues newValues = new ContentValues();

                chatEditText.setText("");

            }
        });

        Button btReceive = (Button) findViewById(R.id.buttonReceive);
        btReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chatMessage.add(new Message(chatEditText.getText().toString(), RECEIVE_MESSAGE));
                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()

                ContentValues newValues = new ContentValues();

                chatEditText.setText("");

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Context context = view.getContext();

                TextView textViewItem = ((TextView) view.findViewById(R.id.message_text));

                // get the clicked item name
                String listItemText = textViewItem.getText().toString();

                // just toast it
                Toast.makeText(context, "Item: " + listItemText , Toast.LENGTH_SHORT).show();

                Log.d("chatListView", "onItemClick: " + i + " " + l);
            }
        });


    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        Log.i(ACTIVITY_NAME,"In onDestroy()");
    }

    //inner class ChatAdapter
    public class ChatAdapter extends ArrayAdapter<Message> {

        public ChatAdapter(Context ctx) {

            super(ctx, 0);
        }

        public int getCount() {

            return chatMessage.size();
        }

        public long getItemId(int position) {
            return position;

        }

        public Message getItem(int position) {

            return chatMessage.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;

            if (getItem(position).getMessage_type() == RECEIVE_MESSAGE) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }

            TextView message = (TextView) result.findViewById(R.id.message_text);

            message.setText(getItem(position).getMessage());

            return result;
        }

    }

    public void onActivityResult(int requestCode,int resultCode, Intent data) {
        if ((requestCode == 5)&&(resultCode == Activity.RESULT_OK)){
            Log.i(ACTIVITY_NAME, "Returned to ChatWindow.onActivityResult");
        }


    }


    class Message {
        private String message;
        private int message_type;

        public Message(String message){
            setMessage(message);
        }

        public Message(String message, int message_type){
            setMessage(message);
            setMessage_type(message_type);
        }


        public String getMessage(){
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getMessage_type() {
            return message_type;
        }

        public void setMessage_type(int message_type) {
            this.message_type = message_type;
        }
    }

}