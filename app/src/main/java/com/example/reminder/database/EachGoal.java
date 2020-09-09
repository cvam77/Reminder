package com.example.reminder.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "aim")
public class EachGoal
{
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String goalName;
    private Date originalDeadlineDate;
    private Date virtualDeadlineDate;
    private boolean allowVD;
    private boolean vdCreatedForThisAim;
    private boolean nearestToTodayDate;
    private boolean notificationShown;
    private boolean notificationAlarm;

    public EachGoal(int id, String goalName, Date originalDeadlineDate, Date virtualDeadlineDate, boolean allowVD, boolean vdCreatedForThisAim, boolean nearestToTodayDate, boolean notificationShown, boolean notificationAlarm) {
        this.id = id;
        this.goalName = goalName;
        this.originalDeadlineDate = originalDeadlineDate;
        this.virtualDeadlineDate = virtualDeadlineDate;
        this.allowVD = allowVD;
        this.vdCreatedForThisAim = vdCreatedForThisAim;
        this.nearestToTodayDate = nearestToTodayDate;
        this.notificationShown = notificationShown;
        this.notificationAlarm = notificationAlarm;
    }

    @Ignore
    public EachGoal(String goalName, Date originalDeadlineDate, Date virtualDeadlineDate, boolean allowVD, boolean vdCreatedForThisAim, boolean nearestToTodayDate, boolean notificationShown, boolean notificationAlarm) {
        this.goalName = goalName;
        this.originalDeadlineDate = originalDeadlineDate;
        this.virtualDeadlineDate = virtualDeadlineDate;
        this.allowVD = allowVD;
        this.vdCreatedForThisAim = vdCreatedForThisAim;
        this.nearestToTodayDate = nearestToTodayDate;
        this.notificationShown = notificationShown;
        this.notificationAlarm = notificationAlarm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public Date getOriginalDeadlineDate() {
        return originalDeadlineDate;
    }

    public void setOriginalDeadlineDate(Date originalDeadlineDate) {
        this.originalDeadlineDate = originalDeadlineDate;
    }

    public Date getVirtualDeadlineDate() {
        return virtualDeadlineDate;
    }

    public void setVirtualDeadlineDate(Date virtualDeadlineDate) {
        this.virtualDeadlineDate = virtualDeadlineDate;
    }

    public boolean isAllowVD() {
        return allowVD;
    }

    public void setAllowVD(boolean allowVD) {
        this.allowVD = allowVD;
    }

    public boolean isVdCreatedForThisAim() {
        return vdCreatedForThisAim;
    }

    public void setVdCreatedForThisAim(boolean vdCreatedForThisAim) {
        this.vdCreatedForThisAim = vdCreatedForThisAim;
    }

    public boolean isNearestToTodayDate() {
        return nearestToTodayDate;
    }

    public void setNearestToTodayDate(boolean nearestToTodayDate) {
        this.nearestToTodayDate = nearestToTodayDate;
    }

    public boolean isNotificationShown() {
        return notificationShown;
    }

    public void setNotificationShown(boolean notificationShown) {
        this.notificationShown = notificationShown;
    }

    public boolean isNotificationAlarm() {
        return notificationAlarm;
    }

    public void setNotificationAlarm(boolean notificationAlarm) {
        this.notificationAlarm = notificationAlarm;
    }
}