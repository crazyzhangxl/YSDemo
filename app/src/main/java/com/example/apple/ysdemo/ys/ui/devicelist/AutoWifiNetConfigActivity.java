package com.example.apple.ysdemo.ys.ui.devicelist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hikvision.wifi.configuration.BaseUtil;
import com.example.apple.ysdemo.ys.RootActivity;
import com.videogo.util.ConnectionDetector;
import com.videogo.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import ezviz.ezopensdk.R;

public class AutoWifiNetConfigActivity extends RootActivity implements OnClickListener {

    /** wifi密码 */
    public static final String WIFI_PASSWORD = "wifi_password";

    /** wifiSSID */
    public static final String WIFI_SSID = "wifi_ssid";

    /** deviceType */
    public static final String DEVICE_TYPE = "device_type";

    public static final String SUPPORT_WIFI = "support_Wifi";
    public static final String SUPPORT_NET_WORK = "support_net_work";

    private Button btnNext;

    private TextView tvSSID;

    private EditText edtPassword;

    private String seriaNo;

    private String veryCode = null;

    private boolean isSupportNetWork = false;

    private boolean isFromDeviceSetting;

    private String deviceType;

    private TextView tvTitle;
    protected static final String CONFIG_TYPE_KEY = "config_type";
    protected static final int CONFIG_TYPE_WIRED_CONNECTION = 0;
    protected static final int CONFIG_TYPE_SMARTCONFIG = 1;
    protected static final int CONFIG_TYPE_SOUNDWAVE = 2;
    protected static final int CONFIG_TYPE_AP = 3;
    protected static final int CONFIG_TYPE_SMART_SOUNDWAVE = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_wifi_net_config);
        init();
        initTitleBar();
        findViews();
        initUI();
        setListener();
    }


    private void initTitleBar() {
        TitleBar mTitleBar = (TitleBar) findViewById(R.id.title_bar);
        tvTitle = mTitleBar.setTitle(R.string.auto_wifi_cer_config_title1);
        mTitleBar.addBackButton(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void showWifiRequiredDialog() {

        new AlertDialog.Builder(this).setTitle(R.string.auto_wifi_dialog_title_wifi_required)
                .setMessage(R.string.please_open_wifi_network)
                .setNegativeButton(R.string.auto_wifi_dialog_btn_wifi, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {

                        dialog.dismiss();
                        // 跳转wifi设置界面
                        if (android.os.Build.VERSION.SDK_INT > 10) {
                            // 3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
                            startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                        } else {
                            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                        }
                    }
                }).setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        onBackPressed();
                    }
                }).setCancelable(false).create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ConnectionDetector.getConnectionType(this) != ConnectionDetector.WIFI) {
            tvSSID.setText(R.string.unknow_ssid);
            showWifiRequiredDialog();
        } else {
            tvSSID.setText(BaseUtil.getWifiSSID(this));
        }
    }

    private void init() {
        seriaNo = getIntent().getStringExtra(SeriesNumSearchActivity.BUNDE_SERIANO);
        veryCode = getIntent().getStringExtra(SeriesNumSearchActivity.BUNDE_VERYCODE);
        isSupportNetWork = getIntent().getBooleanExtra(SUPPORT_NET_WORK, false);
        isFromDeviceSetting = getIntent().getBooleanExtra(ResetIntroduceActivity.IS_FROM_DEVICE_SETTING, false);
        deviceType = getIntent().getStringExtra(DEVICE_TYPE);
    }

    /**
     * 这里对方法做描述
     * 
     * @see
     * @since V1.0
     */
    private void findViews() {
        btnNext = (Button) findViewById(R.id.btnNext);
        tvSSID = (TextView) findViewById(R.id.tvSSID);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
    }

    /**
     * 这里对方法做描述
     * 
     * @see
     * @since V1.0
     */
    private void initUI() {

        if (isFromDeviceSetting) {
            tvTitle.setText(R.string.auto_wifi_cer_config_title1);
        } else {
            tvTitle.setText(R.string.auto_wifi_cer_config_title2);
        }
        tvSSID.setText(BaseUtil.getWifiSSID(this));

        WifiManager mWifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        // 取得WifiInfo对象
        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        // 路由器的mac地址
        String mac = (mWifiInfo == null) ? "" : mWifiInfo.getBSSID();
        String password = "";
//        if (!TextUtils.isEmpty(mac)) {
//            password = LocalInfo.getInstance().getWifiPassword(mac);
//        }

        edtPassword.setText(password);
    }

    /**
     * 这里对方法做描述
     * 
     * @see
     * @since V1.0
     */
    private void setListener() {
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btnNext:
                //if (isFromDeviceSetting) {
                //    //intent = new Intent(this, AutoWifiOfflineDeviceActivity.class);
                //} else {
                //    intent = new Intent(this, AutoWifiConnectingActivity.class);
                //}
                //intent.putExtra(WIFI_SSID, tvSSID.getText().toString());
                //intent.putExtra(WIFI_PASSWORD, TextUtils.isEmpty(edtPassword.getText().toString()) ? "smile"
                //        : edtPassword.getText().toString());
                //intent.putExtra(SeriesNumSearchActivity.BUNDE_SERIANO, seriaNo);
                //intent.putExtra(SeriesNumSearchActivity.BUNDE_VERYCODE, veryCode);
                //intent.putExtra(SUPPORT_WIFI, true);
                //intent.putExtra(SUPPORT_NET_WORK, isSupportNetWork);
                //intent.putExtra(ResetIntroduceActivity.IS_FROM_DEVICE_SETTING, isFromDeviceSetting);
                //intent.putExtra(DEVICE_TYPE, deviceType);
                //startActivity(intent);
                showSelectNetConfig();
                break;
            default:
                break;
        }
    }

    private void showSelectNetConfig() {
        final List<String> list = new ArrayList<String>();
        final List<Integer> list_types = new ArrayList<Integer>();
        list.add(getString(R.string.string_wired_connection));
        list_types.add(CONFIG_TYPE_WIRED_CONNECTION);

        boolean support_Wifi = getIntent().getBooleanExtra("support_Wifi",false);
        boolean support_Ap = getIntent().getBooleanExtra("support_Ap",false);
        boolean support_sound_wave = getIntent().getBooleanExtra("support_sound_wave",false);
        boolean support_unknow_mode = getIntent().getBooleanExtra("support_unknow_mode",false);
        if (support_unknow_mode){
            // 未查询到设备信息，不确定设备支持的配网能力,需要用户根据指示灯判断
            //若设备指示灯红蓝闪烁，请选择
            list.add( getString(R.string.string_smart_connection_normal));
            list_types.add(CONFIG_TYPE_SMARTCONFIG);

            // 若设备指示灯蓝色闪烁，请选择
            list.add(getString(R.string.string_ap_connection_normal));
            list_types.add(CONFIG_TYPE_AP);

        }else{
            // 查询到设备信息，根据能力级选择配网方式
            if (support_Wifi) {
                list.add(getString(R.string.string_smart_connection));
                list_types.add(CONFIG_TYPE_SMARTCONFIG);
            }
            if (support_sound_wave) {
                list.add(getString(R.string.string_sound_wave_connection));
                list_types.add(CONFIG_TYPE_SOUNDWAVE);
            }
            if (support_Ap) {
                list.add(getString(R.string.string_ap_connection));
                list_types.add(CONFIG_TYPE_AP);
            }
        }

        final String items[] = new String[list.size()];
        list.toArray(items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.string_device_network_mode).setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, final int i) {
                dialogInterface.dismiss();
                Intent intent = null;
                switch (list_types.get(i)) {
                    case CONFIG_TYPE_WIRED_CONNECTION:
                        // TODO: 2018/6/28 有线连接
                        break;
                    case CONFIG_TYPE_SMARTCONFIG:
                        // TODO: 2018/6/26 smart config wifi
                        intent = new Intent(AutoWifiNetConfigActivity.this, AutoWifiConnectingActivity.class);

                        intent.putExtra(WIFI_SSID, tvSSID.getText().toString());
                        intent.putExtra(WIFI_PASSWORD, TextUtils.isEmpty(edtPassword.getText().toString()) ? "smile"
                                : edtPassword.getText().toString());
                        intent.putExtra(SeriesNumSearchActivity.BUNDE_SERIANO, seriaNo);
                        intent.putExtra(SeriesNumSearchActivity.BUNDE_VERYCODE, veryCode);
                        intent.putExtra("support_Wifi", true);
                        intent.putExtra(SUPPORT_NET_WORK, isSupportNetWork);
                        intent.putExtra(ResetIntroduceActivity.IS_FROM_DEVICE_SETTING, isFromDeviceSetting);
                        intent.putExtra(DEVICE_TYPE, deviceType);
                        startActivity(intent);
                        break;
                    case CONFIG_TYPE_SOUNDWAVE:
                        // TODO: 2018/6/26 声波配网
                        intent = new Intent(AutoWifiNetConfigActivity.this, AutoWifiConnectingActivity.class);
                        intent.putExtra(WIFI_SSID, tvSSID.getText().toString());
                        intent.putExtra(WIFI_PASSWORD, TextUtils.isEmpty(edtPassword.getText().toString()) ? "smile"
                                : edtPassword.getText().toString());
                        intent.putExtra(SeriesNumSearchActivity.BUNDE_SERIANO, seriaNo);
                        intent.putExtra(SeriesNumSearchActivity.BUNDE_VERYCODE, veryCode);
                        intent.putExtra("support_sound_wave", true);
                        intent.putExtra(SUPPORT_NET_WORK, isSupportNetWork);
                        intent.putExtra(ResetIntroduceActivity.IS_FROM_DEVICE_SETTING, isFromDeviceSetting);
                        intent.putExtra(DEVICE_TYPE, deviceType);
                        startActivity(intent);
                        break;
                    case CONFIG_TYPE_AP:
                        // TODO: 2018/6/26 ap模式配网
                        intent = new Intent(AutoWifiNetConfigActivity.this, APWifiConfigActivity.class);
                        intent.putExtra(WIFI_SSID, tvSSID.getText().toString());
                        intent.putExtra(WIFI_PASSWORD, TextUtils.isEmpty(edtPassword.getText().toString()) ? "smile"
                                : edtPassword.getText().toString());
                        intent.putExtra(SeriesNumSearchActivity.BUNDE_SERIANO, seriaNo);
                        intent.putExtra(SeriesNumSearchActivity.BUNDE_VERYCODE, veryCode);
                        //intent.putExtras(getIntent());
                        startActivity(intent);
                        break;
                    case CONFIG_TYPE_SMART_SOUNDWAVE:
                        // TODO: 2018/6/26 声波配网+普通配网同时进行
                        intent = new Intent(AutoWifiNetConfigActivity.this, AutoWifiConnectingActivity.class);
                        intent.putExtra(WIFI_SSID, tvSSID.getText().toString());
                        intent.putExtra(WIFI_PASSWORD, TextUtils.isEmpty(edtPassword.getText().toString()) ? "smile"
                                : edtPassword.getText().toString());
                        intent.putExtra(SeriesNumSearchActivity.BUNDE_SERIANO, seriaNo);
                        intent.putExtra(SeriesNumSearchActivity.BUNDE_VERYCODE, veryCode);
                        intent.putExtra("support_sound_wave", true);
                        intent.putExtra("support_Wifi", true);
                        intent.putExtra(SUPPORT_NET_WORK, isSupportNetWork);
                        intent.putExtra(ResetIntroduceActivity.IS_FROM_DEVICE_SETTING, isFromDeviceSetting);
                        intent.putExtra(DEVICE_TYPE, deviceType);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        }).setNegativeButton(android.R.string.cancel, null).show();
    }
}
