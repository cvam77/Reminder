package com.example.reminder;

import android.content.Context;
import android.util.Log;

import com.example.reminder.database.AimDatabase;
import com.example.reminder.database.EachGoal;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class DateShuffler
{
    private static AimDatabase mAimDatabase;
    private static boolean vdCreatedForThisAim = false;
    private static boolean isNearestToTodayDate = false;
    private static boolean notificationShownForThisAim = false;
    private static boolean notificationAlarmShownForThisAim = false;

    private static List<EachGoal> GoalListForShuffling;

    public void setGoalList(List<EachGoal> goalList)
    {
       GoalListForShuffling = goalList;
    }


    public static void shuffleDates(Context context)
    {
        mAimDatabase = AimDatabase.getInstance(context);

        for(int i = 0; i < GoalListForShuffling.size(); i++)
        {
            EachGoal eachAim = (EachGoal) GoalListForShuffling.get(i);

            final int eachGoalId = eachAim.getId();

            String eachGoalName = eachAim.getGoalName();

            Date eachGoalDeadlineDate = eachAim.getOriginalDeadlineDate();

            Date eachGoalVirtualDeadlineDate = eachAim.getVirtualDeadlineDate();

            Boolean allowVD = eachAim.isAllowVD();

            vdCreatedForThisAim = eachAim.isVdCreatedForThisAim();

            isNearestToTodayDate = eachAim.isNearestToTodayDate();

            notificationShownForThisAim = eachAim.isNotificationShown();
            notificationAlarmShownForThisAim = eachAim.isNotificationAlarm();

            Calendar singleDayCalendar = Calendar.getInstance();
            singleDayCalendar.setTime(eachGoalDeadlineDate);

            Calendar todayCalendarDefault = Calendar.getInstance();
            todayCalendarDefault.set(Calendar.HOUR_OF_DAY,0);
            todayCalendarDefault.set(Calendar.MINUTE, 0);

            int differenceDays = (int) TimeUnit.DAYS.convert(singleDayCalendar.getTimeInMillis()-todayCalendarDefault.getTimeInMillis(),TimeUnit.MILLISECONDS);


            Random random = new Random();

            //String goalName, Date originalDeadlineDate, Date virtualDeadlineDate, boolean allowVD
            if(allowVD && vdCreatedForThisAim == false)
            {
                switch (differenceDays)
                {
                    case 0:
                        break;

                    case 1:
                        Calendar defaultCalendar = Calendar.getInstance();
                        defaultCalendar.set(Calendar.HOUR_OF_DAY,24);

                        int hoursRemaining = (int) TimeUnit.MILLISECONDS.toHours((eachGoalDeadlineDate.getTime() - defaultCalendar.getTimeInMillis()));

                        Log.d("Sherlock", "Hours Remaining: " + hoursRemaining);
                        if(hoursRemaining > 18 )
                        {
                            int offsetHours = 1 + random.nextInt(4);
                            long originalDateForEachAimLong = eachGoalDeadlineDate.getTime();
                            originalDateForEachAimLong = originalDateForEachAimLong - TimeUnit.HOURS.toMillis(offsetHours);
                            Date virtualDate = new Date(originalDateForEachAimLong);
                            eachGoalVirtualDeadlineDate = virtualDate;

                            vdCreatedForThisAim = true;
                            final EachGoal eachGoal = new EachGoal(eachGoalName,eachGoalDeadlineDate,eachGoalVirtualDeadlineDate,allowVD,vdCreatedForThisAim,
                                    isNearestToTodayDate,notificationShownForThisAim,notificationAlarmShownForThisAim,eachAim.isPastDate());

                            GoalExecutor.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    eachGoal.setId(eachGoalId);
                                    mAimDatabase.aimDao().updateGoal(eachGoal);
                                }
                            });
                        }
                        break;

                    case 2:
                        int oneOrTwo = random.nextInt(2);
                        if(oneOrTwo == 0)
                        {
                            Calendar singleDayCalendarForCaseTwo = Calendar.getInstance();
                            singleDayCalendarForCaseTwo.setTime(eachGoalDeadlineDate);
                            singleDayCalendarForCaseTwo.set(Calendar.DAY_OF_MONTH, singleDayCalendar.get(Calendar.DAY_OF_MONTH) - 1);
                            eachGoalVirtualDeadlineDate = singleDayCalendarForCaseTwo.getTime();
                        }
                        else
                        {
                            int offsetHours = 2 + random.nextInt(4);
                            long originalDateForEachAimLong = eachGoalDeadlineDate.getTime();
                            originalDateForEachAimLong = originalDateForEachAimLong - TimeUnit.HOURS.toMillis(offsetHours);
                            Date virtualDate = new Date(originalDateForEachAimLong);
                            eachGoalVirtualDeadlineDate = virtualDate;
                        }
                        vdCreatedForThisAim = true;
                        final EachGoal eachGoal = new EachGoal(eachGoalName,eachGoalDeadlineDate,eachGoalVirtualDeadlineDate,allowVD,
                                vdCreatedForThisAim,isNearestToTodayDate,notificationShownForThisAim,notificationAlarmShownForThisAim,eachAim.isPastDate());

                        GoalExecutor.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                eachGoal.setId(eachGoalId);
                                mAimDatabase.aimDao().updateGoal(eachGoal);
                            }
                        });
                        break;

                    case 3:
                        int singleOrDouble = random.nextInt(3);
                        if(singleOrDouble != 0)
                        {
                            Calendar singleDayCalendarForCaseThree = Calendar.getInstance();
                            singleDayCalendarForCaseThree.setTime(eachGoalDeadlineDate);
                            singleDayCalendarForCaseThree.set(Calendar.DAY_OF_MONTH, singleDayCalendar.get(Calendar.DAY_OF_MONTH) - 1);
                            eachGoalVirtualDeadlineDate = singleDayCalendarForCaseThree.getTime();
                        }
                        else if(singleOrDouble == 0)
                        {
                            int offsetHours = 2 + random.nextInt(4);
                            long originalDateForEachAimLong = eachGoalDeadlineDate.getTime();
                            originalDateForEachAimLong = originalDateForEachAimLong - TimeUnit.HOURS.toMillis(offsetHours);
                            Date virtualDate = new Date(originalDateForEachAimLong);
                            eachGoalVirtualDeadlineDate = virtualDate;
                        }
                        Log.d("holmes","eachGoalVirtualDeadlineDate = " + eachGoalVirtualDeadlineDate);

                        vdCreatedForThisAim = true;
                        final EachGoal eachAimForCaseThree = new EachGoal(eachGoalName,eachGoalDeadlineDate,eachGoalVirtualDeadlineDate,allowVD,
                                vdCreatedForThisAim,isNearestToTodayDate,notificationShownForThisAim,notificationAlarmShownForThisAim,eachAim.isPastDate());

                        GoalExecutor.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                eachAimForCaseThree.setId(eachGoalId);
                                mAimDatabase.aimDao().updateGoal(eachAimForCaseThree);
                            }
                        });
                        break;

                    case 4:
                        int reduceDays = 1 + random.nextInt(2);
                        Calendar calendarForFourDays = Calendar.getInstance();
                        calendarForFourDays.setTime(eachGoalDeadlineDate);
                        Log.d("peter","four days case (original date: " + calendarForFourDays.getTime() );
                        calendarForFourDays.add(Calendar.DAY_OF_MONTH,-reduceDays);
                        Log.d("peter","four days case (new date: " + calendarForFourDays.getTime() );
                        eachGoalVirtualDeadlineDate = calendarForFourDays.getTime();

                        vdCreatedForThisAim = true;
                        final EachGoal eachAimForCaseFour = new EachGoal(eachGoalName,eachGoalDeadlineDate,eachGoalVirtualDeadlineDate,allowVD,
                                vdCreatedForThisAim,isNearestToTodayDate,notificationShownForThisAim,notificationAlarmShownForThisAim,eachAim.isPastDate());

                        GoalExecutor.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                eachAimForCaseFour.setId(eachGoalId);
                                mAimDatabase.aimDao().updateGoal(eachAimForCaseFour);
                            }
                        });

                        break;

                    default:
                        Calendar calendarInstanceForDefaultCase = Calendar.getInstance();
                        calendarInstanceForDefaultCase.setTime(eachGoalDeadlineDate);
                        if(differenceDays % 2 == 0)
                        {
                            int lowerLimit = differenceDays / 7 + 1;

                            int upperLimit = (differenceDays == 6) ? (differenceDays / 2 - 1) : (differenceDays / 2 - lowerLimit);

                            int subtractDays = lowerLimit + random.nextInt(upperLimit);
                            calendarInstanceForDefaultCase.add(Calendar.DAY_OF_YEAR,-subtractDays);
                            eachGoalVirtualDeadlineDate = calendarInstanceForDefaultCase.getTime();
                        }
                        else
                        {
                            int lowerLimit = differenceDays / 7 + 1;
                            Log.d("remainder", "lower limit: " + lowerLimit);

                            int upperLimit = ((differenceDays - 1) / 2) - 1;

                            Log.d("remainder", "upper limit: " + upperLimit);

                            int subtractDays = lowerLimit + random.nextInt(upperLimit);
                            calendarInstanceForDefaultCase.add(Calendar.DAY_OF_YEAR,-subtractDays);
                            Log.d("remainder", "new calendar for default: " + calendarInstanceForDefaultCase.getTime());
                            eachGoalVirtualDeadlineDate = calendarInstanceForDefaultCase.getTime();
                        }

                        vdCreatedForThisAim = true;
                        final EachGoal eachAimForCaseDefault = new EachGoal(eachGoalName,eachGoalDeadlineDate,eachGoalVirtualDeadlineDate,allowVD,
                                vdCreatedForThisAim,isNearestToTodayDate,notificationShownForThisAim,notificationAlarmShownForThisAim,eachAim.isPastDate());

                        GoalExecutor.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                eachAimForCaseDefault.setId(eachGoalId);
                                mAimDatabase.aimDao().updateGoal(eachAimForCaseDefault);
                            }
                        });
                        break;
                }
            }
        }
    }
}
