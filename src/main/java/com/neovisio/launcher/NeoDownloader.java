package com.neovisio.launcher;

import android.webkit.DownloadListener;
import android.net.Uri;
import android.app.DownloadManager;
import android.app.DownloadManager.*;
import android.content.Context;
import android.os.*;
import android.app.Dialog;
import android.app.*;
import android.content.DialogInterface;
import android.widget.*;
import java.io.*;

public class NeoDownloader implements DownloadListener {
  private Context context;

  NeoDownloader(Context context) {
   this.context = context;
  }
  
  public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype, final long contentLength) {
	try {
   final Core.UserData data = new Core(context).data;
   AlertDialog.Builder dialog = new AlertDialog.Builder(context);
   dialog.setTitle(MainActivity.self.getTitle());
   dialog.setMessage("Download " + MainActivity.self.getTitle() + " (" + (contentLength / (1024 * 1024)) + "MB) ?");
   
   dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	public void onClick(DialogInterface dialog, int which) {
	 Toast.makeText(context, url, 3000).show();
	 DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
     String app = MainActivity.self.getTitle() + ".apk";
	 File direct = new File(Environment.getExternalStorageDirectory()
						   + "/neovisio_files");

	 if (!direct.exists())
	  direct.mkdirs();
	 
	 
	 if(data.networkPolicy.equals("1"))
	   request.setAllowedNetworkTypes(request.NETWORK_WIFI);
	 else
	 if(data.networkPolicy.equals("2"))
	  request.setAllowedNetworkTypes(request.NETWORK_MOBILE);

	 request.setMimeType("application/vnd.android.package-archive");
	 request.setDescription("Downloading application...");
	 request.setTitle(app);
	 request.setDestinationInExternalPublicDir("/neovisio_files", app);
	 request.allowScanningByMediaScanner();
	 request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
	 MainActivity.download = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
	 MainActivity.file.id = MainActivity.download.enqueue(request);
	 MainActivity.file.name = MainActivity.self.getTitle().toString();
	 MainActivity.file.size = contentLength;
	 MainActivity.file.uri = Environment.getExternalStoragePublicDirectory("/neovisio_files") + "/" + app;
	}
   });
   
   dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
	public void onClick(DialogInterface dialog, int which) {
	 dialog.dismiss();
	}
   });
   
    dialog.show();
	}
	catch(Exception e) {Toast.makeText(context, e.getMessage(), 3000).show();}
  }
 }
