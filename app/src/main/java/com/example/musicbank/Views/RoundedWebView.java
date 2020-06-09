package com.example.musicbank.Views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.webkit.WebView;


public class RoundedWebView extends WebView
{
    private Context context;

    private int width;

    private int height;

    private int radius;

    public RoundedWebView(Context context)
    {
        super(context);

        initialize(context);
    }

    public RoundedWebView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        initialize(context);
    }

    public RoundedWebView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        initialize(context);
    }

    private void initialize(Context context)
    {
        this.context = context;
    }

    // This method gets called when the view first loads, and also whenever the
    // view changes. Use this opportunity to save the view's width and height.
    @Override protected void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight)
    {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);

        width = newWidth;

        height = newHeight;

        radius = dpToPx(10);
    }

    @Override protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        Path path = new Path();

        path.setFillType(Path.FillType.INVERSE_WINDING);

        path.addRoundRect(new RectF(0, getScrollY(), width, getScrollY() + height), radius, radius, Path.Direction.CW);

        canvas.drawPath(path, createPorterDuffClearPaint());
    }

    private Paint createPorterDuffClearPaint()
    {
        Paint paint = new Paint();

        paint.setColor(Color.TRANSPARENT);

        paint.setStyle(Paint.Style.FILL);

        paint.setAntiAlias(true);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        return paint;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
