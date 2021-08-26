package com.fdananda.gitroomdatabase.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.fdananda.gitroomdatabase.model.User;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {

    private static UserDatabase INSTANCE;

    public static UserDatabase getInstance(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    UserDatabase.class,
                    "user_database"
            ).build();
        }
        return INSTANCE;
    }

    public abstract UserDao userDao();
}
