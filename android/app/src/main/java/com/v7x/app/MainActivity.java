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

        if (android.os.Build.VERSION.SDK_INT >= 33) {
            requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
        }

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#020617")); // لون الخلفية الداكن
        
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        
        WebView webView = this.bridge.getWebView();
        
        if (webView != null && swipeRefreshLayout == null) {
            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setDatabaseEnabled(true);
            
            settings.setUserAgentString("V7X_Android_App_Mobile");

            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);

            settings.setSupportZoom(false);
            settings.setBuiltInZoomControls(false);
            settings.setDisplayZoomControls(false);

            swipeRefreshLayout = new SwipeRefreshLayout(this);
            // زيادة المسافة المطلوبة للسحب لمنع التحديث بالخطأ
            swipeRefreshLayout.setDistanceToTriggerSync(400); 

            ViewGroup parent = (ViewGroup) webView.getParent();
            if (parent != null) {
                int index = parent.indexOfChild(webView);
                parent.removeView(webView);
                swipeRefreshLayout.addView(webView, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 
                        ViewGroup.LayoutParams.MATCH_PARENT));
                parent.addView(swipeRefreshLayout, index);
            }

            swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#00ff00"));

            swipeRefreshLayout.setOnRefreshListener(() -> {
                webView.reload();
                webView.postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 1500);
            });

            // تعديل منطق التحقق من السحب ليكون أكثر دقة
            webView.getViewTreeObserver().addOnScrollChangedListener(() -> {
                if (!webView.canScrollVertically(-1)) {
                    swipeRefreshLayout.setEnabled(true);
                } else {
                    swipeRefreshLayout.setEnabled(false);
                }
            });
            
            webView.loadUrl("https://v7x.fun/user/login");
        }
    }

    @Override
    public void onBackPressed() {
        if (this.bridge.getWebView().canGoBack()) {
            this.bridge.getWebView().goBack();
        } else {
            super.onBackPressed();
        }
    }
}
