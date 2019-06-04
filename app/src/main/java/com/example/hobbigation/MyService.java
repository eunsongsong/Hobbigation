package com.example.hobbigation;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MyService extends Service {
    NotificationManager Notifi_M;
    ServiceThread thread;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        myServiceHandler handler = new myServiceHandler();
        thread = new ServiceThread(handler);
        thread.start();
        return START_STICKY;
    }

    //서비스가 종료될 때 할 작업

    public void onDestroy() {
        thread.stopForever();
        thread = null;//쓰레기 값을 만들어서 빠르게 회수하라고 null을 넣어줌.
    }

    class myServiceHandler extends Handler {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(android.os.Message msg) {
                //푸시 설정 되었다는 알림 보내기
                String channelId = "channel";
                String channelName = "Channel Name";

                NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
                    notifManager.createNotificationChannel(mChannel);
                }

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);

                Intent notificationIntent = new Intent(getApplicationContext(), BeforeSignin.class);

                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                int requestID = (int) System.currentTimeMillis();

                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                        requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentTitle("하비게이션") // required
                        .setContentText("취미 활동은 어떠셨나요?")  // required
                        .setDefaults(Notification.DEFAULT_ALL) // 알림, 사운드 진동 설정
                        .setAutoCancel(true) // 알림 터치시 반응 후 삭제
                        .setSound(RingtoneManager
                                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setSmallIcon(R.mipmap.logo)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo))
                        .setBadgeIconType(R.mipmap.logo2)
                        .setContentIntent(pendingIntent);

                notifManager.notify(0, builder.build());
        };
    }
}
