package com.example.swarna.fitnessapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.swarna.clapapp.R;

/**
 * Created by swarnav on 4/11/16.
 */



public class BmiCalculator extends Activity {

    EditText feetvalue;
    EditText inchvalue;
    EditText poundvalue;
    TextView bmivalue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bmi);
        bmivalue = (TextView) findViewById(R.id.bmivalue);
        bmivalue.setText("");
    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onStop() {
        super.onStop();

    }

    public void computeBMI(View v){
      feetvalue = (EditText)findViewById(R.id.feet_value);
      inchvalue = (EditText)findViewById(R.id.inch_value);
      poundvalue = (EditText)findViewById(R.id.lb_value);
        float feet = Float.valueOf(feetvalue.getText().toString());
        float inch = Float.valueOf(inchvalue.getText().toString());
        float pound = Float.valueOf(poundvalue.getText().toString());
        inch = inch + ( feet * 12 );
        float bmi = ( pound * 703 ) / ( inch * inch );

        bmivalue.setText("Your Body Mass Index is : "+ bmi);

    }
    public void startMainActivity(View v) {
        Intent intent = new Intent(BmiCalculator.this, MainActivity.class);
        startActivity(intent);
    }


    public void finishBMI(View v) {
        BmiCalculator.this.finish();
    }
}
