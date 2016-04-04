package me.mathiasluo.model.dining;

import android.os.Parcel;
import android.os.Parcelable;

import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import java.util.Date;
import java.util.List;

import lombok.Data;

@ParcelablePlease
@Data
public class DiningPeriod implements Parcelable {

    String Id;
    String Name;
    Date EffectiveDate;
    List<DiningDay> Days;
    public static final Creator<DiningPeriod> CREATOR = new Creator<DiningPeriod>() {
        public DiningPeriod createFromParcel(Parcel source) {
            DiningPeriod target = new DiningPeriod();
            DiningPeriodParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public DiningPeriod[] newArray(int size) {
            return new DiningPeriod[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        DiningPeriodParcelablePlease.writeToParcel(this, dest, flags);
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Date getEffectiveDate() {
        return EffectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        EffectiveDate = effectiveDate;
    }

    public List<DiningDay> getDays() {
        return Days;
    }

    public void setDays(List<DiningDay> days) {
        Days = days;
    }

    public static Creator<DiningPeriod> getCREATOR() {
        return CREATOR;
    }


}
