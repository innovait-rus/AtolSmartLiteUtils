
/////////////////////////////////////////////////////////////////////////////////////////////////////
//
// Examples for the report "Making external components for 1C mobile platform for Android""
// at the conference INFOSTART 2018 EVENT EDUCATION https://event.infostart.ru/2018/
//
// Sample 1: Delay in code
// Sample 2: Getting device information
// Sample 3: Device blocking: receiving external event about changing of sceen
//
// Copyright: Igor Kisil 2018
//
/////////////////////////////////////////////////////////////////////////////////////////////////////

package org.innovait.atolsmartliteutils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class MainApp implements Runnable {

  // in C/C++ code the function will have name Java_org_innovait_atolsmartliteutils_MainApp_OnBroadcastReceive
  static native void OnBroadcastReceive(long pObject, String event,  String type, String bc);

  private static final String NEW_KEY_UP = "org.innovait.action.NEW_KEY_UP";
  private static final String KEY_CODE = "KEY_CODE";

  private long m_V8Object; // 1C application context
  private Activity m_Activity; // custom activity of 1C:Enterprise
  private BroadcastReceiver m_Receiver;


  public MainApp(Activity activity, long v8Object)
  {
    m_Activity = activity;
    m_V8Object = v8Object;
  }

  public void run()
  {
    System.loadLibrary("org_innovait_AtolSmartLiteUtils");
  }

  public void show()
  {
    m_Activity.runOnUiThread(this);
  }

  public void startEventsWatch()
  {

    if (m_Receiver==null)
    {
      m_Receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
          if (intent != null) {
            String event, type, data;

            switch (intent.getAction()) {
              case "com.xcheng.scanner.action.BARCODE_DECODING_BROADCAST":
                event = "NewBarcode";
                type = intent.getStringExtra("EXTRA_BARCODE_DECODING_SYMBOLE");
                data = intent.getStringExtra("EXTRA_BARCODE_DECODING_DATA");
                OnBroadcastReceive(m_V8Object, event, type, data);
                break;
              case NEW_KEY_UP:
                event = "NewKeyUP";
                type = "key";
                data = intent.getStringExtra(KEY_CODE);
                OnBroadcastReceive(m_V8Object, event, type, data);
            }
          }
        }
      };

      IntentFilter filter = new IntentFilter();
      filter.addAction("com.xcheng.scanner.action.BARCODE_DECODING_BROADCAST");
      filter.addAction(NEW_KEY_UP);

      m_Activity.registerReceiver(m_Receiver, filter);
    }

  }

  public void stopEventsWatch()
  {
    if (m_Receiver != null)
    {
      m_Activity.unregisterReceiver(m_Receiver);
      m_Receiver = null;
    }
  }
}
