package demo.tyx.com.reader.weight;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import demo.tyx.com.mylibrary.Util.ShareData;


/**
 * Created by admin on 2016/12/24.
 */

public class MyView extends View {
    private ValueAnimator valueAnimator;
    private Path path;
    private int position = -1;
    private PathInfo info;
    private ArrayList<PathInfo> pathArrayList = new ArrayList<>();

    public MyView(Context context) {
        super(context);

        init();
    }

    public void init() {
        path = new Path();
        info = new PathInfo();

        path.moveTo(ShareData.getScreenW() / 2, 6);

        info.x = (float) ShareData.getScreenW()-6;
        info.y = (float) 6;
        pathArrayList.add(info);

        info.x = (float) ShareData.getScreenW()-6;
        info.y = (float) ShareData.getScreenW() / 2-6;
        pathArrayList.add(info);

        info = new PathInfo();
        info.x = (float) ShareData.getScreenW()-6;
        info.y = (float) ShareData.getScreenW();
        pathArrayList.add(info);

        info = new PathInfo();
        info.x = (float) ShareData.getScreenW() / 2;
        info.y = (float) ShareData.getScreenW();
        pathArrayList.add(info);

        info = new PathInfo();
        info.x = (float) 6;
        info.y = (float) ShareData.getScreenW();
        pathArrayList.add(info);

        info = new PathInfo();
        info.x = (float) 6;
        info.y = (float) ShareData.getScreenW() / 2;
        pathArrayList.add(info);

        info = new PathInfo();
        info.x = (float) 6;
        info.y = (float) 6;
        pathArrayList.add(info);

        info = new PathInfo();
        info.x = (float) ShareData.getScreenW() / 2;
        info.y = (float) 6;
        pathArrayList.add(info);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.RED);  //设置画笔颜色
        paint.setStyle(Paint.Style.STROKE);//填充样式改为描边
        paint.setStrokeWidth(6);//设置画笔宽度
        if (path != null) {
            canvas.drawPath(path, paint);

        }
    }

    public synchronized void getWitchPath(int position) {
        this.position = position;
        initValueAnimator();

//        new CountDownTimer()
    }

    public void initValueAnimator() {
        if (position == 0) {
//            if (valueAnimator == null) {
            ValueAnimator valueAnimator1 = ValueAnimator.ofFloat(ShareData.getScreenW() / 2, pathArrayList.get(position).x);
//            }
            valueAnimator1.setDuration(250);
            valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float value = (Float) animation.getAnimatedValue();
                    path.lineTo(value, 6);
                    Log.d("TAG", "cuurent value is " + value);
                    invalidate();
                }
            });
            valueAnimator1.start();
//            path.moveTo(pathArrayList.get(position).x,pathArrayList.get(position).y);
        } else {
            //开始位置x,y值
            float x1 = pathArrayList.get(position - 1).x;
            float y1 = pathArrayList.get(position - 1).y;

            //当前位置x,y值
            final float x2 = pathArrayList.get(position).x;
            final float y2 = pathArrayList.get(position).y;

            if (x2 == x1) {

                ValueAnimator valueAnimator2 = ValueAnimator.ofFloat(y1, y2);


                valueAnimator2.setDuration(250);
                valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Float value = (Float) animation.getAnimatedValue();

                        path.lineTo(x2, value);
                        Log.d("TAG", "cuurent value is " + value);
                        invalidate();
                    }
                });
                valueAnimator2.start();
            } else {

                ValueAnimator valueAnimator3 = ValueAnimator.ofFloat(x1, x2);

                valueAnimator3.setDuration(250);
                valueAnimator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Float value = (Float) animation.getAnimatedValue();
                        path.lineTo(value, y2);
                        Log.d("TAG", "cuurent value is " + value);
                        invalidate();
                    }
                });
                valueAnimator3.start();
            }
        }
    }


    class PathInfo {

        float x;
        float y;

    }
}
