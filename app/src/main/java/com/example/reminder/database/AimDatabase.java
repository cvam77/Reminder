package com.example.reminder.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {EachGoal.class}, version = 7, exportSchema = false)
@TypeConverters(DateHandler.class)
public abstract class AimDatabase extends RoomDatabase
{
    private static final String TAG = AimDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DB_NAME = "aims";
    private static AimDatabase sInstance;

    static Migration migrationOneToTwo = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE 'aim' ADD COLUMN 'vdCreatedForThisAim' INTEGER NOT NULL DEFAULT 0");
        }
    };

    static Migration migrationTwoToThree = new Migration(2,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE 'aim' ADD COLUMN 'nearestToTodayDate' INTEGER NOT NULL DEFAULT 0");
        }
    };

    static Migration migrationThreeToFour = new Migration(3,4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE 'aim' ADD COLUMN 'notificationShown' INTEGER NOT NULL DEFAULT 0");
        }
    };

    static Migration migrationFourToFive = new Migration(4,5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE 'aim' ADD COLUMN 'notificationAlarm' INTEGER NOT NULL DEFAULT 0");
        }
    };

    static Migration migrationFiveToSix = new Migration(5,6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE 'aim' ADD COLUMN 'pastDate' INTEGER NOT NULL DEFAULT 0");
        }
    };

    static Migration migrationSixToSeven = new Migration(6,7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE 'aim' ADD COLUMN 'taskDone' INTEGER NOT NULL DEFAULT 0");
        }
    };

    public static AimDatabase getInstance(Context context)
    {
        if(sInstance == null)
        {
            synchronized (LOCK)
            {
                Log.d(TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AimDatabase.class, AimDatabase.DB_NAME)
                        .addMigrations(migrationOneToTwo,migrationTwoToThree,migrationThreeToFour,migrationFourToFive,
                                migrationFiveToSix,migrationSixToSeven)
                        .build();
            }


        }
        Log.d(TAG,"Getting the instance");
        return sInstance;
    }

    public abstract AimDao aimDao();
}
