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
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MyFirebaseMessagingServices extends FirebaseMessagingService
{
    int id = 1;
   // public static final String channel_id="app.gobusiness.com.androidnotificationdivyanshu";
    public static final String channel_name="Android Norification";
    private static final int NOTIFICATION_ID = 1593;





    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) 
    {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "From:"+ remoteMessage.getFrom());

        if (remoteMessage.getData().size()>0)
        {
            Log.d(TAG, "Message Data PayLoad: "+remoteMessage.getData());
            notificationBuild(remoteMessage.getData());
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
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channel_id,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("NEW_TOKEN",s);
    }

}
