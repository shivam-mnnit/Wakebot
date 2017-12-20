package com.example.dell.wakebot;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by DELL on 27-09-2017.
 */

public class RingtonePlayingService extends Service {

    MediaPlayer media_song;
    int startId;
    boolean isRunning;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Local Service", "Received Start Id " + startId + ": " + intent);

        //fetch the extra string values
        String state = intent.getExtras().getString("extra");


        //this converts the extra strings from intent to start Ids values 0/1
        assert state != null;
        switch (state) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }

       //if else statements
        //if there is no music playing and user sets the "alarm on"
        //music should start playing
        if(!this.isRunning && startId == 1) {

            //create an instance of the media player
            media_song = MediaPlayer.create(this, R.raw.dove);
           //start the ringtone
            media_song.start();
            //now the music is running
            this.isRunning=true;
            this.startId=0;

            //set up the notification service
            NotificationManager notify_manager= (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);
            //set up an intent that goes to the main activity
            Intent intent_main_activity = new Intent(this.getApplicationContext(), MainActivity.class);

            //set up a pending intent
            PendingIntent pending_intent_main_activity=PendingIntent.getActivity(this, 0, intent_main_activity, 0);

            //make the notification parameters
            Notification notification_popup= new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.launch)
                    .setContentTitle("An alarm is running!")
                    .setContentText("Click me!")
                    .setContentIntent(pending_intent_main_activity)
                    .setAutoCancel(true)
                    .build();

            //set up the notification call command
            notify_manager.notify(0, notification_popup);
            Vibrator v2 = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v2.vibrate(new long[]{0, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500}, -1);
        }
        //if there is music playing and user set the "alarm off"
        //music should stop playing
        else if(this.isRunning && startId == 0) {
        //stop the ringtone
            media_song.stop();
            media_song.reset();

            this.isRunning=false;
            this.startId=0;
        }
        //if user presses random buttons
        //if there is no music playing and the user presses the "alarm off" button
        //do nothing
        else if(!this.isRunning && startId == 0) {


            this.isRunning=false;
            this.startId=0;

        }
        //if there is music playing and he presses the "alarm on" button
       //do nothing
        else if(this.isRunning && startId == 1) {
          this.isRunning=true;
            this.startId=1;

        }
        else {
       Log.e("nbfss", "bskncw");
        }

        return START_NOT_STICKY;
    }
@Override
    public void onDestroy()
{
   //tell the user we stopped
    Log.e("on destroy called", "ta da");
    super.onDestroy();
    this.isRunning=false;


}

}
