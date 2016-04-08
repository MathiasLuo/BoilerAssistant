package me.mathiasluo.page.weather.weather;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

// Currently, Forecast.io API is being used for weather data.
// Example Request: https://api.forecast.io/forecast/0feee70df504bc654228bb08b8b36fb3/40.4240,-86.9290

public class WeatherAPI
{
	private static final String API_KEY = "0feee70df504bc654228bb08b8b36fb3";  //API key for api.forecast.io.
	private static final String PURDUE_COORDINATES = "40.4240,-86.9290";
	private static final String JSON_CACHE_FILENAME = "cachedWeatherJSON.json";

	/*
	 * Requests the weather data from the API. Returns null if the request fails.
	 */
	public static String getWeatherData(Context context) {
		String cachedJSON = WeatherAPI.readJSONFromCache(context, JSON_CACHE_FILENAME);

		if (cachedJSON == null) {		
			// Cached data is does not exist, request it from API
			String weatherData = sendGetRequest("https://api.forecast.io/forecast/"+API_KEY+"/"+PURDUE_COORDINATES);
			WeatherAPI.writeJSONToCache(weatherData, context, JSON_CACHE_FILENAME);

			return weatherData;
		} else {
			int cacheTimestamp=0;
			try {
				JSONObject object = (JSONObject) new JSONTokener(cachedJSON).nextValue();
				cacheTimestamp = Integer.parseInt((object.getJSONObject("currently").get("time")+""));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}

			Calendar currentCal = Calendar.getInstance(); 
			Calendar cachedCal = Calendar.getInstance();
			cachedCal.setTimeInMillis(cacheTimestamp*1000L);

			if ((cachedCal.get(Calendar.YEAR)!=currentCal.get(Calendar.YEAR)) || (cachedCal.get(Calendar.MONTH)!=currentCal.get(Calendar.MONTH)) || cachedCal.get(Calendar.DAY_OF_MONTH)!=currentCal.get(Calendar.DAY_OF_MONTH) || (cachedCal.get(Calendar.HOUR_OF_DAY)!=currentCal.get(Calendar.HOUR_OF_DAY))) {
				// Cached data is older than an hour, request it from API
				String weatherData = sendGetRequest("https://api.forecast.io/forecast/"+API_KEY+"/"+PURDUE_COORDINATES);
				WeatherAPI.writeJSONToCache(weatherData, context, JSON_CACHE_FILENAME);

				return weatherData;
			}

			// Cached data is not older than an hour, so return it
			return cachedJSON;
		}		
	}

	public static void writeJSONToCache(String JSONData, Context context, String fileName) {
		try
		{
			File file = context.getFileStreamPath(fileName);

			if (!file.exists()) {
				file.createNewFile();
			}

			FileOutputStream writer = context.openFileOutput(file.getName(), Context.MODE_PRIVATE);

			writer.write(JSONData.getBytes());
			writer.flush();

			writer.close();
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}   
	}

	public static String readJSONFromCache(Context context, String fileName) {
		String ret = "";

		try {
			InputStream inputStream = context.openFileInput(fileName);

			if ( inputStream != null ) {
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String receiveString = "";
				StringBuilder stringBuilder = new StringBuilder();

				while ( (receiveString = bufferedReader.readLine()) != null ) {
					stringBuilder.append(receiveString);
				}

				inputStream.close();
				ret = stringBuilder.toString();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return ret;
	}

	/*
	 * Parses the JSON data contained in {@object}, then clears and updates {@hourlyDataList}.
	 * 
	 * @param object The JSONObject containing the weather data to be parsed.
	 * @param hourlyDataList The ArrayList of HourlyForecastData objects that will be updated with the parsed data.
	 */
	public static void parseHourlyData(JSONObject object, ArrayList<HourlyForecastData> hourlyDataList) throws JSONException {
		// Parse the hourly forecast data
		hourlyDataList.clear();
		JSONArray jsonHourlyData = (JSONArray) object.getJSONObject("hourly").getJSONArray("data");
		for (int i=0; i<jsonHourlyData.length() && i<13; i++) {
			JSONObject jObject = (JSONObject) jsonHourlyData.getJSONObject(i);
			double temp = Double.parseDouble(jObject.get("temperature")+"");
			long timeStamp =  Long.parseLong((String) jObject.get("time").toString());
			Date date = new Date(timeStamp*1000L);												
			SimpleDateFormat sdf = new SimpleDateFormat("ha", Locale.US);
			sdf.setTimeZone(TimeZone.getTimeZone("GMT-5"));
			String time = sdf.format(date);
			String summary = (String) jObject.get("summary").toString();
			String iconTitle = (String) jObject.get("icon").toString();

			hourlyDataList.add(new HourlyForecastData(iconTitle, time, temp, summary));
		}
	}

	/*
	 * Send a GET request to {@endpoint}
	 * 
	 * @param endpoint The endpoint to send the GET request to.
	 */
	private static String sendGetRequest(String endpoint) {
		String result = null;
		if (endpoint.startsWith("http://") || endpoint.startsWith("https://"))
		{
			// Send a GET request
			try
			{
				// Send data
				URL url = new URL(endpoint);
				URLConnection conn = url.openConnection();

				// Get the response
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuffer sb = new StringBuffer();
				String line;
				while ((line = rd.readLine()) != null)
				{
					sb.append(line);
				}
				rd.close();
				result = sb.toString();
			} catch (Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
		return result;
	}

	public static boolean isCachedDataValid(String cachedJSON) {
		if (cachedJSON == null) {
			return false;
		}
		
		int cacheTimestamp=0;
		try {
			JSONObject object = (JSONObject) new JSONTokener(cachedJSON).nextValue();
			cacheTimestamp = Integer.parseInt((object.getJSONObject("currently").get("time")+""));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Calendar currentCal = Calendar.getInstance(); 
		Calendar cachedCal = Calendar.getInstance();
		cachedCal.setTimeInMillis(cacheTimestamp*1000L);

		if ((cachedCal.get(Calendar.YEAR)!=currentCal.get(Calendar.YEAR)) || (cachedCal.get(Calendar.MONTH)!=currentCal.get(Calendar.MONTH)) || cachedCal.get(Calendar.DAY_OF_MONTH)!=currentCal.get(Calendar.DAY_OF_MONTH) || (cachedCal.get(Calendar.HOUR_OF_DAY)!=currentCal.get(Calendar.HOUR_OF_DAY))) {
			// Cached data is older than an hour, it is expired
			return false;
		}
		
		return true;
	}
}
