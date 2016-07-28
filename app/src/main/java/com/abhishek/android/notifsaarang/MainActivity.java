package com.abhishek.android.notifsaarang;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.Calendar;

//TODO - do something to change the time of the notification
public class MainActivity extends AppCompatActivity {

    private NotificationCompat.Builder mBuilder;
    private PendingIntent resultPendingIntent;
    public static final String ACTION = "com.abhishek.android.notifsaarang.A";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        int status = GoogleApiAvailability.isGooglePlayServicesAvailable(this);
//        if(status != ConnectionResult.SUCCESS) {
//            if(status == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED){
//                Toast.makeText(this,"please udpate your google play service",Toast.LENGTH_SHORT).show();
//            }
//            else {
//                Toast.makeText(this, "please download the google play service", Toast.LENGTH_SHORT).show();
//            }
//        }
        if(!checkPlayServices()){
            Toast.makeText(getApplicationContext(),"OUT OF date GPS",Toast.LENGTH_SHORT).show();
            return;
        }
            //TODO- haven't tried this yet.

        String shortmsg = "Open this Notification";

        mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setContentTitle("Hello World!")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_notif))
                .setSmallIcon(R.drawable.ic_backspace_white_24dp)
                .setContentText(shortmsg);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ACTION);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 5*1000, alarmIntent);
    }
//Right now, the notification comes only on pressing the button. But this function can be used elsewhere as well.
    public void notify(View view) {
        String newactlongmsg = "This is a primitive notification for opening an entirely new activity which " +
                "is not linked to the flow of the mobile application of Saarang 2017.";
        String secactlongmsg = "This is a primitive notification for opening an activity which " +
                "is linked to the flow of the mobile application of Saarang 2017, and forms the " +
                "secondary activity which has the main activity as its parent.";
        int mNotificationId = 1; //just to give a default
        Intent newIntent;
        if (view.getId() == R.id.b_newact) {
            mNotificationId = 1;
            newIntent = new Intent(this, IndepActivity.class);
            resultPendingIntent = PendingIntent.getActivity(this, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(newactlongmsg));
            mBuilder.setCategory(NotificationCompat.CATEGORY_ALARM);  //TODO - don't know how to use this
            mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);    // TODO - doesn't seem to be working properly
        } else if (view.getId() == R.id.b_secact) {
            mNotificationId = 2;
            newIntent = new Intent(this, SecondaryActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this); // Adds the back stack
            stackBuilder.addParentStack(SecondaryActivity.class); // Adds the Intent to the top of the stack
            stackBuilder.addNextIntent(newIntent); // Gets a PendingIntent containing the entire back stack
            resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(secactlongmsg));
            mBuilder.setPriority(NotificationCompat.PRIORITY_MIN);
        }
/*
        //To create a summary notification
        final static String GROUP_KEY_EMAILS = "group_key_emails";
        NotificationCompat.Builder summaryNotification = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setContentTitle("2 new messages")
                .setSmallIcon(R.drawable.ic_notif)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_notif))
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine("Alex Faaborg   Check this out")
                        .addLine("Jeff Chang   Launch Party")
                        .setBigContentTitle("2 new messages")
                        .setSummaryText("johndoe@gmail.com"))
                .setGroup(GROUP_KEY_EMAILS)
                .setGroupSummary(true);
*/
        mBuilder.setContentIntent(resultPendingIntent);

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

        ((Button) view).setText("Notified!");
    }

    public void createAlarmNotification(View view) {
        //Set the alarm to 10 seconds from now
//        Calendar c = Calendar.getInstance();
//        c.add(Calendar.SECOND, 10);
//        long firstTime = c.getTimeInMillis();
//        // Schedule the alarm!
//        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//        am.set(AlarmManager.RTC_WAKEUP, firstTime, mAlarmSender);


    }
}
