package com.neovisio.launcher;

import android.app.*;
import android.content.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.graphics.BitmapFactory;
import java.io.*;
import android.graphics.drawable.*;

public class MainActivity extends Activity  {  
 private Intent app;
 public static Context context;
 public static Core.UserData data;
 public static Activity self;
 public static DownloadManager download;
 public static Core.App file;
 
 public static WebView Web;
 private WebSettings Settings;
 private String UA;
 public static ProgressBar Bar;
 
 private void showSplash() {
  Intent Splash = new Intent(this, SplashActivity.class);
  startActivity(Splash);
 }
	
 private void launchSearch() {
  Web.requestFocus(View.FOCUS_DOWN);
  Web.loadUrl("javascript: menu(); $('search').focus();");
  Core.showKeyboard(context);
 }
	
 private void showOptions() {
  Intent Options = new Intent(this, OptionsActivity.class);
  startActivity(Options);
 }

 public static String id() {
  if(Web.getUrl().length() == 0) return "";
  String[] tmp = Web.getUrl().split("/");
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
 
 private String icon() {
  return Core.URL_MEDIA + id() +".png";
 }
 
 private void notification(String ticker, String title, String content) {
   Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
   PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 123, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
   NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

   Notification.Builder builder = new Notification.Builder(getApplicationContext());

   builder.setContentIntent(contentIntent)
	 .setSmallIcon(R.drawable.notification_icon)
     .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notification_my_apps))
	 .setTicker(ticker)
	 .setWhen(System.currentTimeMillis())
	 .setAutoCancel(true)
	 .setContentTitle(title)
	 .setContentText(content);
	 
   if(!data.silentPush)
    builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
	 
   Notification n = builder.build();

   nm.notify(123, n);
   }
  
  public static void clearCache() {
   Web.clearCache(true);
  }
   
  private void setupDownload() {
   Button btnInstall = ((Button) findViewById(R.id.DownloadInstall));
   btnInstall.setOnClickListener(new OnClickListener() {
	public void onClick(View view) {
	 Core.install(getApplicationContext(), "");
	 view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_download));
	 view.setVisibility(View.GONE);
	}
   });
  }
  
  private void playAnimation() {
   String value = data.launchFx;
   Animation animation;
   
   if(value.equals("0"))
	animation = AnimationUtils.loadAnimation(context, R.anim.slide);
   else
   if(value.equals("1"))
	animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
   else
   animation = AnimationUtils.loadAnimation(context, R.anim.fadein);
   
   ((RelativeLayout) findViewById(R.id.Main)).startAnimation(animation);
  }
  
  private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
   @Override
   public void onReceive(Context context, Intent intent) {
   String action = intent.getAction();
   if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
  // long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
   Toast.makeText(context, "Downloaded: " + file.name, 3000).show();
   Query query = new Query();
   query.setFilterById(file.id);
   Cursor c = download.query(query);
   if (c.moveToFirst()) {
   int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
   if(DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
     String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
	 Toast.makeText(context, "complete:" + Uri.parse(uriString).getPath(), 5000).show();
     Core.Apk apk = Core.apkInfo(context, Uri.parse(uriString).getPath());
	 Core.showDownload(context, self, apk.icon, apk.label);
	 notification("Download completed.", apk.label, "Download completed.");
    }
	else {
	  try {
	 Toast.makeText(context, c.getInt(c.getColumnIndex(DownloadManager.COLUMN_REASON)), 5000).show();
	 }
	 catch(Exception e) {Toast.makeText(context, e.getMessage(), 4000).show();}
	}
   }
  }
 }
};
  
  
 @Override
 protected void onCreate(Bundle savedInstanceState) {
  app = getIntent();
  context = getApplicationContext();
  self = this;

//  if(!!Core.isOnline(context)) {
 //  Core.offline(context);
 //  finish();
 // }
  
  Core.theme(context, this);
  super.onCreate(savedInstanceState);
  setContentView(R.layout.main);
  
  Web = (WebView) findViewById(R.id.Web);
  Bar = (ProgressBar) findViewById(R.id.Bar);
  data = new Core(context).data;
  file = new Core.App();
   
  Core.scale(context, data.scale);
  Core.skinProgress(context, data.appSkin);
   
  if(data.firstRun.equals("true")) {
   showSplash();
   Core.saveValue(context, "app_first_run", "false");
  }

  Bar.setMax(100);
  Bar.setProgress(0);
  Core.skinProgress(self, data.appSkin);
		
  Settings = Web.getSettings();
  UA = "NeoLauncher v.1.0.";
  
  setupDownload();
  registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	
  Settings.setUserAgentString(UA);
  Settings.setJavaScriptEnabled(true);
  Settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
  Settings.setJavaScriptCanOpenWindowsAutomatically(false);
  Settings.setSupportMultipleWindows(false);
   
  if(data.cache)
   Settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
  else
   Settings.setCacheMode(WebSettings.LOAD_NORMAL);
   
  if(data.hardwareAcceleration)
   Web.setLayerType(View.LAYER_TYPE_HARDWARE, null);
  else
   Web.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
   
  Web.setWebChromeClient(new NeoChrome());	
  Web.setWebViewClient(new NeoClient(context));
  Web.setDownloadListener(new NeoDownloader(MainActivity.this));
	
  if(app.hasExtra("do-update"))
   Web.loadUrl(Core.URL_LAUNCHER);
  else
  if(app.hasExtra("url") && app.getStringExtra("url").length() > 0) {
   String url = app.getStringExtra("url");
   
   if(url.contains(Core.SCHEME))
    Core.myApps(context);
   else
    Web.loadUrl(app.getStringExtra("url"));
  }
  else
  if(app.getDataString() != null && app.getDataString().length() > 0) {
   Uri data = app.getData();

   if(data.getScheme().equals("neovisio")) {
	if(!data.getHost().equals("view"))
	 Web.loadUrl(Core.URL_HOME);
   }
   else
   if(data.getScheme().equals("http"))
	Web.loadUrl(app.getDataString());
   }
   else
    Web.loadUrl(Core.URL_HOME);
	 
   playAnimation();
  }
	
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
   MenuInflater menuInflater = getMenuInflater();
   menuInflater.inflate(R.layout.menu, menu);
   return true;
  }

  @Override
   public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
	 case R.id.menu_home:
      Web.loadUrl(Core.URL_HOME);
	  return true;
		  
	 case R.id.menu_refresh:
	  Web.reload();
	  return true;

	 case R.id.menu_search:
	  launchSearch();
	  return true;
				
	 case R.id.menu_homescreen:
	  String title = title();
	  new IconDownloader(self, "ShortcutLauncher", title, Web.getUrl()).execute(icon());
	  return true;

	 case R.id.menu_share:
      Core.share(context, Web.getUrl());
	  return true;
			 
	 case R.id.menu_options:
	  showOptions();
	  return true;
				
	 case R.id.menu_exit:
	  moveTaskToBack(true);
	  return true;

	 default:
	  return super.onOptionsItemSelected(item);
     }
    }    
	
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
   if(keyCode == KeyEvent.KEYCODE_BACK) {
	if(Web.canGoBack())
     Web.goBack();
    else
	  moveTaskToBack(true);
	 return true;
	}
	return super.onKeyDown(keyCode, event);
  }
}
