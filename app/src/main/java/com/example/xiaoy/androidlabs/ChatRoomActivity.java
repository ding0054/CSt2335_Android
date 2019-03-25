package com.example.xiaoy.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {
    protected EditText chatEditText;
    private ArrayList<Message> chatMessage = new ArrayList<>();
    protected static final String ACTIVITY_NAME = "ChatRoomActivity";
    protected static final int SEND_MESSAGE = 1;
    protected static final int RECEIVE_MESSAGE = 2;

    // SQLite database
    protected static ChatDatabase chatData;
    protected SQLiteDatabase db;
    Cursor results;
    ChatAdapter messageAdapter;

    //// lab 8
    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_TYPE = "TYPE";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    public static final int EMPTY_ACTIVITY = 345;
    ////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        chatData = new ChatDatabase(this);
        db = chatData.getWritableDatabase();

        results = db.query(false, chatData.TABLE_NAME,
                new String[] {chatData.KEY_ID, chatData.KEY_MESSAGE, chatData.KEY_TYPE},
                chatData.KEY_ID +" not null", null, null, null, null, null, null);
        int rows = results.getCount();
        results.moveToFirst();
        while(! results.isAfterLast()){
            // get message from database and add message to chatMessage
            chatMessage.add(new Message(results.getString(results.getColumnIndex(ChatDatabase.KEY_MESSAGE)),
                    results.getInt(results.getColumnIndex(ChatDatabase.KEY_TYPE)),
                    results.getLong(results.getColumnIndex(ChatDatabase.KEY_ID))
            ));
            //Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + results.getString( results.getColumnIndex( ChatDatabase.KEY_MESSAGE)));
            results.moveToNext();
        }

        ListView listView = (ListView) findViewById(R.id.listConversation);
        chatEditText = (EditText) findViewById(R.id.editTextChatMsg);

        messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);

        ////lab8
        boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded

        ////lab8
        Button btSend = (Button) findViewById(R.id.buttonSend);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chatMessage.add(new Message(chatEditText.getText().toString(),SEND_MESSAGE));
                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()

                ContentValues newValues = new ContentValues();
                newValues.put(chatData.KEY_MESSAGE, chatEditText.getText().toString());
                newValues.put(chatData.KEY_TYPE, ChatRoomActivity.SEND_MESSAGE);
                db.insert(chatData.TABLE_NAME, "", newValues );
                chatEditText.setText("");
                results = db.query(false, chatData.TABLE_NAME,
                        new String[] { chatData.KEY_ID, chatData.KEY_MESSAGE, chatData.KEY_TYPE }, null, null, null, null, null, null);

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
                newValues.put(chatData.KEY_MESSAGE, chatEditText.getText().toString());
                newValues.put(chatData.KEY_TYPE, ChatRoomActivity.RECEIVE_MESSAGE);
                db.insert(chatData.TABLE_NAME, "", newValues );
                chatEditText.setText("");
                results = db.query(false, chatData.TABLE_NAME,
                        new String[] { chatData.KEY_ID, chatData.KEY_MESSAGE, chatData.KEY_TYPE }, null, null, null, null, null, null);

                chatEditText.setText("");

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = view.getContext();

                TextView textViewItem = ((TextView) view.findViewById(R.id.message_text));

                // get the clicked item name
                String listItemText = textViewItem.getText().toString();

                // just toast it
                /*Toast.makeText(context, "Item: " + listItemText , Toast.LENGTH_SHORT).show();
                Log.d("chatListView", "onItemClick: " + i + " " + l);
                results = db.query(false, chatData.TABLE_NAME,
                        new String[] {chatData.KEY_ID, chatData.KEY_MESSAGE, chatData.KEY_TYPE},
                        chatData.KEY_ID +" not null", null, null, null, null, null, null);
                //int rows = results.getCount();
                //results.moveToFirst();
                printCursor(results);*/
                ///lab 8
                Bundle dataToPass = new Bundle();
                dataToPass.putString(ITEM_SELECTED, chatMessage.get(position).getMessage());
                dataToPass.putInt(ITEM_TYPE,chatMessage.get(position).getMessage_type());
                dataToPass.putInt(ITEM_POSITION, position);
                dataToPass.putLong(ITEM_ID, chatMessage.get(position).getId());

                if(isTablet)
                {
                    DetailFragment dFragment = new DetailFragment(); //add a DetailFragment
                    dFragment.setArguments( dataToPass ); //pass it a bundle for information
                    dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                            .addToBackStack("AnyName") //make the back button undo the transaction
                            .commit(); //actually load the fragment.
                }
                else //isPhone
                {
                    Intent nextActivity = new Intent(ChatRoomActivity.this, EmptyActivity.class);
                    nextActivity.putExtras(dataToPass); //send data to next activity
                    startActivityForResult(nextActivity, EMPTY_ACTIVITY); //make the transition
                }
            }

            ///lab 8

        });


    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        // database
        chatData.close();
        Log.i(ACTIVITY_NAME,"In onDestroy()");
    }

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
            LayoutInflater inflater = ChatRoomActivity.this.getLayoutInflater();
            View result = null;

            if (getItem(position).getMessage_type() == RECEIVE_MESSAGE) {
                result = inflater.inflate(R.layout.chat_message_received, null);
            } else {
                result = inflater.inflate(R.layout.chat_message_sent, null);
            }

            TextView message = (TextView) result.findViewById(R.id.message_text);

            message.setText(getItem(position).getMessage());

            return result;
        }

    }

    public void onActivityResult(int requestCode,int resultCode, Intent data) {
        if ((requestCode == 5)&&(resultCode == Activity.RESULT_OK)){
            Log.i(ACTIVITY_NAME, "Returned to ChatRoomActivity.onActivityResult");
        }

        if(requestCode == EMPTY_ACTIVITY)
        {
            if(resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
                long id = data.getLongExtra(ITEM_ID, 0);
                deleteListMessage(id);
            }
        }

    }

    private void printCursor(Cursor c){
        //•	The database version number
        Log.i(ACTIVITY_NAME,  "SQL MESSAGE: Databse version " + String.valueOf(db.getVersion()));
        //•	The number of columns in the cursor.
        Log.i(ACTIVITY_NAME,  "SQL MESSAGE: Column Count " + String.valueOf(c.getColumnCount()));
        //•	The name of the columns in the cursor.
        for (String s : c.getColumnNames()) {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: Column Name " + s);
        }
        //•	The name of the columns in the cursor.
        for (int i = 0; i < c.getColumnCount(); i++) {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: Column Name " + c.getColumnName(i));
        }
        //•	The number of results in the cursor
        Log.i(ACTIVITY_NAME, "SQL MESSAGE: rows returned "+String.valueOf(c.getCount()));
        //•	Each row of results in the cursor. 
        c.moveToFirst();
        while(! c.isAfterLast()){
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + c.getString( c.getColumnIndex( ChatDatabase.KEY_MESSAGE)));
            results.moveToNext();
        }

    }
    public void deleteListMessage(Long id)
    {
        final SQLiteDatabase db = chatData.getWritableDatabase();

        db.delete(ChatDatabase.TABLE_NAME, "_id = " + id , null);
        chatMessage.clear();
        results = db.query(false, chatData.TABLE_NAME,
                new String[] {chatData.KEY_ID, chatData.KEY_MESSAGE, chatData.KEY_TYPE},
                chatData.KEY_ID +" not null", null, null, null, null, null, null);

        int rows = results.getCount();
        results.moveToFirst();
        while(! results.isAfterLast()){
            // get message from database and add message to chatMessage
            chatMessage.add(new Message(results.getString(results.getColumnIndex(ChatDatabase.KEY_MESSAGE)),
                    results.getInt(results.getColumnIndex(ChatDatabase.KEY_TYPE)),
                    results.getLong(results.getColumnIndex(ChatDatabase.KEY_ID))
            ));
            //Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + results.getString( results.getColumnIndex( ChatDatabase.KEY_MESSAGE)));
            results.moveToNext();
        }

        messageAdapter.notifyDataSetChanged();
    }

//    public void deleteMessageId(int id)
//    {
//        Log.i("Delete this message:" , " id="+id);
//        //source.remove(id);
//        //theAdapter.notifyDataSetChanged();
//    }
}

