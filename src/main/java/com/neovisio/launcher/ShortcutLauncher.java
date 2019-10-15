package com.neovisio.launcher;

import android.app.*;
import android.os.*;
import android.view.*;
import android.content.*;
import android.app.Activity.*;
import android.view.animation.*;
//import android.view.animation.AnimationUtils;
import android.widget.*;
import android.view.animation.Animation.AnimationListener;
import android.graphics.*;
//import android.widget.Toast;

public class ShortcutLauncher extends Activity {
 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
   
  requestWindowFeature(Window.FEATURE_NO_TITLE);
  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
  
  setContentView(R.layout.shortcut);
  
  RelativeLayout shortcut = (RelativeLayout) findViewById(R.id.shortcut);
  ImageView icon = (ImageView) findViewById(R.id.shortcutIcon);
  TextView label = (TextView) findViewById(R.id.shortcutLabel);
  final Intent main = new Intent(getApplicationContext(), MainActivity.class);
  
  if(getIntent().hasExtra("icon"))
   icon.setImageBitmap((Bitmap) getIntent().getParcelableExtra("icon"));
   
  if(getIntent().hasExtra("title"))
   label.setText(getIntent().getStringExtra("title").toUpperCase());
  
  String url = getIntent().getStringExtra("url");  
  main.putExtra("url", url);
  
  Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide);
  slide.setDuration(1400);
  slide.setAnimationListener(new Animation.AnimationListener() {
   @Override
   public void onAnimationStart(Animation animation) {}
    
   @Override
   public void onAnimationRepeat(Animation animation) {}
  
   @Override
   public void onAnimationEnd(Animation animation) {
    startActivity(main);
	finish();
   }
  });
   shortcut.startAnimation(slide);
 }
}
