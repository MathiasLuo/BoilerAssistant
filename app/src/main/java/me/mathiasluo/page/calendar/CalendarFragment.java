package me.mathiasluo.page.calendar;

import android.app.TimePickerDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.InjectView;
import me.mathiasluo.APP;
import me.mathiasluo.R;
import me.mathiasluo.activity.MainActivity;
import me.mathiasluo.base.MainFragment;
import me.mathiasluo.page.calendar.bean.ClassEvent;
import me.mathiasluo.page.calendar.bean.DailyEvent;
import me.mathiasluo.page.calendar.bean.DataModel;
import me.mathiasluo.utils.InputMananger;
import me.mathiasluo.widget.AgendaCalendarView;
import me.mathiasluo.widget.WeekPickLayout;

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

    int[] colors = {
            R.color.orange_dark, R.color.blue_dark, R.color.yellow, R.color.primary, R.color.colorAccent
    };


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

        initAgendaCalendarView();

    }

    private void initAgendaCalendarView() {

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
        // mAgendaCalendarView.addEventRenderer(new DrawableEventRenderer());
        mAgendaCalendarView.addEventRenderer(new DailyEventRenderer());
        mAgendaCalendarView.addEventRenderer(new ClassEventRenderer());

    }

    @Override
    public void onDaySelected(DayItem dayItem) {
        Log.d(LOG_TAG, String.format("Selected day: %s", dayItem));
    }

    @Override
    public void onEventSelected(CalendarEvent event) {
        Log.d(LOG_TAG, String.format("Selected event: %s", event));

        Log.e("=============》》》》》", event.getId() + "");
        if (event.getId() == 11) {
            Log.e("================>>>>>>", ((DailyCalendarEvent) event).getmDailyEvent().getTitle());
            showUpdateDailyEvent((DailyCalendarEvent) event);
        } else if (event.getId() == 22) {
            showUpdateClassEvent((ClassCalendarEvent) event);
        }

    }

    @Override
    public void onScrollToDate(Calendar calendar) {
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        }
    }

    private void mockList(List<CalendarEvent> eventList) {
       /* Calendar startTime1 = Calendar.getInstance();
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

        eventList.add(event3);*/

        List<ClassEvent> classEvents = DataModel.getAllClassEvents();
        addClassEventToCalendarEvent(classEvents, eventList);
        List<DailyEvent> dailyEvents = DataModel.getAllDailyEvents();
        addDailyEventToCalendarEvent(dailyEvents, eventList);
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
        final MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .theme(Theme.LIGHT)
                .title(R.string.AddDailyCalendarEvent)
                .customView(R.layout.dialog_adddailyevent, wrapInScrollView)
                .build();

        View view = dialog.getCustomView();

        final EditText startTime = (EditText) view.findViewById(R.id.event_edt_start_time);
        startTime.setOnFocusChangeListener((view1, b) -> {
            if (b)
                showTimeSelect((timePicker, i, i1) -> setTimeToEdit(startTime, timePicker));
        });

        final EditText endTime = (EditText) view.findViewById(R.id.event_edt_end_time);
        endTime.setOnFocusChangeListener((view1, b) -> {
            if (b)
                showTimeSelect((timePicker, i, i1) -> setTimeToEdit(endTime, timePicker));
        });

        EditText titleEdit = (EditText) view.findViewById(R.id.event_edt_title);
        CheckBox mCheckBox = (CheckBox) view.findViewById(R.id.notification_event_check);

        TextView saveButton = (TextView) view.findViewById(R.id.save_btn);
        saveButton.setTextColor(APP.getInstance().getResources().getColor(R.color.orange_dark));
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!titleEdit.getText().toString().equals("") && !startTime.getText().toString().equals("") && !endTime.getText().toString().equals("")) {

                    Date startDate = getCurrentDayItem();
                    String[] start_times = getTime(startTime.getText().toString());
                    startDate.setHours(Integer.parseInt(start_times[0]));
                    startDate.setMinutes(Integer.parseInt(start_times[1]));

                    Date endDate = getCurrentDayItem();
                    String[] end_times = getTime(endTime.getText().toString());
                    endDate.setHours(Integer.parseInt(end_times[0]));
                    endDate.setMinutes(Integer.parseInt(end_times[1]));

                    String title = titleEdit.getText().toString();
                    boolean isNotifytion = mCheckBox.isChecked();

                    saveDailyEvent(startDate, endDate, title, isNotifytion);
                    dialog.dismiss();
                    Toast.makeText(getActivity(), getString(R.string.save_sucess), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.no_edit), Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView cancelButton = (TextView) view.findViewById(R.id.cancel_btn);
        cancelButton.setTextColor(APP.getInstance().getResources().getColor(R.color.orange_dark));
        cancelButton.setOnClickListener(view1 -> dialog.dismiss());

        dialog.show();
    }

    private void showAddClassEvent() {

        boolean wrapInScrollView = true;
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .theme(Theme.LIGHT)
                .title(R.string.addClssEvent)
                .customView(R.layout.dialog_add_class, wrapInScrollView)
                .build();

        View view = dialog.getCustomView();
        WeekPickLayout weekPickLayout = (WeekPickLayout) view.findViewById(R.id.weekPickLayout);
        EditText editClassName = (EditText) view.findViewById(R.id.class_edt_classname);
        EditText editClassNumber = (EditText) view.findViewById(R.id.class_edt_number);
        EditText editClassMeeting = (EditText) view.findViewById(R.id.class_edt_meeting_at);

        Spinner mSpinner = (Spinner) view.findViewById(R.id.class_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.class_type, android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);


        final EditText startTime = (EditText) view.findViewById(R.id.event_edt_start_time);
        startTime.setOnFocusChangeListener((view1, b) -> {
            if (b) {
                showTimeSelect((timePicker, i, i1) -> setTimeToEdit(startTime, timePicker));
            }
        });

        final EditText endTime = (EditText) view.findViewById(R.id.event_edt_end_time);
        endTime.setOnFocusChangeListener((view1, b) -> {
            if (b) {
                showTimeSelect((timePicker, i, i1) -> setTimeToEdit(endTime, timePicker));
            }
        });


        TextView saveButton = (TextView) view.findViewById(R.id.save_btn);
        saveButton.setTextColor(APP.getInstance().getResources().getColor(R.color.orange_dark));
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editClassName.getText().toString().equals("")
                        && !editClassNumber.getText().toString().equals("")
                        && !editClassMeeting.getText().toString().equals("")
                        && !startTime.getText().toString().equals("")
                        && !endTime.getText().toString().equals("")) {

                    Date startDate = getCurrentDayItem();
                    String[] start_times = getTime(startTime.getText().toString());
                    startDate.setHours(Integer.parseInt(start_times[0]));
                    startDate.setMinutes(Integer.parseInt(start_times[1]));

                    Date endDate = getCurrentDayItem();
                    String[] end_times = getTime(endTime.getText().toString());
                    endDate.setHours(Integer.parseInt(end_times[0]));
                    endDate.setMinutes(Integer.parseInt(end_times[1]));

                    String className = editClassName.getText().toString();
                    String classNumber = editClassNumber.getText().toString();
                    String classMeeting = editClassMeeting.getText().toString();

                    String[] weeks = APP.getInstance().getResources().getStringArray(R.array.weeks);
                    String[] selectWeeks = getWeeks(weekPickLayout.getValues());

                    int one = -1;
                    int two = -1;
                    int thr = -1;
                    int fro = -1;
                    int fiv = -1;
                    int six = -1;
                    int sev = -1;

                    for (int i = 0; i < selectWeeks.length; i++) {
                        if (selectWeeks[i].equals(weeks[0])) {
                            sev = 1;
                        } else if (selectWeeks[i].equals(weeks[1])) {
                            one = 2;
                        } else if (selectWeeks[i].equals(weeks[2])) {
                            two = 3;
                        } else if (selectWeeks[i].equals(weeks[3])) {
                            thr = 4;
                        } else if (selectWeeks[i].equals(weeks[4])) {
                            fro = 5;
                        } else if (selectWeeks[i].equals(weeks[5])) {
                            fiv = 6;
                        } else if (selectWeeks[i].equals(weeks[6])) {
                            six = 7;
                        }
                    }

                    int classType = mSpinner.getSelectedItemPosition();

                    ClassEvent currentEvent = new ClassEvent(startDate, endDate, className, classNumber, classMeeting, classType,
                            one, two, thr, fro, fiv, six, sev);

                    if (DataModel.isHaveClassInTime(currentEvent)) {
                        Log.e("================>>>>>>>>>>", "已经在里面了哟");
                    } else {
                        saveClassEvent(startDate, endDate, className, classNumber, classMeeting, classType, one, two, thr, fro, fiv, six, sev);
                        dialog.dismiss();
                        Toast.makeText(getActivity(), getString(R.string.save_sucess), Toast.LENGTH_SHORT).show();
                        Log.e("==============》》》》》》》》》》》", DataModel.getAllClassEvents().toString());

                    }


                } else {
                    Toast.makeText(getActivity(), getString(R.string.no_edit), Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView cancelButton = (TextView) view.findViewById(R.id.cancel_btn);
        cancelButton.setTextColor(APP.getInstance().getResources().getColor(R.color.orange_dark));
        cancelButton.setOnClickListener(view1 -> dialog.dismiss());
        dialog.show();

    }

    private void showTimeSelect(TimePickerDialog.OnTimeSetListener onTimeSetListener) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), onTimeSetListener, 20, 20, true);
        timePickerDialog.show();
    }

    private void saveDailyEvent(Date startDate, Date endDate, String title, boolean notification) {
        DataModel.saveDailyEvent(new DailyEvent(startDate, endDate, title, notification));
        initAgendaCalendarView();
    }

    private void saveDailyEventAndDelectOld(Date startDate, Date endDate, String oldTitle, String title, boolean notification) {
        DataModel.removeDailyEventByTitle(oldTitle);
        DataModel.saveDailyEvent(new DailyEvent(startDate, endDate, title, notification));
        initAgendaCalendarView();
    }


    private void saveClassEvent(Date startDate, Date endDate, String className, String classNumber,
                                String classMeetingAt, int classType, int one, int two, int thr, int fro, int fiv, int six, int sev) {
        DataModel.saveClassEvent(new ClassEvent(startDate, endDate, className, classNumber, classMeetingAt, classType, one, two, thr, fro, fiv, six, sev));
        initAgendaCalendarView();
    }

    private void saveClassEventAndDelectOld(String oldClassNumber, Date startDate, Date endDate, String className, String classNumber, String classMeetingAt, int classType, int one, int two, int thr, int fro, int fiv, int six, int sev) {
        DataModel.removeClassEventByTitle(oldClassNumber);
        DataModel.saveClassEvent(new ClassEvent(startDate, endDate, className, classNumber, classMeetingAt, classType, one, two, thr, fro, fiv, six, sev));
        initAgendaCalendarView();
    }


    private Date getCurrentDayItem() {
        DayItem dayItem = mAgendaCalendarView.getmCalendarView().getSelectedDay();
        Date date = dayItem.getDate();
        return (Date) date.clone();
    }


    private void setTimeToEdit(EditText edit, TimePicker timePicker) {
        InputMananger.closeInputMethod(getActivity(), edit);
        edit.setText((timePicker.getCurrentHour() < 9 ? "0" + timePicker.getCurrentHour() : timePicker.getCurrentHour()) + ":" + (timePicker.getCurrentMinute() < 9 ? "0" + timePicker.getCurrentMinute() : timePicker.getCurrentMinute()));
    }

    private String[] getTime(String time) {
        String[] times = time.split(":");
        return times;
    }

    private String[] getWeeks(String weeks) {
        String[] times = weeks.split("、");
        return times;
    }


    private void addClassEventToCalendarEvent(List<ClassEvent> datas, List<CalendarEvent> calendarEvents) {
        if (datas == null) return;
        for (ClassEvent classEvent : datas) {
            for (Integer day : classEvent.getWeeks()) {
                List<Calendar> listCalendars = getCa(day);
                for (Calendar calendar : listCalendars) {
                    Calendar startTime = (Calendar) calendar.clone();
                    startTime.set(Calendar.HOUR_OF_DAY, classEvent.getStartDate().getHours());
                    startTime.set(Calendar.MINUTE, classEvent.getStartDate().getMinutes());

                    Calendar endTime = (Calendar) calendar.clone();
                    endTime.set(Calendar.HOUR_OF_DAY, classEvent.getEndDate().getHours());
                    endTime.set(Calendar.MINUTE, classEvent.getEndDate().getMinutes());
                    ClassCalendarEvent mClassCalendarEvent = new ClassCalendarEvent(
                            classEvent.getClassName(),
                            classEvent.getClassNumber(),
                            classEvent.getClassMeetingAt(),
                            ContextCompat.getColor(APP.getInstance(), colors[new Random().nextInt(5)]),
                            startTime,
                            endTime,
                            false,
                            classEvent);

                    mClassCalendarEvent.setId(22);
                    calendarEvents.add(mClassCalendarEvent);

                }
/*
                Calendar startTime1 = Calendar.getInstance();
                // startTime1.setTime(classEvent.getStartDate());
                Calendar endTime1 = Calendar.getInstance();
                endTime1.add(Calendar.DAY_OF_WEEK, day);
                //endTime1.setTime(classEvent.getEndDate());
                ClassCalendarEvent mClassCalendarEvent = new ClassCalendarEvent(
                        classEvent.getClassName(),
                        classEvent.getClassNumber(),
                        classEvent.getClassMeetingAt(),
                        ContextCompat.getColor(APP.getInstance(), R.color.blue_dark),
                        startTime1,
                        endTime1,
                        false,
                        classEvent);
                mClassCalendarEvent.setId(22);
                calendarEvents.add(mClassCalendarEvent);*/
            }

        }
    }

    private void addDailyEventToCalendarEvent(List<DailyEvent> datas, List<CalendarEvent> calendarEvents) {
        if (datas == null) return;
        for (DailyEvent dailyEvent : datas) {
            Calendar startTime = Calendar.getInstance();
            Calendar endTime = Calendar.getInstance();
            startTime.setTime(dailyEvent.getStartDate());
            endTime.setTime(dailyEvent.getEndDate());

            DailyCalendarEvent mDailyCalendarEvent =
                    new DailyCalendarEvent(dailyEvent.getTitle(),
                            dailyEvent.getTitle(),
                            dailyEvent.getTitle(),
                            ContextCompat.getColor(APP.getInstance(), colors[new Random().nextInt(5)]),
                            startTime, endTime, false, dailyEvent);
            mDailyCalendarEvent.setId(11);
            calendarEvents.add(mDailyCalendarEvent);
        }
    }


    private void showUpdateDailyEvent(DailyCalendarEvent event) {

        String oldTitle = event.getTitle();
        boolean wrapInScrollView = true;
        final MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .theme(Theme.LIGHT)
                .title(R.string.AddDailyCalendarEvent)
                .customView(R.layout.dialog_update_dailyevent, wrapInScrollView)
                .build();
        View view = dialog.getCustomView();

        final EditText startTime = (EditText) view.findViewById(R.id.event_edt_start_time);
        startTime.setOnFocusChangeListener((view1, b) -> {
            if (b)
                showTimeSelect((timePicker, i, i1) -> setTimeToEdit(startTime, timePicker));
        });

        final EditText endTime = (EditText) view.findViewById(R.id.event_edt_end_time);
        endTime.setOnFocusChangeListener((view1, b) -> {
            if (b)
                showTimeSelect((timePicker, i, i1) -> setTimeToEdit(endTime, timePicker));
        });

        EditText titleEdit = (EditText) view.findViewById(R.id.event_edt_title);
        CheckBox mCheckBox = (CheckBox) view.findViewById(R.id.notification_event_check);

        TextView saveButton = (TextView) view.findViewById(R.id.save_btn);
        saveButton.setTextColor(APP.getInstance().getResources().getColor(R.color.orange_dark));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!titleEdit.getText().toString().equals("") && !startTime.getText().toString().equals("") && !endTime.getText().toString().equals("")) {

                    Date startDate = getCurrentDayItem();
                    String[] start_times = getTime(startTime.getText().toString());
                    startDate.setHours(Integer.parseInt(start_times[0]));
                    startDate.setMinutes(Integer.parseInt(start_times[1]));

                    Date endDate = getCurrentDayItem();
                    String[] end_times = getTime(endTime.getText().toString());
                    endDate.setHours(Integer.parseInt(end_times[0]));
                    endDate.setMinutes(Integer.parseInt(end_times[1]));

                    String title = titleEdit.getText().toString();
                    boolean isNotifytion = mCheckBox.isChecked();

                    saveDailyEventAndDelectOld(startDate, endDate, oldTitle, title, isNotifytion);

                    dialog.dismiss();
                    Toast.makeText(getActivity(), getString(R.string.save_sucess), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.no_edit), Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView removeBtn = (TextView) view.findViewById(R.id.delect_btn);
        removeBtn.setTextColor(APP.getInstance().getResources().getColor(R.color.orange_dark));
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataModel.removeDailyEventByTitle(oldTitle);
                initAgendaCalendarView();
                dialog.dismiss();
            }
        });
        TextView cancelButton = (TextView) view.findViewById(R.id.cancel_btn);
        cancelButton.setTextColor(APP.getInstance().getResources().getColor(R.color.orange_dark));
        cancelButton.setOnClickListener(view1 -> dialog.dismiss());

        titleEdit.setText(event.getTitle());

        startTime.setText(event.getStartTime().getTime().getHours() + ":" + event.getStartTime().getTime().getMinutes());
        endTime.setText(event.getEndTime().getTime().getHours() + ":" + event.getEndTime().getTime().getMinutes());
        mCheckBox.setChecked(event.getmDailyEvent().isNotification());
        dialog.show();

    }

    private void showUpdateClassEvent(ClassCalendarEvent event) {

        String oldClassNumeber = event.getmClassEvent().getClassNumber();
        ClassEvent oldClassEvent = event.getmClassEvent();

        boolean wrapInScrollView = true;
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .theme(Theme.LIGHT)
                .title(R.string.addClssEvent)
                .customView(R.layout.dialog_update_classevent, wrapInScrollView)
                .build();

        View view = dialog.getCustomView();
        WeekPickLayout weekPickLayout = (WeekPickLayout) view.findViewById(R.id.weekPickLayout);
        EditText editClassName = (EditText) view.findViewById(R.id.class_edt_classname);
        EditText editClassNumber = (EditText) view.findViewById(R.id.class_edt_number);
        EditText editClassMeeting = (EditText) view.findViewById(R.id.class_edt_meeting_at);

        //默认值
        editClassName.setText(oldClassEvent.getClassName());
        editClassNumber.setText(oldClassEvent.getClassNumber());
        editClassMeeting.setText(oldClassEvent.getClassMeetingAt());

        String values = "";
        for (int i : oldClassEvent.getWeeks()) {
            values += APP.getInstance().getResources().getStringArray(R.array.weeks)[i - 1] + "、";
        }
        weekPickLayout.setVaules(values);


        Spinner mSpinner = (Spinner) view.findViewById(R.id.class_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.class_type, android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);
        //默认值
        mSpinner.setSelection(oldClassEvent.getClassType());


        final EditText startTime = (EditText) view.findViewById(R.id.event_edt_start_time);
        startTime.setOnFocusChangeListener((view1, b) -> {
            if (b) {
                showTimeSelect((timePicker, i, i1) -> setTimeToEdit(startTime, timePicker));
            }
        });

        final EditText endTime = (EditText) view.findViewById(R.id.event_edt_end_time);
        endTime.setOnFocusChangeListener((view1, b) -> {
            if (b) {
                showTimeSelect((timePicker, i, i1) -> setTimeToEdit(endTime, timePicker));
            }
        });


        TextView saveButton = (TextView) view.findViewById(R.id.save_btn);
        saveButton.setTextColor(APP.getInstance().getResources().getColor(R.color.orange_dark));
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editClassName.getText().toString().equals("")
                        && !editClassNumber.getText().toString().equals("")
                        && !editClassMeeting.getText().toString().equals("")
                        && !startTime.getText().toString().equals("")
                        && !endTime.getText().toString().equals("")) {

                    Date startDate = getCurrentDayItem();
                    String[] start_times = getTime(startTime.getText().toString());
                    startDate.setHours(Integer.parseInt(start_times[0]));
                    startDate.setMinutes(Integer.parseInt(start_times[1]));

                    Date endDate = getCurrentDayItem();
                    String[] end_times = getTime(endTime.getText().toString());
                    endDate.setHours(Integer.parseInt(end_times[0]));
                    endDate.setMinutes(Integer.parseInt(end_times[1]));

                    String className = editClassName.getText().toString();
                    String classNumber = editClassNumber.getText().toString();
                    String classMeeting = editClassMeeting.getText().toString();

                    String[] weeks = APP.getInstance().getResources().getStringArray(R.array.weeks);
                    String[] selectWeeks = getWeeks(weekPickLayout.getValues());

                    int one = -1;
                    int two = -1;
                    int thr = -1;
                    int fro = -1;
                    int fiv = -1;
                    int six = -1;
                    int sev = -1;

                    for (int i = 0; i < selectWeeks.length; i++) {
                        if (selectWeeks[i].equals(weeks[0])) {
                            sev = 7;
                        } else if (selectWeeks[i].equals(weeks[1])) {
                            one = 1;
                        } else if (selectWeeks[i].equals(weeks[2])) {
                            two = 2;
                        } else if (selectWeeks[i].equals(weeks[3])) {
                            thr = 3;
                        } else if (selectWeeks[i].equals(weeks[4])) {
                            fro = 4;
                        } else if (selectWeeks[i].equals(weeks[5])) {
                            fiv = 5;
                        } else if (selectWeeks[i].equals(weeks[6])) {
                            six = 6;
                        }
                    }

                    int classType = mSpinner.getSelectedItemPosition();
                    saveClassEvent(startDate, endDate, className, classNumber, classMeeting, classType, one, two, thr, fro, fiv, six, sev);
                    dialog.dismiss();
                    Toast.makeText(getActivity(), getString(R.string.save_sucess), Toast.LENGTH_SHORT).show();
                    Log.e("==============》》》》》》》》》》》", DataModel.getAllClassEvents().toString());
                } else {
                    Toast.makeText(getActivity(), getString(R.string.no_edit), Toast.LENGTH_SHORT).show();
                }
            }
        });
        TextView cancelButton = (TextView) view.findViewById(R.id.cancel_btn);
        cancelButton.setTextColor(APP.getInstance().getResources().getColor(R.color.orange_dark));
        cancelButton.setOnClickListener(view1 -> dialog.dismiss());
        TextView removeBtn = (TextView) view.findViewById(R.id.delect_btn);
        removeBtn.setTextColor(APP.getInstance().getResources().getColor(R.color.orange_dark));
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataModel.removeClassEventByTitle(oldClassNumeber);
                initAgendaCalendarView();
                dialog.dismiss();
            }
        });

        startTime.setText(event.getStartTime().getTime().getHours() + ":" + event.getStartTime().getTime().getMinutes());
        endTime.setText(event.getEndTime().getTime().getHours() + ":" + event.getEndTime().getTime().getMinutes());

        dialog.show();


    }

    private List<Calendar> getCa(int Week) {

        List<Calendar> calendars = new ArrayList<>();
        Calendar c_begin = new GregorianCalendar();
        Calendar c_end = new GregorianCalendar();
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] weeks = dfs.getWeekdays();

        c_begin.set(2016, 1, 1); //Calendar的月从0-11，所以4月是3.
        c_end.set(2016, 6, 30); //Calendar的月从0-11，所以5月是4.

        int count = 1;
        c_end.add(Calendar.DAY_OF_YEAR, 1);  //结束日期下滚一天是为了包含最后一天

        while (c_begin.before(c_end)) {
            if (c_begin.get(Calendar.DAY_OF_WEEK) == Week) {
                count++;
                Calendar calendar = (Calendar) c_begin.clone();
                calendars.add(calendar);
            }
            c_begin.add(Calendar.DAY_OF_YEAR, 1);
        }
        return calendars;
    }

    private List<Calendar> getAllCalendars() {
        List<ClassEvent> classEvents = DataModel.getAllClassEvents();
        List<Calendar> listCalendars = null;
        if (classEvents == null) return null;
        for (ClassEvent classEvent : classEvents) {
            for (Integer day : classEvent.getWeeks()) {
                listCalendars.addAll(getCa(day));
            }
        }
        return listCalendars;
    }

}
