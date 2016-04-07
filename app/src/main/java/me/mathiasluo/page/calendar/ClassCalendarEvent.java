package me.mathiasluo.page.calendar;

import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;

import java.util.Calendar;

import me.mathiasluo.page.calendar.bean.ClassEvent;

/**
 * Created by mathiasluo on 16-4-6.
 */
public class ClassCalendarEvent extends BaseCalendarEvent {

    private ClassEvent mClassEvent;

    public ClassCalendarEvent(long id, int color, String title, String description, String location, long dateStart, long dateEnd, int allDay, String duration, ClassEvent mClassEvent) {
        super(id, color, title, description, location, dateStart, dateEnd, allDay, duration);
        this.mClassEvent = mClassEvent;
    }

    public ClassCalendarEvent(String title, String description, String location, int color, Calendar startTime, Calendar endTime, boolean allDay, ClassEvent mClassEvent) {
        super(title, description, location, color, startTime, endTime, allDay);
        this.mClassEvent = mClassEvent;
    }

    public ClassCalendarEvent(ClassCalendarEvent calendarEvent) {
        super(calendarEvent);
        this.mClassEvent = calendarEvent.getmClassEvent();
    }


    public ClassEvent getmClassEvent() {
        return mClassEvent;
    }

    public void setmClassEvent(ClassEvent mClassEvent) {
        this.mClassEvent = mClassEvent;
    }

    @Override
    public CalendarEvent copy() {
        return new ClassCalendarEvent(this);
    }
}
