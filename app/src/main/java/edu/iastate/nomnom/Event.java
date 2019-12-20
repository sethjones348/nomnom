package edu.iastate.nomnom;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Database;
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

    @ColumnInfo(name = "imgRef")
    private String imgRef;

    /**
     * Constructor for an Event object
     * @param eventID
     * @param title
     * @param food
     * @param latitude
     * @param longitude
     * @param locationDetails
     * @param startTime
     * @param endTime
     * @param imgRef
     */
    @Ignore
    public Event(String eventID, String title, String food, double latitude, double longitude, String locationDetails, String startTime, String endTime, String imgRef) {
        this.eventId = eventID;
        this.title = title;
        this.latitude= latitude;
        this.longitude = longitude;
        this.locationDetails = locationDetails;
        this.startTime = startTime;
        this.endTime = endTime;
        this.food = food;
        this.imgRef = imgRef;
    }

    /**
     * Empty Event object constructor
     */
    public Event() {

    }

    /**
     * Gets the eventId
     * @return eventId
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * Sets the eventId
     * @param eventId
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * Gets the title
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the latitude
     * @return latitude
     */
    public double getLatitude(){
        return latitude;
    }

    /**
     * Sets the latitude
     * @param latitude
     */
    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    /**
     * Gets the longitude
     * @return longitude
     */
    public double getLongitude(){
        return longitude;
    }

    /**
     * Sets the longitude
     * @param longitude
     */
    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    /**
     * Gets the locationDetails
     * @return locationDetails
     */
    public String getLocationDetails() {
        return locationDetails;
    }

    /**
     * Sets the locationDetails
     * @param locationDetails
     */
    public void setLocationDetails(String locationDetails) {
        this.locationDetails = locationDetails;
    }

    /**
     * Gets the startTime
     * @return startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Sets the startTime
     * @param startTime
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the endTime
     * @return endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Sets the endTime
     * @param endTime
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the food
     * @return food
     */
    public String getFood() {
        return food;
    }

    /**
     * Sets the food
     * @param details
     */
    public void setFood(String details) {
        this.food = details;
    }

    /**
     * Gets the imgRef
     * @return imgRef
     */
    public String getImgRef() {
        return imgRef;
    }

    /**
     * Sets the imgRef
     * @param imgRef
     */
    public void setImgRef(String imgRef) {
        this.imgRef = imgRef;
    }

    /**
     * Creates the string representation of this event
     * @return Event String
     */
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
