package com.albanfontaine.go4lunch.Controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.albanfontaine.go4lunch.R;
import com.albanfontaine.go4lunch.Utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity {
    @BindView(R.id.webview) WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        String url = getIntent().getExtras().getString(Constants.RESTAURANT_URL);
        mWebView.loadUrl(url);

    }
}

