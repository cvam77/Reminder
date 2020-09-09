package com.example.reminder;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.example.reminder.database.AimDatabase;
import com.example.reminder.database.EachGoal;

import java.util.List;

public class ViewModelForMainActivity extends AndroidViewModel
{
    private LiveData<List<EachGoal>> goalList;

    public ViewModelForMainActivity(@NonNull Application application)
    {
        super(application);
        AimDatabase goalDatabase = AimDatabase.getInstance(this.getApplication());
        goalList = goalDatabase.aimDao().loadAllGoals();
    }

    public LiveData<List<EachGoal>> getGoalList()
    {
        return goalList;
    }

}
