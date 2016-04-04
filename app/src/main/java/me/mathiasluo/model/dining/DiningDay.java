package me.mathiasluo.model.dining;

import android.os.Parcel;
import android.os.Parcelable;

import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import me.mathiasluo.model.Day;
import me.mathiasluo.model.Hours;

@ParcelablePlease
@Data
public class DiningDay implements Day, Parcelable {

    String Name;
    Integer DayOfWeek;
    List<Meal> Meals;

    public static final Creator<DiningDay> CREATOR = new Creator<DiningDay>() {
        public DiningDay createFromParcel(Parcel source) {
            DiningDay target = new DiningDay();
            DiningDayParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public DiningDay[] newArray(int size) {
            return new DiningDay[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        DiningDayParcelablePlease.writeToParcel(this, dest, flags);
    }

    @Override
    public List<Hours> getHours() {
        List<Hours> hours = new ArrayList<>();
        for (Meal m : getMeals()) {
            hours.add(m.getHours());
        }
        return hours;
    }

    public List<Meal> getOpenMeals() {
        List<Meal> result = new ArrayList<>();
        for (Meal m : getMeals()) {
            if (m.getStations() != null && m.getStations().size() > 0) {
                result.add(m);
            }
        }
        return result;
    }

    @Override
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public Integer getDayOfWeek() {
        return DayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        DayOfWeek = dayOfWeek;
    }

    public List<Meal> getMeals() {
        return Meals;
    }

    public void setMeals(List<Meal> meals) {
        Meals = meals;
    }

    public static Creator<DiningDay> getCREATOR() {
        return CREATOR;
    }
}
