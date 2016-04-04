package me.mathiasluo.model.dining.item;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import java.util.List;

import lombok.Data;

@ParcelablePlease
@Data
public class Item implements Parcelable {

    String ID;
    String Name;
    Boolean IsVegetarian;

    @Nullable
    String Ingredients;

    @Nullable
    List<Allergen> Allergens;

    @Nullable
    ItemSchedule ItemSchedule;

    @Nullable
    List<NutritionFact> Nutrition;
    public static final Creator<Item> CREATOR = new Creator<Item>() {
        public Item createFromParcel(Parcel source) {
            Item target = new Item();
            ItemParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ItemParcelablePlease.writeToParcel(this, dest, flags);
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Boolean getIsVegetarian() {
        return IsVegetarian;
    }

    public void setIsVegetarian(Boolean vegetarian) {
        IsVegetarian = vegetarian;
    }

    @Nullable
    public String getIngredients() {
        return Ingredients;
    }

    public void setIngredients(@Nullable String ingredients) {
        Ingredients = ingredients;
    }

    @Nullable
    public List<Allergen> getAllergens() {
        return Allergens;
    }

    public void setAllergens(@Nullable List<Allergen> allergens) {
        Allergens = allergens;
    }

    @Nullable
    public me.mathiasluo.model.dining.item.ItemSchedule getItemSchedule() {
        return ItemSchedule;
    }

    public void setItemSchedule(@Nullable me.mathiasluo.model.dining.item.ItemSchedule itemSchedule) {
        ItemSchedule = itemSchedule;
    }

    @Nullable
    public List<NutritionFact> getNutrition() {
        return Nutrition;
    }

    public void setNutrition(@Nullable List<NutritionFact> nutrition) {
        Nutrition = nutrition;
    }


}
