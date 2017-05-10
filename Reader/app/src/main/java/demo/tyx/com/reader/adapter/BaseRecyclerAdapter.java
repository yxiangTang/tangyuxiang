package demo.tyx.com.reader.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import demo.tyx.com.mylibrary.Util.ShareData;
import demo.tyx.com.reader.connector.OnItemClickLitener;

/**
 * Created by admin on 2017/4/11.
 */

public abstract class BaseRecyclerAdapter<D,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements View.OnClickListener {

    private  List<D> data;
    private  int layoutID;
    private static final String TAG = "BaseRecyclerAdapter";
    private OnItemClickLitener clickListener;

    public BaseRecyclerAdapter(int layoutID, List<D> data){
        this.data = data == null ? new ArrayList<D>() : data;
        this.layoutID=layoutID;
        if (layoutID!=0){
            this.layoutID=layoutID;
        }else{
            throw new NullPointerException("请设置资源Id");
        }
    }


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(layoutID,parent,false);

        return (VH)new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(position);
        bindTheData(holder,data.get(position),position);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+data.size());
        return data.size();
    }

    protected abstract void bindTheData(VH holder,D data,int position);

    @Override
    public void onClick(View v) {
        if (clickListener!=null){
            clickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    class BaseViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<Object> mViews;

        public BaseViewHolder(View itemView) {
            super(itemView);
            mViews = new SparseArray<>();
        }

        public <T extends View> T findViewById(int viewId) {
            View view = (View) mViews.get(viewId);
            if (view == null) {
                view = itemView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }


        /**
         * 设置文本资源
         *
         * @param viewId view id
         * @param s      字符
         */
        public TextView setText(int viewId, CharSequence s) {
            TextView view = findViewById(viewId);
            view.setText(s);
            return view;
        }

        public ImageView setImage(int viewId, int imgId, Context context) {
            ImageView view = findViewById(viewId);
            view.setImageResource(imgId);
            return view;
        }
        public ImageView loadImage(ImageView img, String  url, Context context) {
            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ShareData.getScreenW()/2,ShareData.getScreenW()/2);
            ((ViewGroup)itemView).addView(img,params);
            Glide
                    .with(context)
                    .load(url)
                    .into(img);
            return img;
        }
    }


    /**
     * 设置点击监听
     *
     * @param clickListener 监听器
     */
    public void setOnItemClickLitener(OnItemClickLitener clickListener) {
        this.clickListener = clickListener;
    }
}
