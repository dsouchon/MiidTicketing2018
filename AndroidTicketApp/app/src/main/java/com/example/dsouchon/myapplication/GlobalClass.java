package com.example.dsouchon.myapplication;

/**
 * Created by dsouchon on 9/10/2015.
 */
import android.app.Application;

public class GlobalClass extends Application{

    private String eventName;
    private String eventImage;


    public String getEventName() {

        return eventName;
    }

    public void setEventName(String aName) {

        eventName = aName;

    }

    public String getEventImage() {

        return eventImage;
    }

    public void setEventImage(String aEventImage) {

        eventImage = aEventImage;
    }

}
