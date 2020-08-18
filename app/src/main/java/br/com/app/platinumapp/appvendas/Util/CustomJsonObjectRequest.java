package br.com.app.platinumapp.appvendas.Util;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class CustomJsonObjectRequest extends Request<JSONObject> {
	private Listener<JSONObject> response;
	private Map<String, String> params;
	
	
	public CustomJsonObjectRequest(int method, String url, Map<String, String> params, Listener<JSONObject> response, ErrorListener listener) {
		super(method, url, listener);
		this.params = params;
		this.response = response;
		
		
	}
	public CustomJsonObjectRequest(String url, Map<String, String> params, Listener<JSONObject> response, ErrorListener listener) {
		super(Method.GET, url, listener);
		this.params = params;
		this.response = response;
		
	}
	
	public Map<String, String> getParams() throws AuthFailureError{
		return params;
	}
	
	public Map<String, String> getHeaders() throws AuthFailureError{
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("apiKey", "Essa e minha API KEY: json object");
		
		return(header);
	}
	
	public Priority getPriority(){
		return(Priority.NORMAL);
	}




	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
			String js = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			Log.d("Script", "Resposta da WEB : " + js);
			String str  =  js.replace("\r\n","");
			return (Response.success(new JSONObject(str), HttpHeaderParser.parseCacheHeaders(response)));

		} catch (UnsupportedEncodingException e) {
			Log.d("Script", "UnsupportedEncodingException : " + e.getMessage());
		} catch (JSONException e) {
			Log.d("Script", "JSONException : " + e.getMessage());

		}
		return null;
	}

	
	@Override
	protected void deliverResponse(JSONObject response) {
		this.response.onResponse(response);
	}

}
