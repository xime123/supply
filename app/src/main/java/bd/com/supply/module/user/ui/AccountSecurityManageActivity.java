package bd.com.supply.module.user.ui;

import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.TextView;

import bd.com.appcore.ui.activity.BaseUiActivity;
import bd.com.appcore.ui.view.CommonLineTextView;
import bd.com.appcore.util.AppSettings;
import bd.com.appcore.util.TDString;
import bd.com.supply.R;
import bd.com.appcore.util.TimeConstant;
import bd.com.supply.module.common.ViewUtil;
import bd.com.supply.util.Navigator;
import bd.com.supply.widget.SelectWalletDialog;
import bd.com.supply.module.common.ViewUtil;
import bd.com.supply.util.Navigator;
import bd.com.supply.widget.SelectWalletDialog;

import static bd.com.appcore.util.AppSettings.getAppSettings;
import static bd.com.appcore.util.TimeConstant.onlineTimeObjs;
import static bd.com.appcore.util.TimeConstant.onlineTimes;
import static bd.com.appcore.util.TimeConstant.timeObjs;
import static bd.com.appcore.util.TimeConstant.times;


public class AccountSecurityManageActivity extends BaseUiActivity<SecurityPresenter, SecurityPresenter.View> implements SecurityPresenter.View, View.OnClickListener {
    private SwitchCompat mGestureSwitch;
    private CommonLineTextView needGestureCtv;
    private TextView walletPathTv;
    private CommonLineTextView updateGestureTv;
    private CommonLineTextView onlineTimeCtv;
    private CompoundButton.OnCheckedChangeListener mLockPatternOpenListener;


    @Override
    protected SecurityPresenter initPresenter() {
        return new SecurityPresenter();
    }

    @Override
    protected void initContentView(ViewGroup containerView) {
        super.initContentView(containerView);
        actionBar.setTitle("安全管理");
        mGestureSwitch = (SwitchCompat) findViewById(R.id.switch_lockpattern);
        needGestureCtv = (CommonLineTextView) findViewById(R.id.need_gesture_ctv);
        updateGestureTv = (CommonLineTextView) findViewById(R.id.tv_edit_lockpattern);
        onlineTimeCtv = (CommonLineTextView) findViewById(R.id.on_line_time_ctv);
    }

    protected void setListener() {
        super.setListener();
        updateGestureTv.setOnClickListener(this);
        needGestureCtv.setOnClickListener(this);
        onlineTimeCtv.setOnClickListener(this);
        mLockPatternOpenListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateLockPatternOpenStatue(!isChecked);
                if (isChecked) {
                    Navigator.toLockPatternSetUp(AccountSecurityManageActivity.this);
                } else {
                    Navigator.toLockPatternClose(AccountSecurityManageActivity.this);
                }
            }
        };
        mGestureSwitch.setOnCheckedChangeListener(mLockPatternOpenListener);
    }

    @Override
    protected SecurityPresenter.View initView() {
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_security_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_edit_lockpattern://修改手势密码
                String lockPattern = getAppSettings().getLockPattern();
                if (TextUtils.isEmpty(lockPattern)) {
                    showToast("您还未设置手势密码");
                    return;
                }
                Navigator.toLockPatternReset(this);
                break;
            case R.id.need_gesture_ctv:
                showSelectGestureTimeDialog();
                break;
            case R.id.on_line_time_ctv:
                showSelectOnLineTimeDialog();
                break;
        }

    }

    private void setGestureTimeView() {
        long time = getAppSettings().getLockPatternTime();
        String timeStr = "";
        //因为只有几个字段，对效率问题几乎没影响，所以这样遍历。后期如果比较多，需要进行缓存，从缓存中直接读取。
        if (time != -1) {
            for (TimeConstant.GestureTime gestureTime : timeObjs) {
                if (time == gestureTime.time) {
                    timeStr = gestureTime.timeName;
                    break;
                }
            }

        }
        needGestureCtv.setContent(timeStr);
    }

    private void setOnlineTimeView() {
        long time = AppSettings.getAppSettings().getOnlineTime();
        String timeStr = "";
        //因为只有几个字段，对效率问题几乎没影响，所以这样遍历。后期如果比较多，需要进行缓存，从缓存中直接读取。
        if (time != -1) {
            for (TimeConstant.GestureTime gestureTime : onlineTimeObjs) {
                if (time == gestureTime.time) {
                    timeStr = gestureTime.timeName;
                    break;
                }
            }

        }
        onlineTimeCtv.setContent(timeStr);
    }

    /**
     * 弹出手势密码启动时间选择
     */
    private void showSelectGestureTimeDialog() {
        // if(!AppSettings.getAppSettings().isLockPatternOpen())return;
        SelectWalletDialog dialog = new SelectWalletDialog(this);
        dialog.setData(times, TDString.getStr(R.string.select_gesture_time), TDString.getStr(R.string.select_gesture_time_des));
        dialog.setOnItemClickListener(new OnDialogItemClick());
        dialog.show();

    }

    /**
     * 弹出在线时间选择
     */
    private void showSelectOnLineTimeDialog() {

        SelectWalletDialog dialog = new SelectWalletDialog(this);
        dialog.setData(onlineTimes, TDString.getStr(R.string.select_online_time), TDString.getStr(R.string.select_online_time_des));
        dialog.setOnItemClickListener(new OnSelectOnlineItemClick());
        dialog.show();

    }

    /**
     * 手势密码启动时间
     */
    class OnDialogItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (!times.isEmpty()) {
                final String time = timeObjs.get(position).timeName;
                long timeLong = timeObjs.get(position).time;
                //加入到AppSettings中
                getAppSettings().setLockPatternTime(timeLong);
                needGestureCtv.setContent(time);
            }
        }
    }

    /**
     * 在线时间选择
     */
    class OnSelectOnlineItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (!onlineTimes.isEmpty()) {
                final String time = onlineTimeObjs.get(position).timeName;
                long timeLong = onlineTimeObjs.get(position).time;
                //加入到AppSettings中
                AppSettings.getAppSettings().setOnlineTime(timeLong);
                onlineTimeCtv.setContent(time);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateLockPatternOpenStatue(AppSettings.getAppSettings().isLockPatternOpen());
    }
    private void updateLockPatternOpenStatue(boolean isOpen) {
        mGestureSwitch.setOnCheckedChangeListener(null);
        ViewUtil.setInvisible(mGestureSwitch, true);
        mGestureSwitch.setChecked(isOpen);
        ViewUtil.setInvisible(mGestureSwitch, false);
        mGestureSwitch.setOnCheckedChangeListener(mLockPatternOpenListener);
    }
}
