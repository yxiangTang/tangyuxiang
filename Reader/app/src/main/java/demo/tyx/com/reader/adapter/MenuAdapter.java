package demo.tyx.com.reader.adapter;

import android.content.Context;
import android.view.View;

import java.util.List;
import java.util.Map;

import demo.tyx.com.reader.R;
import demo.tyx.com.reader.connector.OnItemClickLitener;

/**
 * Created by admin on 2017/2/4.
 */

public class MenuAdapter extends BaseRecyclerAdapter<Map<String,Object>,BaseRecyclerAdapter.BaseViewHolder> {


    private  Context mContext;


    public MenuAdapter(int layoutID, List<Map<String, Object>> data, Context context) {
        super(layoutID, data);
        mContext=context;
    }


    protected void bindTheData(final BaseRecyclerAdapter.BaseViewHolder holder, Map<String, Object> data, final int position) {
        holder.setText(R.id.menuName,data.get("title").toString());
        holder.setImage(R.id.icon, (Integer) data.get("icon"),mContext);
//        if (onItemClickLitener!=null) {
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onItemClickLitener.onItemClick(holder.itemView,position);
//                }
//            });
//        }
    }



}
