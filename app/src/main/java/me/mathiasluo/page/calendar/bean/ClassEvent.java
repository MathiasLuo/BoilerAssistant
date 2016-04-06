package me.mathiasluo.page.calendar.bean;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by mathiasluo on 16-4-6.
 */
public class ClassEvent extends RealmObject {
    Date startDate;
    Date endDate;

    String className;
    String classNumber;

    String classMeetingAt;
    int classType;
    byte[] weeks = new byte[7];



    public ClassEvent() {
    }

    public ClassEvent(Date startDate, Date endDate, String className, String classNumber, String classMeetingAt, int classType, byte[] weeks) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.className = className;
        this.classNumber = classNumber;
        this.classMeetingAt = classMeetingAt;
        this.classType = classType;
        this.weeks = weeks;
    }

    public byte[] getWeeks() {
        return weeks;
    }

    public void setWeeks(byte[] weeks) {
        this.weeks = weeks;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(String classNumber) {
        this.classNumber = classNumber;
    }

    public String getClassMeetingAt() {
        return classMeetingAt;
    }

    public void setClassMeetingAt(String classMeetingAt) {
        this.classMeetingAt = classMeetingAt;
    }

    public int getClassType() {
        return classType;
    }

    public void setClassType(int classType) {
        this.classType = classType;
    }


}
