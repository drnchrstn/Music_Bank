package com.example.musicbank;

import android.animation.Animator;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.example.musicbank.Services.YoutubeWidgetService;
import com.example.musicbank.Services.YoutubeWidgetService;
import com.example.musicbank.Views.YoutubeiFrame;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ActivityYoutubeMax extends AppCompatActivity{
    private WebView webview;
    private int start;
    private String youtubeId;
    private TextView rightTime, leftTime;
    private SeekBar seekbar;
    private ImageView smallscreen, back, play, pause;
    private TranslateAnimation animate;
    private Handler firstShow;
    private Runnable runFirst;
    private RelativeLayout mRlayouttop, mRlayoutbottom, clickR, buttonLayout, ytlogo;
    private boolean isShown = false;
    private boolean isFullscreen = false;


    public static final int BUFFERING = 3;
    public static final int CUED = 5;
    public static final int ENDED = 0;
    public static final int PAUSED = 2;
    public static final int PLAYING = 1;
    public static final int UNKNOWN = -2;
    public static final int UNSTARTED = -1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        getIntentfunction();
        initview();
        initFunction();
        seekingBar();
        inithandler();
        setlayoutclick();
        buttons();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    public void getIntentfunction(){
        start = getIntent().getIntExtra("second", 0);
        youtubeId = getIntent().getStringExtra("id");

    }
    public void initview(){
        webview = (WebView)findViewById(R.id.webViewYT);
        seekbar = (SeekBar)findViewById(R.id.seekBar);
        rightTime = (TextView)findViewById(R.id.timeRemaining);
        leftTime = (TextView)findViewById(R.id.timeLeft);
        smallscreen = (ImageView)findViewById(R.id.imgsmall);
        back = (ImageView)findViewById(R.id.imgBack);
        play = (ImageView)findViewById(R.id.playbutton);
        pause = (ImageView)findViewById(R.id.pausebutton);
        mRlayouttop = (RelativeLayout)findViewById(R.id.topToolbar);
        mRlayoutbottom = (RelativeLayout)findViewById(R.id.seekcon);
        buttonLayout = (RelativeLayout)findViewById(R.id.buttonlayout);
        ytlogo = (RelativeLayout)findViewById(R.id.yt_logo);
        clickR = (RelativeLayout)findViewById(R.id.clickcon);
    }

    public void initFunction(){
        smallscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(YoutubeWidgetService.RESUME_SERVICE, null, ActivityYoutubeMax.this, YoutubeWidgetService.class);
                intent.putExtra("second", seekbar.getProgress());
                startService(intent);
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(ActivityYoutubeMax.this, YoutubeWidgetService.class));
                finish();
            }
        });

        ytlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                try{
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + youtubeId));
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + youtubeId));
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
        //webview
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent intent = null;
                try{
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + youtubeId));
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + youtubeId));
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                return true;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap facIcon) {
            }
            @Override
            public void onPageFinished(WebView view, String url) {
            }});
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.addJavascriptInterface(this, "Android");
        webview.getSettings().setAppCachePath(getCacheDir().getAbsolutePath());
        webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webview.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
        webview.loadDataWithBaseURL("http://www.youtube.com", YoutubeiFrame.frame_youtube(youtubeId, start), "text/html", "UTF-8", "http://www.youtube.com");
    }

    @JavascriptInterface
    public void notifyCurrentTime(int secs) {
        seekbar.setProgress(secs);
        leftTime.setText(ConvertSecondToHHMMString(secs));
    }

    public void getCurrentTime() {
        if(webview != null) {
            webview.loadUrl("javascript:getCurrentTime()");
        }
    }

    private Handler mSeekbarUpdateHandler = new Handler();
    private Runnable mUpdateSeekbar = new Runnable() {
        @Override
        public void run() {
            getCurrentTime();
            mSeekbarUpdateHandler.postDelayed(this, 20);
        }
    };

    private void updateProgressBar() {
        mSeekbarUpdateHandler.postDelayed(updateTimeTask, 20);
    }

    private Runnable updateTimeTask = new Runnable() {
        public void run() {
            getCurrentTime();
            getDuration();
            mSeekbarUpdateHandler.postDelayed(this, 20);
        }
    };


    private String ConvertSecondToHHMMString(int secondtTime) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("mm:ss");
        if(secondtTime >= 3600) {
            df = new SimpleDateFormat("HH:mm:ss");
        }
        df.setTimeZone(tz);
        String time = df.format(new Date(secondtTime*1000L));
        return time;
    }
    @JavascriptInterface
    public void notifyDuration(int secs) {
        seekbar.setMax(secs);
        rightTime.setText(ConvertSecondToHHMMString(secs- seekbar.getProgress()));
    }

    public void getDuration() {
        if(webview != null) {
            webview.loadUrl("javascript:getDuration()");
        }
    }

    @JavascriptInterface
    public void playerStateChange(int state) {
        switch (state) {
            case BUFFERING:
                Log.v("BUFFERING", "");
                pause.setVisibility(View.VISIBLE);
                play.setVisibility(View.GONE);
                mSeekbarUpdateHandler.removeCallbacks(updateTimeTask);
                break;
            case CUED:
                Log.v("CUED", "");
                break;
            case ENDED:
                Log.v("ENDED", "");
                mSeekbarUpdateHandler.removeCallbacks(updateTimeTask);
                firstShow.removeCallbacks(runFirst);
                break;
            case PAUSED:
                Log.v("PAUSED", "");
                mSeekbarUpdateHandler.removeCallbacks(updateTimeTask);
                firstShow.removeCallbacks(runFirst);
                ytlogo.setVisibility(View.GONE);
                if(!isFullscreen){
                    ytlogo.setVisibility(View.VISIBLE);
                }
                break;
            case PLAYING:
                ytlogo.setVisibility(View.GONE);
                firstShow.removeCallbacks(runFirst);
                Log.v("ActivityYoutube", "PLAYING");
                updateProgressBar();
                firstShow.postDelayed(runFirst, 2000);
                break;
            case UNSTARTED:
                Log.v("UNSTARTED", "");
                pause.setVisibility(View.VISIBLE);
                play.setVisibility(View.GONE);
                isShown = true;
                break;
        }
    }

    public void seekingBar(){
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if (fromUser)
//                    video.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                onStartTrackingTouched(seekBar);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                onStopTrackingTouched(seekBar);
            }
        });
    }

    public void onStartTrackingTouched(SeekBar seekbar) {
        mSeekbarUpdateHandler.removeCallbacks(updateTimeTask);
    }
    public void onStopTrackingTouched(SeekBar seekbar) {
        mSeekbarUpdateHandler.removeCallbacks(updateTimeTask);
        seekTo(seekbar.getProgress(), true);
        updateProgressBar();
    }

    public void seekTo(float seconds, boolean allowSeekAhead) {
        webview.loadUrl("javascript:player.seekTo(" + seconds + "," + allowSeekAhead + ")");
    }

    public void setlayoutclick(){
        clickR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstShow.removeCallbacks(runFirst);
                if(isShown){
                    hideSetting();
                }else{
                    showSetting();
                    Log.v("ActivityYoutube", "PLAYING");
                    firstShow.postDelayed(runFirst, 2000);
                }
            }
        });
    }
    public void showSetting(){
        slideDownfromoutside(mRlayouttop);
        slideUp(mRlayoutbottom);
        fadeIn(buttonLayout);
        isShown= true;
    }

    public void hideSetting(){
        SlideToAbove(mRlayouttop);
        slideDown(mRlayoutbottom);
        fadeOut(buttonLayout);
        isShown= false;
    }

    public void slideUp(View view){
        animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(300);
        animate.setFillAfter(true);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(final View view){
        animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(!isShown){
                    view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDownfromoutside(final View view){
        animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                -view.getHeight(),                 // fromYDelta
                0); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(animate);
    }
    public void inithandler(){
        firstShow = new Handler();
        runFirst = new Runnable() {
            public void run() {
                Log.v("ActivityYoutube", "hide");
                hideSetting();
            }
        };
    }


    public void fadeIn(View view){
        // Prepare the View for the animation
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0.0f);
        // Start the animation
        view.animate()
                .setDuration(500)
                .alpha(0.7f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        super.onAnimationCancel(animation);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        super.onAnimationRepeat(animation);
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        view.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationPause(Animator animation) {
                        super.onAnimationPause(animation);
                    }
                });
    }

    public void fadeOut(View view){
        // Prepare the View for the animation
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0.7f);
        // Start the animation
        view.animate()
                .setDuration(500)
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        super.onAnimationCancel(animation);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        super.onAnimationRepeat(animation);
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                    }

                    @Override
                    public void onAnimationPause(Animator animation) {
                        super.onAnimationPause(animation);
                    }
                });
    }

    public void SlideToAbove(View view) {
        Animation slide = null;
        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -5.0f);

        slide.setDuration(500);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        view.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                view.setVisibility(View.GONE);
            }
        });

    }

    public void buttons() {
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
                pause.setVisibility(View.VISIBLE);
                play.setVisibility(View.GONE);
                updateProgressBar();
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause();
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
                mSeekbarUpdateHandler.removeCallbacks(updateTimeTask);
                firstShow.removeCallbacks(runFirst);
            }
        });
    }

    public void pause() {
        webview.loadUrl("javascript:player.pauseVideo();");
    }

    public void play() {
        webview.loadUrl("javascript:player.playVideo();");
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        Log.d("tag", "config changed");
        super.onConfigurationChanged(newConfig);

        int orientation = newConfig.orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d("tag", "Portrait");
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, dpToPx(200));
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            webview.setLayoutParams(layoutParams);
            isFullscreen = false;
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("tag", "Landscape");
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            webview.setLayoutParams(layoutParams);
            isFullscreen = true;
        } else {
            Log.w("tag", "other: " + orientation);
        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(webview != null) {
            webview.destroy();
            webview = null;
        }
    }
}
