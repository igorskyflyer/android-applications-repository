package com.neovisio.launcher;

import android.app.*;
import android.os.*;
import android.view.*;
import android.content.Context;
import android.graphics.Bitmap;
//import android.view.Window;
import android.widget.*;
import android.text.*;

public class IconDownloader extends AsyncTask<String, Integer, Bitmap> {
 private Context context;
 public String activity;
 public String title;
 public Bitmap icon;
 public String url;
 public Dialog dialog = new Dialog(MainActivity.self);
 
 IconDownloader(Context context, String activity, String title, String url)   {
  this.context = context;
  this.activity = activity;
  this.title = title;
  this.url = url;
 }
 
 @Override
 protected void onPreExecute() {
  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
  dialog.setContentView(R.layout.dialog_shortcut);
  ((EditText) dialog.findViewById(R.id.dialogName)).addTextChangedListener(new TextWatcher() {
   @Override
   public void onTextChanged(CharSequence s, int start, int before, int count) {
	((Button) dialog.findViewById(R.id.dialogOk)).setEnabled(count > 0);
   }
   
	 @Override
	 public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
	 
	 @Override
	 public void afterTextChanged(Editable s) {}
  });
  ((Button) dialog.findViewById(R.id.dialogOk)).setEnabled(false);
  dialog.findViewById(R.id.dialogLoading).setVisibility(View.VISIBLE);
  dialog.findViewById(R.id.dialogIcon).setVisibility(View.GONE);
  dialog.show();
 }

 @Override
 protected Bitmap doInBackground(String... param) {
  return Core.downloadIcon(param[0]);
 }

 @Override
 protected void onProgressUpdate(Integer... progress) {}

 @Override
 protected void onPostExecute(Bitmap result) {
  dialog.findViewById(R.id.dialogLoading).setVisibility(View.GONE);
  dialog.findViewById(R.id.dialogIcon).setVisibility(View.VISIBLE);
  Core.addToHomescreen(context, activity, title, result, url, dialog);
 }
}
 
