package com.v7x.app;

import android.os.Bundle;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- 1. طلب إذن الإشعارات تلقائياً لأندرويد 13 فما فوق ---
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
        }

        // --- 2. إظهار الساعة والبطارية (Status Bar) ---
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#020617")); // لون الخلفية الداكن
        
        // جعل الأيقونات (الساعة/البطارية) باللون الأبيض لتبدو واضحة
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        
        // استدعاء الـ WebView بعدما يجهز الـ Bridge تماماً لمنع الانهيار
        WebView webView = this.bridge.getWebView();
        
        if (webView != null && swipeRefreshLayout == null) {
            // --- 3. إعدادات الأداء ومنع الزوم وتصغير العناصر ---
            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setDatabaseEnabled(true);
            
            // ضبط البصمة (User Agent) ليتعرف السيرفر أن هذا هو التطبيق
            settings.setUserAgentString("V7X_Android_App_Mobile");

            // حل مشكلة حجم العناصر الكبير (Scaling)
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);

            settings.setSupportZoom(false);
            settings.setBuiltInZoomControls(false);
            settings.setDisplayZoomControls(false);

            // --- 4. إضافة ميزة السحب للتحديث (Native Swipe Refresh) ---
            swipeRefreshLayout = new SwipeRefreshLayout(this);
            ViewGroup parent = (ViewGroup) webView.getParent();
            if (parent != null) {
                int index = parent.indexOfChild(webView);
                parent.removeView(webView);
                swipeRefreshLayout.addView(webView, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 
                        ViewGroup.LayoutParams.MATCH_PARENT));
                parent.addView(swipeRefreshLayout, index);
            }

            // تخصيص لون دائرة التحميل (أخضر نيون V7X)
            swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#00ff00"));

            swipeRefreshLayout.setOnRefreshListener(() -> {
                webView.reload();
                webView.postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 1500);
            });

            // تفعيل السحب فقط عندما تكون الصفحة في الأعلى
            webView.getViewTreeObserver().addOnScrollChangedListener(() -> {
                if (webView.getScrollY() == 0) {
                    swipeRefreshLayout.setEnabled(true);
                } else {
                    swipeRefreshLayout.setEnabled(false);
                }
            });
            
            // --- 5. تحميل الرابط ---
            webView.loadUrl("https://v7x.fun/user/login");
        }
    }

    // --- 6. زر الرجوع الذكي للتنقل داخل المنصة ---
    @Override
    public void onBackPressed() {
        if (this.bridge.getWebView().canGoBack()) {
            this.bridge.getWebView().goBack();
        } else {
            super.onBackPressed();
        }
    }
}
