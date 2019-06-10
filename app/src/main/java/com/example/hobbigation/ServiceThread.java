package com.example.hobbigation;

import android.os.Handler;

/**
 *  데모용 Service Thread - 1분 간격 푸시 알림
 *  60초를 세고 핸들러에게 메세지 전달
 */
public class ServiceThread extends Thread {
    Handler handler;
    boolean isRun = true;

    public ServiceThread(Handler handler) {
        this.handler = handler;
    }

    public void stopForever() {
        synchronized (this) {
            this.isRun = false;
        }
    }

    public void run() {
        //반복적으로 수행할 작업
        while (isRun) {
            handler.sendEmptyMessage(0); //쓰레드에 있는 핸들러에게 메세지를 보냄
            try {
                Thread.sleep(60000); //60초씩 쉰다
            } catch (Exception e) {
            }
        }
    }
}
