package com.neovisio.launcher;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import android.view.View.*;
import android.view.animation.*;

public class SplashActivity extends Activity {
  private Button ButtonNext;
  private Button ButtonStart;
  
  private void goNext() {
   View Splash = (RelativeLayout) findViewById(R.id.Splash_1);
   View Start = (RelativeLayout) findViewById(R.id.Splash_2);
   
   Animation animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
   Splash.startAnimation(animFadeOut);
   Splash.setVisibility(View.GONE);
   
   Start.setVisibility(View.VISIBLE);
   Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
   Start.startAnimation(animFadeIn);
  }
  
  private void startApp() {
   Animation animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
   ((RelativeLayout) findViewById(R.id.Splash_2)).startAnimation(animFadeOut);
   
   Core.saveValue(getApplicationContext(), "app_first_run", "false");
   Core.saveConfig(getApplicationContext());
   
   Intent Main = new Intent(this, MainActivity.class);
   startActivity(Main);
   finish();
  }
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
	Core.theme(getApplicationContext(), this);
	
	super.onCreate(savedInstanceState);
	setContentView(R.layout.splash);
	
	View Splash = (RelativeLayout) findViewById(R.id.Splash_1);
	Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
	Splash.startAnimation(animFadeIn);
	
    ButtonNext = (Button) findViewById(R.id.SplashNext);
	ButtonStart = (Button) findViewById(R.id.SplashButton);
	  
	ButtonNext.setOnClickListener(new OnClickListener() {
	 @Override
	 public void onClick(View view) {
	  goNext();
	 }
	});
	
	ButtonStart.setOnClickListener(new OnClickListener() {
	 @Override
	 public void onClick(View view) {
	  startApp();
	 }
	});
   }
  
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
	if(keyCode == KeyEvent.KEYCODE_BACK) {
	 moveTaskToBack(true);
	 return true;
	}

	return super.onKeyDown(keyCode, event);
  }
}
