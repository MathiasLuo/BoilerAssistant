package me.mathiasluo.page.calendar;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.tibolte.agendacalendarview.render.EventRenderer;

import java.util.Date;

import me.mathiasluo.R;

/**
 * Created by mathiasluo on 16-4-7.
 */
public class ClassEventRenderer extends EventRenderer<ClassCalendarEvent> {

    @Override
    public void render(View view, ClassCalendarEvent event) {

        LinearLayout descriptionContainer = (LinearLayout) view.findViewById(com.github.tibolte.agendacalendarview.R.id.view_agenda_event_description_container);
        descriptionContainer.setBackgroundColor(event.getColor());

        TextView className = (TextView) view.findViewById(R.id.text_class_name);
        TextView classTime = (TextView) view.findViewById(R.id.text_class_time);
        TextView classLocation = (TextView) view.findViewById(R.id.text_class_location);

        classLocation.setText(event.getmClassEvent().getClassMeetingAt());
        className.setText(event.getmClassEvent().getClassName());


        Date startDate = event.getStartTime().getTime();
        String startTime = startDate.getHours() + ":" + startDate.getMinutes();

        Date endDate = event.getEndTime().getTime();
        String endTime = endDate.getHours() + ":" + endDate.getMinutes();

        classTime.setText(startTime + "--" + endTime);

        className.setTextColor(view.getResources().getColor(com.github.tibolte.agendacalendarview.R.color.theme_text_icons));
        classTime.setTextColor(view.getResources().getColor(com.github.tibolte.agendacalendarview.R.color.theme_text_icons));
        classLocation.setTextColor(view.getResources().getColor(com.github.tibolte.agendacalendarview.R.color.theme_text_icons));


    }

    @Override
    public int getEventLayout() {
        return R.layout.list_item_classevent;
    }


    @Override
    public Class<ClassCalendarEvent> getRenderType() {
        return ClassCalendarEvent.class;
    }
}
