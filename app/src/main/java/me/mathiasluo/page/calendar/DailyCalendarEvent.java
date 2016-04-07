package me.mathiasluo.page.calendar;

import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;

import java.util.Calendar;

import me.mathiasluo.page.calendar.bean.DailyEvent;

/**
 * Created by mathiasluo on 16-4-6.
 */
public class DailyCalendarEvent extends BaseCalendarEvent {
    private DailyEvent mDailyEvent;

    public DailyCalendarEvent(DailyCalendarEvent calendarEvent) {
        super(calendarEvent);
        this.mDailyEvent = calendarEvent.getmDailyEvent();
    }

    public DailyCalendarEvent(String title, String description, String location, int color, Calendar startTime, Calendar endTime, boolean allDay, DailyEvent mDailyEvent) {
        super(title, description, location, color, startTime, endTime, allDay);
        this.mDailyEvent = mDailyEvent;
    }

    public DailyCalendarEvent(long id, int color, String title, String description, String location, long dateStart, long dateEnd, int allDay, String duration, DailyEvent mDailyEvent) {
        super(id, color, title, description, location, dateStart, dateEnd, allDay, duration);
        this.mDailyEvent = mDailyEvent;
    }

    public DailyEvent getmDailyEvent() {
        return mDailyEvent;
    }

    public void setmDailyEvent(DailyEvent mDailyEvent) {
        this.mDailyEvent = mDailyEvent;
    }

    @Override
    public CalendarEvent copy() {
        return new DailyCalendarEvent(this);
    }
}
