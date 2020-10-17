package com.example.reminder;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobService;

public class AimFirebaseJobService extends JobService
{

    private AsyncTask mAsyncTask;

    Context serviceContext = MainActivity.mContext;

    MainActivity mainActivity = new MainActivity();

    @Override
    public boolean onStartJob(final com.firebase.jobdispatcher.JobParameters job) {
        mAsyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                DateShuffler.shuffleDates(serviceContext);
                mainActivity.makeNotification(serviceContext);
                mainActivity.setCurrentDate();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job,false);
            }
        };

        mAsyncTask.execute();

        return true;
    }

    @Override
    public boolean onStopJob(com.firebase.jobdispatcher.JobParameters job) {
        if(mAsyncTask != null) mAsyncTask.cancel(true);

        return true;
    }
}
