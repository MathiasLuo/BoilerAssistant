package me.mathiasluo.page.weather.weather;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import me.mathiasluo.R;


/*
 * This class is intended to update the data and styles of the WeatherActivity's user interface.
 */
public class WeatherUIHelper {

	private static final int NUM_OF_FORECAST_DAYS = 7;
	private static final String DEGREE_SYMBOL = "\u00b0";

	/*
	 * Colors the weekly forecast HorizontalScrollView's RelativeLayout children with 
	 * alternating colors for a muted zebra stripe effect.
	 * 
	 * @param weatherAct The instance of the WeatherActivity whose layout contains the HorizontalScrollView
	 */
	public static void colorizeWeeklyForecastView(WeatherActivity weatherAct) {
		// Set the colors of each individual day of the week RelativeLayout
		for(int dayNum=0; dayNum<NUM_OF_FORECAST_DAYS; dayNum++) {
			int resID = weatherAct.getResources().getIdentifier("day_"+(dayNum+1), "id", weatherAct.getPackageName());
			RelativeLayout rl = (RelativeLayout) weatherAct.findViewById(resID);
			// Alternate the background colors for a muted zebra striping effect
			if (dayNum%2 == 0) {
				// Darker Gray
				rl.setBackgroundColor(Color.parseColor("#1a1a1a"));
			} else {
				// Dark Gray
				rl.setBackgroundColor(Color.parseColor("#262626"));
			}
		}
	}

	/*
	 * Parses the weekly weather data and updates the weekly forecast data UI components.
	 * 
	 * @param weatherAct The instance of the WeatherActivity whose layout contains the HorizontalScrollView
	 */
	public static void updateWeeklyForecastView(View weatherAct, String weatherJSONData) throws JSONException {
		if (weatherJSONData != null) {
			JSONObject object = (JSONObject) new JSONTokener(weatherJSONData).nextValue();		
			JSONArray jsonWeekData = (JSONArray) object.getJSONObject("daily").getJSONArray("data");

			for (int i=0; i<jsonWeekData.length() && i<NUM_OF_FORECAST_DAYS; i++) {
				int resID = weatherAct.getResources().getIdentifier("day_"+(i+1), "id", weatherAct.getContext().getPackageName());
				RelativeLayout rl = (RelativeLayout) weatherAct.findViewById(resID);
				TextView dayTitle = (TextView) rl.findViewById(R.id.dayOfWeekTitle);
				TextView daySummary = (TextView) rl.findViewById(R.id.daySummary);
				/*TextView dayTempHigh = (TextView) rl.findViewById(R.id.high);
				TextView dayTempLow = (TextView) rl.findViewById(R.id.low);*/
				ImageView dayOfWeekIcon = (ImageView) rl.findViewById(R.id.dayOfWeekIcon);

				JSONObject jObject = (JSONObject) jsonWeekData.getJSONObject(i);
				if (i==0) {
					// The forecast is for today
					dayTitle.setText("Today");
				} else {
					// Parse what day of the week it is
					long timeStamp =  Long.parseLong((String) jObject.get("time").toString());
					Date date = new Date(timeStamp*1000L);												
					SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.US);
					sdf.setTimeZone(TimeZone.getTimeZone("GMT-5"));
					String dayOfWeek = sdf.format(date);

					// Set the title of the relative view to the day of the week
					dayTitle.setText(dayOfWeek);
				}

				// Parse the icon title and set the icon
				String iconTitle = (String) jObject.get("icon").toString();
				updateWeatherIcon(dayOfWeekIcon, iconTitle);
				// Parse the day's summary and set the summary text view
				String summary = (String) jObject.get("summary").toString();
				daySummary.setText(summary);
				// Parse the day's high and low temps and set their respective text views
				/*double high = (double) Double.parseDouble(jObject.get("temperatureMax")+"");
				double low = (double) Double.parseDouble(jObject.get("temperatureMin")+"");*/
				/*dayTempHigh.setText("HIGH: "+getScaledTempString(weatherAct.getContext(), high));
				dayTempLow.setText("LOW: "+getScaledTempString(weatherAct.getContext(), low));*/
			}
		}
	}
	
	/* 
	 * Scales the given temperature into either Celcius or Farenheight.
	 * The temperature is rounded and returned as a String with the 
	 * degree symbol appended.
	 */
	public static String getScaledTempString(Context weatherAct, double temp) {
	    // Restore temperature scale preference
	    SharedPreferences settings = weatherAct.getSharedPreferences("weatherPrefs", 0);
	    String tempScale = settings.getString("tempScale", "farenheight");
	    
		if (tempScale.compareTo("celcius") == 0) {
			return Math.round((temp-32)*(5f/9))+DEGREE_SYMBOL;	// Return the rounded temp in celcius as a string
		} else {
			return Math.round(temp)+DEGREE_SYMBOL;	// Return the rounded temp in farenheight as a string
		}
	}
	
	/*
	 * Updates the given ImageView {@iconImageView} with the appropriate weather icon
	 * based on the String {@iconTitle}
	 * 
	 * @param iconImageView The ImageView to update
	 * @param iconTitle The title of the weather icon to use
	 */
	public static void updateWeatherIcon(ImageView iconImageView, String iconTitle) {		
		if (iconTitle.compareTo("clear-day") == 0) {
			iconImageView.setImageResource(R.drawable.clear_day);
		} else if (iconTitle.compareTo("clear-night") == 0) {
			iconImageView.setImageResource(R.drawable.clear_night);
		} else if (iconTitle.compareTo("rain") == 0) {
			iconImageView.setImageResource(R.drawable.rain);
		} else if (iconTitle.compareTo("wind") == 0) {
			iconImageView.setImageResource(R.drawable.wind);
		} else if (iconTitle.compareTo("snow") == 0) {
			iconImageView.setImageResource(R.drawable.snow);
		} else if (iconTitle.compareTo("sleet") == 0) {
			iconImageView.setImageResource(R.drawable.snow);
		} else if (iconTitle.compareTo("wind") == 0) {
			iconImageView.setImageResource(R.drawable.cloudy); // TODO Update this icon
		} else if (iconTitle.compareTo("fog") == 0) {
			iconImageView.setImageResource(R.drawable.fog);
		} else if (iconTitle.compareTo("cloudy") == 0) {
			iconImageView.setImageResource(R.drawable.cloudy);
		} else if (iconTitle.compareTo("partly-cloudy-day") == 0) {
			iconImageView.setImageResource(R.drawable.partly_cloudy_day);
		} else if (iconTitle.compareTo("partly-cloudy-night") == 0) {
			iconImageView.setImageResource(R.drawable.partly_cloudy_night);
		} else {
			iconImageView.setImageResource(R.drawable.ic_action_about);
		}
	}

}
