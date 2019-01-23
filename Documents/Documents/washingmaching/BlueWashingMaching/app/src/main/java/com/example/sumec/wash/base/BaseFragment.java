package com.example.sumec.wash.base;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * sumec
 * User:zhdk1996
 * Date: 2017/7/8.
 * Time:9:15.
 */

public abstract class BaseFragment extends Fragment {
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    public static final String LIST_NAME = "NAME";
    public static final String LIST_UUID = "UUID";

    public FragmentActivity mActivity;
    public View view;

    /**
     * 此方法可以得到上下文对象
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    /*
     * 返回一个需要展示的View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mActivity = getActivity();
        if (view == null) {
            view = initView(inflater);
            initFindViewById(view);
        }

        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        return view;
    }


    /**
     * 子类可以复写此方法初始化事件
     */
    protected void initEvent() {

    }

    /*
     * 当Activity初始化之后可以在这里进行一些数据的初始化操作
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initEvent();
    }

    /**
     * 子类实现此抽象方法返回View进行展示
     *
     * @return
     */
    public abstract View initView(LayoutInflater inflater);

    /**
     * 初始化控件
     */
    protected abstract void initFindViewById(View view);

    /**
     * 子类在此方法中实现数据的初始化
     */
    public abstract void initData();

    public void startActivity(Context context, Class clazz) {
        startActivity(new Intent(context, clazz));
    }

    public void startActivityParam(Context context, Class clazz, String key, String param) {
        Intent intent = new Intent(context, clazz);
        Bundle bundle = new Bundle();
        bundle.putString(key, param);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void startActivityParam(Context context, Class clazz, String titleKey, String titleParam, String contentKey, String contentParam) {
        Intent intent = new Intent(context, clazz);
        Bundle bundle = new Bundle();
        bundle.putString(titleKey, titleParam);
        bundle.putString(contentKey, contentParam);
        intent.putExtras(bundle);
        startActivity(intent);
    }


}
