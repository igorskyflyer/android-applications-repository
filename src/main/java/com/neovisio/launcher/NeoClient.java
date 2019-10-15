package com.neovisio.launcher;

import android.webkit.*;
import android.graphics.Bitmap;
import android.view.*;
import android.widget.Toast;
import android.content.Context;
import android.os.*;
import java.text.*;
import java.util.Date;

public class NeoClient extends WebViewClient {
 private Context context;
 
 NeoClient(Context context) {
  this.context = context;
 }
 
  private String id() {
   if(MainActivity.Web.getUrl().length() == 0) return MainActivity.Web.getTitle();
   String[] tmp = MainActivity.Web.getUrl().split("/");
   return tmp[tmp.length - 1];
  }
 
 private String title() {
  String[] parts = id().split("-");
  int len = parts.length;
  String title = "";

  for(int i = 0; i < len; i++) {
   title += Character.toUpperCase(parts[i].charAt(0)) + parts[i].substring(1);

   if(i < (len - 1))
    title += " ";
  }
  return title;
 }

 @Override
 public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
  view.loadUrl("file:///android_asset/error.html");
  Toast.makeText(context, error.getDescription(), 3000);
  //super.onReceivedError(view, request, error);
 }
 
 @Override
 public boolean shouldOverrideUrlLoading(WebView view, String url) {
  if(url.contains(Core.SCHEME))
   Core.myApps(context);
  else
   view.loadUrl(url);
  return true;
  }

  @Override
  public void onFormResubmission(WebView view, Message dontResend,Message  resend) {
   resend.sendToTarget();
  }

  @Override
  public void onPageFinished(WebView view, String url) {
   String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
   MainActivity.Bar.setVisibility(View.INVISIBLE);
   MainActivity.Bar.setProgress(0);
   Core.saveValue(context, "last-access", now);
   super.onPageFinished(view, url);
  }

  @Override
  public void onPageStarted(WebView view, String url, Bitmap favicon) {
   MainActivity.Bar.setVisibility(View.VISIBLE);
   Toast.makeText(context, "Started: " + url, 4000).show();
   
   if(url.equals(Core.URL_HOME))
	MainActivity.self.setTitle("NeoVisio");
   else
   if(url.startsWith("data"))
	MainActivity.self.setTitle("My apps");
   else
   if(url.contains("/app/"))
    MainActivity.self.setTitle(title());
	
   super.onPageStarted(view, url, favicon);
  }
}
 
