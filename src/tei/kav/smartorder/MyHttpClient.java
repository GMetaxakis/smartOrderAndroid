package tei.kav.smartorder;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MyHttpClient extends AsyncTask<String, Void, String> {
	public static String ip = "192.168.1.65";
	public static String port = "8080";

	private static boolean ipSetted = false;
	private static boolean portSetted = false;
	private static Handler mHandler;
	private String paramToReturn;
	private String urlString = "http://" + ip + ":" + port;

	private Exception exception;

	public static void setIp(String _ip) {
		ip = _ip;
		ipSetted = true;
	}

	public static void setPort(String _port) {
		port = _port;
		portSetted = true;
	}

	public static void setHandler(Handler handler) {
		mHandler = handler;
	}

	@Override
	protected String doInBackground(String... params) {
		if (!ipSetted || !portSetted) {
			this.exception = new Exception("ip and port not setted");
			return "ip and port not setted";
		}
		String result = "";
		urlString+="/"+params[0]+params[1];
		paramToReturn = params[1];
		
		try {
			if (params[0] == "post") {
				result = postVarious(params);
			} else if (params[0] == "get") {
				result = getVarious(params[1]);
			}
		} catch (Exception e) {
			this.exception = e;
			return "error";
		}

		return result;
	}
	
	private String postVarious(String... param) throws URISyntaxException, UnsupportedEncodingException {
		HttpClient httpclient = new DefaultHttpClient();
		URI url = new URI(urlString);
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
		nameValuePair.add(new BasicNameValuePair("username", Utils.username));
		nameValuePair.add(new BasicNameValuePair("password", Utils.password));
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
		
		//if(Utils.userid != null)
		//	httppost.addHeader("user_id", Utils.userid);
		
		if (param[1]=="RemoveOrder"){
			httppost.addHeader("order_id",param[2]);
		}
		else if(param[1] == "Order"){
			httppost.addHeader("table_id",param[2]);
			httppost.addHeader("item_id",param[3]);
			httppost.addHeader("user_id",param[4]);
			httppost.addHeader("characteristicsCount",param[5]);
			int characteristicsSize =  Integer.parseInt(param[5]);
			for(int i=0;i<characteristicsSize;i++){
				httppost.addHeader("characteristic"+i,param[6+i]);
			}
			
			//header for : 
			//				item_id
			//				table_id
			//				characteristics count
			//				header for each enabled characteristic
			//				example : 
			//							characteristic1 - id
			//							characteristic2 - id
			//							etc.
		}
		else if (param[1] == "UserOffline"){
			httppost.addHeader("user_id", Utils.userid);
		}
		
		
		HttpResponse response;

		try {
			response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = Utils.convertStreamToString(instream);
				if(param[1]=="User")
					Utils.userid = result.substring(0,result.indexOf('\n'));
				else if (param[1]=="RemoveOrder"){
					//nothing
				}
				else if (param[1]=="UserOffline"){
					
				}
				
				instream.close();
			}

		} catch (ClientProtocolException e) {
			this.exception = e;
			return "error";
		} catch (IOException e) {
			this.exception = e;
			return "error";
		}

		return "ok";
	}

	private String getVarious(String param) throws URISyntaxException {
		HttpClient httpclient = new DefaultHttpClient();
		URI url = new URI(urlString);
		HttpGet httpget = new HttpGet(url);
		httpget.addHeader("user_id", Utils.userid);
		
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				InputStream instream = entity.getContent();

				String result = Utils.convertStreamToString(instream);
				if(param=="Categories")
					Utils.loadCategories(result);
				else if (param=="Products")
					Utils.loadProducts(result);
				else if (param=="Items")
					Utils.loadItems(result);
				else if (param=="Characteristics")
					Utils.loadCharacteristics(result);
				else if (param=="Tables")
					Utils.loadTables(result);
				else{
					//do something
				}
				Log.i(Utils.LOGTAG, result);

				instream.close();
			}

		} catch (ClientProtocolException e) {
			this.exception = e;
			e.printStackTrace();
			return "error";
		} catch (IOException e) {
			this.exception = e;
			e.printStackTrace();
			return "error";
		} catch (JSONException e) {
			this.exception = e;
			e.printStackTrace();
			return "error";
		}

		return "ok";
		
	}
	
	protected void onPostExecute(String result) {

		Message msg = Message.obtain();
		if (exception != null) {
			if (exception.getMessage() != null)
				Log.e(Utils.LOGTAG, exception.getMessage());

			msg.obj = "onPostExecute exception : " + exception.getMessage();
		} else {
			msg.obj = "onPostExecute ok "+paramToReturn;
		}
		msg.setTarget(mHandler);
		msg.sendToTarget();
	}
}
