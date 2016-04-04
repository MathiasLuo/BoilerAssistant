package me.mathiasluo.model.schedule;

import java.util.*;

/**
 *Dumb cookie manager
 */
public class Cookies {
	private Map<String, String> cookies;
	private String[] okCookies;
	public Cookies(String[] okCookies) {
		this.okCookies = okCookies;
		cookies = new HashMap<String, String>();
	}

	public Cookies(String[] okCookies, Map<String, List<String>> headers) {
		this(okCookies);
		setCookies(headers);
	}

	/**
	 *Set a cookie
	 * @param key Cookie name
	 * @param val Cookie value
	 */
	public void setCookie(String key, String val) {
		cookies.put(key, val);
	}

	/**
	 *
	 *Clear a cookie
	 * @param key cookie name
	 */
	public void clearCookie(String key) {
		cookies.remove(key);
	}

	/**
	 *
	 * Set cookies
	 * @param headers headers from a response
	 */
	public void setCookies(Map<String, List<String>> headers) {
		setCookies(headers.get("Set-Cookie"));
	}

	/**
	 * Set cookies
	 * @param list raw cookies in format from "Set-Cookie" header
	 */
	public void setCookies(List<String> rawCookies) {
		//for every cookie in the list
		for(String cookie : rawCookies) {
			//split it up, making sure we are setting the cookie to something
			String[] fields = cookie.split(";");
			String[] keyVal = fields[0].split("=");
			if(keyVal.length != 2) {
				continue;
			}
			//check if this cookie is valid
			String key = keyVal[0].trim();
			for(String validCookie : okCookies) {
				//if so, put it in our list
				if(key.equals(validCookie)) {
					cookies.put(key, keyVal[1].trim());
				}
			}
		}
	}
	
	@Override
	public String toString() {
		//convert cookie map into Cookie header for requests
		StringBuilder cookieStr = new StringBuilder();
		boolean prev = false;
		for(Map.Entry<String, String> cookie : cookies.entrySet()) {
			if(prev) {
				cookieStr.append("; ");
			}
			prev = true;
			cookieStr.append(cookie.getKey() + "=" + cookie.getValue());
		}
		return cookieStr.toString();
	}
}

