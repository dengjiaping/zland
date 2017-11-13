package com.zhisland.android.blog.common.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.util.SoftInputUtil;
import com.zhisland.lib.util.DensityUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Mr.Tui on 2016/4/27.
 */
public class CityPickDlg {

    Context context;

    Dialog cityDialog;

    @InjectView(R.id.startCityPicker)
    CityPicker cityPicker;

    @InjectView(R.id.tvCityPickerNote)
    TextView tvCityPickerNote;

    CallBack callBack;

    String noteStr;

    public CityPickDlg(Context context, CallBack callBack, String noteStr) {
        this.context = context;
        this.callBack = callBack;
        this.noteStr = noteStr;
        View cityPickerView = LayoutInflater.from(context).inflate(
                R.layout.city_picker, null);
        cityDialog = new Dialog(context, R.style.ActionDialog);
        cityDialog.setContentView(cityPickerView);
        WindowManager.LayoutParams wmlp = cityDialog.getWindow()
                .getAttributes();
        wmlp.gravity = Gravity.BOTTOM;
        wmlp.width = DensityUtil.getWidth();
        ButterKnife.inject(this, cityDialog);
        tvCityPickerNote.setText(noteStr);
    }

    public void setCityPickerNote(String noteStr){
        this.noteStr = noteStr;
        tvCityPickerNote.setText(noteStr);
    }

    @OnClick(R.id.btnCityOk)
    void OkClick() {
        int cityId = cityPicker.getCityId();
        String cityName = cityPicker.getCityName();
        int provinceId = cityPicker.getProvinceId();
        String provinceName = cityPicker.getProvinceName();
        if (callBack != null) {
            callBack.OkClick(provinceId, provinceName, cityId, cityName);
        }
        cityDialog.dismiss();
    }

    public void show() {
        if (context instanceof Activity) {
            SoftInputUtil.hideInput((Activity) context);
        }
        cityDialog.show();
    }

    public void setCity(int provinceId, int cityId) {
        if (provinceId > 0 && cityId > 0) {
            cityPicker.setCity(provinceId, cityId);
        }
    }

    public String getCityName() {
        return cityPicker.getCityName();
    }

    public String getProvinceName() {
        return cityPicker.getProvinceName();
    }

    public static interface CallBack {
        void OkClick(int provinceId, String provinceName, int cityId, String cityName);
    }

}
