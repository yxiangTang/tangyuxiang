package demo.tyx.com.reader.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;


import java.util.Timer;
import java.util.TimerTask;

import demo.tyx.com.mylibrary.Util.ShareData;


public class SquareProgressView extends View {
    int i = 0;
    private double progress;
    private Paint progressBarPaint;
    private Paint outlinePaint;
    private Paint textPaint;

    private float widthInDp = 10;
    private float strokewidth = 0;
    private Canvas canvas;

    private boolean outline = false;
    private boolean startline = false;
    private boolean showProgress = false;
    private boolean centerline = false;


    private boolean clearOnHundred = false;
    private boolean isIndeterminate = false;
    private int indeterminate_count = 1;

    private float indeterminate_width = 20.0f;
    private FinishProgressCallBack callBack;
    private Timer timer;
    private TimerTask timerTask;

    public SquareProgressView(Context context) {
        super(context);
        initializePaints(context);
    }

    public SquareProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializePaints(context);
    }

    public SquareProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializePaints(context);
    }

    private void initializePaints(Context context) {
        progressBarPaint = new Paint();
        progressBarPaint.setColor(context.getResources().getColor(
                android.R.color.holo_green_dark));
        progressBarPaint.setStrokeWidth(convertDpToPx(
                widthInDp, getContext()));
        progressBarPaint.setAntiAlias(true);
        progressBarPaint.setStyle(Style.STROKE);

        outlinePaint = new Paint();
        outlinePaint.setColor(context.getResources().getColor(
                android.R.color.black));
        outlinePaint.setStrokeWidth(1);
        outlinePaint.setAntiAlias(true);
        outlinePaint.setStyle(Style.STROKE);

        textPaint = new Paint();
        textPaint.setColor(context.getResources().getColor(
                android.R.color.black));
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas = canvas;
        super.onDraw(canvas);
        strokewidth = convertDpToPx(widthInDp, getContext());
        float scope = ShareData.getScreenW() * 4 - strokewidth;

        if (isOutline()) {
            drawOutline();
        }

        if (isStartline()) {
            drawStartline();
        }

        /*if (isShowProgress()) {
            drawPercent(percentSettings);
        }*/

        if (isCenterline()) {
            drawCenterline(strokewidth);
        }

        if ((isClearOnHundred() && progress == 100.0) || (progress <= 0.0)) {
            return;
        }

        if (isIndeterminate()) {
            Path path = new Path();
            DrawStop drawEnd = getDrawEnd((scope / 100) * Float.valueOf(String.valueOf(indeterminate_count)), canvas);

            if (drawEnd.place == Place.TOP) {
                path.moveTo(drawEnd.location - indeterminate_width - strokewidth, strokewidth / 2);
                path.lineTo(drawEnd.location, strokewidth / 2);
                canvas.drawPath(path, progressBarPaint);
            }

            if (drawEnd.place == Place.RIGHT) {
                path.moveTo(canvas.getWidth() - (strokewidth / 2), drawEnd.location - indeterminate_width);
                path.lineTo(canvas.getWidth() - (strokewidth / 2), strokewidth
                        + drawEnd.location);
                canvas.drawPath(path, progressBarPaint);
            }

            if (drawEnd.place == Place.BOTTOM) {
                path.moveTo(drawEnd.location - indeterminate_width - strokewidth,
                        ShareData.getScreenW() - (strokewidth / 2));
                path.lineTo(drawEnd.location, ShareData.getScreenW()
                        - (strokewidth / 2));
                canvas.drawPath(path, progressBarPaint);
            }

            if (drawEnd.place == Place.LEFT) {
                path.moveTo((strokewidth / 2), drawEnd.location - indeterminate_width
                        - strokewidth);
                path.lineTo((strokewidth / 2), drawEnd.location);
                canvas.drawPath(path, progressBarPaint);
            }

            indeterminate_count++;
            if (indeterminate_count > 100) {
                indeterminate_count = 0;
            }
            invalidate();
        } else {
            Path path = new Path();
            DrawStop drawEnd = getDrawEnd((scope / 100) * Float.valueOf(String.valueOf(progress)), canvas);

            if (drawEnd.place == Place.TOP) {
                if (drawEnd.location > (canvas.getWidth() / 2)) {
                    path.moveTo(canvas.getWidth() / 2, strokewidth / 2);
                    path.lineTo(drawEnd.location, strokewidth / 2);
                } else {
                    path.moveTo(canvas.getWidth() / 2, strokewidth / 2);
                    path.lineTo(canvas.getWidth() - (strokewidth / 2), strokewidth / 2);
                    path.lineTo(canvas.getWidth() - (strokewidth / 2), ShareData.getScreenW() - strokewidth / 2);
                    path.lineTo(strokewidth / 2, ShareData.getScreenW() - strokewidth / 2);
                    path.lineTo(strokewidth / 2, strokewidth / 2);
                    path.lineTo(drawEnd.location, strokewidth / 2);
                }
                canvas.drawPath(path, progressBarPaint);
            }

            if (drawEnd.place == Place.RIGHT) {
                path.moveTo(canvas.getWidth() / 2, strokewidth / 2);
                path.lineTo(canvas.getWidth() - (strokewidth / 2), strokewidth / 2);
                path.lineTo(canvas.getWidth() - (strokewidth / 2), strokewidth / 2
                        + drawEnd.location);
                canvas.drawPath(path, progressBarPaint);
            }

            if (drawEnd.place == Place.BOTTOM) {
                path.moveTo(canvas.getWidth() / 2, strokewidth / 2);
                path.lineTo(canvas.getWidth() - (strokewidth / 2), strokewidth / 2);
                path.lineTo(canvas.getWidth() - (strokewidth / 2), ShareData.getScreenW());
                path.moveTo(canvas.getWidth(), ShareData.getScreenW() - strokewidth / 2);
                path.lineTo(drawEnd.location, ShareData.getScreenW()
                        - (strokewidth / 2));
                canvas.drawPath(path, progressBarPaint);
            }

            if (drawEnd.place == Place.LEFT) {
                path.moveTo(canvas.getWidth() / 2, strokewidth / 2);
                path.lineTo(canvas.getWidth() - (strokewidth / 2), strokewidth / 2);
                path.lineTo(canvas.getWidth() - (strokewidth / 2), ShareData.getScreenW() - strokewidth / 2);
                path.lineTo(0, ShareData.getScreenW() - strokewidth / 2);
                path.moveTo(strokewidth / 2, ShareData.getScreenW() - strokewidth / 2);
                path.lineTo((strokewidth / 2), drawEnd.location);
                canvas.drawPath(path, progressBarPaint);
            }
        }
    }

    private void drawStartline() {
        Path outlinePath = new Path();
        outlinePath.moveTo(canvas.getWidth() / 2, 0);
        outlinePath.lineTo(canvas.getWidth() / 2, strokewidth);
        canvas.drawPath(outlinePath, outlinePaint);
    }

    public void drawOutline() {
        Path outlinePath = new Path();
        outlinePath.moveTo(0, 0);
        outlinePath.lineTo(canvas.getWidth(), 0);
        outlinePath.lineTo(canvas.getWidth(), canvas.getHeight());
        outlinePath.lineTo(0, canvas.getHeight());
        outlinePath.lineTo(0, 0);
        canvas.drawPath(outlinePath, outlinePaint);
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
        this.invalidate();
    }

    public void setColor(int color) {
        progressBarPaint.setColor(color);
        this.invalidate();
    }

    public void setWidthInDp(int width) {
        this.widthInDp = width;
        progressBarPaint.setStrokeWidth(convertDpToPx(
                widthInDp, getContext()));
        this.invalidate();
    }

    public boolean isOutline() {
        return outline;
    }

    public void setOutline(boolean outline) {
        this.outline = outline;
        this.invalidate();
    }

    public boolean isStartline() {
        return startline;
    }

    public void setStartline(boolean startline) {
        this.startline = startline;
        this.invalidate();
    }

 /*   private void drawPercent(PercentStyle setting) {
        textPaint.setTextAlign(setting.getAlign());
        if (setting.getTextSize() == 0) {
            textPaint.setTextSize((ShareData.getScreenW() / 10) * 4);
        } else {
            textPaint.setTextSize(setting.getTextSize());
        }

        String percentString = new DecimalFormat("###").format(getProgress());
        if (setting.isPercentSign()) {
            percentString = percentString + percentSettings.getCustomText();
        }

        textPaint.setColor(percentSettings.getTextColor());

        canvas.drawText(
                percentString,
                canvas.getWidth() / 2,
                (int) ((ShareData.getScreenW() / 2) - ((textPaint.descent() + textPaint
                        .ascent()) / 2)), textPaint);
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
        this.invalidate();
    }

    public void setPercentStyle(PercentStyle percentSettings) {
        this.percentSettings = percentSettings;
        this.invalidate();
    }

    public PercentStyle getPercentStyle() {
        return percentSettings;
    }*/

    public void setClearOnHundred(boolean clearOnHundred) {
        this.clearOnHundred = clearOnHundred;
        this.invalidate();
    }

    public boolean isClearOnHundred() {
        return clearOnHundred;
    }

    private void drawCenterline(float strokewidth) {
        float centerOfStrokeWidth = strokewidth / 2;
        Path centerlinePath = new Path();
        centerlinePath.moveTo(centerOfStrokeWidth, centerOfStrokeWidth);
        centerlinePath.lineTo(canvas.getWidth() - centerOfStrokeWidth, centerOfStrokeWidth);
        centerlinePath.lineTo(canvas.getWidth() - centerOfStrokeWidth, ShareData.getScreenW() - centerOfStrokeWidth);
        centerlinePath.lineTo(centerOfStrokeWidth, ShareData.getScreenW() - centerOfStrokeWidth);
        centerlinePath.lineTo(centerOfStrokeWidth, centerOfStrokeWidth);
        canvas.drawPath(centerlinePath, outlinePaint);
    }

    public boolean isCenterline() {
        return centerline;
    }

    public void setCenterline(boolean centerline) {
        this.centerline = centerline;
        this.invalidate();
    }

    public boolean isIndeterminate() {
        return isIndeterminate;
    }

    public void setIndeterminate(boolean indeterminate) {
        isIndeterminate = indeterminate;
        this.invalidate();
    }

    public DrawStop getDrawEnd(float percent, Canvas canvas) {
        DrawStop drawStop = new DrawStop();
        strokewidth = convertDpToPx(widthInDp, getContext());
        float halfOfTheImage = ShareData.getScreenW() / 2;

        if (percent > halfOfTheImage) {
            float second = percent - halfOfTheImage;

            if (second > ShareData.getScreenW()) {
                float third = second - ShareData.getScreenW();

                if (third > ShareData.getScreenW()) {
                    float forth = third - ShareData.getScreenW();

                    if (forth > ShareData.getScreenW()) {
                        float fifth = forth - ShareData.getScreenW();

                        if (fifth == halfOfTheImage) {
                            drawStop.place = Place.TOP;
                            drawStop.location = halfOfTheImage;
                        } else {
                            drawStop.place = Place.TOP;
                            drawStop.location = strokewidth + fifth;
                        }
                    } else {
                        drawStop.place = Place.LEFT;
                        drawStop.location = ShareData.getScreenW() - forth;
                    }

                } else {
                    drawStop.place = Place.BOTTOM;
                    drawStop.location = ShareData.getScreenW() - third;
                }
            } else {
                drawStop.place = Place.RIGHT;
                drawStop.location = strokewidth + second;
            }

        } else {
            drawStop.place = Place.TOP;
            drawStop.location = halfOfTheImage + percent;
        }

        return drawStop;
    }

    private class DrawStop {

        private Place place;
        private float location;

        public DrawStop() {

        }
    }

    public enum Place {
        TOP, RIGHT, BOTTOM, LEFT
    }

    public void start() {

        /*ValueAnimator valueAnimator = ValueAnimator.ofInt(1,101);
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value=(int)animation.getAnimatedValue();

                Log.d("4444",value+"");

                setProgress(value);
                if (value==101){
                    Log.d("4444","stop");
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (callBack!=null){
                        callBack.finish();
                    }

                }



            }
        });


        valueAnimator.start();*/

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {

//                i=i++;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("1111",i+"");
                        setProgress(++i);

                        if (i == 101) {
                            stop();
                        }
                    }
                });


            }
        };

        timer.schedule(timerTask, 0, 20);


    }


    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            if (timerTask != null) {
                timerTask.cancel();
                timerTask = null;
                i = 1;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                callBack.finish();

            }
        }
    }

    public interface FinishProgressCallBack {
        void finish();
    }

    public void addOnFinishProgressCallBack(FinishProgressCallBack callBack) {

        this.callBack = callBack;
    }

    public int convertDpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }
}
