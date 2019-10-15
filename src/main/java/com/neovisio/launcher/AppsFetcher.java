package com.neovisio.launcher;

import org.json.JSONObject;
import org.apache.http.entity.*;
//import java.nio.charset.*;
import org.apache.http.util.*;
import org.apache.http.client.*;
import org.apache.http.impl.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.params.*;
import org.apache.http.*;
import android.os.*;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
import android.app.Dialog;
import android.view.Window;
import android.graphics.drawable.*;
import android.graphics.*;
import android.content.Context;

public class AppsFetcher extends AsyncTask<Void, Void, String> {
  Dialog dialog;
  Context context;
  
  AppsFetcher(Context context) {
   this.context = context;
  }
  
  @Override
  protected void onPreExecute() {
   this.dialog = new Dialog(MainActivity.self, R.style.ShortcutDialog);
   dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
   dialog.setContentView(R.layout.loading);
   dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
   dialog.show();
  }

  @Override
  protected String doInBackground(Void... param) {
	String result = "";
	try {
  HttpClient httpClient = new DefaultHttpClient();
  httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "NeoVisio Launcher 1.0");
  try {
  HttpPost request = new HttpPost(Core.URL_MY_APPS);
  request.addHeader("user-agent", "NeoLauncher 1.0");
  request.addHeader("Content-type", "application/json");
  request.addHeader("Accept", "application/json");

  JSONObject obj = new JSONObject();  
  obj.put("pkg", Core.getApps(context));

  StringEntity se = new StringEntity(obj.toString());
  se.setContentEncoding("UTF-8");
  se.setContentType("application/json");
  request.setEntity(se);
  HttpResponse response = httpClient.execute(request);
  result = EntityUtils.toString(response.getEntity(), "UTF-8");
  return result;
  }catch (Exception ex) {
   result = ex.getMessage();
  } 
   }
  catch(Exception e) {result = e.getMessage();}
   return result;
  }

  @Override
  protected void onProgressUpdate(Void... progress) {}

  @Override
  protected void onPostExecute(String result) {
   this.dialog.dismiss();
   MainActivity.Web.loadData(result, "text/html", "UTF-8");
   MainActivity.self.setTitle("My apps");
  }
 }
