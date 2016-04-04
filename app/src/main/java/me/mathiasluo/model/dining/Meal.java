package me.mathiasluo.model.dining;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import java.util.List;

import lombok.Data;
import me.mathiasluo.model.Hours;

@ParcelablePlease
@Data
public class Meal implements Parcelable, Comparable<Meal> {

    String Name;
    Integer Order;
    String Status;
    Hours Hours;

    @Nullable
    List<Station> Stations;
    public static final Creator<Meal> CREATOR = new Creator<Meal>() {
        public Meal createFromParcel(Parcel source) {
            Meal target = new Meal();
            MealParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        MealParcelablePlease.writeToParcel(this, dest, flags);
    }

    @Override
    public int compareTo(Meal meal) {
        return getOrder().compareTo(meal.getOrder());
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getOrder() {
        return Order;
    }

    public void setOrder(Integer order) {
        Order = order;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public me.mathiasluo.model.Hours getHours() {
        return Hours;
    }

    public void setHours(me.mathiasluo.model.Hours hours) {
        Hours = hours;
    }

    @Nullable
    public List<Station> getStations() {
        return Stations;
    }

    public void setStations(@Nullable List<Station> stations) {
        Stations = stations;
    }

    public static Creator<Meal> getCREATOR() {
        return CREATOR;
    }
}
