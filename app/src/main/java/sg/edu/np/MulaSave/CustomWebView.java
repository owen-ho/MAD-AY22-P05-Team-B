package sg.edu.np.MulaSave;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.WebView;

public class CustomWebView extends WebView { //Create custom webview using previous webview which adds in gesture detection
    private GestureDetector gestureDetector;
    public CustomWebView(Context context) {
        super(context);
    }
    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public CustomWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Overrides WebView's onScrollChanged method to allow usage of gesture detection on scroll
     * @param l Horizontal position
     * @param t Vertical position
     * @param oldl Old Horizontal position
     * @param oldt Old Vertical position
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    /**
     * Overrides WebView's onTouchEvent method to allow usage of gesture detection on touch
     * @param ev Motion event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return gestureDetector.onTouchEvent(ev) || super.onTouchEvent(ev);
    }

    /**
     * Adds gesture detection to webview
     * @param gestureDetector Gesture
     */
    public void setGestureDetector(GestureDetector gestureDetector) {
        this.gestureDetector = gestureDetector;
    }
}
