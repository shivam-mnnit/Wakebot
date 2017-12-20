package com.example.dell.wakebot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by DELL on 27-09-2017.
 */

public class Alarm_Receiver extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("we are in the receiver.", "Yay!");

       //fetch extra string from intent
        String get_your_string = intent.getExtras().getString("extra");
        Log.e("what is the key? ", get_your_string);
        //create an intent to the ringtone service
        Intent service_intent= new Intent(context, RingtonePlayingService.class);

        //pass the  extra string from main activity to  the Rigtone Playing Services
        service_intent.putExtra("extra", get_your_string);

        //start the ringtone service
        context.startService(service_intent);

    }
}
