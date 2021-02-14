package com.cyberking.developer.eh;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.cyberking.developer.eh.EH;
import com.cyberking.developer.eh.R;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;

public class EHActivity extends Activity {

  private String eFullLog;
  private String eStackTrace;
  private String eActivityLog;
  private String[] eEmailAddresses;

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    setTheme (R.style.EHTheme);

    super.onCreate (savedInstanceState);
    setContentView (R.layout.activity_eh_layout);
    getActionBar ().setDisplayUseLogoEnabled (false);

    Intent intent = getIntent ();

    if (intent != null) {
      eStackTrace = intent.getStringExtra (EH.EXTRA_STACK_TRACE);
      eActivityLog = intent.getStringExtra (EH.EXTRA_ACTIVITY_LOG);
      eEmailAddresses = intent.getStringArrayExtra (EH.EXTRA_EMAIL_ADDRESSES);
    }

    TextView textView = findViewById (R.id.activity_eh_layout_details_text_view_0);
    textView.setText (getMetaInfo ());

    textView = findViewById (R.id.activity_eh_layout_details_text_view_1);
    textView.setText (eActivityLog == null ? "" : eActivityLog);

    textView = findViewById (R.id.activity_eh_layout_details_text_view_2);
    textView.setText (eStackTrace == null ? "" : eStackTrace);
  }

  @Override
  public boolean onCreateOptionsMenu (Menu menu) {
    MenuItem item = menu.add (R.string.activity_eh_options_menu_copy);
    item.setShowAsAction (MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    item.setOnMenuItemClickListener (new MenuItem.OnMenuItemClickListener () {
        @Override
        public boolean onMenuItemClick (MenuItem p1) {
          copyLogToClipboard ();
          return true;
        }
      });

    item = menu.add (R.string.activity_eh_options_menu_save);
    item.setShowAsAction (MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    item.setOnMenuItemClickListener (new MenuItem.OnMenuItemClickListener () {
        @Override
        public boolean onMenuItemClick (MenuItem p1) {
          saveLogToFile ();
          return true;
        }
      });

    item = menu.add (R.string.activity_eh_options_menu_share);
    item.setShowAsAction (MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    item.setOnMenuItemClickListener (new MenuItem.OnMenuItemClickListener () {
        @Override
        public boolean onMenuItemClick (MenuItem p1) {
          shareLog ();
          return true;
        }
      });

    item = menu.add (R.string.activity_eh_options_menu_exit);
    item.setShowAsAction (MenuItem.SHOW_AS_ACTION_NEVER | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    item.setOnMenuItemClickListener (new MenuItem.OnMenuItemClickListener () {
        @Override
        public boolean onMenuItemClick (MenuItem p1) {
          onBackPressed ();
          return true;
        }
      });

    return super.onCreateOptionsMenu (menu);
  }

  @Override
  public void onBackPressed () {
    EH.closeApplication (this);
  }

  public void onSendErrorLogClick (View view) {
    Intent intent = new Intent (Intent.ACTION_SEND);

    intent.setType ("text/plain");
    intent.putExtra (Intent.EXTRA_EMAIL, eEmailAddresses);
    intent.addFlags (Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

    String packageName = getApplicationContext ().getPackageName ();

    intent.putExtra (Intent.EXTRA_SUBJECT, " EH crash log for [ " + packageName + " ]");
    intent.putExtra (Intent.EXTRA_TEXT, new StringBuffer (getString (R.string.activity_eh_email_message, packageName))
                     .append ("\n\n\n")                 
                     .append (getFullLog ())
                     .toString ());

    intent.addFlags (Intent.FLAG_GRANT_READ_URI_PERMISSION);
    startActivity (Intent.createChooser (intent, "Send log to developers with..."));
  }

  private void copyLogToClipboard () {
    ClipboardManager manager = (ClipboardManager) getSystemService (CLIPBOARD_SERVICE);

    if (manager != null) {
      manager.setPrimaryClip (ClipData.newPlainText ("EH crash log", getFullLog ()));
      Toast.makeText (this, "Copied log to clipboard!", Toast.LENGTH_SHORT).show ();
    }
  }

  private void saveLogToFile () {
    File ext = Environment.getExternalStorageDirectory ();

    try {
      if (Environment.getExternalStorageState ().equals (Environment.MEDIA_MOUNTED) && ext != null && ext.exists () && ext.canRead () && ext.canWrite () && ext.canExecute ()) {
        File dir = new File (ext, "EHLogs");
        dir.mkdirs ();

        File file = new File (dir, String.format ("%1$tH_%1$tM_%1$tS_%1$td_%1$tm_%1$tY__EA.LOG", new Date ()));
        PrintWriter writer = new PrintWriter (new FileWriter (file));

        writer.write (getFullLog ());
        writer.close ();

        Toast.makeText (getApplicationContext (), "Saved log to :\n" + file.getAbsolutePath (), Toast.LENGTH_LONG).show ();
      }
      else
        Toast.makeText (getApplicationContext (), "Log NOT saved.\n Storage access required!", Toast.LENGTH_LONG).show ();
    }
    catch (Throwable t) {
      Toast.makeText (getApplicationContext (), "Log NOT saved!\n" + t.getMessage (), Toast.LENGTH_LONG).show ();
    }
  }

  private void shareLog () {
    Intent intent = new Intent (Intent.ACTION_SEND);

    intent.setType ("text/plain");
    intent.addFlags (Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
    intent.putExtra (Intent.EXTRA_SUBJECT, "EH crash log");
    intent.putExtra (Intent.EXTRA_TITLE, "EH crash log");
    intent.putExtra (Intent.EXTRA_TEXT, getFullLog ());

    startActivity (Intent.createChooser (intent, "Share log with..."));
  }

  private String getMetaInfo () {
    StringBuffer buffer = new StringBuffer ();
    String del = "\n";

    buffer
      .append ("\tDEVICE INFORMATION")
      .append (del)
      .append (del)
      .append ("Brand        : ")
      .append (Build.BRAND)
      .append (del)
      .append ("Device       : ")
      .append (Build.DEVICE)
      .append (del)
      .append ("Model        : ")
      .append (Build.MODEL)
      .append (del)
      .append ("Manufacturer : ")
      .append (Build.MANUFACTURER)
      .append (del)
      .append ("Product      : ")
      .append (Build.PRODUCT.replace ('\n', ' '))
      .append (del)
      .append ("SDK          : ")
      .append (Build.VERSION.SDK)
      .append (del)
      .append ("Release      : ")
      .append (Build.VERSION.RELEASE)
      .append (del)
      .append ("CPU ABI      : ")
      .append (Build.CPU_ABI)
      .append (del)
      .append ("CPU ABI2     : ")
      .append (Build.CPU_ABI2)
      .append (del)
      .append ("Display      : ")
      .append (Build.DISPLAY)
      .append (del)
      .append ("Fingerprint  : ")
      .append (Build.FINGERPRINT)
      .append (del)
      .append ("Hardware     : ")
      .append (Build.HARDWARE)
      .append (del)
      .append ("Host         : ")
      .append (Build.HOST)
      .append (del)
      .append ("ID           : ")
      .append (Build.ID)
      .append (del);

    Context context = getApplicationContext ();

    try {
      PackageManager packageManager = context.getPackageManager ();
      PackageInfo packageInfo = packageManager.getPackageInfo (context.getPackageName (), PackageManager.GET_PERMISSIONS);

      buffer
        .append (del)
        .append ("\tAPP INFORMATION")
        .append (del)
        .append (del)
        .append ("Name         : ")
        .append (packageManager.getApplicationLabel (packageInfo.applicationInfo))
        .append (del)
        .append ("Version Code : ")
        .append (packageInfo.versionCode)
        .append (del)
        .append ("Version Name : ")
        .append (packageInfo.versionName)
        .append (del)
        .append ("Package Name : ")
        .append (packageInfo.packageName)
        .append (del)
        .append ("Installed On : ")
        .append (new Date (packageInfo.firstInstallTime))
        .append (del)
        .append ("Updated On   : ")
        .append (new Date (packageInfo.lastUpdateTime))
        .append (del)
        .append (del)
        .append ("Permissions")
        .append (del)
        .append (del);

      String[] perms = packageInfo.requestedPermissions;

      if (perms == null || perms.length < 1)
        buffer
          .append ("\tNONE")
          .append (del);
      else
        for (String permission : perms) {
          int isGranted = context.checkSelfPermission (permission);

          buffer
            .append ('\t')
            .append (permission)
            .append (" : ")
            .append (isGranted == PackageManager.PERMISSION_GRANTED ? "GRANTED" : isGranted  == PackageManager.PERMISSION_DENIED ? "DENIED" : "unknown")
            .append (del);
        }
    }
    catch (Throwable t) {
      buffer
        .append (" E: retrieving app info : ")
        .append (t)
        .append (del);
    }
    buffer
      .append (del)
      .append ("CRASHED ON   : ")
      .append (new Date (EH.getLastCrash (context)));

    return buffer.toString ();
  }

  private String getFullLog () {
    return eFullLog == null ? new StringBuffer ()
      .append (getMetaInfo ())
      .append ("\n\n\tACTIVITY LOG")
      .append (eActivityLog)
      .append ("\n\n\tSTACK TRACE\n\n")
      .append (eStackTrace)
      .toString ()

      : eFullLog;
  }
}
