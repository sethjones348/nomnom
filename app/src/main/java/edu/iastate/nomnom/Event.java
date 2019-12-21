package edu.iastate.nomnom;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "event")
public class Event {

    /**
     *  an ID for the object from firebase
     */
    @NonNull
    @PrimaryKey()
    private String eventId;

    /**
     * title of the event
     */
    @ColumnInfo(name = "title")
    private String title;

    /**
     * food details at the event
     */
    @ColumnInfo(name = "food")
    private String food;

    /**
     * name of the location of the event
     */
    @ColumnInfo(name = "locationDetails")
    private String locationDetails;

    /**
     * the starting time of the event
      */
    @ColumnInfo(name = "startTime")
    private String startTime;

    /**
     * the ending time of the event
     */
    @ColumnInfo(name = "endTime")
    private String endTime;

    /**
     *  the latitude of the location of the event
     */
    @ColumnInfo(name = "lat")
    private double latitude;

    /**
     * the longitude of the location of the event
     */
    @ColumnInfo(name = "long")
    private double longitude;

    /**
     * reference of the image taken
     */
    @ColumnInfo(name = "imgRef")
    private String imgRef;

    /**
     * Constructor for an Event object
     * @param eventID
     *      an ID for the object from firebase
     * @param title
     *      title of the event
     * @param food
     *      food details at the event
     * @param latitude
     *      the latitude of the location of the event
     * @param longitude
     *      the longitude of the location of the event
     * @param locationDetails
     *      name of the location of the event
     * @param startTime
     *      the starting time of the event
     * @param endTime
     *      the ending time of the event
     * @param imgRef
     *      reference of the image taken
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
     *      the event ID
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * Sets the eventId
     * @param eventId
     *      the event ID
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * Gets the title
     * @return title
     *      the name of the event
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title
     * @param title
     *      name of the event
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the latitude
     * @return latitude
     *      latitude of the location
     */
    public double getLatitude(){
        return latitude;
    }

    /**
     * Sets the latitude
     * @param latitude
     *      latitude of the location
     */
    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    /**
     * Gets the longitude
     * @return longitude
     *      longitude of the location
     */
    public double getLongitude(){
        return longitude;
    }

    /**
     * Sets the longitude
     * @param longitude
     *      latitude of the location
     */
    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    /**
     * Gets the locationDetails
     * @return locationDetails
     *      name of the location
     */
    public String getLocationDetails() {
        return locationDetails;
    }

    /**
     * Sets the locationDetails
     * @param locationDetails
     *      name of the location
     */
    public void setLocationDetails(String locationDetails) {
        this.locationDetails = locationDetails;
    }

    /**
     * Gets the startTime
     * @return startTime
     *      starting time
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Sets the startTime
     * @param startTime
     *      starting time
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
     *      ending time
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the food
     * @return food
     *      food details
     */
    public String getFood() {
        return food;
    }

    /**
     * Sets the food
     * @param details
     *      food details
     */
    public void setFood(String details) {
        this.food = details;
    }

    /**
     * Gets the imgRef
     * @return imgRef
     *      image reference
     */
    public String getImgRef() {
        return imgRef;
    }

    /**
     * Sets the imgRef
     * @param imgRef
     *      image reference
     */
    public void setImgRef(String imgRef) {
        this.imgRef = imgRef;
    }

    /**
     * Creates the string representation of this event
     * @return Event String
     *      a string representation of the event object
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
