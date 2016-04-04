package me.mathiasluo.model.dining;

import android.os.Parcel;
import android.os.Parcelable;

import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

import java.util.List;

import lombok.Data;
import me.mathiasluo.model.dining.item.Item;

@ParcelablePlease
@Data
public class SearchResults implements Parcelable {

    List<Item> Results;
    public static final Creator<SearchResults> CREATOR = new Creator<SearchResults>() {
        public SearchResults createFromParcel(Parcel source) {
            SearchResults target = new SearchResults();
            SearchResultsParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public SearchResults[] newArray(int size) {
            return new SearchResults[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        SearchResultsParcelablePlease.writeToParcel(this, dest, flags);
    }

    public List<Item> getResults() {
        return Results;
    }

    public void setResults(List<Item> results) {
        Results = results;
    }

    public static Creator<SearchResults> getCREATOR() {
        return CREATOR;
    }
}
