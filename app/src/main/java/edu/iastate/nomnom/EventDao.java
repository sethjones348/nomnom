package edu.iastate.nomnom;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EventDao {

    /**
     * Gets all the events in the SQLite database
     * @return A List containing all events in the database
     */
    @Query("SELECT * FROM event")
    List<Event> getAll();

    /**
     * Gets the event with the matching eventId from the database
     * @param eventId String eventId of the Event
     * @return Event with the matching eventId
     */
    @Query("SELECT * FROM event WHERE eventId LIKE :eventId")
    Event findByID(String eventId);

    /**
     * Inserts the event into the database
     * @param event Event to be inserted
     */
    @Insert
    void insertEvent(Event event);

    /**
     * Deletes the given event from the database
     * @param event Event to be deleted
     */
    @Delete
    void delete(Event event);

    /**
     * Updates the changed parameters of the given Event
     * @param event Event to be updated
     */
    @Update
    void update(Event event);

}
