package com.example.reminder;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.reminder.database.AimDatabase;
import com.example.reminder.database.EachGoal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddGoal extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener
{
    Calendar calendarInstance = Calendar.getInstance();

    EditText mEtEnterGoal;
    Button mButtonPickDate;
    Button mButtonPickTime;
    TextView mTvDeadlineDate;
    TextView mTvDeadlineTime;
    Button mButtonSave;
    String deadlineTimeString;
    String deadlineDateString;

    int datePickerDayVirtual = calendarInstance.get(Calendar.DAY_OF_MONTH);
    int datePickerMonthVirtual = calendarInstance.get(Calendar.MONTH);
    int datePickerYearVirtual = calendarInstance.get(Calendar.YEAR);
    int timePickerHourVirtual = calendarInstance.get(Calendar.HOUR_OF_DAY);
    int timePickerMinuteVirtual = calendarInstance.get(Calendar.MINUTE);

    int datePickerDayOriginal = calendarInstance.get(Calendar.DAY_OF_MONTH);
    int datePickerMonthOriginal = calendarInstance.get(Calendar.MONTH);
    int datePickerYearOriginal = calendarInstance.get(Calendar.YEAR);
    int timePickerHourOriginal = calendarInstance.get(Calendar.HOUR_OF_DAY);
    int timePickerMinuteOriginal = calendarInstance.get(Calendar.MINUTE);

    Switch mSwitchVirtualDeadline;
    TextView mTvVirtualDeadline;

    long timeNow = System.currentTimeMillis();
    Date longToDate = new Date(timeNow);

    private AimDatabase mAimDatabase;

    public static final String SAVED_INSTANCE_ID = "saved_instance_id";
    public static final String GOAL_ID_EXTRA = "goal_id";

    private static final int DEFAULT_GOAL_ID = -1;
    private int mGoalId = DEFAULT_GOAL_ID;

    boolean isSwitchChecked = true;

    boolean isVdCreatedForThisAim = false;

    boolean isNearestToTodayDate = false;

    boolean notificationShownForThisAim=false;
    boolean notificationAlarm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);

        initializeAllViews();

        mAimDatabase = AimDatabase.getInstance(getApplicationContext());

        if(savedInstanceState != null &&
                savedInstanceState.containsKey(SAVED_INSTANCE_ID))
        {
            mGoalId = savedInstanceState.getInt(SAVED_INSTANCE_ID,DEFAULT_GOAL_ID);
        }

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(GOAL_ID_EXTRA))
        {
            mButtonSave.setText("Update");
            if(mGoalId == DEFAULT_GOAL_ID)
            {
                mGoalId = intent.getIntExtra(GOAL_ID_EXTRA,DEFAULT_GOAL_ID);

                ViewModelFactoryForAddGoal viewModelFactory = new
                        ViewModelFactoryForAddGoal(mAimDatabase,mGoalId);

                final ViewModelForAddGoal viewModel = ViewModelProviders.of(this,viewModelFactory)
                        .get(ViewModelForAddGoal.class);

                viewModel.getIndividualGoal().observe(this, new Observer<EachGoal>() {
                    @Override
                    public void onChanged(EachGoal eachGoal) {
                        viewModel.getIndividualGoal().removeObserver(this);
                        loadGoalAndFillViews(eachGoal);
                    }
                });
            }
        }
    }

    private void initializeAllViews()
    {
        mEtEnterGoal = findViewById(R.id.etEnterGoal);
        mTvDeadlineTime = findViewById(R.id.tvAddGoalDeadlineTime);
        mButtonPickTime = findViewById(R.id.buttonSetDeadlineTime);
        mTvDeadlineDate = findViewById(R.id.tvAddGoalDeadlineDate);
        mButtonSave = findViewById(R.id.buttonSave);


        mButtonPickTime = findViewById(R.id.buttonSetDeadlineTime);
        mButtonPickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        mButtonPickDate = findViewById(R.id.buttonSetDeadlineDate);
        mButtonPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment pickDate = new com.example.reminder.DatePicker();
                pickDate.show(getSupportFragmentManager(),"Pick Date");
            }
        });

        mTvVirtualDeadline = findViewById(R.id.tvVirtualDeadline);
        mSwitchVirtualDeadline = findViewById(R.id.switchVirtualDeadline);
        mSwitchVirtualDeadline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    isSwitchChecked = true;
                    mTvVirtualDeadline.setText("\"Virtual Deadline Enabled\"");
                }
                else if(!isChecked)
                {
                    isSwitchChecked = false;
                    mTvVirtualDeadline.setText("\"Virtual Deadline Disabled\"");
                }
            }
        });

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGoal();
            }
        });
    }

    private void loadGoalAndFillViews(EachGoal loadedGoal)
    {
        if(loadedGoal == null)
            return;

        Date virtualDeadlineDate = loadedGoal.getVirtualDeadlineDate();

        isVdCreatedForThisAim = loadedGoal.isVdCreatedForThisAim();

        isNearestToTodayDate = loadedGoal.isNearestToTodayDate();

        Calendar virtualCalendar = Calendar.getInstance();
        virtualCalendar.setTime(virtualDeadlineDate);

        datePickerDayVirtual = virtualCalendar.get(Calendar.DAY_OF_MONTH);
        datePickerMonthVirtual = virtualCalendar.get(Calendar.MONTH);
        datePickerYearVirtual = virtualCalendar.get(Calendar.YEAR);
        timePickerHourVirtual = virtualCalendar.get(Calendar.HOUR_OF_DAY);
        timePickerMinuteVirtual = virtualCalendar.get(Calendar.MINUTE);

        Date originalDeadlineDate = loadedGoal.getOriginalDeadlineDate();

        isSwitchChecked = loadedGoal.isAllowVD();

        Calendar originalCalendar = Calendar.getInstance();
        originalCalendar.setTime(originalDeadlineDate);

        datePickerDayOriginal = originalCalendar.get(Calendar.DAY_OF_MONTH);
        datePickerMonthOriginal = originalCalendar.get(Calendar.MONTH);
        datePickerYearOriginal = originalCalendar.get(Calendar.YEAR);
        timePickerHourOriginal = originalCalendar.get(Calendar.HOUR_OF_DAY);
        timePickerMinuteOriginal = originalCalendar.get(Calendar.MINUTE);

        String deadlineDateString = DateFormat.getDateInstance(DateFormat.FULL).format(virtualDeadlineDate);

        DateFormat format = new SimpleDateFormat("hh:mm a");

        String deadlineTimeString = format.format(virtualDeadlineDate);

        mEtEnterGoal.setText(loadedGoal.getGoalName());
        mTvDeadlineDate.setText(deadlineDateString);
        mButtonPickDate.setText("CHANGE DEADLINE DATE");
        mTvDeadlineTime.setText(deadlineTimeString);
        mButtonPickTime.setText("CHANGE DEADLINE TIME");
        mSwitchVirtualDeadline.setChecked(isSwitchChecked);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        if((year != datePickerYearVirtual || year != datePickerYearOriginal) || (month != datePickerMonthOriginal || month != datePickerMonthVirtual)
        || (dayOfMonth != datePickerDayOriginal || dayOfMonth != datePickerDayVirtual))
        {
            Log.d("virat","setting vd created for it to false");
            isVdCreatedForThisAim = false;
        }
        Log.d("cricket","date set called");
        long currentTimeInMillis = System.currentTimeMillis();

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR,year);
        mCalendar.set(Calendar.MONTH,month);
        mCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        long calendarInLong = mCalendar.getTimeInMillis();

        if(calendarInLong >= currentTimeInMillis)
        {
            datePickerDayVirtual = dayOfMonth;
            datePickerMonthVirtual = month;
            datePickerYearVirtual = year;

            datePickerDayOriginal = dayOfMonth;
            datePickerMonthOriginal = month;
            datePickerYearOriginal = year;

            longToDate = new Date(calendarInLong);
            deadlineDateString = DateFormat.getDateInstance(DateFormat.FULL).format(longToDate);
            mTvDeadlineDate.setText(deadlineDateString);
        }
        else
        {
            Toast toast = Toast.makeText(this,"Must choose a future date!", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void showTimePicker()
    {
        Calendar mCalendar = Calendar.getInstance();
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = mCalendar.get(Calendar.MINUTE);

        if(mGoalId == DEFAULT_GOAL_ID)
        {
             hour = mCalendar.get(Calendar.HOUR_OF_DAY);
             minute = mCalendar.get(Calendar.MINUTE);
        }
        else
        {
            hour = timePickerHourVirtual;
            minute = timePickerMinuteVirtual;
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,this,hour,minute, android.text.format.DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {
        if((hourOfDay != timePickerHourOriginal || hourOfDay != timePickerHourVirtual) || (minute != timePickerMinuteOriginal || minute != timePickerMinuteVirtual))
        {
            Log.d("virat","setting vd created for it to false");
            isVdCreatedForThisAim = false;
        }
        Log.d("cricket","time set called");
        long currentTimeMillis = System.currentTimeMillis();

        Calendar calendarTimeChosen = Calendar.getInstance();
        calendarTimeChosen.set(Calendar.YEAR, datePickerYearVirtual);
        calendarTimeChosen.set(Calendar.MONTH, datePickerMonthVirtual);
        calendarTimeChosen.set(Calendar.DAY_OF_MONTH, datePickerDayVirtual);
        calendarTimeChosen.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendarTimeChosen.set(Calendar.MINUTE,minute);
        long calendarTimeChosenInLong = calendarTimeChosen.getTimeInMillis();

        if(calendarTimeChosenInLong >= currentTimeMillis)
        {
            timePickerHourVirtual = hourOfDay;
            timePickerMinuteVirtual = minute;

            timePickerHourOriginal = hourOfDay;
            timePickerMinuteOriginal = minute;

            calendarTimeChosen.set(Calendar.HOUR_OF_DAY, timePickerHourVirtual);
            calendarTimeChosen.set(Calendar.MINUTE, timePickerMinuteVirtual);

            Date date = calendarTimeChosen.getTime();
            DateFormat format = new SimpleDateFormat("hh:mm a");

            deadlineTimeString = format.format(date);
            mTvDeadlineTime.setText(deadlineTimeString);
        }
        else
        {
            Toast toast = Toast.makeText(this,"Must choose a future time!", Toast.LENGTH_LONG);
            toast.show();
        }

    }

    public void saveGoal()
    {
        final String eachGoalName = mEtEnterGoal.getText().toString();

        Calendar calendarVirtualGoingToRoom = Calendar.getInstance();
        calendarVirtualGoingToRoom.set(Calendar.YEAR, datePickerYearVirtual);
        calendarVirtualGoingToRoom.set(Calendar.MONTH, datePickerMonthVirtual);
        calendarVirtualGoingToRoom.set(Calendar.DAY_OF_MONTH, datePickerDayVirtual);
        calendarVirtualGoingToRoom.set(Calendar.HOUR_OF_DAY, timePickerHourVirtual);
        calendarVirtualGoingToRoom.set(Calendar.MINUTE, timePickerMinuteVirtual);

        final Date eachGoalVirtualDeadline = calendarVirtualGoingToRoom.getTime();

        Calendar calendarOriginalGoingToRoom = Calendar.getInstance();
        calendarOriginalGoingToRoom.set(Calendar.YEAR,datePickerYearOriginal);
        calendarOriginalGoingToRoom.set(Calendar.MONTH,datePickerMonthOriginal);
        calendarOriginalGoingToRoom.set(Calendar.DAY_OF_MONTH, datePickerDayOriginal);
        calendarOriginalGoingToRoom.set(Calendar.HOUR_OF_DAY,timePickerHourOriginal);
        calendarOriginalGoingToRoom.set(Calendar.MINUTE,timePickerMinuteOriginal);
        final Date eachGoalOriginalDeadline = calendarOriginalGoingToRoom.getTime();

        final boolean allowVD = isSwitchChecked;



        GoalExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if(mGoalId == DEFAULT_GOAL_ID)
                {
                    EachGoal eachGoal = new EachGoal(eachGoalName,eachGoalOriginalDeadline,eachGoalVirtualDeadline,allowVD,
                            isVdCreatedForThisAim,isNearestToTodayDate,notificationShownForThisAim, notificationAlarm);
                    mAimDatabase.aimDao().insertGoal(eachGoal);
                }
                else
                {
                    EachGoal eachGoal = new EachGoal(eachGoalName,eachGoalOriginalDeadline,eachGoalVirtualDeadline,allowVD,
                            isVdCreatedForThisAim,isNearestToTodayDate,notificationShownForThisAim, notificationAlarm);
                    eachGoal.setId(mGoalId);
                    mAimDatabase.aimDao().updateGoal(eachGoal);
                }
                finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {
        outState.putInt(SAVED_INSTANCE_ID,mGoalId);
        super.onSaveInstanceState(outState);
    }
}