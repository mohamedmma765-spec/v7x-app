package com.v7x.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.i("V7X_System", "Boot Completed: V7X is ready to receive push notifications.");
            // هذا الكود يضمن بقاء خدمات التطبيق نشطة لاستلام الإشعارات فور تشغيل الجهاز
        }
    }
}
