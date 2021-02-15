package com.cyberking.developer.eh.test;

import android.app.Activity;
import android.os.Bundle;
import com.cyberking.developer.eh.EH;

public class MainActivity extends Activity {

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    new EH.Builder (getApplicationContext ())
      .addEmailAddresses ("email0@gmail.com", "email1@gmail.com", "email2@gmail.com")
      .init ();

    super.onCreate (savedInstanceState);
    setContentView (R.layout.main);
  }
}
