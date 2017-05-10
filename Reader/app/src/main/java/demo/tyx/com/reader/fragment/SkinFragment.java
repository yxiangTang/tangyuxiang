package demo.tyx.com.reader.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import demo.tyx.com.reader.R;

/**
 * Created by admin on 2017/3/22.
 */

public class SkinFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        TextView textView=new TextView(getContext());
        textView.setText("Skin");

        View skinView=inflater.inflate(R.layout.fragment_chang_skin,null);

        return skinView;
    }
}
