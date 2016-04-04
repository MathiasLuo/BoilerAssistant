package me.mathiasluo.model.dining.item;

import android.os.Parcel;
import android.os.Parcelable;

import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import lombok.Data;

@ParcelablePlease
@Data
public class Allergen implements Parcelable {

    String Name;
    Boolean Value;
    public static final Creator<Allergen> CREATOR = new Creator<Allergen>() {
        public Allergen createFromParcel(Parcel source) {
            Allergen target = new Allergen();
            AllergenParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public Allergen[] newArray(int size) {
            return new Allergen[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        AllergenParcelablePlease.writeToParcel(this, dest, flags);
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Boolean getValue() {
        return Value;
    }

    public void setValue(Boolean value) {
        Value = value;
    }

    public static Creator<Allergen> getCREATOR() {
        return CREATOR;
    }
}
