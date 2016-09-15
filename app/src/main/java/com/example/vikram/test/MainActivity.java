package com.example.vikram.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.physicaloid.lib.Physicaloid;
import com.physicaloid.lib.usb.driver.uart.ReadLisener;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {
    Physicaloid mPhysicaloid; // initialising library
    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!OpenCVLoader.initDebug()) {
            Log.e(this.getClass().getSimpleName(), "  OpenCVLoader.initDebug(), not working.");
        } else {
            Log.d(this.getClass().getSimpleName(), "  OpenCVLoader.initDebug(), working.");
        }
        mPhysicaloid = new Physicaloid(this);
        et = (EditText) findViewById(R.id.speed);
    }

    protected void onResume() {
        super.onResume();
        mPhysicaloid.setBaudrate(9600);
        if (mPhysicaloid.open()) {
            mPhysicaloid.addReadListener(new ReadLisener() {
                @Override
                public void onRead(int size) {
                    byte[] buf = new byte[size];
                    mPhysicaloid.read(buf, size);
                    //tvAppend(tvRead, Html.fromHtml("<font color=blue>" + new String(buf) + "</font>"));
                }
            });
        } else {
            Toast.makeText(this, "Cannot open", Toast.LENGTH_LONG).show();
        }
    }

    public void close() {
        if (mPhysicaloid.close()) {
            mPhysicaloid.clearReadListener();
        }
    }


    public void write(String str) {
        str = str + "\r\n";
        if (str.length() > 0) {
            byte[] buf = str.getBytes();
            mPhysicaloid.write(buf, buf.length);
        }
    }

    protected void onPause() {
        super.onPause();
        close();
    }

    public void up(View view) {
        String speed = "up," + et.getText().toString();
        write(speed);
    }

    public void down(View view) {
        String speed = "down," + et.getText().toString();
        write(speed);
    }

    public void right(View view) {
        String speed = "right," + et.getText().toString();
        write(speed);
    }

    public void left(View view) {
        String speed = "left," + et.getText().toString();
        write(speed);
    }
    public void stop(View view) {
        String speed = "stop,0" + et.getText().toString();
        write(speed);
    }
    // vid = 9025
    // pid = 67
}
