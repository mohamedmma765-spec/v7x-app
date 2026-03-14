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

        // --- 1. إظهار الساعة والبطارية (Status Bar) ---
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
            // --- 2. إعدادات الأداء ومنع الزوم للألعاب ---
            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setDatabaseEnabled(true);
            settings.setSupportZoom(false);
            settings.setBuiltInZoomControls(false);
            settings.setDisplayZoomControls(false);

            // --- 3. إضافة ميزة السحب للتحديث (Native Swipe Refresh) ---
            // الطريقة الآمنة: نجعل الـ SwipeRefreshLayout هو الأب للـ WebView
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
            
            // --- 4. تحميل الرابط ---
            webView.loadUrl("https://v7x.fun/user/login");
        }
    }

    // --- 5. زر الرجوع الذكي للتنقل داخل المنصة ---
    @Override
    public void onBackPressed() {
        if (this.bridge.getWebView().canGoBack()) {
            this.bridge.getWebView().goBack();
        } else {
            super.onBackPressed();
        }
    }
}
