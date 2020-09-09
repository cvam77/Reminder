package com.example.reminder.database;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateHandler
{
    @TypeConverter
    public static Date convertLongToDate(Long longDate)
    {
        if(longDate == null)
        {
            return null;
        }
        else
        {
            return new Date(longDate);
        }
    }

    @TypeConverter
    public static Long convertDateToLong(Date date)
    {
        if(date == null)
        {
            return null;
        }
        else
        {
            return date.getTime();
        }
    }
}
