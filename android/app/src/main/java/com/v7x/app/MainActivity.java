package com.v7x.app;
import android.os.Bundle; // ضيف السطر ده لو مش موجود
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // إجبار التطبيق على فتح الرابط بتاعك فوراً
        this.bridge.getWebView().loadUrl("https://v7x.fun/user/login");
    }
}
