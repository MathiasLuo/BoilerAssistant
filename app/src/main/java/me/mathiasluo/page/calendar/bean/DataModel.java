package me.mathiasluo.page.calendar.bean;

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
        return result1.subList(0, result1.size() - 1);
    }


    public final static List<ClassEvent> getAllClassEvents() {
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<ClassEvent> query = realm.where(ClassEvent.class);
        RealmResults<ClassEvent> result1 = query.findAll();
        return result1.subList(0, result1.size() - 1);
    }

    public final static DailyEvent saveDailyEvent(DailyEvent event) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        DailyEvent realmUser = realm.copyToRealm(event);
        realm.commitTransaction();
        return realmUser;
    }

    public final static ClassEvent saveClassEvent(ClassEvent event) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        ClassEvent realmUser = realm.copyToRealm(event);
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


}
