package me.mathiasluo.page.calendar.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mathiasluo on 16-4-6.
 */
public class ClassEvent extends RealmObject {
    Date startDate;
    Date endDate;

    String className;
    @PrimaryKey
    String classNumber;
    String classMeetingAt;

    int classType;

    int one = -1;
    int two = -1;
    int thr = -1;
    int fro = -1;
    int fiv = -1;
    int six = -1;
    int sev = -1;


    public ClassEvent() {
    }

    public List<Integer> getWeeks() {

        List<Integer> weeks = new ArrayList<>();
        if (one != -1) {
            weeks.add(one);
        }
        if (two != -1) {
            weeks.add(two);
        }
        if (thr != -1) {
            weeks.add(thr);
        }
        if (fro != -1) {
            weeks.add(fro);
        }
        if (fiv != -1) {
            weeks.add(fiv);
        }
        if (six != -1) {
            weeks.add(six);
        }
        if (sev != -1) {
            weeks.add(sev);
        }
        return weeks;

    }

    public ClassEvent(Date startDate, Date endDate, String className, String classNumber, String classMeetingAt, int classType, int one, int two, int thr, int fro, int fiv, int six, int sev) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.className = className;
        this.classNumber = classNumber;
        this.classMeetingAt = classMeetingAt;
        this.classType = classType;
        this.one = one;
        this.two = two;
        this.thr = thr;
        this.fro = fro;
        this.fiv = fiv;
        this.six = six;
        this.sev = sev;
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
