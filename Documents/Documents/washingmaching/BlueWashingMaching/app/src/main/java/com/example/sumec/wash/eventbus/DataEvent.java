package com.example.sumec.wash.eventbus;

/**
 * Created by zhdk on 2017/12/6.
 */

public class DataEvent {
    private String eventType;
    private String message;

    public DataEvent(String eventType,String message){
        this.eventType = eventType;
        this.message = message;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
