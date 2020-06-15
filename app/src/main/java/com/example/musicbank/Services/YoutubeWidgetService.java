package com.example.musicbank.Services;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.musicbank.R;
import com.example.musicbank.Utils.PreferenceUtils;
import com.example.musicbank.Views.RoundedWebView;
import com.example.musicbank.Views.YoutubeiFrame;

import java.lang.reflect.InvocationTargetException;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class YoutubeWidgetService extends Service {

    private WindowManager mWindowManager;
    private View mFloatingWidget;
    private WindowManager.LayoutParams params;
    private RoundedWebView webView;
    private ImageView close;
    private ImageView fullscreen, imgbuttonplay;
    private String id;
    private ProgressBar progressBar;
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;
    private RelativeLayout rViewClick, buttonSetting, pausesetting, ytlogo;
    private boolean isShown = false;
    private boolean isPause = false;
    private Handler firstShow;
    private Runnable runFirst;
    public static final String START_SERVICE = "com.example.musicbank.intent.action.START_SERVICE";
    public static final String RESUME_SERVICE = "com.example.musicbank.intent.action.RESUME_SERVICE";
    private NotificationManager manager;




    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startMyOwnForeground();
        }

        id = PreferenceUtils.getYoutubetrack(getApplicationContext());

        HandlerThread thread = new HandlerThread("YoutubeWidgetService");
        thread.start();
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
        mFloatingWidget = LayoutInflater.from(this).inflate(R.layout.youtube_floating_widget, null);
        initView();
        inithandler();
        params = smallScreen();
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingWidget, params);
        movefunction(rViewClick);

    }

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            Intent intent = (Intent)msg.obj;
            String action = intent.getAction();

            if (action.equals(START_SERVICE)) {
                return;
            }
            if (action.equals(RESUME_SERVICE)) {
                setResumeService(intent.getIntExtra("second", 0));
                return;
            }

        }
    }

    public void setResumeService(int seconds){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                mFloatingWidget.setVisibility(View.VISIBLE);
                seekTo(seconds, true);
                play();
            }
        });
    }

    public void seekTo(float seconds, boolean allowSeekAhead) {
        webView.loadUrl("javascript:player.seekTo(" + seconds + "," + allowSeekAhead + ")");
    }

    public void pause() {
        webView.loadUrl("javascript:player.pauseVideo();");
    }

    public void play() {
        webView.loadUrl("javascript:player.playVideo();");
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String channelID = "com.app.vsionteq.chatsauce";
        NotificationChannel notificationChannel = new NotificationChannel(channelID, "Background Service", NotificationManager.IMPORTANCE_NONE);
        notificationChannel.enableLights(false);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(notificationChannel);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelID);
        Notification notification = notificationBuilder.setOngoing(false)
                .setSmallIcon(R.drawable.play_icon)
                .setContentTitle("MusicBank")
                .setContentText("Mediaplayer is playing")
                .setPriority(NotificationManager.IMPORTANCE_LOW)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();

        startForeground(1234, notification);
    }


    public void initView(){
        progressBar = (ProgressBar)mFloatingWidget.findViewById(R.id.progressBar);
        close = (ImageView)mFloatingWidget.findViewById(R.id.closeyoutube);
        fullscreen = (ImageView)mFloatingWidget.findViewById(R.id.fullscreenImg);
        imgbuttonplay = (ImageView)mFloatingWidget.findViewById(R.id.pauseSetting);
        rViewClick = (RelativeLayout)mFloatingWidget.findViewById(R.id.webviewClick);
        buttonSetting = (RelativeLayout)mFloatingWidget.findViewById(R.id.buttons);
        ytlogo = (RelativeLayout)mFloatingWidget.findViewById(R.id.yt_logo);
        pausesetting = (RelativeLayout)mFloatingWidget.findViewById(R.id.pauselayout);
        fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentTime();
            }
        });
        setImgbuttonplay();
        setButtonSetting();
        close.setOnClickListener(new View.OnClickListener() {
            //            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    stopForeground(false);
                    manager.cancel(1234);
                    stopSelf();

                } else {
                    stopSelf();
                }
                if(PreferenceUtils.getYoutubetrack(getApplicationContext())!=null){
                    PreferenceUtils.setYoutubeTrack(getApplicationContext(), null);
                }
            }
        });
        webView = (RoundedWebView)mFloatingWidget.findViewById(R.id.webview);
        ytlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPause) {
                    Intent intent = null;
                    try {
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + id));
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + id));
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        stopForeground(false);
                        manager.cancel(1234);
                        stopSelf();

                    } else {
                        stopSelf();
                    }
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent intent = null;
                try{
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + id));
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + id));
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    stopForeground(false);
                    manager.cancel(1234);
                    stopSelf();

                } else {
                    stopSelf();
                }
                return true;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                //SHOW LOADING IF IT ISNT ALREADY VISIBLE
                progressBar.setVisibility(View.VISIBLE);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }});
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(this, "Android");
        webView.getSettings().setAppCachePath(getCacheDir().getAbsolutePath());
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
//        webView.loadUrl("https://www.youtube.com/embed/" + id + "?showinfo=0&fs=0&autoplay=1");
        webView.loadDataWithBaseURL("http://www.youtube.com", YoutubeiFrame.frame_youtube(id, 0), "text/html", "UTF-8", "http://www.youtube.com");

    }

    public void getCurrentTime() {
        webView.loadUrl("javascript:getCurrentTime()");
    }

    public void setImgbuttonplay(){
        imgbuttonplay.setBackground(getResources().getDrawable(R.drawable.mr_media_pause_light));
        pausesetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPause){
                    imgbuttonplay.setBackground(getResources().getDrawable(R.drawable.mr_media_pause_light));
                    play();
                    isPause = false;
                    firstShow.postDelayed(runFirst, 2000);
                    Log.v("YoutubeWidgetService","play Tapbutton");
                }else{
                    imgbuttonplay.setBackground(getResources().getDrawable(R.drawable.mr_media_play_light));
                    pause();
                    isPause = true;
                    firstShow.removeCallbacks(runFirst);
                    Log.v("YoutubeWidgetService","pause Tapbutton");
                }
            }
        });
    }


    public WindowManager.LayoutParams smallScreen(){
        WindowManager.LayoutParams params1 = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params1 = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }else{
            params1 = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

        }
        return params1;
    }

    public void setButtonSetting(){
        rViewClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstShow.removeCallbacks(runFirst);
                Log.v("YoutubeWidgetService","pause Relativebutton");
                if(isShown){
                    hideSetting();
                }else{
                    showSetting();
                    if(!isPause){
                        firstShow.postDelayed(runFirst, 2000);
                        Log.v("YoutubeWidgetService","play Relative");
                    }
                }
            }
        });
    }

    public void showSetting(){
        fadeIn(buttonSetting);
        isShown =true;
    }
    public void hideSetting(){
        fadeOut(buttonSetting);
        isShown = false;
    }

    public void fadeIn(View view){
        // Prepare the View for the animation
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0.0f);
        // Start the animation
        view.animate()
                .setDuration(500)
                .alpha(1.0f)
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
        view.setAlpha(1.0f);
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

    public void inithandler(){
        firstShow = new Handler();
        runFirst = new Runnable() {
            public void run() {
                hideSetting();
            }
        };
        firstShow.postDelayed(runFirst, 2000);
        Log.v("YoutubeWidgetService","play First");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        android.os.Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        intent = intent != null ? intent : new Intent(START_SERVICE);
        String action = intent.getAction();
        if (action != null && action.equals(RESUME_SERVICE)) {
            msg.obj = intent;
            serviceHandler.sendMessage(msg);
        }
        return START_STICKY;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingWidget != null) mWindowManager.removeView(mFloatingWidget);
        if(PreferenceUtils.getYoutubetrack(getApplicationContext())!=null){
            PreferenceUtils.setYoutubeTrack(getApplicationContext(), null);
        }
        try {
            Class.forName("android.webkit.WebView")
                    .getMethod("onPause()", (Class[]) null)
                    .invoke(webView, (Object[]) null);

        } catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch(NoSuchMethodException nsme) {
            nsme.printStackTrace();
        } catch(InvocationTargetException ite) {
            ite.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        }
        if(webView != null) {
            webView.destroy();
            webView = null;
        }
    }

    public final static int FINGER_RELEASED = 0;
    public final static int FINGER_TOUCHED = 1;
    public final static int FINGER_DRAGGING = 2;
    public final static int FINGER_UNDEFINED = 3;

    private int fingerState = FINGER_RELEASED;
    public void movefunction(View view){

        view.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return false;
                    case MotionEvent.ACTION_UP:
                        if(fingerState != FINGER_DRAGGING) {
                            fingerState = FINGER_RELEASED;

                            // Your onClick codes

                        }
                        else if (fingerState == FINGER_DRAGGING) fingerState = FINGER_RELEASED;
                        else fingerState = FINGER_UNDEFINED;
                        return false;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mFloatingWidget, params);
                        return false;
                }
                return false;
            }
        });
    }
}
