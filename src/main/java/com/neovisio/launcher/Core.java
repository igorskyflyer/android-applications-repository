package com.neovisio.launcher;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.net.*;
import android.preference.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.view.inputmethod.*;
import android.widget.*;
import java.io.*;
import java.net.*;
import java.util.*;
import android.content.pm.PackageManager;

public class Core {
 private Context context;
 public UserData data; 
  
 public Core(Context ctx) {
  this.context = ctx;
  this.data = new UserData(ctx);
 }
  
 public final static String URL_HOME = "http://neovisio.org/";
 public final static String URL_SEARCH = "http://neovisio.org/search/";
 public final static String URL_TAG = "http://neovisio.org/tag/";
 public final static String URL_CATEGORY = "http://neovisio.org/category/";
 public final static String URL_APPS = "http://neovisio.org/posts/";
 public final static String URL_MY_APPS = "http://neovisio.org/public/my-apps.php";
 public final static String URL_LAUNCHER = "http://neovisio.org/launcher";
 public final static String URL_UPDATE = "http://neovisio.org/launcher/version.php";
 public final static String URL_MEDIA = "http://neovisio.org/media/";
 public final static String SCHEME = "neovisio://";
 
 
 public static class UserData {
  private final Context context;
  public String username;
  public boolean showMyApps;
  public String appTheme;
  public String appSkin;
  public boolean fullscreen;
  public String scale;
  public String launchFx;
  public boolean silentPush;
  public String largeDownloads;
  public boolean cache;
  public String cacheSize;
  public boolean hardwareAcceleration;
  public boolean autoUpdate;
  public String firstRun;
  public String networkPolicy;

  UserData(Context ctx) {
   this.context = ctx;
   this.username = getValue(context, "app_username", "");
   this.showMyApps = getBool(context, "app_myapps", true);
   this.appTheme = getValue(context, "app_theme", "0");
   this.appSkin = getValue(context, "app_skin", "0");
   this.fullscreen = getBool(context, "app_fullscreen", false);
   this.scale = getValue(context, "app_scale", "0");
   this.launchFx = getValue(context, "app_effect", "0");
   this.silentPush = getBool(context, "app_notif_silent", false);
   this.largeDownloads = getValue(context, "app_large", "0");
   this.cache = getBool(context, "app_cache", false);
   this.cacheSize = getValue(context, "app_cache_size", "10");
   this.hardwareAcceleration = getBool(context, "app_hardware", true);
   this.autoUpdate = getBool(context, "app_autoupdate", false);
   this.firstRun = getValue(context, "app_first_run", "true");
   this.networkPolicy = getValue(context, "app_policy", "1");
  }
 }
  
 public static class App {
  long id;
  String name;
  String uri;
  long size;
  
  App() {
   this.id = -1;
   this.name = "";
   this.uri = "";
   this.size = -1;
  }
 }
 
 public static String getApps(Context ctx) {
  PackageManager pm = ctx.getPackageManager();
  List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_META_DATA);
  int len = apps.size();
  String result = "";
 
  for(int i = 0; i < len; i++) {
 //  if((apps.get(i).applicationInfo.flags & apps.get(i).applicationInfo.FLAG_SYSTEM) == 1) continue;
   result += "'" + apps.get(i).applicationInfo.packageName + "',";
  }
  
  return result.substring(0, result.length() - 1);
 }
 
  public static SharedPreferences loadConfig(Context context) {
   return PreferenceManager.getDefaultSharedPreferences(context);
  }

  public static void saveConfig(Context ctx) {
   loadConfig(ctx).edit().commit();
  }

  public static void saveValue(Context ctx, String name, String value) {
   SharedPreferences.Editor editor = loadConfig(ctx).edit();
   editor.putString(name, value);
   editor.commit();
  }	

  public static String getValue(Context ctx, String name, String defValue) {
   return loadConfig(ctx).getString(name, defValue);
  }

  public static boolean getBool(Context ctx, String name, boolean value) {
   return loadConfig(ctx).getBoolean(name, value);
  }
  
  public static int getInt(Context ctx, String name, int value) {
   return loadConfig(ctx).getInt(name, value);
  }
  
  public static void showKeyboard(Context ctx) {
   InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
   imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
  }

  public static void share(Context ctx, String uri) {
   Intent sharingIntent = new Intent(Intent.ACTION_SEND);
   sharingIntent.setType("text/plain");
   sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, uri);
   sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
   ctx.startActivity(Intent.createChooser(sharingIntent, "Share using"));
  }
  
  public static void theme(Context ctx, Activity activity) {
   String theme = getValue(ctx, "app_theme", "0");
   Boolean fullscreen = getBool(ctx, "app_fullscreen", false);
	  
   if(theme.equals("1")) {
	if(fullscreen)
	 activity.setTheme(android.R.style.Theme_Holo_NoActionBar_Fullscreen);
	else
     activity.setTheme(android.R.style.Theme_Holo_Wallpaper);
   }
   else 
   if(theme.equals("2")) {
	if(fullscreen)
     activity.setTheme(android.R.style.Theme_Holo_Wallpaper_NoTitleBar);
    else
     activity.setTheme(android.R.style.Theme_Holo_Wallpaper);
   }
   else {
	if(fullscreen)
	 activity.setTheme(android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen); 
	else
     activity.setTheme(android.R.style.Theme_Holo_Light);
   }
  }
  
  public static void install(Context ctx, String path) {
   Intent install = new Intent(Intent.ACTION_VIEW);
   install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
   install.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
   ctx.startActivity(install);
  }
  
  public static boolean isOnline(Context ctx){
   ConnectivityManager connMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
   NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
   NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

   return(wifi.isConnected() || mobile.isConnected());
  }
 
  public static void offline(Context ctx) {
   Intent intent = new Intent(ctx.getApplicationContext(), Offline.class);
   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
   ctx.startActivity(intent);
   MainActivity.self.finish();
  }
  
  public static void shortcut(Context ctx, String activity, String title, Bitmap icon, String url) {
   Intent shortcutIntent = new Intent();
   shortcutIntent.setClassName("com.neovisio.launcher", "com.neovisio.launcher." + activity);
   Intent intent = new Intent();
   shortcutIntent.putExtra("title", title);
   shortcutIntent.putExtra("url", url);
   intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
   intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);

   if(icon != null)
	intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, icon);
   else
	intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(ctx, R.drawable.ic_launcher));

   intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
   ctx.sendBroadcast(intent);
  }
  
  public static void addToHomescreen(final Context ctx, final String activity, final String title, final Bitmap favicon, final String url, final Dialog dialog) {
   ((ImageView) dialog.findViewById(R.id.dialogIcon)).setImageBitmap(favicon);
   ((EditText) dialog.findViewById(R.id.dialogName)).setText(title);

   if(favicon != null)
	((ImageView) dialog.findViewById(R.id.dialogIcon)).setImageBitmap(favicon);
   else
	((ImageView) dialog.findViewById(R.id.dialogIcon)).setImageResource(R.drawable.ic_launcher);

   ((Button) dialog.findViewById(R.id.dialogOk)).setOnClickListener(new OnClickListener() {
    public void onClick(View view) {
	  try {
	 shortcut(ctx, activity, title, favicon, url);
	 dialog.dismiss();
	 Toast.makeText(ctx, "\"" + title +"\" was added to homescreen.", Toast.LENGTH_SHORT).show();
	 }
	 catch(Exception e) {Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG).show();}
	}
   });
  }
  
  public static Bitmap downloadIcon(String url) {
   try {
    URL http = new URL(url);
    try {
     URLConnection connection = http.openConnection();
     InputStream Stream = connection.getInputStream();
     return BitmapFactory.decodeStream(Stream);
    }
    catch(Exception e) {}
   }
   catch(Exception exp) {}
  return null;
  }
  
  public static void showDownload(Context context, Activity activity, Drawable icon, String label) {
   final RelativeLayout download = ((RelativeLayout) activity.findViewById(R.id.Download));
   Animation animShow = AnimationUtils.loadAnimation(context, R.anim.slide_in_download);
   final Animation animHide = AnimationUtils.loadAnimation(context, R.anim.slide_out_download);
   
   animShow.setAnimationListener(new Animation.AnimationListener() {
	@Override
	public void onAnimationStart(Animation anim) {}
	
	@Override
	public void onAnimationRepeat(Animation anim) {}
	
	@Override
	public void onAnimationEnd(Animation anim) {
	 animHide.setAnimationListener(new Animation.AnimationListener() {
	  @Override
	  public void onAnimationStart(Animation anim) {}
	  
	  @Override
	  public void onAnimationRepeat(Animation anim) {}
	  
	  @Override
	  public void onAnimationEnd(Animation anim) {
	   download.setVisibility(View.GONE);
	  }
	 });
	 
	 download.startAnimation(animHide);
	}
   });
   
   ((ImageView) download.findViewById(R.id.DownloadIcon)).setImageDrawable(icon);
   ((TextView) download.findViewById(R.id.DownloadLabel)).setText(label);
   ((Button) download.findViewById(R.id.DownloadInstall)).setOnClickListener(new OnClickListener() {
	 public void onClick(View view) {
	  download.setVisibility(View.GONE);
	  install(MainActivity.self, MainActivity.file.uri);
	 }
   });
   
   download.setVisibility(View.VISIBLE);
   download.startAnimation(animShow);
  }
  
  public static void myApps(Context context) {
   new AppsFetcher(context).execute();
  }
  
  public static void skinProgress(Context ctx, String skin) {
   Drawable skinRes;
   
   switch(skin) {
    case "0":
     skinRes = ctx.getResources().getDrawable(R.drawable.progress_red);
    break;

    case "1":
     skinRes = ctx.getResources().getDrawable(R.drawable.progress_green);
    break;
 
    case "2":
     skinRes = ctx.getResources().getDrawable(R.drawable.progress_blue);
    break;

    case "3":
     skinRes = ctx.getResources().getDrawable(R.drawable.progress_orange);
    break;

    case "4":
     skinRes = ctx.getResources().getDrawable(R.drawable.progress_yellow);
    break;

    case "5":
     skinRes = ctx.getResources().getDrawable(R.drawable.progress_black);
    break;

    default:
     skinRes = ctx.getResources().getDrawable(R.drawable.progress_red);
    break;
   }

   MainActivity.Bar.setProgressDrawable(skinRes);
  }
  
  public static void scale(Context ctx, String scale) {
   ViewGroup.LayoutParams params;
   params = MainActivity.Bar.getLayoutParams();
   int size;

  switch(scale) {
   case "0":
    size = 3;
   break;

   case "1":
    size = 5;
   break;

   case "2":
    size = 8;
   break;

   default:
    size = 3;
   break;
  }

   params.height = size;
   MainActivity.Bar.setLayoutParams(params);
  }
  
  public static class Apk {
   public Drawable icon;
   public String label;
  } 
  
  public static Apk apkInfo(Context ctx, String uri) {
   PackageManager pm = ctx.getPackageManager();
   PackageInfo    pi = pm.getPackageArchiveInfo(uri, 0);
   pi.applicationInfo.sourceDir= uri;
   pi.applicationInfo.publicSourceDir = uri;

   Apk app = new Apk();
   
   app.icon = pi.applicationInfo.loadIcon(pm);
   app.label = (String) pi.applicationInfo.loadLabel(pm);
   return app;
  }
  
  }
