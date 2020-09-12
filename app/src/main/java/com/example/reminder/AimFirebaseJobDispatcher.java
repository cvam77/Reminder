package com.example.reminder;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.reminder.AimFirebaseJobService;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

public class AimFirebaseJobDispatcher
{
    private static final int INTERVAL_MINUTES = 60;

    private static final int INTERVAL_IN_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(INTERVAL_MINUTES));

    private static final int INTERVAL_WIDTH = INTERVAL_IN_SECONDS + INTERVAL_IN_SECONDS;

    private static boolean sInstance;

    private static final String DISPATCHER_TAG = "dispatcher_tag";

    synchronized public static void scheduleJob(@NonNull final Context context)
    {
        if(sInstance) return;

        Driver driver = new GooglePlayDriver(context);

        FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(driver);

        Job mJob = firebaseJobDispatcher.newJobBuilder()
                .setService(AimFirebaseJobService.class)
                .setTag(DISPATCHER_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(INTERVAL_IN_SECONDS,INTERVAL_WIDTH))
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setReplaceCurrent(true)
                .build();

        firebaseJobDispatcher.schedule(mJob);

        sInstance = true;

    }
}
















