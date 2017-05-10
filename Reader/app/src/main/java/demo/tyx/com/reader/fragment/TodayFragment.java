package demo.tyx.com.reader.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.GridItemDecoration;
import com.github.jdsjlzx.ItemDecoration.SpacesItemDecoration;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import demo.tyx.com.mylibrary.Util.ShareData;
import demo.tyx.com.mylibrary.net.NetConfig;
import demo.tyx.com.reader.R;
import demo.tyx.com.reader.activity.DetailContentActivity;
import demo.tyx.com.reader.activity.MainActivity;
import demo.tyx.com.reader.adapter.IndexAdapter;
import demo.tyx.com.reader.connector.OnItemClickLitener;
import okhttp3.Call;

/**
 * Created by admin on 2017/3/22.
 */

public class TodayFragment extends Fragment implements OnItemClickLitener {
    private static final String TAG = "TodayFragment";
    private NetConfig netConfig;
    private ArrayList<Map<String, Object>> dataList = new ArrayList<>();
    private LRecyclerView recyclerView;
    private StringCallback callback;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private MyOnRefreshListener onRefreshListener;
    private IndexAdapter indexAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_today_fragment, null);
        netConfig = NetConfig.getInstance();
        initView(view);
        initThread();
        return view;
    }

    private void initThread() {
        callback = new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(TAG, "onError: " + e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d(TAG, "onResponse: " + response);
                checkForResult(response);
                recyclerView.refreshComplete(1);

            }
        };
        recyclerView.forceToRefresh();
    }

    private void checkForResult(String result) {
        if (!TextUtils.isEmpty(result)) {
            Document doc = Jsoup.parse(result);
            Elements links = doc.select("table.user_list").select("a");
            dataList.clear();
            for (Element e : links) {
                String href = e.attr("href");
                String imgSrc = e.select("img").attr("src");
                String authorName = e.select("img").attr("alt");
                Log.d(TAG, "checkForResult: " + href + " " + imgSrc + " " + authorName + "\n");
                HashMap<String, Object> itemMap = new HashMap();
                itemMap.put("href", href);
                itemMap.put("imgSrc", imgSrc);
                itemMap.put("authorName", authorName);
                dataList.add(itemMap);
            }
            setData(dataList);
        }
    }

    private void setData(ArrayList<Map<String, Object>> dataList) {
        if (lRecyclerViewAdapter == null) {
            indexAdapter = new IndexAdapter(R.layout.item_indexlast_layout, dataList, getContext());
            indexAdapter.setOnItemClickLitener(this);
            lRecyclerViewAdapter = new LRecyclerViewAdapter(indexAdapter);
            recyclerView.setAdapter(lRecyclerViewAdapter);
        } else {
            lRecyclerViewAdapter.notifyDataSetChanged();
        }


    }

    private void initView(View view) {

        recyclerView = (LRecyclerView) view.findViewById(R.id.now_journal_cover);
        GridLayoutManager glMg = new GridLayoutManager(getContext(), 2);

        GridItemDecoration divider = new GridItemDecoration.Builder(getContext())
                .setHorizontal(R.dimen.x4)
                .setVertical(R.dimen.y4)
//                .setColorResource(R.color.split)
                .build();
        recyclerView.addItemDecoration(divider);
        recyclerView.setLayoutManager(glMg);
        onRefreshListener = new MyOnRefreshListener();
        recyclerView.setOnRefreshListener(onRefreshListener);


    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getContext(), DetailContentActivity.class);
        intent.putExtra("href", dataList.get(position).get("href").toString());
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }


    class MyOnRefreshListener implements OnRefreshListener {

        @Override
        public void onRefresh() {
            OkHttpUtils.get().url(netConfig.getINDEX_API_URl()).build().execute(callback);
        }
    }
}
