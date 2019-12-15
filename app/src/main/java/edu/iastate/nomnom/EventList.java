package edu.iastate.nomnom;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class EventList extends ViewModel {
    private MutableLiveData<ArrayList<Event>> eventList;

    public EventList(){
        eventList = new MutableLiveData<>();
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
        list.remove(e);

        this.eventList.setValue(list);
    }
}

