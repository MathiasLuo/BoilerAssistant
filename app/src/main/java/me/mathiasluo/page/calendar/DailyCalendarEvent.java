package me.mathiasluo.page.calendar;

import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;

import java.util.Calendar;

/**
 * Created by mathiasluo on 16-4-6.
 */
public class DailyCalendarEvent extends BaseCalendarEvent {
    public DailyCalendarEvent(String title, String description, String location, int color, Calendar startTime, Calendar endTime, boolean allDay) {
        super(title, description, location, color, startTime, endTime, allDay);
    }

    public DailyCalendarEvent(BaseCalendarEvent calendarEvent) {
        super(calendarEvent);
    }

    public DailyCalendarEvent(Calendar day, String title) {
        super(day, title);
    }
}
