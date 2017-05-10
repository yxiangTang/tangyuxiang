package demo.tyx.com.reader.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;

import demo.tyx.com.mylibrary.Util.ShareData;
import demo.tyx.com.reader.R;
import demo.tyx.com.reader.weight.MyNestedScrollView;
import okhttp3.Call;

import static demo.tyx.com.reader.R.id.flower_count;

/**
 * Created by admin on 2017/5/8.
 */

public class DetailContentActivity extends BaseActivity {

    private String href;
    private StringCallback callback;
    private static final String TAG = "DetailContentActivity";
    private String avater;
    private ImageView coverImg;
    private String signature;
    private TextView signature_tv;
    private String date;
    private TextView flower_count_tv;
    private String flowerCount;
    private TextView date_tv;
    private LinearLayout note_text_ll;
    private TextView name_tv;
    private String name;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        RelativeLayout contentView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.activity_content, null);
        setContentView(R.layout.activity_content);
        Intent intent = getIntent();
        href = intent.getStringExtra("href");
        initView();
        initThread();

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
//                recyclerView.refreshComplete(1);

            }
        };
        Log.d(TAG, "initThread: " + netConfig.setIndexItemUrl(href));
        OkHttpUtils.get().url(netConfig.setIndexItemUrl(href)).build().execute(callback);

    }


    private void checkForResult(String result) {
        if (!TextUtils.isEmpty(result)) {
            Document doc = Jsoup.parse(result);
            Elements links = doc.select("div.img_shadow").select("img");
            avater = links.attr("src");
             name = doc.select("div.note_username").text();
            signature = doc.select("div.quote").select("span").text();


            Elements content = doc.select("div.main_right");
            Elements status=content.select("span");
            Log.d(TAG, "checkForResult: " + status.get(1).text());
            date = status.get(0).text();
            String flowerurl = netConfig.setIndexItemUrl(status.get(1).select("img").attr("src"));
            flowerCount = status.get(1).text();

            Elements noteText=content.select("div.note_each");
            for (Element e:noteText){
                LinearLayout.LayoutParams timeParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                timeParams.setMargins(20,20,20,20);
                TextView time_tv=new TextView(this);
                time_tv.setText(e.select("div.note_time").text());
                TextView note=new TextView(this);
                note.setLineSpacing(10,2);
                note.setTextSize(18);
                note.setText(e.select("div.note_content").text());
                note_text_ll.addView(time_tv,timeParams);
                note_text_ll.addView(note,timeParams);
            }

        }

        setData();
    }

    private void setData() {
        Glide.with(this).load(avater).into(coverImg);
        if (!TextUtils.isEmpty(signature)) {
            signature_tv.setText("“" + signature + "”");
        }
        flower_count_tv.setText(flowerCount);
        date_tv.setText(date);
        name_tv.append(name);
    }


    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);//决定左上角的图标是否可以点击
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//决定左上角图标的右侧是否有向左的小箭头
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        coverImg = (ImageView) findViewById(R.id.backdrop);
        signature_tv = (TextView) findViewById(R.id.signature);
        flower_count_tv = (TextView) findViewById(flower_count);
        date_tv = (TextView) findViewById(R.id.date);
        note_text_ll=(LinearLayout)findViewById(R.id.note_text_ll);
        name_tv=(TextView)findViewById(R.id.name);
        NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.scrollView);

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.d(TAG, "onScrollChange: " + scrollX + " " + scrollY + " " + oldScrollX + " " + oldScrollY);
            }

        });
    }
}
