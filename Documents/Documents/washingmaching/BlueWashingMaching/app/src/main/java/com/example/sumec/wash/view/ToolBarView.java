package com.example.sumec.wash.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sumec.wash.R;

/**
 * Created by zhdk on 2019/1/14.
 * ToolBar工具类
 */

public class ToolBarView extends RelativeLayout {
    private ImageView imageBack;
    private ImageView imageMore;
    private TextView tvTitle,tvDone;

    public ToolBarView(Context context) {
        super(context);

    }

    public ToolBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.toolbar_layout, this);
        imageBack = (ImageView) findViewById(R.id.toolbarBack);
        imageMore = (ImageView) findViewById(R.id.toolBarMore);
        tvTitle = (TextView) findViewById(R.id.toolBarTitle);
        tvDone = (TextView) findViewById(R.id.tvDone);
    }
    public void setImageBackOnListener(OnClickListener listener){
        imageBack.setOnClickListener(listener);
    }
    public void setImageMoreOnListener(OnClickListener listener){
        imageMore.setOnClickListener(listener);
    }
    public void setTvDoneOnListener(OnClickListener listener){
        tvDone.setOnClickListener(listener);
    }
    public void setTitleText(String title){
        tvTitle.setText(title);
    }

    /**
     *visibilityType:0 VISIBLE
     * visibilityType:1 GONE
     * visibilityType:2 INVISIBLE
     * @param visibilityType
     */
    public void setImageBackVisibility(int visibilityType){
        imageBack.setVisibility(visibilityType);

    }

    public void setImageMoreVisibility(int visibilityType){
        imageMore.setVisibility(visibilityType);
    }

    public void setTvDoneVisibility(int visibilityType){
        tvDone.setVisibility(visibilityType);
    }
}
