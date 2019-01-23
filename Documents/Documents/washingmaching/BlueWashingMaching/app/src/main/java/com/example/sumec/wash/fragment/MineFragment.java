package com.example.sumec.wash.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.sumec.wash.R;
import com.example.sumec.wash.activity.AboutActivity;
import com.example.sumec.wash.activity.HistoryActivity;
import com.example.sumec.wash.activity.ServiceActivity;
import com.example.sumec.wash.activity.WebActivity;
import com.example.sumec.wash.base.BaseFragment;

/**
 * Created by zhdk on 2019/1/14.
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {
    private View mineView;
    private LinearLayout officialLl,serviceLl,aboutLl,historyLl;
    @Override
    public View initView(LayoutInflater inflater) {
        if (mineView == null){
            mineView = inflater.inflate(R.layout.fragment_mine,null);
        }else {
            return mineView;
        }
        return mineView;
    }

    @Override
    protected void initFindViewById(View view) {
        officialLl = (LinearLayout) view.findViewById(R.id.officialLl);
        serviceLl = (LinearLayout) view.findViewById(R.id.serviceLl);
        aboutLl = (LinearLayout) view.findViewById(R.id.aboutLl);
        historyLl = (LinearLayout) view.findViewById(R.id.historyLl);
        officialLl.setOnClickListener(this);
        serviceLl.setOnClickListener(this);
        aboutLl.setOnClickListener(this);
        historyLl.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.officialLl:
                startActivityParam(getActivity(), WebActivity.class,"title","官网","url","https://www.sumecpower.com/");
                break;
            case R.id.serviceLl:
                startActivity(getActivity(), ServiceActivity.class);
                break;
            case R.id.aboutLl:
                startActivity(getActivity(), AboutActivity.class);
                break;
            case R.id.historyLl:
                startActivity(getActivity(), HistoryActivity.class);
                break;
        }
    }
}
