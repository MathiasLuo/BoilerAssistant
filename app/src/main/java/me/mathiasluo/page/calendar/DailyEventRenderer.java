package me.mathiasluo.page.calendar;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.tibolte.agendacalendarview.render.EventRenderer;

import java.util.Date;

import me.mathiasluo.R;

/**
 * Created by mathiasluo on 16-4-7.
 */
public class DailyEventRenderer extends EventRenderer<DailyCalendarEvent> {


    @Override
    public void render(View view, DailyCalendarEvent event) {
        TextView textViewTitle = (TextView) view.findViewById(R.id.view_agenda_event_title);
        TextView textViewTime = (TextView) view.findViewById(R.id.view_agenda_event_location);
        ImageView imageView = (ImageView) view.findViewById(R.id.list_item_notifition);

        if (event.getmDailyEvent().isNotification()) {
            imageView.setImageResource(R.mipmap.ic_notifications_none_white_24dp);
        }


        textViewTitle.setText(event.getTitle());
        Date startDate = event.getStartTime().getTime();
        String startTime = startDate.getHours() + ":" + startDate.getMinutes();

        Date endDate = event.getEndTime().getTime();
        String endTime = endDate.getHours() + ":" + endDate.getMinutes();

        textViewTime.setText(startTime + "--" + endTime);

        LinearLayout descriptionContainer = (LinearLayout) view.findViewById(com.github.tibolte.agendacalendarview.R.id.view_agenda_event_description_container);
        descriptionContainer.setBackgroundColor(event.getColor());


        textViewTitle.setTextColor(view.getResources().getColor(com.github.tibolte.agendacalendarview.R.color.theme_text_icons));
        textViewTime.setTextColor(view.getResources().getColor(com.github.tibolte.agendacalendarview.R.color.theme_text_icons));
    }

    @Override
    public int getEventLayout() {
        return R.layout.list_item_dailyevent;
    }

    @Override
    public Class<DailyCalendarEvent> getRenderType() {
        return DailyCalendarEvent.class;
    }
}
