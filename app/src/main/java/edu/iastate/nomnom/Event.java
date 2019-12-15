package edu.iastate.nomnom;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

//import androidx.room.ColumnInfo;
//import androidx.room.Entity;
//import androidx.room.PrimaryKey;
import com.google.firebase.database.DatabaseReference;

//    @Entity(tableName = "event")
public class Event {

    private int eventId;

    private String title;

    private String food;

    private String locationDetails;

    private String startTime;

    private String endTime;

    private LatLng location;

    private DatabaseReference imgRef;

    public Event(String title, String food, LatLng location, String locationDetails, String startTime, String endTime, DatabaseReference imgRef) {
        this.title = title;
        this.location = location;
        this.locationDetails = locationDetails;
        this.startTime = startTime;
        this.endTime = endTime;
        this.food = food;
        this.imgRef = imgRef;
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
    public DatabaseReference getImgRef() {
        return imgRef;
    }

    public void setImgRef(DatabaseReference imgRef) {
        this.imgRef = imgRef;
    }
}
