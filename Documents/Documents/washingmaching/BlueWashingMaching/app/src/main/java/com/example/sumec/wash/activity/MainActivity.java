package com.example.sumec.wash.activity;

import com.example.sumec.wash.R;
import com.example.sumec.wash.SampleGattAttributes;
import com.example.sumec.wash.base.BaseActivity;
import com.example.sumec.wash.eventbus.DataEvent;
import com.example.sumec.wash.eventbus.DataEventType;
import com.example.sumec.wash.fragment.FindFragment;
import com.example.sumec.wash.fragment.MineFragment;
import com.example.sumec.wash.fragment.ProductFragment;
import com.example.sumec.wash.fragment.SettingFragment;
import com.example.sumec.wash.utils.BleUtils;
import com.example.sumec.wash.utils.LogUtil;
import com.example.sumec.wash.utils.ToastUtil;
import com.example.sumec.wash.view.ToolBarView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import dmax.dialog.SpotsDialog;


public class MainActivity extends BaseActivity implements FragmentCallback {
    public static MainActivity instance = null;
    private FragmentTabHost fragmentTabHost;
    private String texts[] = {"产品", "设置", "发现", "我"};
    private int imageButton[] = {R.drawable.product1, R.drawable.setting, R.drawable.find, R.drawable.mine};
    private int imageButtonSle[] = {R.drawable.product_sle, R.drawable.setting_sle, R.drawable.find_sle, R.drawable.mine_sle};
    private Class fragmentArray[] = {ProductFragment.class, SettingFragment.class, FindFragment.class, MineFragment.class};
    private ToolBarView toolBarView;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    public static BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<HashMap<String, String>>> uuids;
    private String serviceUuids;
    private String characterUuid;
    private String notifyUuid;
    private BluetoothGattService mnotyGattService;
    public static BluetoothGattCharacteristic characteristic;
    private BluetoothGattCharacteristic changeNameCharacteristic;
    private int rssi;
    private int writeStatus;
    private static byte[] writeBytes;
    private String mDeviceAddress = "";
    private Dialog dialog;

    @Override
    public int getContentView() {
        return R.layout.activity_main;

    }

    @Override
    public void initView() {
        instance = this;
        setToolBarView();
        fragmentTabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        fragmentTabHost.setup(this, getSupportFragmentManager(), R.id.mainContent);
        fragmentTabHost.getTabWidget().setDividerDrawable(null);
        fragmentTabHost.getTabWidget().setBackgroundColor(getResources().getColor(R.color.white));

        for (int i = 0; i < texts.length; i++) {
            TabHost.TabSpec spec = fragmentTabHost.newTabSpec(texts[i]).setIndicator(getView(i));
            fragmentTabHost.addTab(spec, fragmentArray[i], null);
        }
        fragmentTabHost.setCurrentTab(0);
        TextView tv = (TextView) fragmentTabHost.getTabWidget().getChildAt(0).findViewById(R.id.tabText);
        ImageView image = (ImageView) fragmentTabHost.getTabWidget().getChildAt(0).findViewById(R.id.tabImage);
        tv.setTextColor(this.getResources().getColor(R.color.blue1));
        image.setImageResource(R.drawable.product_sle);
        image.setLayoutParams(new LinearLayout.LayoutParams(60, 60));
        image.setPadding(0, 10, 0, 0);
        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                updateTab(fragmentTabHost);//tab按钮动态切换
            }
        });
        //蓝牙连接相关
        setBLE();
        dialog = new SpotsDialog(this);
    }

    private void setToolBarView() {
        toolBarView = (ToolBarView) findViewById(R.id.toolbar);
        toolBarView.setImageBackVisibility(View.GONE);
        toolBarView.setImageMoreVisibility(View.GONE);
        toolBarView.setTitleText("佳孚锂电高压清洗机");
        toolBarView.setTvDoneVisibility(View.GONE);
    }

    @SuppressLint("NewApi")
    public void setBLE() {

        // 初始化 Bluetooth adapter, 通过蓝牙管理器得到一个参考蓝牙适配器(API必须在以上android4.3或以上和版本)
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mDeviceAddress = getIntent().getStringExtra(SampleGattAttributes.DEVICE_ADDRESS);
        //判断是否获取到蓝牙address
        if (mDeviceAddress != null) {
            //如果有则去连接蓝牙
            Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
            bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        } else {
            //TODO:否则通知ProductFragment隐藏产品信息
            EventBus.getDefault().post(new DataEvent(DataEventType.CONNECT_FAILED, DataEventType.CONNECT_FAILED));
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                finish();
            }
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };


    public static BluetoothGattCharacteristic notifyCharacteristic;
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @SuppressLint("RestrictedApi")
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                //TODO:如果连接成功需要通知ProductFragment显示产品信息
                EventBus.getDefault().post(new DataEvent(DataEventType.CONNECT_SUCCESS, DataEventType.CONNECT_SUCCESS));
                dialog.show();
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                //TODO:如果断开连接需要知ProductFragment隐藏产品信息
                EventBus.getDefault().post(new DataEvent(DataEventType.CONNECT_FAILED, DataEventType.CONNECT_FAILED));
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                uuids = displayGattServices(mBluetoothLeService, mBluetoothLeService.getSupportedGattServices());
                //                serviceUuids = mBluetoothLeService.getSupportedGattServices().get(uuids.size()-2).getUuid().toString();
                serviceUuids = "00001000-0000-1000-8000-00805f9b34fb";
//                characterUuid = uuids.get(uuids.size() - 2).get(0).get(LIST_UUID);
                characterUuid = "00001001-0000-1000-8000-00805f9b34fb";
//                notifyUuid= uuids.get(uuids.size() -2).get(1).get(LIST_UUID);
                notifyUuid = "00001002-0000-1000-8000-00805f9b34fb";
                mnotyGattService = mBluetoothLeService.getSupportedGattServices(UUID.fromString(serviceUuids));
                notifyCharacteristic = mnotyGattService.getCharacteristic(UUID.fromString(notifyUuid));
                characteristic = mnotyGattService.getCharacteristic(UUID.fromString(characterUuid));

                changeNameCharacteristic = mnotyGattService.getCharacteristic(UUID.fromString("0000ff06-0000-1000-8000-00805f9b34fb"));
                mBluetoothLeService.setCharacteristicNotification(notifyCharacteristic, true);


            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                String callData = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                LogUtil.fussenLog().d("返回主类" + callData);
                //TODO:处理接受收据
                processData(callData);
            } else if (BluetoothLeService.RSSI.equals(action)) {
                rssi = intent.getIntExtra(BluetoothLeService.EXTRA_DATA, 0);
                //TODO:蓝牙信号强度

                invalidateOptionsMenu();
            } else if (BluetoothLeService.WRITE_SUCCESS.equals(action)) {
                writeStatus = intent.getIntExtra(BluetoothLeService.EXTRA_DATA, 0);
            }
        }
    };


    private void processData(String callData) {
        //TODO:分发处理数据
        if (!TextUtils.isEmpty(callData) && callData.length() > 10) {
            String str = callData.substring(6, 10);
            if (str.equals(DataEventType.GET_SN)) {
                EventBus.getDefault().post(new DataEvent(DataEventType.GET_SN_OK, callData));
            } else if (str.equals(DataEventType.SET_SN)) {
                EventBus.getDefault().post(new DataEvent(DataEventType.SET_SN_OK, callData));
            } else if (str.equals(DataEventType.GET_DATE)) {
                EventBus.getDefault().post(new DataEvent(DataEventType.GET_DATE_OK, callData));
            } else if (str.equals(DataEventType.SET_DATE)) {
                EventBus.getDefault().post(new DataEvent(DataEventType.SET_DATE_OK, callData));
            } else if (str.equals(DataEventType.GET_VERSION)) {
                EventBus.getDefault().post(new DataEvent(DataEventType.GET_VERSION_OK, callData));
            } else if (str.equals(DataEventType.SET_VERSION)) {
                EventBus.getDefault().post(new DataEvent(DataEventType.SET_VERSION_OK, callData));
            } else if (str.equals(DataEventType.SET_WORK)) {
                EventBus.getDefault().post(new DataEvent(DataEventType.SET_WORK_OK, callData));
            } else if (str.equals(DataEventType.SET_CLOSE)) {
                EventBus.getDefault().post(new DataEvent(DataEventType.SET_CLOSE_OK, callData));
            } else if (str.equals(DataEventType.GET_BATTERY)) {
                EventBus.getDefault().post(new DataEvent(DataEventType.GET_BATTERY_OK, callData));
            } else if (str.equals(DataEventType.GET_CURRENT)) {
                EventBus.getDefault().post(new DataEvent(DataEventType.GET_CURRENT_OK, callData));
            } else if (str.equals(DataEventType.GET_MAX_CURRENT)) {
                EventBus.getDefault().post(new DataEvent(DataEventType.GET_MAX_CURRENT_OK, callData));
            } else if (str.equals(DataEventType.GET_FREQUENCY)) {
                EventBus.getDefault().post(new DataEvent(DataEventType.GET_FREQUENCY_OK, callData));
            } else if (str.equals(DataEventType.GET_TOTAL_TIME)) {
                EventBus.getDefault().post(new DataEvent(DataEventType.GET_TOTAL_TIME_OK, callData));
            } else if (str.equals(DataEventType.GET_WORKING_TIME)) {
                EventBus.getDefault().post(new DataEvent(DataEventType.GET_WORKING_TIME_OK, callData));
            } else if (str.equals(DataEventType.SET_WORKING_TIME)) {
                EventBus.getDefault().post(new DataEvent(DataEventType.SET_WORKING_TIME_OK, callData));
            } else if (str.equals(DataEventType.GET_SPEED_DRIVE)) {
                EventBus.getDefault().post(new DataEvent(DataEventType.GET_SPEED_DRIVE_OK, callData));
            } else if (str.equals(DataEventType.SET_SPEED_DRIVE)) {
                EventBus.getDefault().post(new DataEvent(DataEventType.SET_SPEED_DRIVE_OK, callData));
            } else if (str.equals(DataEventType.GET_HEART)) {
                EventBus.getDefault().post(new DataEvent(DataEventType.GET_HEART_OK, callData));
            } else if (str.equals(DataEventType.SET_RESET)) {
                EventBus.getDefault().post(new DataEvent(DataEventType.SET_RESET_OK, callData));
            } else if (str.equals(DataEventType.SET_FREQUENCY)) {
                EventBus.getDefault().post(new DataEvent(DataEventType.SET_FREQUENCY_OK, callData));
            } else if (str.equals(DataEventType.SET_TOTAL_TIME)) {
                EventBus.getDefault().post(new DataEvent(DataEventType.SET_TOTAL_TIME_OK, callData));
            } else if (str.equals(DataEventType.GET_PIN)) {
                EventBus.getDefault().post(new DataEvent(DataEventType.GET_PIN_OK, callData));
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
            } else if (str.equals(DataEventType.SET_PIN)) {
                EventBus.getDefault().post(new DataEvent(DataEventType.SET_PIN_OK, callData));
            }
        } else {
            EventBus.getDefault().post(new DataEvent(DataEventType.GET_PIN_OK, callData));
            if (dialog.isShowing()){
                dialog.dismiss();
            }
        }
    }

    private void updateTab(FragmentTabHost mTabHost) {
        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(R.id.tabText);
            ImageView image = (ImageView) mTabHost.getTabWidget().getChildAt(i).findViewById(R.id.tabImage);
            if (mTabHost.getCurrentTab() == i) {//选中
                tv.setTextColor(this.getResources().getColor(R.color.blue1));
                image.setImageResource(imageButtonSle[i]);
                image.setLayoutParams(new LinearLayout.LayoutParams(60, 60));
                image.setPadding(0, 10, 0, 0);
            } else {//不选中
                tv.setTextColor(this.getResources().getColor(R.color.black));
                image.setImageResource(imageButton[i]);
                image.setLayoutParams(new LinearLayout.LayoutParams(60, 60));
                image.setPadding(0, 10, 0, 0);
            }
        }
    }


    private View getView(int i) {
        View view = View.inflate(MainActivity.this, R.layout.tab_content, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.tabImage);
        TextView textView = (TextView) view.findViewById(R.id.tabText);
        imageView.setImageResource(imageButton[i]);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(60, 60));
        imageView.setPadding(0, 10, 0, 0);
        textView.setText(texts[i]);

        return view;
    }

    @Override
    public void setWindowFeature() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDeviceAddress != null) {
            registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        } else {
            //TODO:否则通知ProductFragment隐藏产品信息
            EventBus.getDefault().post(new DataEvent(DataEventType.CONNECT_FAILED, DataEventType.CONNECT_FAILED));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - firstTime > 2000) {
                ToastUtil.showToast(MainActivity.this, getString(R.string.exit));
                firstTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 通过蓝牙向主板发送命令
     */
    public static void sendData(final Context context, String text) {
        if (characteristic != null) {

            writeBytes = new byte[20];
            String t = "";
            if (!TextUtils.isEmpty(text) && !text.equals("1234")) {
                String length = Integer.toHexString(text.length() / 2);
                if (length.length() < 2) {
                    length = "0" + length;
                }
                String str1 = SampleGattAttributes.BLE_PROTOCOL + length + text;
                String checkNum1 = BleUtils.makeChecksum(str1);
                t = str1 + checkNum1;
            } else if (text.equals("1234")) {
                t = "00";
            }
            writeBytes = (BleUtils.hex2byte(t.getBytes()));
            new Thread(new MainRunnable()).start();
        }

    }

    @Override
    public MainActivity getMainActivity() {
        return this;
    }

    public static class MainRunnable implements Runnable {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void run() {
            //数据分包
            setDataSubcontractor(writeBytes);
            //读
//                mBluetoothLeService.setCharacteristicNotification(notifyCharacteristic, true);
            mBluetoothLeService.readCharacteristic(notifyCharacteristic);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void setDataSubcontractor(byte[] writeBytes) {
        int tmpLen = writeBytes.length;
        int start = 0;
        int end = 0;
        while (tmpLen > 0) {
            byte[] sendData = new byte[20];
            if (tmpLen >= 20) {
                end += 20;
                sendData = Arrays.copyOfRange(writeBytes, start, end);
                start += 20;
                tmpLen -= 20;
            } else {
                end += tmpLen;
                sendData = Arrays.copyOfRange(writeBytes, start, end);
                tmpLen = 0;
            }


            characteristic.setValue(sendData);
            mBluetoothLeService.writeCharacteristic(characteristic);
            SystemClock.sleep(200);
        }
    }

}
