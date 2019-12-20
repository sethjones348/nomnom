package edu.iastate.nomnom;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Event.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    /**
     * The EventDao instance for this app
     * @return EventDao instance of the app
     */
    public abstract EventDao eventDao();

    /**
     * Instantiates the AppDatabase for this app
     * @param context Context in which the AppDatabase is being created
     * @return INSTANCE The RoomDatabase instance for this app
     */
    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "events.db")
                    .addMigrations(MIGRATION_2_3)
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE events "
                    + " ADD COLUMN imgRef STRING");
        }
    };

    /**
     * Destroys the AppDatabase instance
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

}
