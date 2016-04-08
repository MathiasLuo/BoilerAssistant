package me.mathiasluo.page.weather.weather;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;

public class HourlyForecastData {
	
	private String iconTitle = null;
	private SpannableString time = null;
	private double temp = 0.0;
	private String summary = null;
	
	public HourlyForecastData(String iconTitle, String time, double temp, String summary) {
		this.iconTitle = iconTitle;
		this.temp = temp;
		this.summary = summary;
		// Make the time a spannable string so that the AM or PM can small script
		this.time = new SpannableString(time);
		this.time.setSpan(new RelativeSizeSpan(.5f), this.time.length()-2, this.time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
	
	public String getIconTitle() {return this.iconTitle;} 
	public SpannableString getTime() {return this.time;} 
	public double getTemp() {return this.temp;}  
	public String getSummary() {return this.summary;}
	
	public void setIconTitle(String iconTitle) {this.iconTitle = iconTitle;} 
	public void setTemp(double temp) {this.temp = temp;} 
	public void setSummary(String summary) {this.summary = summary;} 
	public void setTime(String time) {	
		this.time = new SpannableString(time);
		this.time.setSpan(new RelativeSizeSpan(.5f), this.time.length()-2, this.time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	} 
}
