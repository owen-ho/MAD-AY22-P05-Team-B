package sg.edu.np.MulaSave;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.net.URI;
import java.net.URISyntaxException;

public class WebActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    private TextView toolbarTitle;
    private ImageView backBtn;
    private ImageView forwardBtn;
    private ImageView refreshBtn;
    private ImageView browserBtn;
    private ImageView closeBtn;
    private CustomWebView customWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        linearLayout = findViewById(R.id.webviewLinearLayout);
        progressBar = findViewById(R.id.webviewProgressBar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        backBtn = findViewById(R.id.backBtn);
        forwardBtn = findViewById(R.id.forwardBtn);
        refreshBtn = findViewById(R.id.refreshBtn);
        browserBtn = findViewById(R.id.browserBtn);
        closeBtn = findViewById(R.id.closeBtn);
        customWebView= findViewById(R.id.webview);

        customWebView.setGestureDetector(new GestureDetector(new CustomeGestureDetector()));//Calls gesture detector class to hide toolbars
        progressBar.setMax(100);

        customWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                linearLayout.setVisibility(View.VISIBLE); //Shows progress bar when loading another page
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                linearLayout.setVisibility(View.GONE); //Hides progress bar when page has finished loading

                URI uri = null;
                try {
                    uri = new URI(url); //Uses Java's URI to get domain name
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                String domain = uri.getHost(); //Domain name is used as title
                toolbarTitle.setText(domain.startsWith("www.") ? domain.substring(4) : domain);//Changes toolbar title to url of page after each page

                super.onPageFinished(view, url);
            }
        });
        customWebView.loadUrl(url); //Load the website

        WebSettings webSettings = customWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        customWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress); //Updates progress bar when loading web page
            }

        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customWebView.canGoBack()){
                    customWebView.goBack();//Go back to previous website if there is one
                }
            }
        });

        forwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customWebView.canGoForward()) {
                    customWebView.goForward();//Go forward if there is a next webpage
                }
            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customWebView.reload();
            }
        });

        browserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);//Intent to user's own external browser
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (customWebView.canGoBack()){
            customWebView.goBack();//Go back to previous website if there is one
        }else{
            super.onBackPressed();//Returns to app otherwise
        }
    }
    private class CustomeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        private Toolbar bottomToolbar = findViewById(R.id.webviewBottomToolbar);
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1 == null || e2 == null) return false;//Ignore if first or second touch does not exist
            if(e1.getPointerCount() > 1 || e2.getPointerCount() > 1) return false;
            else {
                try {
                    if(e1.getY() - e2.getY() > 20 ) {
                        // Hide Actionbar when scrolling down (Y value of start is greater than end i.e. swiping up)
                        bottomToolbar.setVisibility(View.GONE);//Hide toolbar
                        customWebView.invalidate();//Make view invalid until next gesture
                        return false;
                    }
                    else if (e2.getY() - e1.getY() > 20 ) {
                        // Show Actionbar when scrolling up (Y value of start is lesser than end i.e. swiping down)
                        bottomToolbar.setVisibility(View.VISIBLE);
                        customWebView.invalidate();//Make view invalid until next gesture
                        return false;
                    }

                } catch (Exception e) {
                    customWebView.invalidate();//Make view invalid until next gesture
                }
                return false;
            }


        }
    }
}