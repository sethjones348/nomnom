package edu.iastate.nomnom;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EventDao {

    @Query("SELECT * FROM event")
    List<Event> getAll();

    @Query("SELECT * FROM event WHERE eventId LIKE :eventId")
    Event findByID(String eventId);

    @Insert
    void insertEvent(Event event);

    @Delete
    void delete(Event event);

}
