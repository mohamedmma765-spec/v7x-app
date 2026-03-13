package com.v7x.app;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // إجبار التطبيق على فتح الرابط الخاص بالمنصة فوراً
        // تم استخدام loadUrl لضمان توجيه المستخدم مباشرة لصفحة تسجيل الدخول
        this.bridge.getWebView().loadUrl("https://v7x.fun/user/login");

        // تحسين إعدادات الويب لعرض المحتوى بملء الشاشة ودعم الإشعارات
        WebView webView = this.bridge.getWebView();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
    }

    @Override
    public void onBackPressed() {
        // ميزة زر الرجوع الذكي:
        // إذا كان المستخدم داخل صفحات الموقع، يرجع خطوة للخلف بدل إغلاق التطبيق
        if (this.bridge.getWebView().canGoBack()) {
            this.bridge.getWebView().goBack();
        } else {
            // إذا كان في الصفحة الرئيسية (Login)، يخرج من التطبيق بشكل طبيعي
            super.onBackPressed();
        }
    }
}
