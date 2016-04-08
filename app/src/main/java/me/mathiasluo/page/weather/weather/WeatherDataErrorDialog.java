package me.mathiasluo.page.weather.weather;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import me.mathiasluo.R;


public class WeatherDataErrorDialog extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final WeatherActivity weatherActivity = (WeatherActivity) this.getActivity();
		AlertDialog.Builder builder = new AlertDialog.Builder(weatherActivity);
		
		builder.setIcon(R.drawable.ic_action_warning);
		builder.setTitle("Sorry...");
		
		// Get the message passed in from a bundle. This allows for the reuse of this dialog object
		Bundle args = getArguments();
		String message = args.getString("message");
		if (message != null) {
			builder.setMessage(message);
		} else {
			builder.setMessage("Failed to retrieve weather data.");
		}
		
		builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Attempt to load the weather data again
				weatherActivity.updateWeatherData();
			}
		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Close the dialogue without retrying
				Toast.makeText(weatherActivity.getApplicationContext(), "Weather Data Not Retrieved", Toast.LENGTH_LONG).show();
			}
		});

		return builder.create();
	}

}
