package me.mathiasluo.model.retail;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import lombok.Data;
import me.mathiasluo.model.Address;
import me.mathiasluo.model.Hours;
import me.mathiasluo.model.Location;
import me.mathiasluo.service.DiningServiceHelper;

@Data
@ParcelablePlease
public class RetailLocation implements Location, Parcelable {

    String Name;
    Address Address;
    String PhoneNumber;
    double Latitude;
    double Longitude;
    RetailNormalHours NormalHours;
    String Logo;
    String Banner;
    String Type;
    String Description;
    String DescriptionUrl;

    @Nullable
    String MenuUrl;
    public static final Creator<RetailLocation> CREATOR = new Creator<RetailLocation>() {
        public RetailLocation createFromParcel(Parcel source) {
            RetailLocation target = new RetailLocation();
            RetailLocationParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public RetailLocation[] newArray(int size) {
            return new RetailLocation[size];
        }
    };

    @Override
    public String getFullName() {
        return getName();
    }

    @Override
    public String getTileImage() {
        return getBanner();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        RetailLocationParcelablePlease.writeToParcel(this, dest, flags);
    }

    @Override
    public String getTimings(boolean twentyFourHourFormat) {
        RetailDay day = getToday();

        if (day == null)
            return null;

        StringBuilder str = new StringBuilder();

        boolean first = true;
        if (day.getHours() != null) {
            for (Hours h : day.getHours()) {
                if (!first)
                    str.append("<br/>");
                first = false;
                str.append(String.format("<i>%s</i>", h.toSimpleString(twentyFourHourFormat)));
            }
        }
        String result = str.toString();
        return (result.length() > 0 ? result : null);
    }

    @Nullable
    @Override
    public RetailDay getToday() {
        return DiningServiceHelper.getToday(getNormalHours().getDays());
    }

    @Override
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public me.mathiasluo.model.Address getAddress() {
        return Address;
    }

    public void setAddress(me.mathiasluo.model.Address address) {
        Address = address;
    }

    @Override
    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    @Override
    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    @Override
    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public RetailNormalHours getNormalHours() {
        return NormalHours;
    }

    public void setNormalHours(RetailNormalHours normalHours) {
        NormalHours = normalHours;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public String getBanner() {
        return Banner;
    }

    public void setBanner(String banner) {
        Banner = banner;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDescriptionUrl() {
        return DescriptionUrl;
    }

    public void setDescriptionUrl(String descriptionUrl) {
        DescriptionUrl = descriptionUrl;
    }

    @Nullable
    public String getMenuUrl() {
        return MenuUrl;
    }

    public void setMenuUrl(@Nullable String menuUrl) {
        MenuUrl = menuUrl;
    }

    public static Creator<RetailLocation> getCREATOR() {
        return CREATOR;
    }
}
