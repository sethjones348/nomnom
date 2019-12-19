package edu.iastate.nomnom;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class EventList extends ViewModel {

    public transient MutableLiveData<ArrayList<Event>> eventList;

    public EventList(){
        eventList = new MutableLiveData<>();
        eventList.setValue(new ArrayList<Event>());
    }

    public MutableLiveData<ArrayList<Event>> getEventList() {
        return eventList;
    }

    public void setEventList(MutableLiveData<ArrayList<Event>> eventList) {
        this.eventList = eventList;
    }

    public void addEvent(Event e){
        ArrayList<Event> list = this.eventList.getValue();
        list.add(e);

        this.eventList.setValue(list);
    }

    public void deleteEvent(Event e){
        ArrayList<Event> list = this.eventList.getValue();
        int deleteIndex = getIndex(e.getEventId());
        list.remove(deleteIndex);

        this.eventList.setValue(list);
    }

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

