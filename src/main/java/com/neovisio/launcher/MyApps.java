package com.neovisio.launcher;

import android.os.*;
import android.app.*;
import android.content.*;
import android.widget.RelativeLayout;
import android.view.animation.*;

public class MyApps extends Activity {
 @Override
 public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.myapps);
  
 if(new Core(getApplicationContext()).data.firstRun.equals("true")) {
  Intent splash = new Intent(this, SplashActivity.class);
  Core.saveValue(getApplicationContext(), "app_first_run", "false");
  Core.saveConfig(getApplicationContext());
  splash.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
  startActivity(splash);
  finish();
 }
  
  Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide);
  animation.setAnimationListener(new Animation.AnimationListener() {
   @Override
   public void onAnimationStart(Animation anim) {}
   public void onAnimationRepeat(Animation anim) {}
   public void onAnimationEnd(Animation anim) {
    Intent main = new Intent(getApplicationContext(), MainActivity.class);
    main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    main.putExtra("url", "neovisio://my-apps");
    startActivity(main);
    finish();
   }
  });
  
  ((RelativeLayout) findViewById(R.id.myApps)).startAnimation(animation);
 }
}
