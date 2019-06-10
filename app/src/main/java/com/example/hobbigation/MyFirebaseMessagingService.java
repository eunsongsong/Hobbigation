package com.example.hobbigation;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * 메시지를 수신하여 Notification으로 알림해주는 클래스
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    // 메시지 수신
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i(TAG, "onMessageReceived");

        String title = remoteMessage.getNotification().getTitle();
        String messagae = remoteMessage.getNotification().getBody();

        sendNotification(title, messagae);
    }

    //FCM 메세지를 포함하는 알림 생성
    private void sendNotification(String title, String message) {

        String channelId = "channel";
        String channelName = "Channel Name";

        NotificationManager notifManager = (NotificationManager) getSystemService  (Context.NOTIFICATION_SERVICE);

        //채널 생성
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notifManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
        Intent notificationIntent = new Intent(this, BeforeSignin.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        int requestID = (int) System.currentTimeMillis();

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentTitle(title)
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_ALL) // 알림, 사운드 진동 설정
                .setAutoCancel(true) // 알림 터치시 반응 후 삭제
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.logo)  //아이콘 설정
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo))
                .setBadgeIconType(R.mipmap.logo2)
                .setContentIntent(pendingIntent);

        notifManager.notify(0, builder.build());
    }
}