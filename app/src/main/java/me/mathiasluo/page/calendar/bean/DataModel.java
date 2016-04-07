package me.mathiasluo.page.calendar.bean;

import android.util.Log;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by mathiasluo on 16-4-6.
 */
public class DataModel {


    public final static List<DailyEvent> getAllDailyEvents() {

        Realm realm = Realm.getDefaultInstance();
        // Build the query looking at all users:
        RealmQuery<DailyEvent> query = realm.where(DailyEvent.class);
      /*  // Add query conditions:
        query.equalTo("name", "John");
        query.or().equalTo("name", "Peter");*/
        // Execute the query:
        RealmResults<DailyEvent> result1 = query.findAll();
        if (result1.size() > 0)
            return result1.subList(0, result1.size());
        else return null;
    }


    public final static List<ClassEvent> getAllClassEvents() {
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<ClassEvent> query = realm.where(ClassEvent.class);
        RealmResults<ClassEvent> result1 = query.findAll();
        if (result1.size() > 0)
            return result1.subList(0, result1.size());
        else return null;
    }

    public final static DailyEvent saveDailyEvent(DailyEvent event) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        DailyEvent realmUser = realm.copyToRealmOrUpdate(event);
        realm.commitTransaction();
        return realmUser;
    }

    public final static ClassEvent saveClassEvent(ClassEvent event) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        ClassEvent realmUser = realm.copyToRealmOrUpdate(event);
        realm.commitTransaction();
        return realmUser;
    }

    public final static DailyEvent updateDailyEvent(DailyEvent event) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        DailyEvent realmUser = realm.copyToRealmOrUpdate(event);
        realm.commitTransaction();
        return realmUser;
    }

    public final static ClassEvent updateClassEvent(ClassEvent event) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        ClassEvent realmUser = realm.copyToRealmOrUpdate(event);
        realm.commitTransaction();
        return realmUser;
    }

    public final static void removeDailyEventByTitle(String title) {
        Realm realm = Realm.getDefaultInstance();
        // obtain the results of a query
        RealmResults<DailyEvent> results = realm.where(DailyEvent.class).endsWith("title", title).findAll();
        // All changes to data must happen in a transaction
        realm.beginTransaction();
        // Delete all matches
        results.clear();
        realm.commitTransaction();
    }


    public final static void removeClassEventByTitle(String classNumber) {
        Realm realm = Realm.getDefaultInstance();
        // obtain the results of a query
        RealmResults<ClassEvent> results = realm.where(ClassEvent.class).endsWith("classNumber", classNumber).findAll();
        // All changes to data must happen in a transaction
        realm.beginTransaction();
        // Delete all matches
        results.clear();
        realm.commitTransaction();
    }


    public final static boolean isHaveClassInTime(Date startDate,
                                                  Date endDate) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<ClassEvent> result1 = realm.where(ClassEvent.class).between("startDate", startDate, endDate).findAll();
        RealmResults<ClassEvent> result2 = realm.where(ClassEvent.class).between("endDate", startDate, endDate).findAll();
        Log.e("==============>>>>>>>>>>", result1.size() + "==========" + result2.size());
        if (result1.size() <= 0 && result2.size() <= 0) {
            return false;
        } else return true;

    }


    public final static boolean isHaveClassInTime(ClassEvent classEvent) {
        List<ClassEvent> datas = getAllClassEvents();

        if (datas == null) return false;
        for (ClassEvent mClass : datas) {
            for (int day : mClass.getWeeks()) {
                for (int mDay : classEvent.getWeeks()) {
                    if (day == mDay) {
                        Date mClassStartDate = mClass.getStartDate();
                        Date mClassEndDate = mClass.getEndDate();
                        mClassEndDate.setDate(1);
                        mClassStartDate.setDate(1);
                        Log.e("============》》》》》》day", day + "=== " + mClassStartDate.toString() + "\n" + mClassEndDate.toString());
                        Date ClassStartDate = classEvent.getStartDate();
                        Date ClassEndDate = classEvent.getEndDate();
                        ClassStartDate.setDate(1);
                        ClassEndDate.setDate(1);
                        Log.e("============》》》》》》", ClassStartDate.toString() + "\n" + ClassEndDate.toString());
                        if (mClassStartDate.after(ClassStartDate) && mClassStartDate.before(ClassEndDate)) {
                            return true;
                        }
                        if (mClassEndDate.after(ClassStartDate) && mClassEndDate.before(ClassEndDate)) {
                            return true;
                        }
                        if (ClassStartDate.after(mClassStartDate) && ClassStartDate.before(mClassEndDate)) {
                            return true;
                        }
                        if (ClassEndDate.after(mClassStartDate) && ClassEndDate.before(mClassEndDate)) {
                            return true;
                        }

                    }

                }
            }

        }
        return false;
    }


}
