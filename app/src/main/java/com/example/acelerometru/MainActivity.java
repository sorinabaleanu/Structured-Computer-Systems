package com.example.acelerometru;



import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements USBSerialListener {


        USBSerialConnector mConnector;
        private ProgressBar progressBar;
        private int progressStatus = 0;
        private TextView textView;
        private Handler handler = new Handler();
        EditText rxTEXT;


        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            mConnector = USBSerialConnector.getInstance();
            rxTEXT = (EditText) findViewById(R.id.rxText);
            rxTEXT.setEnabled(false);

            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            textView = (TextView) findViewById(R.id.textView);

            new Thread(new Runnable() {
                public void run() {
                    while (progressStatus < 255) {
                        progressStatus += 1;

                        handler.post(new Runnable() {
                            public void run() {
                                progressBar.setProgress(progressStatus);
                                textView.setText(progressStatus+"/"+progressBar.getMax());
                            }
                        });
                        try {

                            Thread.sleep(200);
                        } catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

    @Override
    public void onDataReceived(byte[] data) {

    }

    @Override
    public void onErrorReceived(String data) {
        Toast.makeText(this, data, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeviceDisconnected() {

        Toast.makeText(this, "Device disconnected", Toast.LENGTH_LONG).show();
        finish();

    }


    public void onDeviceReady(ResponseStatus responseStatus) {
        rxTEXT.setEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mConnector.setUsbSerialListener(this);
        mConnector.init(this, 115200);
    }
    @Override
    public void onPause() {
        super.onPause();
        mConnector.pausedActivity();
    }

}


