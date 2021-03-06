package me.mathiasluo.model.dining;

import android.os.Parcel;
import android.os.Parcelable;

import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import java.util.List;

import lombok.Data;
import me.mathiasluo.model.dining.item.Item;

@ParcelablePlease
@Data
public class Station implements Parcelable {

    String Name;
    List<Item> Items;
    public static final Creator<Station> CREATOR = new Creator<Station>() {
        public Station createFromParcel(Parcel source) {
            Station target = new Station();
            StationParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public Station[] newArray(int size) {
            return new Station[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        StationParcelablePlease.writeToParcel(this, dest, flags);
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public List<Item> getItems() {
        return Items;
    }

    public void setItems(List<Item> items) {
        Items = items;
    }

    public static Creator<Station> getCREATOR() {
        return CREATOR;
    }
}
