package com.v7x.app;

import android.os.Bundle;
import android.graphics.Color;
import android.view.View;
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

        // --- 1. حل مشكلة اختفاء الساعة والبطارية ---
        Window window = getWindow();
        // إلغاء وضع الـ Fullscreen اللي بيخفي شريط الحالة
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // إظهار شريط الحالة بلون داكن يتناسب مع تصميم V7X
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#020617")); 
        // تجعل أيقونات الساعة والبطارية تظهر باللون الأبيض (فاتحة)
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        WebView webView = this.bridge.getWebView();

        // --- 2. إعدادات الأداء والألعاب ---
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);

        // --- 3. السحب للتحديث (Native) ---
        swipeRefreshLayout = new SwipeRefreshLayout(this);
        swipeRefreshLayout.addView(webView);
        setContentView(swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#00ff00"));

        swipeRefreshLayout.setOnRefreshListener(() -> {
            webView.reload();
            webView.postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 1500);
        });

        // --- 4. رابط المنصة ---
        webView.loadUrl("https://v7x.fun/user/login");
    }

    // --- 5. زر الرجوع الذكي ---
    @Override
    public void onBackPressed() {
        if (this.bridge.getWebView().canGoBack()) {
            this.bridge.getWebView().goBack();
        } else {
            super.onBackPressed();
        }
    }
}
