package me.mathiasluo.page.calendar;

import android.app.TimePickerDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.InjectView;
import me.mathiasluo.APP;
import me.mathiasluo.activity.MainActivity;
import me.mathiasluo.R;
import me.mathiasluo.base.MainFragment;
import me.mathiasluo.page.calendar.bean.ClassEvent;
import me.mathiasluo.page.calendar.bean.DailyEvent;
import me.mathiasluo.page.calendar.bean.DataModel;

/**
 * Created by mathiasluo on 16-3-25.
 */
public class CalendarFragment extends MainFragment<CalendarView, CalendarPresenter> implements CalendarView, CalendarPickerController, View.OnClickListener {

    private static final String LOG_TAG = CalendarFragment.class.getSimpleName();

    @InjectView(R.id.agenda_calendar_view)
    public AgendaCalendarView mAgendaCalendarView;

    @InjectView(R.id.toolbar)
    public Toolbar mToolbar;

    @InjectView(R.id.fab)
    public FloatingActionButton fab;


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_calendar;
    }

    @Override
    public CalendarPresenter createPresenter() {
        return new CalendarPresenter();
    }

    @Override
    public void onResume() {
        super.onResume();

        mToolbar.setTitle("Calendar");
        ((MainActivity) getActivity()).setToolbar(mToolbar);
        fab.setOnClickListener(this);

        // minimum and maximum date of our calendar
        // 2 month behind, one year ahead, example: March 2015 <-> May 2015 <-> May 2016
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        minDate.add(Calendar.MONTH, -2);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);

        //从数据库中获取事件

        List<CalendarEvent> eventList = new ArrayList<>();
        mockList(eventList);

        //
        mAgendaCalendarView.init(eventList, minDate, maxDate, Locale.getDefault(), this);
        mAgendaCalendarView.addEventRenderer(new DrawableEventRenderer());

    }

    @Override
    public void onDaySelected(DayItem dayItem) {
        Log.d(LOG_TAG, String.format("Selected day: %s", dayItem));
    }

    @Override
    public void onEventSelected(CalendarEvent event) {
        Log.d(LOG_TAG, String.format("Selected event: %s", event));
    }

    @Override
    public void onScrollToDate(Calendar calendar) {
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        }
    }

    private void mockList(List<CalendarEvent> eventList) {

        Calendar startTime1 = Calendar.getInstance();
        Calendar endTime1 = Calendar.getInstance();
        endTime1.add(Calendar.MONTH, 1);
        BaseCalendarEvent event1 = new BaseCalendarEvent("Thibault travels in Iceland", "A wonderful journey!", "Iceland",
                ContextCompat.getColor(APP.getInstance(), R.color.orange_dark), startTime1, endTime1, true);

        eventList.add(event1);

        Calendar startTime2 = Calendar.getInstance();
        startTime2.add(Calendar.DAY_OF_YEAR, 1);
        Calendar endTime2 = Calendar.getInstance();
        endTime2.add(Calendar.DAY_OF_YEAR, 3);
        BaseCalendarEvent event2 = new BaseCalendarEvent("Visit to Dalvík", "A beautiful small town", "Dalvík",
                ContextCompat.getColor(APP.getInstance(), R.color.yellow), startTime2, endTime2, true);

        eventList.add(event2);

        Calendar startTime3 = Calendar.getInstance();
        Calendar endTime3 = Calendar.getInstance();
        startTime3.set(Calendar.HOUR_OF_DAY, 14);
        startTime3.set(Calendar.MINUTE, 0);
        endTime3.set(Calendar.HOUR_OF_DAY, 15);
        endTime3.set(Calendar.MINUTE, 0);
        DrawableCalendarEvent event3 = new DrawableCalendarEvent("Visit of Harpa", "", "Dalvík",
                ContextCompat.getColor(APP.getInstance(), R.color.blue_dark), startTime3, endTime3, false, android.R.drawable.ic_dialog_info);

        eventList.add(event3);
    }

    @Override
    public void onClick(View view) {
        int viewID = view.getId();
        switch (viewID) {
            case R.id.fab:
                showAddEvent();
                break;
        }
    }

    private void showAddEvent() {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.addCalendarEvent)
                .theme(Theme.LIGHT)
                .items(R.array.addCalendarEventArray)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        Log.e("============》》》》》", "你选择了添加" + text + "------->>>" + which);
                        switch (which) {
                            case 0:
                                dialog.dismiss();
                                showAddDailyCalendarEvent();
                                break;
                            case 1:
                                dialog.dismiss();
                                showAddClassEvent();
                                break;
                            case -1:
                                Toast.makeText(getActivity(), getString(R.string.todo), Toast.LENGTH_SHORT).show();
                                break;
                        }

                        return true;
                    }
                })
                .positiveText(R.string.choose)
                .show();
    }

    private void showAddDailyCalendarEvent() {
        boolean wrapInScrollView = true;
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .theme(Theme.LIGHT)
                .title(R.string.AddDailyCalendarEvent)
                .customView(R.layout.dialog_adddailyevent, wrapInScrollView)
                .positiveText(getString(R.string.save))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Log.e("============》》》》》", "你选择了onPositive");
                        View view = dialog.getCustomView();

                        EditText title = (EditText) view.findViewById(R.id.event_edt_title);
                        dialog.show();
                    }
                })
                .negativeText(getString(R.string.cancel))
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Log.e("============》》》》》", "你选择了onNegative");
                    }
                })
                .build();

        View view = dialog.getCustomView();

        final EditText startTime = (EditText) view.findViewById(R.id.event_edt_start_time);

        startTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) showTimeSelect(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        startTime.setText(timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute());
                    }
                });
            }
        });

        final EditText endTime = (EditText) view.findViewById(R.id.event_edt_end_time);
        endTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    showTimeSelect(new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int i, int i1) {
                            endTime.setText(timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute());
                        }
                    });
            }
        });

        dialog.show();
    }

    private void showAddClassEvent() {

        boolean wrapInScrollView = true;
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .theme(Theme.LIGHT)
                .title(R.string.addClssEvent)
                .customView(R.layout.dialog_add_class, wrapInScrollView)
                .positiveText(getString(R.string.save))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Log.e("============》》》》》", "你选择了onPositive");
                    }
                })
                .negativeText(getString(R.string.cancel))
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Log.e("============》》》》》", "你选择了onNegative");
                    }
                })
                .build();

        View view = dialog.getCustomView();

        final EditText startTime = (EditText) view.findViewById(R.id.event_edt_start_time);
        startTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) showTimeSelect(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        startTime.setText(timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute());
                    }
                });
            }
        });

        final EditText endTime = (EditText) view.findViewById(R.id.event_edt_end_time);
        endTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    showTimeSelect(new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int i, int i1) {
                            endTime.setText(timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute());
                        }
                    });
            }
        });

        Spinner mSpinner = (Spinner) view.findViewById(R.id.class_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.class_type, android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);

        dialog.show();

    }

    private void showTimeSelect(TimePickerDialog.OnTimeSetListener onTimeSetListener) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), onTimeSetListener, 20, 20, true);
        timePickerDialog.show();
    }


    private void saveDailyEvent(Date startDate, Date endDate, String title, boolean notification) {
        DataModel.saveDailyEvent(new DailyEvent(startDate, endDate, title, notification));
    }

    private void saveClassEvent(Date startDate, Date endDate, String className, String classNumber, String classMeetingAt, int classType, byte[] weeks) {
        DataModel.saveClassEvent(new ClassEvent(startDate, endDate, className, classNumber, classMeetingAt, classType, weeks));
    }

}
