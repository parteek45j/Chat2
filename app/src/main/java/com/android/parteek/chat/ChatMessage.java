package com.android.parteek.chat;

/**
 * Created by Suraj on 7/31/2017.
 */

public class ChatMessage {
    String message;
    boolean left;
    String time;

    public ChatMessage(String message, boolean left,String time) {
        super();
        this.message = message;
        this.left = left;
        this.time=time;
    }

    public String getMessage() {
        return message;
    }
}
