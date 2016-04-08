package me.mathiasluo.page.weather.weather;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

public class WeatherInfoDialog extends DialogFragment {
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setTitle("Info");   
		
		// Info message and credit to forecast.io API
		SpannableString message = new SpannableString("Weather data shown pertains to Purdue University's Campus." +
					"\n(40.4240N, 86.9290 W)" +
					"\nPowered by Forecast.io (http://forecast.io)");
		
		// Format the message
		message.setSpan(new StyleSpan(Typeface.ITALIC), 84, 127, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		message.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 83, 127, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		message.setSpan(new RelativeSizeSpan(.7f), 84, 127, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        
        builder.setMessage(message);
		
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Close the dialogue
			}
		});
		
		return builder.create();
	}

}
