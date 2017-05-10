package demo.tyx.com.reader.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import demo.tyx.com.reader.R;

/**
 * Created by admin on 2017/5/8.
 */

public class DetailContentFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_content,null);
        initView(view);
        initThread();
        return view;
    }

    private void initThread() {
    }

    private void initView(View view) {

    }
}
