package app.gobusiness.com.androidnotificationdivyanshu;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.renderscript.RenderScript;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MyFirebaseMessagingServices extends FirebaseMessagingService
{
    int id = 1;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) 
    {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "From:"+ remoteMessage.getFrom());

        if (remoteMessage.getData().size()>0)
        {
            Log.d(TAG, "Message Data PayLoad: "+remoteMessage.getData());
            sendNotification(remoteMessage.getData());
        }

        else if (remoteMessage.getNotification() != null)
        {
            Log.d(TAG, "Message Notification Body"+remoteMessage.getNotification().getBody());
        }

    }


////////////////////genereate notification using postman
    private void notificationBuild(Map<String,String> messageBody)
    {
        Log.d("MEssaging","building notification");
        Bitmap bitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.stalwart)).getBitmap();





        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent =  PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);


        String channel_id = getString(R.string.default_notification_channel_id);
        Log.d("MEssaging","channel id: "+channel_id);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channel_id)
                .setSmallIcon(R.drawable.stalwart)
                .setContentText(messageBody.get("text"))
                .setContentTitle(messageBody.get("title"))
                .setLargeIcon(bitmap)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setNumber(2)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);



        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            StatusBarNotification[] notifications = notificationManager.getActiveNotifications();

            for (int i=0; i < notifications.length ; i++) {
                if (notifications[i].getPackageName().equals(getApplicationContext().getPackageName())) {

                    NotificationChannel channel = new NotificationChannel(channel_id, "Channel human readable title", NotificationManager.IMPORTANCE_HIGH);
                    channel.setShowBadge(true);
                   channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                    notificationManager.createNotificationChannel(channel);
                }
            }
        }

        notificationManager.notify((int)System.currentTimeMillis() /* ID of notification */, notificationBuilder.build());

    }



    private void sendNotification(Map<String,String> messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_small_notification)
                        .setContentTitle(messageBody.get("text"))
                        .setContentText(messageBody.get("title"))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_HIGH);

            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN",s);
    }

}
