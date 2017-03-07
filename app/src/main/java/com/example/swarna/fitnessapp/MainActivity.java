package com.example.swarna.fitnessapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.swarna.clapapp.R;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends Activity  {
    //flag to detect flash is on or off

    IntentFilter intentFilter;
    private DownloadService serviceBinder;

    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = this;



    }





    @Override
    protected void onResume() {

        super.onResume();


    }


    @Override
    protected void onStop() {

        super.onStop();
    }

    public void closeApp(View view)
    {
        this.finish();
        System.exit(0);
    }

    public void downloadPlan(View v){
        intentFilter = new IntentFilter();
        intentFilter.addAction("FILE_DOWNLOADED_ACTION");
        Intent intent = new Intent(getBaseContext(), DownloadService.class);
        Context context = this;
        try {

            String url1= context.getApplicationContext().getString(R.string.walk_url);

            String url2=context.getApplicationContext().getString(R.string.diet_url);

            System.out.println("urls values:"+url1+url2);
            URL[] urls = new URL[]{
                    new URL(url1),
                    new URL(url2)};
            intent.putExtra("URLs", urls);


        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        startService(intent);

    }
    public void startHeartActivity(View v) {
        Intent intent = new Intent(MainActivity.this, HeartRateMonitor.class);
        startActivity(intent);
    }
    public void startBMIIndex(View v){
        Intent intent = new Intent(MainActivity.this, BmiCalculator.class);
        startActivity(intent);
    }

    public void startStepCounter(View v){
        Intent intent = new Intent(MainActivity.this, StepWatch.class);
        startActivity(intent);
    }

    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(getBaseContext(), "File downloaded!",
                    Toast.LENGTH_LONG).show();
            unregisterReceiver(intentReceiver);
        }
    };

}
