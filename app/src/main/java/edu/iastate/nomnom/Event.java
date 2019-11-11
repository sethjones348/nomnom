package edu.iastate.nomnom;

public class Event {
    private int time;

    private String location;

    private String food;

    public Event(int time, String location, String food){
        this.time = time;
        this.location = location;
        this.food = food;
    }
    
    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }
}
