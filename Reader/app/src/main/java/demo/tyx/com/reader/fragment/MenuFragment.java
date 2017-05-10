package demo.tyx.com.reader.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import demo.tyx.com.reader.R;
import demo.tyx.com.reader.adapter.MenuAdapter;
import demo.tyx.com.reader.connector.OnItemClickLitener;

/**
 * Created by admin on 2017/3/22.
 */

public class MenuFragment extends Fragment implements View.OnClickListener,OnItemClickLitener {
    private Context context;
    private List<Map<String, Object>> dataList;
    private OnItemClickLitener mOnItemClickLitener;

    public MenuFragment(Context context, List<Map<String, Object>> dataList) {
        this.context=context;
        this.dataList=dataList;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment_layout, container, false);
        LRecyclerView recyclerView = (LRecyclerView) view.findViewById(R.id.slidMenu);
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        MenuAdapter menuAdapter = new MenuAdapter(R.layout.item_index_menu, dataList,getContext());
        menuAdapter.setOnItemClickLitener(this);
        LRecyclerViewAdapter adapter = new LRecyclerViewAdapter(menuAdapter);
        View headView =LayoutInflater.from(context).inflate(R.layout.item_headview, null);
        View footView =LayoutInflater.from(context).inflate(R.layout.item_footview,null);
        adapter.addHeaderView(headView);
        adapter.addFooterView(footView);
        recyclerView.setAdapter(adapter);
        recyclerView.setPullRefreshEnabled(false);
        return view;
    }



    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(View view, int position) {
        mOnItemClickLitener.onItemClick(view,position);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        mOnItemClickLitener.onItemClick(view,position);
    }


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}
