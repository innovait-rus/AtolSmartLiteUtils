package org.innovait.atolsmartliteutils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainApp mainApp = new MainApp(this, 0);
        mainApp.show();
        mainApp.startEventsWatch();
    }
}

