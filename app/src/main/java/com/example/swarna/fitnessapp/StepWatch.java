package com.example.swarna.fitnessapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swarna.clapapp.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by swarnav on 4/11/16.
 */

public class StepWatch extends Activity implements SensorEventListener {
    //flag to detect flash is on or off




    TextView walksteps;
    TextView calories;
    TextView miles;
    SensorManager mySensorManager;
    Sensor StepCounter;
    Sensor StepDetector;
    int counterPrev;
    int walkcounter;
    float cal;
    float mile;
    int i = 0;
    long startTime;
    Date startDate;
    int totalStepsvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout);

        walksteps = (TextView) findViewById(R.id.textView1);
        calories = (TextView) findViewById(R.id.textView2);
        miles = (TextView) findViewById(R.id.textView3);
        Context context = this;
        mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        StepCounter =  mySensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        StepDetector = mySensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);


    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        int value = -1;


        if (values.length > 0) {
            value = (int) values[0];
            totalStepsvalue = value;
        }
        if(i == 0) {
            counterPrev = value;
            i++;
        }
        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            walkcounter = value - counterPrev;
            cal = (float) walkcounter/25;
            mile = (float) walkcounter/2000;
            walksteps.setText("Steps Walked : " + walkcounter);
            calories.setText("Calories Burnt : " + cal );
            miles.setText("Miles Covered : " + mile);
        }

    }


    @Override
    protected void onResume() {

        super.onResume();

    }


    @Override
    protected void onStop() {

        super.onStop();
    }
    public void startCounter(View v) {
        Calendar c = Calendar.getInstance();

        Toast.makeText(getBaseContext(),
                "Workout started. Start walking!" + c.getTime(),
                Toast.LENGTH_LONG).show();
        startTime = c.getTimeInMillis();
        startDate = c.getTime();
        walkcounter = 0;
        i=0;
        mySensorManager.registerListener(this, StepCounter, SensorManager.SENSOR_DELAY_FASTEST);
        mySensorManager.registerListener(this, StepDetector, SensorManager.SENSOR_DELAY_FASTEST);

    }

    public void stopCounter(View v) {
        String timediff="";
        Calendar c1 = Calendar.getInstance();

        long diff = c1.getTimeInMillis() - startTime;
        int seconds = (int) (diff / 1000) % 60 ;
        int minutes = (int) ((diff / (1000*60)) % 60);
        int hours   = (int) ((diff / (1000*60*60)) % 24);
        if(hours > 0 ){
            timediff += hours + " hrs";
        }
        if(minutes > 0){
            timediff += minutes + " min";
        }
        if(seconds > 0){
            timediff += seconds + " sec";
        }
        DataController dataController=new DataController(getBaseContext());
        dataController.open();
       boolean retValue= dataController.insert(startDate.toString(),String.valueOf(timediff),String.valueOf(walkcounter),String.valueOf(cal),String.valueOf(mile));

        dataController.close();
        Toast.makeText(getBaseContext(),
                "Walking finished. Workout Details Saved!" + c1.getTime() + "total time:"+timediff,
                Toast.LENGTH_LONG).show();

        Random r = new Random();
        int hbr = r.nextInt(100 - 60) + 60;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://nivedithajh.com/CaloriePlus/login.html?steps="+String.valueOf(walkcounter)+"&calburnt="+String.valueOf(cal)+"&hbr="+String.valueOf(hbr)));
        startActivity(browserIntent);
        mySensorManager.unregisterListener(this, StepCounter);
        mySensorManager.unregisterListener(this, StepDetector);

    }
    public void getPrevWorkouts(View v)
    {

        Intent intent = new Intent(StepWatch.this, WorkoutDialog.class);
        intent.putExtra("param","empty");
        startActivity(intent);

    }
    public void getTotalStat(View v)
    {
        float totalcal = (float)counterPrev / 25;
        float totalmile = (float)counterPrev /2000;
        String message = "Total steps taken: "+ counterPrev + "\n Total calories burnt: "+totalcal+"\n Total miles covered: "+totalmile;
        System.out.println("Total Burndown: "+message);
        Intent intent = new Intent(StepWatch.this, WorkoutDialog.class);
        intent.putExtra("param",message);
        startActivity(intent);
    }
    public void closeApp(View view)
    {
        this.finish();
        System.exit(0);
    }
    public void startMainActivity(View v) {
        Intent intent = new Intent(StepWatch.this, MainActivity.class);
        startActivity(intent);
    }
    public void finishStep(View v) {
        StepWatch.this.finish();
    }

}
