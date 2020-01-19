package org.innovait.atolsmartliteutils;

import android.app.Activity;
import android.support.test.runner.AndroidJUnit4;

import org.innovait.atolsmartliteutils.MainApp;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

  @Test
  public void testSleep() {
    System.loadLibrary("org_innovait_AtolSmartLiteUtils");
    MainApp mainApp = new MainApp(null, 0);
    //mainApp.startEventsWatch();
  }
}
