package com.example.reminder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.reminder.database.AimDatabase;
import com.example.reminder.database.EachGoal;


public class ViewModelForAddGoal extends ViewModel
{
    private LiveData<EachGoal> individualGoal;

    public ViewModelForAddGoal(AimDatabase database, int goalId) {
        individualGoal = database.aimDao().loadGoalById(goalId);
    }

    public LiveData<EachGoal> getIndividualGoal()
    {
        return individualGoal;
    }
}
