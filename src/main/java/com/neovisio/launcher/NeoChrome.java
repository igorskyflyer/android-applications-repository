package com.neovisio.launcher;

import android.webkit.*;

public class NeoChrome extends WebChromeClient {
  @Override
  public void onProgressChanged(WebView view, int newProgress) {			
   MainActivity.Bar.setProgress(newProgress);
   super.onProgressChanged(view, newProgress);
  }
 }
