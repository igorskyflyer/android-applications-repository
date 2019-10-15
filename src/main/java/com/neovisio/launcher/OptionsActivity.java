package com.neovisio.launcher;

import android.app.*;
import android.content.*;
import android.content.SharedPreferences.*;
import android.content.pm.*;
import android.os.*;
import android.preference.*;
import android.preference.Preference.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class OptionsActivity extends PreferenceActivity 
implements OnSharedPreferenceChangeListener {

  Preference prefHWA;
  Preference prefCache;
  Preference prefUpdate;
  Preference prefAbout;
  Preference prefScale;
  Preference prefSkin;
  Preference prefNotification;
  Preference prefChangelog;
  Preference prefCacheClear;
  Preference prefTheme;
  Preference prefFullscreen;
  
 private void updateCheck() {
  new AsyncTask<Void, Void, Integer>(){
   @Override
   protected void onPreExecute() {}

   @Override
   protected Integer doInBackground(Void... param) {
	Integer result = -1;
	try {
	   URL url = new URL(Core.URL_UPDATE);
	   URLConnection http = url.openConnection();
	   InputStream stream = http.getInputStream();
	  
	   if(stream.available() == 0)
		return -1;
		
	   Scanner scanner = new Scanner(stream);
	   double onlineVersion = Double.parseDouble(scanner.next());
	   double version = Double.parseDouble(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
	   result = (onlineVersion > version ? 1 : 0);
	 }
	 catch(Exception e) {
	  result = -1;
	 }
	 return result;
    }

   @Override
   protected void onProgressUpdate(Void... progress) {}

   @Override
   protected void onPostExecute(Integer result) {
    if(result == 0)
	 Toast.makeText(OptionsActivity.this, "You already have the latest version of the application.", Toast.LENGTH_LONG).show();
	else
	if(result == 1) {
	 Intent main = new Intent(OptionsActivity.this, MainActivity.class);
	 main.putExtra("url", "http://neovisio.org/launcher/#download");
	 startActivity(main);
	 finish();
	}
    else
	 Toast.makeText(OptionsActivity.this, "An error has occured while contacting the server.", Toast.LENGTH_LONG).show();
   }
  }.execute();
 }
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    Core.theme(getApplicationContext(), this);
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.layout.options);
	
	prefHWA = findPreference("app_hardware");
    prefCache = findPreference("app_cache");
	prefUpdate = findPreference("app_update");
    prefAbout = findPreference("app_about");
	prefScale = findPreference("app_scale");
	prefSkin = findPreference("app_skin");
	prefNotification = findPreference("app_notif_silent");
	prefChangelog = findPreference("app_changelog");
	prefCacheClear = findPreference("app_cache_clear");
	prefTheme = findPreference("app_theme");
	prefFullscreen = findPreference("app_fullscreen");
	
    prefFullscreen.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
	 public boolean onPreferenceChange(Preference preference, Object obj) {
	  MainActivity.self.recreate();
	  OptionsActivity.this.recreate();
	  return true;
	 }
    });
	
	prefTheme.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
	 public boolean onPreferenceChange(Preference preference, Object obj) {
	  MainActivity.self.recreate();
	  OptionsActivity.this.recreate();
	  return true;
	 }
    });
	
	prefCache.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	 boolean result;
	 public boolean onPreferenceClick(Preference preference) {
	   AlertDialog.Builder dialog = new AlertDialog.Builder(OptionsActivity.this);
	   final boolean isChecked = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("app_cache", false);
	  dialog.setTitle("NeoVisio");
	  dialog.setMessage((isChecked ? "Enable" : "Disable") + " application cache?");
	  dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
	   public void onClick(DialogInterface dialog, int which) {
		dialog.cancel();
		finish();
		result = false;
	   }
	  });

	  dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	   public void onClick(DialogInterface dialog, int which) {
	    result = true;
		
		if(isChecked)
		 Toast.makeText(OptionsActivity.this, "Caching enabled.\nYou may not see all of the updated content.", Toast.LENGTH_LONG).show();
	   }
	 });

    dialog.show();
	return result;
   }
  });
	
	prefHWA.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	 boolean result;
	 public boolean onPreferenceClick(Preference preference) {
	  AlertDialog.Builder dialog = new AlertDialog.Builder(OptionsActivity.this);
	  final boolean isChecked = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("app_hardware", true);
	  
	  dialog.setTitle("NeoVisio");
	  dialog.setMessage((isChecked ? "Enable" : "Disable") + " hardware acceleration?");
	  dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
	   public void onClick(DialogInterface dialog, int which) {
		dialog.cancel();
		finish();
	    result = false;
	   }
	  });
	  
	  dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	   public void onClick(DialogInterface dialog, int which) {
		result = true;
	   }
	  });
	  
	  dialog.show();
      return result;
	 }
	});
	
	prefChangelog.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	 public boolean onPreferenceClick(Preference preference) {
	  AlertDialog.Builder dialog = new AlertDialog.Builder(OptionsActivity.this);
	  dialog.setTitle("Changelog");
	  dialog.setMessage(R.string.app_changelog);
	  dialog.show();
	  return true;
	 }
	});
	
	prefUpdate.setOnPreferenceClickListener(new OnPreferenceClickListener() {
     public boolean onPreferenceClick(Preference preference) {
	  Toast.makeText(getApplicationContext(), "Checking...", Toast.LENGTH_LONG).show();
	  updateCheck();
	  return true;
	 }
   });
   
   prefAbout.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	public boolean onPreferenceClick(Preference preference) {
	 Toast.makeText(getApplicationContext(), "\nAuthor: Igor Dimitrijević, 2016.\n", Toast.LENGTH_LONG).show();
	 return true;
	}
   });
  
   prefCacheClear.setOnPreferenceClickListener(new OnPreferenceClickListener() {
    public boolean onPreferenceClick(Preference preference) {
    AlertDialog.Builder dialog = new AlertDialog.Builder(OptionsActivity.this);
    dialog.setTitle("Clear cache");
    dialog.setMessage("Are you sure that you want to clear the cache?");

    dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
     public void onClick(DialogInterface dialog, int which) {
	  dialog.cancel();
	 }
    });

   dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    public void onClick(DialogInterface dialog, int which) {
	 MainActivity.clearCache();
	 Toast.makeText(getApplicationContext(), "Cache cleared.", Toast.LENGTH_SHORT).show();   
    }
	});
	
	dialog.show();
	return true;
    }
    });
   }
  
  @Override
  protected void onResume(){
	super.onResume();
	getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
  }

  @Override
  protected void onPause() {
   super.onPause();
   getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
  } 

  @Override
  protected void onStop() {
   super.onStop();
   Core.scale(getApplicationContext(), Core.getValue(getApplicationContext(), "app_scale", "0"));
   MainActivity.data = new Core(MainActivity.context).data;
  }  

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
	Preference pref = findPreference(key);

	if (pref instanceof ListPreference) {
	  ListPreference listPref = (ListPreference) pref;
	  pref.setSummary(listPref.getEntry());
	  return;
	}
  }   
}
