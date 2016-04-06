package me.mathiasluo.page.calendar;

import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;

import java.util.Calendar;

/**
 * Created by mathiasluo on 16-4-6.
 */
public class ClassCalendarEvent extends BaseCalendarEvent {




    public ClassCalendarEvent(long id, int color, String title, String description, String location, long dateStart, long dateEnd, int allDay, String duration) {
        super(id, color, title, description, location, dateStart, dateEnd, allDay, duration);
    }

    public ClassCalendarEvent(String title, String description, String location, int color, Calendar startTime, Calendar endTime, boolean allDay) {
        super(title, description, location, color, startTime, endTime, allDay);
    }

    public ClassCalendarEvent(BaseCalendarEvent calendarEvent) {
        super(calendarEvent);
    }

    public ClassCalendarEvent(Calendar day, String title) {
        super(day, title);
    }
}
