package edu.iastate.nomnom;

import com.google.android.gms.maps.model.LatLng;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "event")
public class Event {

    @NonNull
    @PrimaryKey()
    private String eventId;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "food")
    private String food;

    @ColumnInfo(name = "locationDetails")
    private String locationDetails;

    @ColumnInfo(name = "startTime")
    private String startTime;

    @ColumnInfo(name = "endTime")
    private String endTime;

    @ColumnInfo(name = "lat")
    private double latitude;

    @ColumnInfo(name = "long")
    private double longitude;

    @Ignore
    public Event(String eventID, String title, String food, double latitude, double longitude, String locationDetails, String startTime, String endTime) {
        this.eventId = eventID;
        this.title = title;
        this.latitude= latitude;
        this.longitude = longitude;
        this.locationDetails = locationDetails;
        this.startTime = startTime;
        this.endTime = endTime;
        this.food = food;
    }

    public Event() {

    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLatitude(){
        return latitude;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
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

    @Override
    public String toString() {
        return "Event{" +
                "eventId='" + eventId + '\'' +
                ", title='" + title + '\'' +
                ", food='" + food + '\'' +
                ", locationDetails='" + locationDetails + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
