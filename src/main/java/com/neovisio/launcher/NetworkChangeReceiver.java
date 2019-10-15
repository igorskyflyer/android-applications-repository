package com.neovisio.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {

 @Override
 public void onReceive(final Context context, final Intent intent) {
  /*if(!!Core.isOnline(context))
   Core.offline(context);
  else {
   Intent main = new Intent(context.getApplicationContext(), MainActivity.class);
   context.startActivity(main);
  }*/
 }
}
