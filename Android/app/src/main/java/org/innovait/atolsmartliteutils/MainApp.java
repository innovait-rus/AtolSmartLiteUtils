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
  private static final String AtolSmartLite_BARCODE = "com.xcheng.scanner.action.BARCODE_DECODING_BROADCAST";
  private static final String DS2_BARCODE = "app.dsic.barcodetray.BARCODE_BR_DECODING_DATA";

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
              case AtolSmartLite_BARCODE:
                event = "NewBarcode";
                type = intent.getStringExtra("EXTRA_BARCODE_DECODING_SYMBOLE");
                data = intent.getStringExtra("EXTRA_BARCODE_DECODING_DATA");
                //Log.d("innova", type.toString());
                OnBroadcastReceive(m_V8Object, event, type, data);
                break;
              case DS2_BARCODE:
                event = "NewBarcode";
                type = intent.getStringExtra("EXTRA_BARCODE_DECODED_SYMBOLE");
                data = intent.getStringExtra("EXTRA_BARCODE_DECODED_DATA");
                //Log.d("innova", type);
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
      filter.addAction(AtolSmartLite_BARCODE);
      filter.addAction(DS2_BARCODE);
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
