package demo.tyx.com.reader.weight;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by admin on 2017/5/9.
 */

public class MyNestedScrollView extends NestedScrollView implements NestedScrollingParent{
    private ScrollInterface scrollerListenter;

    public MyNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }


    public void setOnScrollerListenter(ScrollInterface scrollerListenter){

        this.scrollerListenter=scrollerListenter;
    }

    public interface ScrollInterface {
        public void onSChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }

}
