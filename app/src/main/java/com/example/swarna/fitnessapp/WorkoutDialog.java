package com.example.swarna.fitnessapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.swarna.clapapp.R;

import java.util.ArrayList;

/**
 * Created by swarnav on 4/12/16.
 */

public class WorkoutDialog extends Activity {
    //flag to detect flash is on or off

    public String startdate;
    public String duration;
    public String steps;
    public String calburned;
    public String mileswalked;
    GridView workout;


    private Button button;
    TextView totalstats;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog);
        totalstats = (TextView)findViewById(R.id.txt_stats);
        Intent intent = getIntent();
        String parameter = intent.getStringExtra("param");
        Context context = this;
        if(parameter.equals("empty")) {

            ArrayList<WorkoutDialog> walklist = new ArrayList<WorkoutDialog>();
            DataController dataController = new DataController(getBaseContext());
            dataController.open();
            walklist = dataController.fetchdata();
            dataController.close();
            StringBuilder workstring = new StringBuilder();
            for (WorkoutDialog obj : walklist) {
                i++;
                System.out.print("date:"+obj.startdate);
                 workstring.append("\nWorkout" + i + ": " + obj.startdate + "\n");
                 workstring.append("Duration: "+obj.duration+ ", Steps Walked: "+obj.steps+"\n");
                 workstring.append("Calories: "+obj.calburned+", Miles: "+obj.mileswalked+"\n");
            }
            totalstats.setText(workstring);
        }
        else
        {
            totalstats.setText(parameter);
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

    public void finishDialog(View v) {
        WorkoutDialog.this.finish();
    }



}
