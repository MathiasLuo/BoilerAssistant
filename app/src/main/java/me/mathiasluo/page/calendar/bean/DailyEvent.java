package me.mathiasluo.page.calendar.bean;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by mathiasluo on 16-4-6.
 */
public class DailyEvent extends RealmObject {
    Date startDate;
    Date endDate;
    String title = "normal";
    boolean notification;

    public DailyEvent() {
    }

    public DailyEvent(Date startDate, Date endDate, String title, boolean notification) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.notification = notification;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }
}
