package edu.iastate.nomnom;

import com.google.android.gms.maps.model.LatLng;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
@Entity(tableName = "event")
public class Event {
//    @Entity(tableName = "event")

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

    @ColumnInfo(name = "location")
    private LatLng location;

    @Ignore
    public Event(String eventId, String title, String food, LatLng location, String locationDetails, String startTime, String endTime) {
        this.eventId=eventId;
        this.title = title;
        this.location = location;
        this.locationDetails = locationDetails;
        this.startTime = startTime;
        this.endTime = endTime;
        this.food = food;
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
