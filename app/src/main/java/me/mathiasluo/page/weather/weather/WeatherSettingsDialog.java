package me.mathiasluo.page.weather.weather;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

public class WeatherSettingsDialog extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final WeatherActivity weatherActivity = (WeatherActivity) getActivity();
		AlertDialog.Builder builder = new AlertDialog.Builder(weatherActivity);
		final SharedPreferences settings = weatherActivity.getBaseContext().getSharedPreferences("weatherPrefs", 0);
		final SharedPreferences.Editor editor = settings.edit();

		builder.setTitle("Settings");
		
		String tempScale = settings.getString("tempScale", "farenheight");
		// Find out what the current scale is so the radio button can be preselected
		int currentScale = -1;
		if (tempScale.compareTo("farenheight") == 0) {
			currentScale = 0;
		} else {
			currentScale = 1;
		}
		
		final CharSequence[] scales = {" Farenheight "," Celcius "};
		builder.setSingleChoiceItems(scales, currentScale, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				switch(item)
				{
				case 0:
					// Farenheight selected
					// Save the temperature scale preference.
					editor.putString("tempScale", "farenheight");
					break;
				case 1:
					// Celcius Selected
					// Save the temperature scale preference.
					editor.putString("tempScale", "celcius");
					break;
				}
			}
		});

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				editor.commit();	// Commit the edits
				weatherActivity.updateWeatherData();
			}
		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Close the dialogue without committing the preference change
			}
		});

		return builder.create();
	}

}
