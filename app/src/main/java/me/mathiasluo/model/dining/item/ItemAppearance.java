package me.mathiasluo.model.dining.item;

import android.os.Parcel;
import android.os.Parcelable;

import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import java.util.Date;

import lombok.Data;

@ParcelablePlease
@Data
public class ItemAppearance implements Parcelable {

    Date Date;
    String Location;
    String Meal;
    String Station;
    public static final Creator<ItemAppearance> CREATOR = new Creator<ItemAppearance>() {
        public ItemAppearance createFromParcel(Parcel source) {
            ItemAppearance target = new ItemAppearance();
            ItemAppearanceParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public ItemAppearance[] newArray(int size) {
            return new ItemAppearance[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ItemAppearanceParcelablePlease.writeToParcel(this, dest, flags);
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getMeal() {
        return Meal;
    }

    public void setMeal(String meal) {
        Meal = meal;
    }

    public String getStation() {
        return Station;
    }

    public void setStation(String station) {
        Station = station;
    }

    public static Creator<ItemAppearance> getCREATOR() {
        return CREATOR;
    }
}
