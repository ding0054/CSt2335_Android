package com.example.xiaoy.androidlabs;

public class Message {
    private String message;
    // message type
    // refactor to boolean
    private int message_type;
    // database
    // id
    private long id;

    public Message(String message){
        setMessage(message);
    }

    public Message(String message, int message_type){
        setMessage(message);
        setMessage_type(message_type);
    }

    public Message(String message, int message_type, long id){
        setMessage(message);
        setMessage_type(message_type);
        setId(id);
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


}
