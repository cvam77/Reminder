package com.example.reminder;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.reminder.database.AimDatabase;


public class ViewModelFactoryForAddGoal extends ViewModelProvider.NewInstanceFactory
{
    private final AimDatabase mDatabase;
    private final int mGoalId;

    public  ViewModelFactoryForAddGoal(AimDatabase database, int goalId)
    {
        mDatabase = database;
        mGoalId = goalId;
    }

    @Override
    public<T extends ViewModel> T create(Class<T> modelClass)
    {
        return (T) new ViewModelForAddGoal(mDatabase,mGoalId);
    }
}
