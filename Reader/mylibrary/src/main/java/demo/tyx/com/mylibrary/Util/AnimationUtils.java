package demo.tyx.com.mylibrary.Util;

import android.animation.ValueAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by cbw on 2016/10/14.
 */

public class AnimationUtils {

    private static View mANIMview;

    // 以下是补间动画

    public static void scaleANIM(View view, float from, float to, long time, boolean fillAfter, Animation.AnimationListener animationListener) {

        ScaleAnimation scaleAnimation = new ScaleAnimation(
                from, to, from, to,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(time);
        scaleAnimation.setFillAfter(fillAfter);
        scaleAnimation.setInterpolator(new LinearInterpolator());
        if (animationListener != null) {
            scaleAnimation.setAnimationListener(animationListener);
        }
        view.startAnimation(scaleAnimation);
    }

    public static void rotateANIM(View view) {
        view.clearAnimation();
        RotateAnimation rotateAnimation = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        view.startAnimation(rotateAnimation);
    }

    public static void alphaANIM(final View view, long time, long startOffset, float s, float end, boolean fillAfter, Animation.AnimationListener animationListener) {
        AlphaAnimation aAnima = new AlphaAnimation(s, end);
        aAnima.setDuration(time);
        aAnima.setStartOffset(startOffset);
        aAnima.setFillAfter(fillAfter);
        if (animationListener != null) {
            aAnima.setAnimationListener(animationListener);
        }
        view.startAnimation(aAnima);
    }

    public static void translateANIM(View view, long time, float fx, float tox, float fy, float toy, boolean fillAfter, Animation.AnimationListener animationListener) {
        Animation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, fx,
                Animation.RELATIVE_TO_SELF, tox,
                Animation.RELATIVE_TO_SELF, fy,
                Animation.RELATIVE_TO_SELF, toy);
        translateAnimation.setInterpolator(new LinearInterpolator());
        translateAnimation.setFillAfter(fillAfter);
        translateAnimation.setDuration(time);
        if (animationListener != null) {
            translateAnimation.setAnimationListener(animationListener);
        }
        view.startAnimation(translateAnimation);
    }

    public static void jumpANIM(View view) {
        view.clearAnimation();
        int time1 = 350;
        int time2 = 200;
        int time3 = 150;
        int time4 = 150;
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setInterpolator(new LinearInterpolator());
        ScaleAnimation scaleAnimation1 = new ScaleAnimation(
                0.0f, 1.2f, 0.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation1.setDuration(time1);
        scaleAnimation1.setFillAfter(true);
        animationSet.addAnimation(scaleAnimation1);
        ScaleAnimation scaleAnimation2 = new ScaleAnimation(
                1.0f, 1.0f / 1.2f, 1.0f, 1.0f / 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation2.setDuration(time2);
        scaleAnimation2.setStartOffset(time1);
        scaleAnimation2.setFillAfter(true);
        animationSet.addAnimation(scaleAnimation2);
        ScaleAnimation scaleAnimation3 = new ScaleAnimation(
                1.0f, 1.1f, 1.0f, 1.1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation3.setDuration(time3);
        scaleAnimation3.setStartOffset(time1 + time2);
        scaleAnimation3.setFillAfter(true);
        animationSet.addAnimation(scaleAnimation3);
        ScaleAnimation scaleAnimation4 = new ScaleAnimation(
                1.0f, 1.0f / 1.1f, 1.0f, 1.0f / 1.1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation4.setDuration(time4);
        scaleAnimation4.setStartOffset(time1 + time2 + time3);
        scaleAnimation4.setFillAfter(true);
        animationSet.addAnimation(scaleAnimation4);
        view.startAnimation(animationSet);
    }

    public static void YoYiYaoANIM(View view, Animation.AnimationListener animationListener) {
        int time1 = 100;
        int time2 = 100;
        int time3 = 50;
        int time4 = 100;
        int time5 = 100;
        int time6 = 50;
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setFillAfter(true);
        animationSet.setInterpolator(new LinearInterpolator());
        animationSet.setStartOffset(200);

        RotateAnimation rotateAnimation1 = new RotateAnimation(
                0, -10,
                Animation.RELATIVE_TO_SELF, 1f,
                Animation.RELATIVE_TO_SELF, 1f);
        rotateAnimation1.setFillAfter(true);
        rotateAnimation1.setDuration(time1);
        animationSet.addAnimation(rotateAnimation1);
        RotateAnimation rotateAnimation2 = new RotateAnimation(
                0, 15,
                Animation.RELATIVE_TO_SELF, 1f,
                Animation.RELATIVE_TO_SELF, 1f);
        rotateAnimation2.setFillAfter(true);
        rotateAnimation2.setStartOffset(time1);
        rotateAnimation2.setDuration(time2);
        animationSet.addAnimation(rotateAnimation2);
        RotateAnimation rotateAnimation3 = new RotateAnimation(
                0, -5,
                Animation.RELATIVE_TO_SELF, 1f,
                Animation.RELATIVE_TO_SELF, 1f);
        rotateAnimation3.setFillAfter(true);
        rotateAnimation3.setStartOffset(time1 + time2);
        rotateAnimation3.setDuration(time3);
        animationSet.addAnimation(rotateAnimation3);

        RotateAnimation rotateAnimation4 = new RotateAnimation(
                0, -10,
                Animation.RELATIVE_TO_SELF, 1f,
                Animation.RELATIVE_TO_SELF, 1f);
        rotateAnimation4.setFillAfter(true);
        rotateAnimation4.setStartOffset(time1 + time2 + time3);
        rotateAnimation4.setDuration(time4);
        animationSet.addAnimation(rotateAnimation4);
        RotateAnimation rotateAnimation5 = new RotateAnimation(
                0, 15,
                Animation.RELATIVE_TO_SELF, 1f,
                Animation.RELATIVE_TO_SELF, 1f);
        rotateAnimation5.setFillAfter(true);
        rotateAnimation5.setStartOffset(time1 + time2 + time3 + time4);
        rotateAnimation5.setDuration(time5);
        animationSet.addAnimation(rotateAnimation5);
        RotateAnimation rotateAnimation6 = new RotateAnimation(
                0, -5,
                Animation.RELATIVE_TO_SELF, 1f,
                Animation.RELATIVE_TO_SELF, 1f);
        rotateAnimation6.setFillAfter(true);
        rotateAnimation6.setStartOffset(time1 + time2 + time3 + time4 + time5);
        rotateAnimation6.setDuration(time6);
        animationSet.addAnimation(rotateAnimation6);
        if (animationListener != null) {
            animationSet.setAnimationListener(animationListener);
        }
        view.startAnimation(animationSet);
    }

    // 边缩放，边旋转，边移动，边改边透明度
    public static void showANIM(View view, long time, float sfx, float stox, float pivotXValue, float pivotYValue,
                                float fromDegrees, float toDegrees,
                                float fromXDelta, float toXDelta, float fromYDelta, float toYDelta,
                                float fromAlpha, float toAlpha,
                                Animation.AnimationListener animationListener) {

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setInterpolator(new LinearInterpolator());

        ScaleAnimation scaleAnimation = new ScaleAnimation(
                sfx, stox, sfx, stox,
                Animation.RELATIVE_TO_SELF, pivotXValue,
                Animation.RELATIVE_TO_SELF, pivotYValue);
        scaleAnimation.setDuration(time);
        if (sfx != stox) {
            animationSet.addAnimation(scaleAnimation);
        }

        RotateAnimation rotateAnimation = new RotateAnimation(
                fromDegrees, toDegrees,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(time);
        if (fromDegrees != toDegrees) {
            animationSet.addAnimation(rotateAnimation);
        }

        TranslateAnimation translateAnimation = new TranslateAnimation(
                fromXDelta,
                toXDelta,
                fromYDelta,
                toYDelta);
        translateAnimation.setDuration(time);
        if (fromXDelta != toXDelta || fromYDelta != toYDelta) {
            animationSet.addAnimation(translateAnimation);
        }

        AlphaAnimation aAnima = new AlphaAnimation(fromAlpha, toAlpha);
        aAnima.setDuration(time);
        if (fromAlpha != toAlpha) {
            animationSet.addAnimation(aAnima);
        }

        if (animationListener != null) {
            animationSet.setAnimationListener(animationListener);
        }
        view.startAnimation(animationSet);
    }


    // 以下是属性动画

    public static void valueAnimaFloat(long time, float from, float to, ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {

        ValueAnimator va;
        va = ValueAnimator.ofFloat(from, to);
        va.addUpdateListener(animatorUpdateListener);
        va.setDuration(time);
        va.start();
    }


    // 特殊封装
    private static View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    AnimationUtils.scaleANIM(v, 1, mScale, 50, true, null);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.clearAnimation();
                    break;
            }

            return false;
        }
    };

    public static float mScale = 0.8f;

    public static void touchViewScaleAnim(View view ,float scale) {
        mScale = scale;
        view.setOnTouchListener(mOnTouchListener);
    }

    // 暂不使用
    public void clearAllANIM() {
        if (mANIMview != null) {
            mANIMview.clearAnimation();
            mANIMview = null;
        }
    }
}
