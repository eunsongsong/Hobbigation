package com.example.hobbigation;

import android.app.Activity;
import android.widget.Toast;
/**
 * 뒤로가기 버튼을 두번 연속 터치시 종료되도록 기존의 onBackPressed() 재정의
 */

public class BackPressCloseHandler {
    private long backKeyPressedTime = 0;
    private Toast toast;
    private Activity activity;

    public BackPressCloseHandler(Activity context) { this.activity = context; }
    public void onBackPressed() {
        //한번 터치 or 2초 초과후 터치시 토스트 메세지 출력
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        //2초 이하로 연속 터치시 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish();
            toast.cancel();
        }
    }

    //종료 알림 토스트 메세지
    public void showGuide() {
        toast = Toast.makeText(activity, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }
}
