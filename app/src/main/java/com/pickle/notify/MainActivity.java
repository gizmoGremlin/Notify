package com.pickle.notify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static String CHANNEL_ID = "TEST_CHANNEL";
    private Button notifyButton;
    private Button cancelButton;
    private Button updateButton;
    private final String ACTION_UPDATE_NOTIFICATION = BuildConfig.APPLICATION_ID + ".ACTION_UPDATE_NOTIFICATION";
    private NotificationReciever reciever = new NotificationReciever();
    private static final int  NOTIFICATION_ID = 0;
   private NotificationManager notifyManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerReceiver(reciever, new IntentFilter(ACTION_UPDATE_NOTIFICATION));
        setNotificationState(true,false,false);
        notifyButton = findViewById(R.id.btn_notify);
        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });

        cancelButton = findViewById(R.id.btn_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelNotification();
            }
        });
        updateButton = findViewById(R.id.btn_update);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNotification();
            }
        });



    createNotificationChannel();
    }


    public void updateNotification(){
        Bitmap androidImage = BitmapFactory
                .decodeResource(getResources(), R.drawable.mascot_1);

        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        notifyBuilder.setStyle(new NotificationCompat.BigPictureStyle()
        .bigPicture(androidImage)
        .setBigContentTitle("Updated!"));
        notifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
        setNotificationState(false,false,true);

    }
    public void cancelNotification(){
        notifyManager.cancel(NOTIFICATION_ID);
        setNotificationState(true, false, false);
    }

    public void sendNotification(){
        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast(this,NOTIFICATION_ID,updateIntent,PendingIntent.FLAG_ONE_SHOT);



      NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
      notifyBuilder.addAction(R.drawable.ic_update,"update Notification",updatePendingIntent);
      notifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
      setNotificationState(false,true, true);
    }

    private void createNotificationChannel() {
       notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
       if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
           NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,"Wu-tang Forever", NotificationManager.IMPORTANCE_HIGH);
           notificationChannel.enableLights(true);
           notificationChannel.setLightColor(Color.RED);
           notificationChannel.enableVibration(true);
           notificationChannel.setDescription("notified C.R.E.A.M");
           notifyManager.createNotificationChannel(notificationChannel);

       }

    }

    private NotificationCompat.Builder getNotificationBuilder(){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,NOTIFICATION_ID,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Wu Tang Notify")
                .setContentText("This is a notification")
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_cake);

      return notifyBuilder;

    }

    void setNotificationState(Boolean isNotifyEnabled, Boolean isUpdateEnabled, Boolean isCancelEnabled){
        notifyButton.setEnabled(isNotifyEnabled);
        updateButton.setEnabled(isUpdateEnabled);
        cancelButton.setEnabled(isCancelEnabled);

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(reciever);
        super.onDestroy();
    }

    public class NotificationReciever extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            updateNotification();
        }
    }

}
