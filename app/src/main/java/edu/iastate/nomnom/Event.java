package edu.iastate.nomnom;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;
//import androidx.room.ColumnInfo;
//import androidx.room.Entity;
//import androidx.room.PrimaryKey;

public class Event {
//    @Entity(tableName = "event")
    private int eventId;

    private String title;

    private String food;

    private String locationDetails;

    private String startTime;

    private String endTime;

    private LatLng location;


    public Event(String title, String food, LatLng location, String locationDetails, String startTime, String endTime) {
        this.title = title;
        this.location = location;
        this.locationDetails = locationDetails;
        this.startTime = startTime;
        this.endTime = endTime;
        this.food = food;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LatLng getLocation(){
        return location;
    }

    public void setLocation(LatLng location){
        this.location = location;
    }

    public String getLocationDetails() {
        return locationDetails;
    }

    public void setLocationDetails(String locationDetails) {
        this.locationDetails = locationDetails;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String details) {
        this.food = details;
    }

}
