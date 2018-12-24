package com.example.apple.ysdemo.ys.devicemgt;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.apple.ysdemo.app.MyApp;
import com.example.apple.ysdemo.ys.RootActivity;
import com.example.apple.ysdemo.ys.ui.cameralist.EZCameraListActivity;
import com.example.apple.ysdemo.ys.ui.util.ActivityUtils;
import com.videogo.constant.IntentConsts;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.exception.BaseException;
import com.videogo.exception.ErrorCode;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.videogo.openapi.bean.EZDeviceVersion;
import com.videogo.util.ConnectionDetector;
import com.videogo.util.LogUtil;
import com.videogo.widget.TitleBar;
import com.videogo.widget.WaitDialog;

public class EZDeviceSettingActivity extends RootActivity {
    private final String TAG = "EZDeviceSettingActivity";
    private static final int REQUEST_CODE_BAIDU = 1;

    private static final int REQUEST_CODE_MODIFY_DEVICE_NAME = 2;

    /**
     * 设置安全模式
     */
    private static final int SHOW_DIALOG_SAFE_MODE = 0;
    /**
     * 关闭安全模式
     */
    private static final int SHOW_DIALOG_CLOSE_SAFE_MODE = 1;
    /**
     * 删除设备
     */
    private final static int SHOW_DIALOG_DEL_DEVICE = 3;
    /**
     * 设备下线上报
     */
    private final static int SHOW_DIALOG_OFFLINE_NOTIFY = 4;
    private final static int SHOW_DIALOG_WEB_SETTINGS_ENCRYPT = 6;
    private final static int SHOW_DIALOG_WEB_SETTINGS_DEFENCE = 7;
    /**
     * 标题栏
     */
    private TitleBar mTitleBar;
    /**
     * 设备基本信息
     */
    private ViewGroup mDeviceInfoLayout;
    /**
     * 设备名称
     */
    private TextView mDeviceNameView;
    /**
     * 设备类型+序列号
     */
    private TextView mDeviceTypeSnView;

    /**
     * 设备序列号
     */
    private ViewGroup mDeviceSNLayout;
    
    /**
     * 防护
     */
    private ViewGroup mDefenceLayout;
    /**
     * 防护
     */
    private TextView mDefenceView;
    /**
     * 防护状态
     */
    private TextView mDefenceStateView;

    /**
     * 防护计划父框架
     */
    private ViewGroup mDefencePlanParentLayout;
    /**
     * 防护计划箭头
     */
    private View mDefencePlanArrowView;

    /**
     * 存储状态
     */
    private ViewGroup mStorageLayout;
    /**
     * 存储提示
     */
    private View mStorageNoticeView;
    /**
     * 设备版本
     */
    private ViewGroup mVersionLayout;
    /**
     * 设备版本状态
     */
    private TextView mVersionView;
    /**
     * 设备版本最新
     */
    private View mVersionNewestView;
    /**
     * 版本提示
     */
    private View mVersionNoticeView;
    /**
     * 版本箭头
     */
    private View mVersionArrowView;

    /**
     * 视频图片加密父框架
     */
    private ViewGroup mEncryptParentLayout;
    /**
     * 视频图片加密切换按钮
     */
    private Button mEncryptButton;
    /**
     * 修改密码
     */
    private ViewGroup mModifyPasswordLayout;

    /* 设备删除 */
    private View mDeviceDeleteView;
    /**
     * 全局按钮监听
     */
    private OnClickListener mOnClickListener;

    private TextView mCurrentVersionTextView;
    private Button mDefenceToggleButton;
    private TextView mDeviceSerialTextView;
    private String mValidateCode;
    private EZDeviceVersion mDeviceVersion = null;
    private EZDeviceInfo mEZDeviceInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 页面统计
        super.onCreate(savedInstanceState);
        setContentView(ezviz.ezopensdk.R.layout.device_setting_page);

        findViews();
        initData();
        initTitleBar();
        initViews();
    }

    /**
     * 控件关联
     */
    private void findViews() {
        mTitleBar = (TitleBar) findViewById(ezviz.ezopensdk.R.id.title_bar);

        mDeviceInfoLayout = (ViewGroup) findViewById(ezviz.ezopensdk.R.id.device_info_layout);
        mDeviceNameView = (TextView) findViewById(ezviz.ezopensdk.R.id.device_name);
        mDeviceTypeSnView = (TextView) findViewById(ezviz.ezopensdk.R.id.device_type_sn);

        mDeviceSNLayout = (ViewGroup) findViewById(ezviz.ezopensdk.R.id.ez_device_serial_layout);

        mDefenceLayout = (ViewGroup) findViewById(ezviz.ezopensdk.R.id.defence_layout);
        mDefenceView = (TextView) findViewById(ezviz.ezopensdk.R.id.defence);
        mDefenceStateView = (TextView) findViewById(ezviz.ezopensdk.R.id.defence_state);

        mDefencePlanParentLayout = (ViewGroup) findViewById(ezviz.ezopensdk.R.id.defence_plan_parent_layout);
        mDefencePlanArrowView = findViewById(ezviz.ezopensdk.R.id.defence_plan_arrow);
        mDefenceToggleButton = (Button) findViewById(ezviz.ezopensdk.R.id.defence_toggle_button);


        mStorageLayout = (ViewGroup) findViewById(ezviz.ezopensdk.R.id.storage_layout);
        mStorageNoticeView = findViewById(ezviz.ezopensdk.R.id.storage_notice);
        mVersionLayout = (ViewGroup) findViewById(ezviz.ezopensdk.R.id.version_layout);
        mVersionView = (TextView) findViewById(ezviz.ezopensdk.R.id.version);
        mVersionNewestView = findViewById(ezviz.ezopensdk.R.id.version_newest);
        mVersionNoticeView = findViewById(ezviz.ezopensdk.R.id.version_notice);
        mVersionArrowView = findViewById(ezviz.ezopensdk.R.id.version_arrow);
        mCurrentVersionTextView = (TextView) findViewById(ezviz.ezopensdk.R.id.current_version);

        mEncryptParentLayout = (ViewGroup) findViewById(ezviz.ezopensdk.R.id.encrypt_parent_layout);
        mEncryptButton = (Button) findViewById(ezviz.ezopensdk.R.id.encrypt_button);
        mModifyPasswordLayout = (ViewGroup) findViewById(ezviz.ezopensdk.R.id.modify_password_layout);

        mDeviceDeleteView = findViewById(ezviz.ezopensdk.R.id.device_delete);
        mDeviceSerialTextView = (TextView) findViewById(ezviz.ezopensdk.R.id.ez_device_serial);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Bundle");
        mEZDeviceInfo = bundle.getParcelable(IntentConsts.EXTRA_DEVICE_INFO);
        if (mEZDeviceInfo == null){
            showToast(ezviz.ezopensdk.R.string.device_have_not_added);
            finish();
        }
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        mTitleBar.setTitle(ezviz.ezopensdk.R.string.ez_setting);
        mTitleBar.addBackButton(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        if (mEZDeviceInfo != null) {
            mOnClickListener = new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent;
                    switch (v.getId()) {
                        case ezviz.ezopensdk.R.id.device_info_layout:
                            intent = new Intent(EZDeviceSettingActivity.this, ModifyDeviceNameActivity.class);
                            intent.putExtra(IntentConsts.EXTRA_NAME, mEZDeviceInfo.getDeviceName());
                            intent.putExtra(IntentConsts.EXTRA_DEVICE_ID,mEZDeviceInfo.getDeviceSerial());
                            startActivityForResult(intent, REQUEST_CODE_MODIFY_DEVICE_NAME);
                            break;

                        case ezviz.ezopensdk.R.id.ez_device_serial_layout:
                            try {
                                if (MyApp.getOpenSDK() instanceof EZOpenSDK) {
                                    EZOpenSDK.getInstance().openCloudPage(mEZDeviceInfo.getDeviceSerial());
                                }
                            }catch (BaseException e) {
                                e.printStackTrace();

                                ErrorInfo errorInfo = (ErrorInfo) e.getObject();
                                LogUtil.debugLog(TAG, errorInfo.toString());
                            }

                            break;

                        case ezviz.ezopensdk.R.id.defence_layout:
                        case ezviz.ezopensdk.R.id.defence_toggle_button:
                        	new SetDefenceTask().execute(!(mEZDeviceInfo.getDefence() != 0));
                        	break;

                        case ezviz.ezopensdk.R.id.defence_plan_button:
                            setDefencePlanNew(false);
                            break;

                        case ezviz.ezopensdk.R.id.defence_plan_status_retry:
                            setDefencePlanNew(false);
                            break;

                        case ezviz.ezopensdk.R.id.defence_plan_set_layout:
                            if (mDefencePlanArrowView.getVisibility() == View.VISIBLE) {
                            }
                            setDefencePlanNew(false);
                            break;

                        case ezviz.ezopensdk.R.id.defence_plan_retry:
                            setDefencePlanNew(false);
                            break;

                        case ezviz.ezopensdk.R.id.storage_layout:
                            break;

                        case ezviz.ezopensdk.R.id.version_layout:
                            intent = new Intent(EZDeviceSettingActivity.this, EZUpgradeDeviceActivity.class);
                            intent.putExtra("deviceSerial", mEZDeviceInfo.getDeviceSerial());
                            startActivity(intent);
                            break;

                        case ezviz.ezopensdk.R.id.encrypt_button:
                            gotoSetSafeMode();
                            break;

                        case ezviz.ezopensdk.R.id.modify_password_layout:
                            gotoModifyPassword();
                            break;
                            
                        case ezviz.ezopensdk.R.id.device_delete:
                            showDialog(SHOW_DIALOG_DEL_DEVICE);
                            break;

                        default:
                            break;
                    }
                }
            };

            new GetDeviceInfoTask().execute();

            // 防护计划设置
            setupSafeModePlan(true);

            mDeviceDeleteView.setOnClickListener(mOnClickListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mCloudStateHelper.onResume();
        setupDeviceInfo();
        setupParentLayout();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mCloudStateHelper.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_BAIDU) {
                setupBaiduDeviceInfo(true, true);
            }
            if (requestCode == REQUEST_CODE_MODIFY_DEVICE_NAME){
                String name = data.getStringExtra(IntentConsts.EXTRA_NAME);
                if (!TextUtils.isEmpty(name)){
                    mEZDeviceInfo.setDeviceName(name);
                }else{
                    LogUtil.debugLog(TAG,"modify device name is null");
                }
            }
        }
    }

    private void setupDeviceInfo() {
        if (mEZDeviceInfo != null) {
            // 设备图片部分
            // 设备名字部分
//            String typeSn = String.format("%s(%s)",
//                    TextUtils.isEmpty(mDeviceModel.getDisplay()) ? mDevice.getFullModel() : mDeviceModel.getDisplay(),
//                    mDevice.getDeviceID());
            String typeSn = mEZDeviceInfo.getDeviceName();
            mDeviceSerialTextView.setText(mEZDeviceInfo.getDeviceSerial());

            mDeviceNameView.setText(TextUtils.isEmpty(typeSn)?"":typeSn);

            mDeviceTypeSnView.setVisibility(View.GONE);

            mDeviceInfoLayout.setOnClickListener(mOnClickListener);
            mDeviceSNLayout.setOnClickListener(mOnClickListener);

            mDefencePlanParentLayout.setVisibility(View.GONE);

            boolean bSupportDefence = true;
            if(bSupportDefence) {
            	mDefenceView.setText(ezviz.ezopensdk.R.string.detail_defend_c1_c2_f1);
                mDefenceStateView.setTextColor(getResources().getColorStateList(ezviz.ezopensdk.R.color.on_off_text_selector));
//                mDefenceStateView.setText(mDevice.isDefenceOn() ? R.string.on : R.string.off);
//                mDefenceStateView.setEnabled(mEZCameraInfo.getDefence() == 1);
                boolean isDefenceEnable = (mEZDeviceInfo.getDefence() != 0);
                mDefenceToggleButton.setBackgroundResource(isDefenceEnable ? ezviz.ezopensdk.R.drawable.autologin_on
                        : ezviz.ezopensdk.R.drawable.autologin_off);
                mDefenceToggleButton.setOnClickListener(mOnClickListener);
            
				mDefenceLayout.setVisibility(View.VISIBLE);
//				mDefenceLayout.setTag(supportMode);
//				mDefenceLayout.setOnClickListener(mOnClickListener); // dont allow to click the list
           }

            // 存储状态部分

            {

                mStorageNoticeView.setVisibility(View.VISIBLE);

                // TODO
                mStorageLayout.setVisibility(View.VISIBLE);
                mStorageLayout.setOnClickListener(mOnClickListener);
            }

            // 版本部分
            if (mEZDeviceInfo.getStatus() == 1 && mDeviceVersion != null) {
                boolean bHasUpgrade = (mDeviceVersion.getIsNeedUpgrade() != 0);
                mCurrentVersionTextView.setText(mDeviceVersion.getCurrentVersion());
                mVersionView.setText(mDeviceVersion.getNewestVersion());
                if (bHasUpgrade){
                    mVersionNewestView.setVisibility(View.VISIBLE);
                } else {
                    mVersionNewestView.setVisibility(View.GONE);
                }

//                bHasUpgrade = true;// TODO stub
                if(bHasUpgrade) {
                    mVersionNoticeView.setVisibility(View.VISIBLE);
                    mVersionArrowView.setVisibility(View.VISIBLE);
                    mVersionLayout.setOnClickListener(mOnClickListener);
                } else {
                    mVersionNoticeView.setVisibility(View.GONE);
                    mVersionArrowView.setVisibility(View.GONE);
                    mVersionLayout.setOnClickListener(null);
                }
                mVersionLayout.setVisibility(View.VISIBLE);
            } else {
                mVersionLayout.setVisibility(View.GONE);
            }

            // 视频图片加密部分
            boolean bSupportEncrypt = true;
            //if (mDevice.getSupportEncrypt() == DeviceConsts.NOT_SUPPORT || !mDevice.isOnline()) {
            if(!bSupportEncrypt) {
                mEncryptParentLayout.setVisibility(View.GONE);
            } else {
                mEncryptButton
                        .setBackgroundResource((mEZDeviceInfo.getIsEncrypt() == 1) ? ezviz.ezopensdk.R.drawable.autologin_on
                                : ezviz.ezopensdk.R.drawable.autologin_off);
                mEncryptButton.setOnClickListener(mOnClickListener);

                mModifyPasswordLayout.setOnClickListener(mOnClickListener);
                boolean bSupportChangePwd = false;
//                if ((mEZCameraInfo.getEncryptStatus() != 1))
                       // || mDevice.getSupportChangeSafePasswd() == DeviceConsts.NOT_SUPPORT) {
                if(!bSupportChangePwd) {
                    mModifyPasswordLayout.setVisibility(View.GONE);
                } else {
                    mModifyPasswordLayout.setVisibility(View.VISIBLE);
                }

                mEncryptParentLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setupParentLayout() {
        // 在线父框架
//        mOnlineParentLayout.setVisibility(mOnlineTimeLayout.getVisibility() & mOfflineNotifyLayout.getVisibility());
    }

    private void setupSafeModePlan(boolean fromServer) {}

    private void setDefencePlanNew(boolean visible) {
    }

    private void setupBaiduDeviceInfo(boolean fromServer, boolean reload) {}

    /**
     * 修改加密密码
     */
    private void gotoModifyPassword() {
    }

    private void gotoSetSafeMode() {
        if (mEZDeviceInfo.getIsEncrypt() == 1) {
            // 如果开启，则关闭
            if (!isFinishing()) {
                showDialog(SHOW_DIALOG_CLOSE_SAFE_MODE);
            }
        } else {
            // 如果关闭，则开启
//            showDialog(SHOW_DIALOG_SAFE_MODE);
            openSafeMode();
        }
    }

    /**
     * 　　开启安全状态
     */
    private void openSafeMode() {
        // 首先判断有没有密码，如果有的话，就不需要设置了
//        if (TextUtils.isEmpty(mDevice.getEncryptPwd()) || mDevice.getEncryptPwd().equals("null")) {
//            Intent intent = new Intent(DeviceSettingActivity.this, DeviceEncryptPasswordActivity.class);
//            intent.putExtra("deviceID", mDevice.getDeviceID());
//            startActivity(intent);
//        } else {
//            new OpenEncryptTask().execute();
//        }
            new OpenEncryptTask().execute(true);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch (id) {
            case SHOW_DIALOG_SAFE_MODE: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this).setPositiveButton(ezviz.ezopensdk.R.string.cancel,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setNegativeButton(ezviz.ezopensdk.R.string.certain, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openSafeMode();
                    }
                });

                {
                    builder.setMessage(getString(ezviz.ezopensdk.R.string.detail_safe_btn_tip));
                }

                dialog = builder.create();
            }
                break;
            case SHOW_DIALOG_CLOSE_SAFE_MODE: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this).setNegativeButton(ezviz.ezopensdk.R.string.cancel,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setPositiveButton(ezviz.ezopensdk.R.string.certain, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        openVideoEncryptDialog();
                    }
                });

                builder.setMessage(getString(ezviz.ezopensdk.R.string.detail_safe_close_btn_tip));

                dialog = builder.create();
            	}
                break;

            case SHOW_DIALOG_DEL_DEVICE:
                dialog = new AlertDialog.Builder(this).setMessage(getString(ezviz.ezopensdk.R.string.detail_del_device_btn_tip))
                        .setNegativeButton(ezviz.ezopensdk.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setPositiveButton(ezviz.ezopensdk.R.string.certain, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new DeleteDeviceTask().execute();
                            }
                        }).create();
                break;

            case SHOW_DIALOG_OFFLINE_NOTIFY:
                break;
            case SHOW_DIALOG_WEB_SETTINGS_ENCRYPT:
            {
                dialog = new AlertDialog.Builder(this).setMessage("该功能暂时只支持页面操作哦")
                        .setNegativeButton(ezviz.ezopensdk.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setPositiveButton(ezviz.ezopensdk.R.string.certain, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
            }
            break;
            case SHOW_DIALOG_WEB_SETTINGS_DEFENCE:
            {
                dialog = new AlertDialog.Builder(this).setMessage("该功能暂时只支持页面操作哦")
                        .setNegativeButton(ezviz.ezopensdk.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setPositiveButton(ezviz.ezopensdk.R.string.certain, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
            }
            break;
            default:
                break;
        }
        return dialog;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        if (dialog != null) {
            removeDialog(id);
//            TextView tv = (TextView) dialog.findViewById(android.R.id.message);
//            tv.setGravity(Gravity.CENTER);
        }
    }

    private void openVideoEncryptDialog() {
        LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup smsVerifyView = (ViewGroup) mLayoutInflater.inflate(ezviz.ezopensdk.R.layout.device_video_encrypt_dialog, null, true);
        final EditText etSmsCode = (EditText) smsVerifyView.findViewById(ezviz.ezopensdk.R.id.ez_sms_code_et);

        new  AlertDialog.Builder(EZDeviceSettingActivity.this)  
        .setTitle(ezviz.ezopensdk.R.string.input_device_verify_code)
        .setIcon(android.R.drawable.ic_dialog_info)   
        .setView(smsVerifyView)
        .setPositiveButton(ezviz.ezopensdk.R.string.ez_dialog_btn_disable_video_encrypt, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            	String sms = null;
            	if(etSmsCode != null) {
            		sms = etSmsCode.getEditableText().toString();
            	}
            	mValidateCode = sms;
            	if(!TextUtils.isEmpty(mValidateCode)) {
            		new OpenEncryptTask().execute(false); //disable video encryption
            	}
            }
            
        })   
        .setNegativeButton(ezviz.ezopensdk.R.string.cancel, null)
        .show();  
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra(IntentConsts.EXTRA_DEVICE_INFO, mEZDeviceInfo);
        setResult(RESULT_OK, data);
        super.finish();
    }

    /**
     * 获取设备信息
     */
    private class GetDeviceInfoTask extends AsyncTask<Void, Void, Boolean> {

        private int mErrorCode = 0;

        @Override
        protected Boolean doInBackground(Void... params) {
        	try {
        		mDeviceVersion = MyApp.getOpenSDK().getDeviceVersion(mEZDeviceInfo.getDeviceSerial());
        		return true;
        	} catch (BaseException e) {;
                ErrorInfo errorInfo = (ErrorInfo) e.getObject();
                mErrorCode = errorInfo.errorCode;
                LogUtil.debugLog(TAG, errorInfo.toString());
        	}
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (result) {
                setupDeviceInfo();
                setupParentLayout();
            } else {
                switch (mErrorCode) {
                    case ErrorCode.ERROR_WEB_NET_EXCEPTION:
                        break;
                    case ErrorCode.ERROR_WEB_SESSION_ERROR:
                        ActivityUtils.handleSessionException(EZDeviceSettingActivity.this);
                        break;
                    case ErrorCode.ERROR_WEB_HARDWARE_SIGNATURE_ERROR:
                        ActivityUtils.handleSessionException(EZDeviceSettingActivity.this);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 设备登录任务
     */
    private class DeviceLoginTask extends AsyncTask<Void, Void, Boolean> {

        private Dialog mWaitDialog;

        private int mErrorCode = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mWaitDialog = new WaitDialog(EZDeviceSettingActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
            mWaitDialog.setCancelable(false);
            mWaitDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            mWaitDialog.dismiss();

//            if (result) {
////                Intent intent = new Intent(DeviceSettingActivity.this, DeviceWifiListActivity.class);
////                intent.putExtra("deviceId", mDevice.getDeviceID());
////                startActivity(intent);
//            } else if (mErrorCode == HCNetSDKException.NET_DVR_PASSWORD_ERROR) {
//                showInputDevicePswDlg();
//            } else {
//                showToast(R.string.device_wifi_set_no_in_subnet);
//            }
        }
    }

    private class SetDefenceTask extends AsyncTask<Boolean, Void, Boolean> {
        private Dialog mWaitDialog;
        private int mErrorCode = 0;
        boolean bSetDefence;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mWaitDialog = new WaitDialog(EZDeviceSettingActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
            mWaitDialog.setCancelable(false);
            mWaitDialog.show();
		}

		@Override
		protected Boolean doInBackground(Boolean... params) {
			bSetDefence = (Boolean) params[0];
			Boolean result = false;
			try {
				result = MyApp.getOpenSDK().setDefence(mEZDeviceInfo.getDeviceSerial(), bSetDefence? EZConstants.EZDefenceStatus.EZDefence_IPC_OPEN:
                        EZConstants.EZDefenceStatus.EZDefence_IPC_CLOSE);
			} catch (BaseException e) {
                ErrorInfo errorInfo = (ErrorInfo) e.getObject();
                mErrorCode = errorInfo.errorCode;
                LogUtil.debugLog(TAG, errorInfo.toString());

				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			mWaitDialog.dismiss();
			if(result) {
				mEZDeviceInfo.setDefence(bSetDefence ? 1 : 0);
				setupDeviceInfo();
			} else {
				switch (mErrorCode) {
                case ErrorCode.ERROR_WEB_NET_EXCEPTION:
                    showToast(ezviz.ezopensdk.R.string.encrypt_password_open_fail_networkexception);
                    break;
                case ErrorCode.ERROR_WEB_SESSION_ERROR:
                    ActivityUtils.handleSessionException(EZDeviceSettingActivity.this);
                    break;
                case ErrorCode.ERROR_WEB_HARDWARE_SIGNATURE_ERROR:
                    ActivityUtils.handleSessionException(EZDeviceSettingActivity.this);
                    break;
                default:
                    showToast(ezviz.ezopensdk.R.string.encrypt_password_open_fail, mErrorCode);
                    break;
				}
			}
		}

    }
    /**
     * 开启设备视频图片加密任务
     */
    private class OpenEncryptTask extends AsyncTask<Boolean, Void, Boolean> {
        private boolean bAction;
        private Dialog mWaitDialog;

        private int mErrorCode = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mWaitDialog = new WaitDialog(EZDeviceSettingActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
            mWaitDialog.setCancelable(false);
            mWaitDialog.show();
        }

        @Override
        protected Boolean doInBackground(Boolean... params) {
        	boolean isEnableEncrypt = params[0];
            bAction = isEnableEncrypt;
            try {
            	MyApp.getOpenSDK().setDeviceEncryptStatus(mEZDeviceInfo.getDeviceSerial(),mValidateCode,isEnableEncrypt);

                return true;
            } catch (BaseException e) {
                ErrorInfo errorInfo = (ErrorInfo) e.getObject();
                mErrorCode = errorInfo.errorCode;
                LogUtil.debugLog(TAG, errorInfo.toString());

                LogUtil.errorLog(TAG, "error description: " + e.getMessage());
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            mWaitDialog.dismiss();

            if (result) {
                showToast(ezviz.ezopensdk.R.string.encrypt_password_open_success);
//                mDevice.setIsEncrypt(1);
//                bAction = !bAction;
                mEZDeviceInfo.setIsEncrypt(bAction ? 1 : 0);
                mEncryptButton.setBackgroundResource(bAction ? ezviz.ezopensdk.R.drawable.autologin_on : ezviz.ezopensdk.R.drawable.autologin_off);
//                if (mDevice.getSupportChangeSafePasswd() != DeviceConsts.NOT_SUPPORT)
//                    mModifyPasswordLayout.setVisibility(View.VISIBLE);
            } else {
                switch (mErrorCode) {
                    case ErrorCode.ERROR_WEB_NET_EXCEPTION:
                        showToast(ezviz.ezopensdk.R.string.encrypt_password_open_fail_networkexception);
                        break;
                    case ErrorCode.ERROR_WEB_SESSION_ERROR:
                        ActivityUtils.handleSessionException(EZDeviceSettingActivity.this);
                        break;
                    case ErrorCode.ERROR_WEB_HARDWARE_SIGNATURE_ERROR:
                        ActivityUtils.handleSessionException(EZDeviceSettingActivity.this);
                        break;
                    default:
                        showToast(ezviz.ezopensdk.R.string.encrypt_password_open_fail, mErrorCode);
                        break;
                }
            }
        }
    }

    /**
     * 删除设备任务
     */
    private class DeleteDeviceTask extends AsyncTask<Void, Void, Boolean> {

        private Dialog mWaitDialog;

        private int mErrorCode = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mWaitDialog = new WaitDialog(EZDeviceSettingActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
            mWaitDialog.setCancelable(false);
            mWaitDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (!ConnectionDetector.isNetworkAvailable(EZDeviceSettingActivity.this)) {
                mErrorCode = ErrorCode.ERROR_WEB_NET_EXCEPTION;
                return false;
            }

            try {
//            	EZCameraInfo cameraInfo = params[0];
                MyApp.getOpenSDK().deleteDevice(mEZDeviceInfo.getDeviceSerial());
                return true;
            } catch (BaseException e) {
                ErrorInfo errorInfo = (ErrorInfo) e.getObject();
                mErrorCode = errorInfo.errorCode;
                LogUtil.debugLog(TAG, errorInfo.toString());

                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            mWaitDialog.dismiss();

            if (result) {
                showToast(ezviz.ezopensdk.R.string.detail_del_device_success);
                Intent intent = new Intent(EZDeviceSettingActivity.this, EZCameraListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                switch (mErrorCode) {
                    case ErrorCode.ERROR_WEB_SESSION_ERROR:
                        ActivityUtils.handleSessionException(EZDeviceSettingActivity.this);
                        break;
                    case ErrorCode.ERROR_WEB_HARDWARE_SIGNATURE_ERROR:
                        ActivityUtils.handleSessionException(EZDeviceSettingActivity.this);
                        break;
                    case ErrorCode.ERROR_WEB_NET_EXCEPTION:
                        showToast(ezviz.ezopensdk.R.string.alarm_message_del_fail_network_exception);
                        break;
                    case ErrorCode.ERROR_WEB_DEVICE_VALICATECODE_ERROR:
                        showToast(ezviz.ezopensdk.R.string.verify_code_error);
                    default:
                        showToast(ezviz.ezopensdk.R.string.alarm_message_del_fail_txt, mErrorCode);
                        break;
                }
            }
        }
    }

}