package com.microsoft.azure.msalwebsample.interceptor;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.azure.msalwebsample.AuthHelper;
import com.microsoft.azure.msalwebsample.HttpClientHelper;
import com.microsoft.azure.msalwebsample.JSONHelper;
import com.microsoft.azure.msalwebsample.User;

@Component
public class AuthorizationImpl {

	@Autowired
	AuthHelper authHelper;

	public boolean authorize(HttpServletRequest request, HttpServletResponse response, String[] hasRole) {

		// httpRequest.getSession()
		try {
			return getUsersFromGraph(request, response, hasRole);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public boolean getUsersFromGraph(HttpServletRequest request, HttpServletResponse response, String[] hasRole) throws Throwable {
		IAuthenticationResult result;
		try {
			result = authHelper.getAuthResultBySilentFlow(request, response);
			if (result != null) {
				String ot = getUserNamesFromGraph(result.accessToken());
				System.out.println(ot);
				return true;

			}
		} catch (ExecutionException e) {

		}
		return false;
	}

	private String getUserNamesFromGraph(String accessToken) throws Exception {
		// Microsoft Graph users endpoint
		URL url = new URL("https://graph.microsoft.com/v1.0/me/getMemberObjects");

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		// Set the appropriate header fields in the request header.
		conn.setRequestProperty("Authorization", "Bearer " + accessToken);
		conn.setRequestProperty("Accept", "application/json");
		
		
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		conn.setDoOutput(true);
		String jsonInputString = "{\"securityEnabledOnly\": true}";
		try(OutputStream os = conn.getOutputStream()) {
		    byte[] input = jsonInputString.getBytes("utf-8");
		    os.write(input, 0, input.length);           
		}
		
		String response = HttpClientHelper.getResponseStringFromConn(conn);

		int responseCode = conn.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK) {
			throw new IOException(response);
		}

		JSONObject responseObject = HttpClientHelper.processResponse(responseCode, response);
		JSONArray users = JSONHelper.fetchDirectoryObjectJSONArray(responseObject);

		// Parse JSON to User objects, and append user names to string
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < users.length(); i++) {
			JSONObject thisUserJSONObject = users.optJSONObject(i);
			User user = new User();
			JSONHelper.convertJSONObjectToDirectoryObject(thisUserJSONObject, user);
			builder.append(user.getUserPrincipalName());
			builder.append("<br/>");
		}
		return builder.toString();
	}
}