package me.mathiasluo.model.retail;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import java.util.List;

import lombok.Data;

@Data
@ParcelablePlease
public class RetailNormalHours implements Parcelable {

    List<RetailDay> Days;

    @Nullable
    RetailPeriod Period;
    public static final Creator<RetailNormalHours> CREATOR = new Creator<RetailNormalHours>() {
        public RetailNormalHours createFromParcel(Parcel source) {
            RetailNormalHours target = new RetailNormalHours();
            RetailNormalHoursParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public RetailNormalHours[] newArray(int size) {
            return new RetailNormalHours[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        RetailNormalHoursParcelablePlease.writeToParcel(this, dest, flags);
    }

    public List<RetailDay> getDays() {
        return Days;
    }

    public void setDays(List<RetailDay> days) {
        Days = days;
    }

    @Nullable
    public RetailPeriod getPeriod() {
        return Period;
    }

    public void setPeriod(@Nullable RetailPeriod period) {
        Period = period;
    }

    public static Creator<RetailNormalHours> getCREATOR() {
        return CREATOR;
    }
}
