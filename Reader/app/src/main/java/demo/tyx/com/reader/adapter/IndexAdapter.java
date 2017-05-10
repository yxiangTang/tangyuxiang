package demo.tyx.com.reader.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.List;
import java.util.Map;

import demo.tyx.com.reader.R;

/**
 * Created by admin on 2017/5/5.
 */

public class IndexAdapter extends BaseRecyclerAdapter<Map<String,Object>,BaseRecyclerAdapter.BaseViewHolder> {
    private  Context mContext;

    public IndexAdapter(int layoutID, List data, Context context) {
        super(layoutID, data);
        mContext=context;
    }



    @Override
    protected void bindTheData(BaseRecyclerAdapter.BaseViewHolder holder, Map<String, Object> data, int position) {
        ImageView img=new ImageView(mContext);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        holder.loadImage(img,data.get("imgSrc").toString(),mContext);

    }
}
