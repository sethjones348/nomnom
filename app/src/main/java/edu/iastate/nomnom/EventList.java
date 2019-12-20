package edu.iastate.nomnom;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 * an view model object to keep a list of all the events in the map
 */
public class EventList extends ViewModel {

    /**
     * an mutable live data object to keep track of the events list
     */
    public transient MutableLiveData<ArrayList<Event>> eventList;

    /**
     * constructor to create a mutable live data object and assign the object to an array list of events
     */
    public EventList(){
        eventList = new MutableLiveData<>();
        eventList.setValue(new ArrayList<Event>());
    }

    /**
     * getter method that returns the mutable live data object of the event list
     * @return
     *      mutable live data object of events
     */
    public MutableLiveData<ArrayList<Event>> getEventList() {
        return eventList;
    }

    /**
     * setter method that assigns the mutable live data object of the event list
     * @param eventList
     *      mutable live data object of events
     */
    public void setEventList(MutableLiveData<ArrayList<Event>> eventList) {
        this.eventList = eventList;
    }

    /**
     * adds an event to the event list
     * @param e
     *      the event that needs to be added
     */
    public void addEvent(Event e){
        ArrayList<Event> list = this.eventList.getValue();
        list.add(e);

        this.eventList.setValue(list);
    }

    /**
     * deletes an event from the event list
     * @param e
     *      the event that needs to be deleted
     */
    public void deleteEvent(Event e){
        ArrayList<Event> list = this.eventList.getValue();
        int deleteIndex = getIndex(e.getEventId());
        list.remove(deleteIndex);

        this.eventList.setValue(list);
    }

    /**
     * get an index value in the list for a give eventID
     * @param eventId
     *      a string of the eventID object made by Firebase
     * @return
     *      an index of that event in the events list
     */
    private int getIndex(String eventId){
        ArrayList<Event> list = this.eventList.getValue();
        for(int i =0; i < list.size(); i++){
            if(list.get(i).getEventId() == eventId){
                return i;
            }
        }
        return -1;
    }
}
