package demo.tyx.com.reader.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import demo.tyx.com.reader.R;
import demo.tyx.com.reader.connector.OnItemClickLitener;
import demo.tyx.com.reader.fragment.AboutFragment;
import demo.tyx.com.reader.fragment.ArticleFragment;
import demo.tyx.com.reader.fragment.MenuFragment;
import demo.tyx.com.reader.fragment.MusicFragment;
import demo.tyx.com.reader.fragment.SkinFragment;
import demo.tyx.com.reader.fragment.TodayFragment;
import demo.tyx.com.reader.weight.SquareProgressView;


/**
 * Created by admin on 2016/12/24.
 */

public class MainActivity extends BaseActivity implements OnItemClickLitener{
    int i = 0;
    SquareProgressView mMyProcessBar;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private int value2 = -1;
    private long starttime;
    private String[] lvs = {"今日", "美文", "音乐", "换肤","关于"};
    private int[] img={R.drawable.icon_today,R.drawable.icon_article,R.drawable.icon_music,R.drawable.icon_skin,R.drawable.icon_about};
    private List<Map<String,Object>> dataList =new ArrayList();
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean isMenuOpen;
    private DrawerLayout drawerlayout;
    private MenuFragment menuFragment;
    private TodayFragment todayFragment;
    private ArticleFragment articleFragment;
    private MusicFragment musicFragment;
    private SkinFragment skinFragment;
    private AboutFragment aboutFragment;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RelativeLayout contentView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        setContentView(contentView);
        initData();
        initView(contentView);

    }

    private void initData() {
       for (int i=0;i<lvs.length;i++){
           HashMap<String,Object> map=new HashMap<>();
           map.put("title",lvs[i]);
           map.put("icon",img[i]);
           dataList.add(map);
       }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isMenuOpen) {
                drawerlayout.closeDrawers();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView(RelativeLayout contentView) {


        title=(TextView)contentView.findViewById(R.id.title);
        drawerlayout = (DrawerLayout) contentView.findViewById(R.id.drawerlayout);

        menuFragment = new MenuFragment(this,dataList);
        menuFragment.setOnItemClickLitener(this);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.add(R.id.menu_fl, menuFragment, "menu");
        tx.commit();


        Toolbar toolbar = (Toolbar) contentView.findViewById(R.id.toolbar);

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                isMenuOpen = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                isMenuOpen = false;
            }
        };
        mDrawerToggle.syncState();
        drawerlayout.addDrawerListener(mDrawerToggle);
        chooseFragmentContent(0);
    }


    private void chooseFragmentContent(int position){

        FragmentManager fm = getFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        drawerlayout.closeDrawers();
        title.setText(dataList.get(position).get("title").toString());
        switch (position){
            case 0:
                todayFragment = new TodayFragment();
                tx.replace(R.id.content_fl, todayFragment, "today");
                tx.commit();
                break;
            case 1:
                articleFragment = new ArticleFragment();
                tx.replace(R.id.content_fl, articleFragment, "article");
                tx.commit();
                break;
            case 2:
                musicFragment = new MusicFragment();
                tx.replace(R.id.content_fl, musicFragment, "music");
                tx.commit();
                break;
            case 3:
                skinFragment = new SkinFragment();
                tx.replace(R.id.content_fl, skinFragment, "skin");
                tx.commit();
                break;
            case 4:
                aboutFragment = new AboutFragment();
                tx.replace(R.id.content_fl, aboutFragment, "about");
                tx.commit();
                break;
        }

    }


    @Override
    public void onItemClick(View view, int position) {
            chooseFragmentContent(position);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}


