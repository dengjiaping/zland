package com.zhisland.android.blog.event.controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.util.EditTextUtil;
import com.zhisland.android.blog.common.util.SoftInputUtil;
import com.zhisland.android.blog.common.util.TimeUtil;
import com.zhisland.android.blog.common.view.CityPickDlg;
import com.zhisland.android.blog.event.dto.Event;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.view.ADateTimePicker;
import com.zhisland.lib.view.NumberPicker;
import com.zhisland.lib.view.NumberPicker.OnScrollListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 活动编辑第一页
 */
public class FragEventCreateFirst extends FragBase {

    private static final int REQ_CATEGORY = 1;

    Event event;

    @InjectView(R.id.tvError)
    TextView tvError;

    @InjectView(R.id.etTitle)
    EditText etTitle;

    @InjectView(R.id.tvStartTime)
    TextView tvStartTime;

    @InjectView(R.id.tvEndTime)
    TextView tvEndTime;

    @InjectView(R.id.tvCity)
    TextView tvCity;

    @InjectView(R.id.etAddress)
    EditText etAddress;

    @InjectView(R.id.etDesc)
    EditText etDesc;

    @InjectView(R.id.llCategory)
    LinearLayout llCategory;

    CityPickDlg cityPickDlg;

    TimeHolder timeHolder;

    @Override
    protected String getPageName() {
        return "EventCreate";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_create_event_first, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        root.setLayoutParams(lp);
        ButterKnife.inject(this, root);
        initView();
        return root;
    }

    private void initView() {
        EditTextUtil.limitEditTextLengthChinese(etTitle, 17, null);
        EditTextUtil.limitEditTextLengthChinese(etAddress, 17, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        event = (Event) getActivity().getIntent().getSerializableExtra(
                ActEventCreate.KEY_EVENT);
        if (event == null) {
            event = new Event();
        } else {
            fillEvent();
        }
        refreshTitle();
    }

    /**
     * 每当信息发生改变时，检查title中的按钮下一步是否可以点击。
     */
    private void refreshTitle() {
        Activity act = getActivity();
        if (act != null && act instanceof ActEventCreate) {
            ((ActEventCreate) act).refreshTitle();
        }
    }

    /**
     * 将event中的数据填到View上
     */
    private void fillEvent() {
        if (event.eventTitle != null) {
            etTitle.setText(event.eventTitle);
            etTitle.setSelection(etTitle.getText().length());
        }
        tvStartTime.setText(TimeUtil.long2String(event.startTime));
        if (event.endTime <= 0) {
            event.endTime = event.startTime + 60 * 60 * 4;
        }
        tvEndTime.setText(TimeUtil.long2String(event.endTime));
        setCityTxt();
        if (event.location != null) {
            etAddress.setText(event.location);
        }
        if (event.content != null) {
            etDesc.setText(event.content);
        }
        setCategory();
    }

    @OnClick(R.id.tvStartTime)
    void startTimeClick() {
        if (timeHolder == null) {
            timeHolder = new TimeHolder(getActivity());
        }
        timeHolder.show(event, TimeHolder.SWITCH_TIME_START);
    }

    @OnClick(R.id.tvEndTime)
    void endTimeClick() {
        if (timeHolder == null) {
            timeHolder = new TimeHolder(getActivity());
        }
        timeHolder.show(event, TimeHolder.SWITCH_TIME_END);
    }

    @OnClick(R.id.tvCity)
    void cityClick() {
        if (cityPickDlg == null) {
            cityPickDlg = new CityPickDlg(getActivity(), cityCallBack,"请选择活动所在城市");
        }
        if (event != null) {
            cityPickDlg.setCity(event.provinceId, event.cityId);
        }
        cityPickDlg.show();
    }

    CityPickDlg.CallBack cityCallBack = new CityPickDlg.CallBack() {

        @Override
        public void OkClick(int provinceId, String provinceName, int cityId, String cityName) {
            event.cityId = cityId;
            event.cityName = cityName;
            event.provinceId = provinceId;
            event.provinceName = provinceName;
            setCityTxt();
        }
    };

    @OnClick(R.id.llCategory)
    public void onCategorySelected() {
        FragEditCategory.invoke(getActivity(), REQ_CATEGORY, event.category);
    }

    /**
     * 根据event将城市设置到View上
     */
    private void setCityTxt() {
        if (event.provinceName == null || event.cityName == null) {
            if (event.provinceId > 0 && event.cityId > 0) {
                if (cityPickDlg == null) {
                    cityPickDlg = new CityPickDlg(getActivity(),cityCallBack,"请选择活动所在城市");
                }
                cityPickDlg.setCity(event.provinceId, event.cityId);
                event.cityName = cityPickDlg.getCityName();
                event.provinceName = cityPickDlg.getProvinceName();
            } else {
                return;
            }
        }
        if (event.provinceName.equals(event.cityName)) {
            tvCity.setText(event.provinceName);
        } else {
            tvCity.setText(event.provinceName + " " + event.cityName);
        }
    }

    class TimeHolder {

        public static final int SWITCH_TIME_START = 0;
        public static final int SWITCH_TIME_END = 1;

        Dialog timeDialog;

        @InjectView(R.id.startTimePicker)
        ADateTimePicker timePicker;

        @InjectView(R.id.tvDataPickerNote)
        TextView tvDataPickerNote;

        @InjectView(R.id.btnOk)
        TextView tvDataPickerOk;

        Event event;

        int switchTime;

        public TimeHolder(final Context context) {
            View datePickerView = LayoutInflater.from(context).inflate(
                    R.layout.date_picker, null);
            timeDialog = new Dialog(context, R.style.ActionDialog);
            timeDialog.setContentView(datePickerView);
            timeDialog.setOnDismissListener(dismissListener);
            WindowManager.LayoutParams wmlp = timeDialog.getWindow()
                    .getAttributes();
            wmlp.gravity = Gravity.BOTTOM;
            wmlp.width = DensityUtil.getWidth();
            ButterKnife.inject(this, timeDialog);

            // 为数字选择器添加滚动监听，每当滚动停止时，将时间赋值到TextView上。
            timePicker.getMonthNumPicker().setOnScrollListener(scrollListener);
            timePicker.getDayNumPicker().setOnScrollListener(scrollListener);
            timePicker.getHourNumPicker().setOnScrollListener(scrollListener);
            timePicker.getMinuteNumPicker().setOnScrollListener(scrollListener);
        }

        OnScrollListener scrollListener = new OnScrollListener() {

            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                if (scrollState == 0) {
                    setTimeToEvent();
                }
            }
        };

        OnDismissListener dismissListener = new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                // 时间选择器消失，如果当前选择的是开始时间，并且结束时间此时为空的情况下，将开始时间加4小时赋值到结束时间。
                if (switchTime == SWITCH_TIME_START && event.endTime <= 0
                        && event.startTime > 0) {
                    event.endTime = event.startTime + 60 * 60 * 4;
                    tvEndTime.setText(TimeUtil.long2String(event.endTime));
                }
            }
        };

        @OnClick(R.id.btnOk)
        void OkClick() {
            setTimeToEvent();
            if (timeDialog != null && timeDialog.isShowing()) {
                timeDialog.dismiss();
            }
        }

        /**
         * 将选择的时间赋值到event和fragEventCreatFirst的View上。
         */
        private void setTimeToEvent() {
            String timeString = getTime(timePicker);
            if (switchTime == SWITCH_TIME_START) {
                event.startTime = TimeUtil.string2Long(timeString);
                tvStartTime.setText(TimeUtil.long2String(event.startTime));
            } else {
                event.endTime = TimeUtil.string2Long(timeString);
                tvEndTime.setText(TimeUtil.long2String(event.endTime));
            }
        }

        /**
         * 显示时间选择器
         *
         * @param event      活动对象
         * @param switchTime 显示开始时间还是结束时间
         */
        public void show(Event event, int switchTime) {
            this.event = event;
            this.switchTime = switchTime;
            if (switchTime == SWITCH_TIME_START) {
                setStartTime();
            } else {
                setEndTime();
            }
            SoftInputUtil.hideInput(getActivity());
            timeDialog.show();
        }

        /**
         * 设置timePicker的时间为开始时间
         */
        private void setStartTime() {
            Time startTime = new Time();
            if (event.startTime > 0) {
                startTime.set(event.startTime * 1000);
            } else {
                startTime.set(System.currentTimeMillis() + 10 * 60 * 1000);
            }
            timePicker.setTime(startTime);
        }

        /**
         * 设置timePicker的时间为结束时间
         */
        private void setEndTime() {
            Time endTime = new Time();
            if (event.endTime > 0) {
                endTime.set(event.endTime * 1000);
            } else {
                if (event.startTime > 0) {
                    endTime.set(event.startTime * 1000 + 60 * 60 * 4 * 1000);
                } else {
                    endTime.set(System.currentTimeMillis() + 10 * 60 * 1000);
                }
            }
            timePicker.setTime(endTime);
        }

        /**
         * 获取timePicker的时间
         */
        private String getTime(ADateTimePicker timePicker) {
            int year = timePicker.getYear();
            int month = timePicker.getMonth();
            int day = timePicker.getDay();
            int hour = timePicker.getHour();
            int min = timePicker.getMin();
            return year + "年" + month + "月" + day + "日 " + hour + ":"
                    + (min == 0 ? "00" : min);
        }
    }

    protected boolean hasEdited() {
        if ((!StringUtil.isNullOrEmpty(etTitle.getText().toString()))
                || (!StringUtil.isNullOrEmpty(tvStartTime.getText().toString()))
                || (!StringUtil.isNullOrEmpty(tvEndTime.getText().toString()))
                || (!StringUtil.isNullOrEmpty(tvCity.getText().toString()))
                || (!StringUtil.isNullOrEmpty(etAddress.getText().toString()))
                || (!StringUtil.isNullOrEmpty(etDesc.getText().toString()))
                || llCategory.getChildCount() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CATEGORY) {
            if (data == null)
                return;
            event.category = data
                    .getStringExtra(FragEditCategory.INTENT_KEY_CATS);
            setCategory();
            refreshTitle();
        }
    }

    /**
     * 设置活动标签显示内容
     */
    private void setCategory() {
        llCategory.removeAllViews();
        if (!StringUtil.isNullOrEmpty(event.category)) {
            String[] categories = event.category.split(",");
            for (String category : categories) {
                TextView tvCategory = (TextView) LayoutInflater.from(
                        getActivity()).inflate(R.layout.tag_text, null);
                int tagHeight = getResources().getDimensionPixelSize(
                        R.dimen.tag_height);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        MarginLayoutParams.WRAP_CONTENT, tagHeight);
                params.rightMargin = DensityUtil.dip2px(10);
                tvCategory.setTextColor(getResources().getColorStateList(
                        R.color.sel_font_color_green));
                tvCategory.setLayoutParams(params);
                tvCategory.setSingleLine(true);
                tvCategory.setText(category);
                llCategory.addView(tvCategory);
            }
        }
    }

    /**
     * 判断信息是否填写完整
     */
    public boolean isInputValidated() {
        boolean isOk = true;
        String error = null;
        if (event.startTime <= 0) {
            isOk = false;
        } else if (event.startTime < System.currentTimeMillis() / 1000) {
            error = "活动开始时间需晚于当前时间";
            isOk = false;
        } else if (event.endTime <= 0) {
            isOk = false;
        } else if (event.startTime > event.endTime) {
            error = "活动结束时间需晚于开始时间";
            isOk = false;
        } else if (StringUtil
                .isNullOrEmpty(etTitle.getText().toString().trim())) {
            isOk = false;
        } else if (event.provinceId <= 0 || event.cityId <= 0) {
            isOk = false;
        } else if (StringUtil.isNullOrEmpty(etAddress.getText().toString()
                .trim())) {
            isOk = false;
        } else if (StringUtil.isNullOrEmpty(etDesc.getText().toString().trim())) {
            isOk = false;
        } else if (StringUtil.isNullOrEmpty(event.category)) {
            isOk = false;
        }
        if (error != null) {
            showError(error);
        } else {
            hideError();
        }
        return isOk;
    }

    private void showError(String error) {
        tvError.setText(error);
        tvError.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        tvError.setVisibility(View.GONE);
    }

    /**
     * 将已经填写的信息保存到event，再保存到intent中。
     */
    public void setEventToIntent() {
        // EditText的信息需要在这里保存到event，其他的信息在改变时已经保存过了。
        event.eventTitle = etTitle.getText().toString().trim();
        event.location = etAddress.getText().toString().trim();
        event.content = etDesc.getText().toString().trim();
        getActivity().getIntent().putExtra(ActEventCreate.KEY_EVENT, event);
    }

    @OnTextChanged(R.id.etTitle)
    void titleChanged() {
        refreshTitle();
    }

    @OnTextChanged(R.id.tvStartTime)
    void startTimeChanged() {
        refreshTitle();
    }

    @OnTextChanged(R.id.tvEndTime)
    void endTimeChanged() {
        refreshTitle();
    }

    @OnTextChanged(R.id.tvCity)
    void cityChanged() {
        refreshTitle();
    }

    @OnTextChanged(R.id.etAddress)
    void addressChanged() {
        refreshTitle();
    }

    @OnTextChanged(R.id.etDesc)
    void descChanged() {
        refreshTitle();
    }

}
