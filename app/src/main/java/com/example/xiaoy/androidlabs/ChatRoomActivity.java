package com.example.xiaoy.androidlabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity implements View.OnClickListener{

    protected static final String ACTIVITY_NAME = "activity_chat_room";
    private EditText chatEditText;
    private ArrayList<Message> chatMessage = new ArrayList<>();
    private MyListAdapter adapter;
    Cursor results;
    protected static ChatDatabase chatDatabase;
    protected static SQLiteDatabase db;
    String[] arguments = new String[]
            { chatDatabase.KEY_ID, chatDatabase.KEY_MESSAGE,chatDatabase.KEY_TYPE};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        chatDatabase = new ChatDatabase(this);
        db = chatDatabase.getWritableDatabase();

        String [] columns = {chatDatabase.KEY_ID, chatDatabase.KEY_MESSAGE,chatDatabase.KEY_TYPE};
        results = db.query(false, chatDatabase.TABLE_NAME,
                columns, null, null, null, null, null, null);

        //  printCursor(results);

        //find the column indices:
        int messageColumnIndex = results.getColumnIndex(chatDatabase.KEY_MESSAGE);
        int idColIndex = results.getColumnIndex(chatDatabase.KEY_ID);
        int idColType = results.getColumnIndex(chatDatabase.KEY_TYPE);

        while(results.moveToNext())
        {
            String msg = results.getString(messageColumnIndex);

            long id = results.getLong(idColIndex);

            int type = results.getInt(idColType);

            //add the new Contact to the array list:

            chatMessage.add(new Message(msg,type));

        }

        printCursor(results);
        //getting the chatText field from the screen
        ListView listView = findViewById(R.id.listConversation);
        chatEditText = findViewById(R.id.editTextChatMsg);

        chatEditText = findViewById(R.id.editTextChatMsg);
        ListView listview = findViewById(R.id.listConversation);
        adapter = new MyListAdapter(this, R.id.listConversation);
        listview.setAdapter(adapter);

        Button buttonSend = findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(this);

        Button buttonReceived = findViewById(R.id.buttonReceive);
        buttonReceived.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String input = chatEditText.getText().toString();

        if (input.length() == 0)
            return;

        switch (v.getId()) {
            case R.id.buttonSend:
                adapter.add(new Message(input, Message.SENT));
                adapter.notifyDataSetChanged();
                ContentValues cv = new ContentValues();
                cv.put(chatDatabase.KEY_MESSAGE,chatEditText.getText().toString());
                cv.put(chatDatabase.KEY_TYPE,Message.SENT);

                db.insert(chatDatabase.TABLE_NAME,"NULLCOLUMN",cv);
                chatEditText.setText("");
                results = db.query(false, chatDatabase.TABLE_NAME,
                        arguments, null, null, null, null, null, null);
                chatEditText.setText("");
                break;
                case R.id.buttonReceive:
                adapter.add(new Message(input, Message.RECEIVED));
                break;
                default:
                break;
        }
        adapter.notifyDataSetChanged();
        chatEditText.setText("");
    }


    /**
     * MessageType Enum Type
     */
    //private enum MessageType { SENT, RECEIVED }

    /**
     * Message representing class
     */
    private class Message {
        private String message;
        private int type;
        public static final int SENT = 0;
        public static final int RECEIVED = 1;

        Message(String message, int type) {
            this.message = message;
            this.type = type;
        }

        String getMessage() {
            return message;
        }

        int getType() {
            return type;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "message='" + message + '\'' +
                    ", type=" + type +
                    '}';
        }
    }

    /**
     * Customized List Adapter, with built-in container for Message
     */
    private class MyListAdapter extends ArrayAdapter<Message> {
        private LayoutInflater inflater;


        MyListAdapter(Context context, int resource) {
            super(context, resource);
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Message message = getItem(position);

            View view = null;
            TextView textView = null;

            if (message.getType() == Message.SENT) {
                view = inflater.inflate(R.layout.chat_message_sent, null);
                textView = view.findViewById(R.id.textViewSent);

            } else if (message.getType() == Message.RECEIVED) {
                view = inflater.inflate(R.layout.chat_message_received, null);
                textView = view.findViewById(R.id.textViewReceived);
            }
            textView.setText(message.getMessage());

            return view;
        }
    }

    private void printCursor(Cursor c) {

        Log.i(ACTIVITY_NAME, "version: " + db.getVersion()+
                ", number of cursor: " + c.getColumnCount() +
                ", name of cursor: " + c.getColumnNames() +
                ",number of results: " + c.getCount());

        c.moveToFirst();

        while(!c.isAfterLast()){

            Log.i(ACTIVITY_NAME,"MESSAGE: "+ c.getString(c.getColumnIndex(chatDatabase.KEY_MESSAGE)));
            results.moveToNext();

        }
}
}