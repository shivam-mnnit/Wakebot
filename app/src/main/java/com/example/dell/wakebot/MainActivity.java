package com.example.dell.wakebot;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    TextView update_text;
    Context context;
    PendingIntent pending_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context=this;

        //initialise alarm manager

        alarm_manager=(AlarmManager) getSystemService(ALARM_SERVICE);

        //initialise our timepicker

        alarm_timepicker=(TimePicker) findViewById(R.id.timePicker);

        //initialise our text update box

        update_text=(TextView) findViewById(R.id.update_Text);
        int _id = (int)System.currentTimeMillis();
        //create an instance of a calender
        final Calendar calendar=Calendar.getInstance();
        final Calendar curCalendar = Calendar.getInstance();



        //create an intent to the alarmreceiver class
        final Intent my_intent=new Intent(this.context, Alarm_Receiver.class);


        //initialise start button
        Button startAlarm=(Button) findViewById(R.id.startAlarm);


        // create an onclick listener to set alarm
       startAlarm.setOnClickListener(new View.OnClickListener() {
           @RequiresApi(api = Build.VERSION_CODES.M)
           @Override
           public void onClick(View v) {

              //setting calendar instance with hour and minute we pick

               //to deal with the 10 min early alarm problem

               curCalendar.set(Calendar.SECOND, 0);
               curCalendar.set(Calendar.MILLISECOND, 0);


               //on the time picker
               calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
               calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());

               //If Alarm Time is now or in the past, set it for tomorrow 24 hours in advance from time selected
               if (calendar.getTimeInMillis() < curCalendar.getTimeInMillis()) {
                   calendar.add(Calendar.HOUR, 24);
               }

               //get the int values of hour and minute
               int hour=alarm_timepicker.getHour();
               int minute=alarm_timepicker.getMinute();

               //convert integer to string
               String hour_string=String.valueOf(hour);
               String minute_string=String.valueOf(minute);

               //convert 24 hr to 12hr  time clock
               if(hour>12)
               {
               hour_string=String.valueOf(hour-12);
               }
               //10:7-->10:07
               if(minute<10)
               {
                   minute_string= "0" + String.valueOf(minute);
               }
               //method that changes the update text
               set_alarm_text("Alarm set to :" + hour_string + ":" + minute_string);

               Intent revintent = new Intent(MainActivity.this, Main2Activity.class);
               startActivity(revintent);
               //put in extra string in my_intent
               //tells the clock that you pressed "alarm ON" button
               my_intent.putExtra("extra", "alarm on");

               //create a pending intent that delays the intent
               //until the specified calendar time

               pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

               //set the alarm manager
               alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);

           }
           
       });

        //initialise stop button

        Button endAlarm=(Button) findViewById(R.id.endAlarm);
        //initialise an oncliclistener to unset alarm
        endAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //method that changes the update text

                set_alarm_text("Alarm is off!");
                pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                //cancel the alarm
                alarm_manager.cancel(pending_intent);

                // put extra string into my_intent
                //tells the clock that you pressed alarm off button
                my_intent.putExtra("extra", "alarm off");

                //stop the ringtone
                sendBroadcast(my_intent);
            }

        });

    }

    private void set_alarm_text(String s) {

        update_text.setText(s);
    }



}

