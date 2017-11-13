package com.zhisland.android.blog.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.zhisland.android.blog.common.dto.City;
import com.zhisland.android.blog.common.dto.Dict;
import com.zhisland.android.blog.common.dto.ZHDicItem;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.view.NumberPicker;
import com.zhisland.lib.view.NumberPicker.Drawer;
import com.zhisland.lib.view.NumberPicker.Formatter;
import com.zhisland.lib.view.NumberPicker.OnValueChangeListener;

import java.util.ArrayList;
import java.util.List;

public class CityPicker extends LinearLayout {

    /**
     * 所有城市列表
     */
    private ArrayList<City> cities;
    private List<ZHDicItem> provinces;
    private SparseArray<List<ZHDicItem>> allCities;
    private List<ZHDicItem> selectedCities;

    private CityListener cityFormatter;
    private ProvinceListener provinceFormatter;
    private NumberPicker npProvince, npCity;

    public CityPicker(Context context) {
        super(context);
        initView(context);
    }

    public CityPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CityPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        setGravity(Gravity.CENTER_HORIZONTAL);
        npProvince = new NumberPicker(context);
        npCity = new NumberPicker(context);
        int width = (DensityUtil.getWidth() * 7) / 7;
        this.addView(npProvince, width / 2, LayoutParams.WRAP_CONTENT);
        this.addView(npCity, width / 2, LayoutParams.WRAP_CONTENT);

        cityFormatter = new CityListener();
        provinceFormatter = new ProvinceListener();

        npProvince.setDrawer(provinceFormatter);
        npProvince.setOnValueChangedListener(provinceFormatter);
        npProvince.setEditOnTouch(false);
        npProvince.setFocusable(true);
        npProvince.setFocusableInTouchMode(true);

        npCity.setDrawer(cityFormatter);
        npCity.setOnValueChangedListener(cityFormatter);
        npCity.setEditOnTouch(false);
        npCity.setFocusable(true);
        npCity.setFocusableInTouchMode(true);

        initData();
    }

    class CityListener implements Formatter, Drawer, OnValueChangeListener {

        @Override
        public void draw(Canvas canvas, String text, float x, float y,
                         Paint large, Paint largeSmall) {
            if (text.length() < 5) {
                canvas.drawText(text, x, y, large);
            } else {
                canvas.drawText(text, x, y, largeSmall);
            }
        }

        @Override
        public String format(int value) {
            if (selectedCities == null) return "unknown";

            ZHDicItem city = selectedCities.get(value);
            return city.name;
        }

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        }

    }

    class ProvinceListener implements Formatter, Drawer, OnValueChangeListener {

        @Override
        public void draw(Canvas canvas, String text, float x, float y,
                         Paint large, Paint largeSmall) {
            if (text != null) {
                if (text.length() < 5) {
                    canvas.drawText(text, x, y, large);
                } else {
                    canvas.drawText(text, x, y, largeSmall);
                }
            }
        }

        @Override
        public String format(int value) {
            ZHDicItem province = provinces.get(value);
            return province.name;
        }

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            citySelector(newVal);
        }
    }

    private void citySelector(int provinceIndex) {
        ZHDicItem province = provinces.get(provinceIndex);

        selectedCities = allCities.get(province.code);

        int maxValue = selectedCities.size() - 1;
        int minValue = 0;


        npCity.setMaxValue(maxValue);
        npCity.setMinValue(minValue);
        npCity.setValue(0);
    }

    private void initData() {
        if (cities == null) {
            cities = Dict.getInstance().getCities();
        }
        provinces = getCityByParentCode(0);
        if (provinces == null) {
            return;
        }
        allCities = new SparseArray<>();

        List<ZHDicItem> cities;
        for (ZHDicItem item : provinces) {
            cities = getCityByParentCode(item.code);
            if (cities != null && cities.size() > 0) {
                allCities.put(item.code, cities);
            } else {
                cities = new ArrayList<>();
                ZHDicItem i = new ZHDicItem(item.code, item.name);
                cities.add(i);
                allCities.put(item.code, cities);
            }
        }

        npProvince.setFormatter(provinceFormatter);
        npCity.setFormatter(cityFormatter);

        npProvince.setMinValue(0);
        npProvince.setMaxValue(provinces.size() - 1);
        npProvince.setValue(0);

        citySelector(0);
    }

    /**
     * 通过 parentCode 获取城市的 code name
     */
    private ArrayList<ZHDicItem> getCityByParentCode(int parentCode) {
        ArrayList<ZHDicItem> dicItems = new ArrayList<>();
        ZHDicItem dicItem;
        for (City city : cities) {
            if (city.parentCode == parentCode) {
                dicItem = new ZHDicItem(city.code, city.name);
                dicItems.add(dicItem);
            }
        }
        return dicItems;
    }

    public void setCity(int provinceId, int cityId) {
        ZHDicItem item;
        int provinceIndex = 0;
        for (int i = 0; i < provinces.size(); i++) {
            item = provinces.get(i);
            if (item.code == provinceId) {
                provinceIndex = i;
                break;
            }
        }
        npProvince.setValue(provinceIndex);
        citySelector(provinceIndex);
        int cityIndex = 0;
        for (int i = 0; i < selectedCities.size(); i++) {
            item = selectedCities.get(i);
            if (item.code == cityId) {
                cityIndex = i;
                break;
            }
        }
        npCity.setValue(cityIndex);
    }

    public int getProvinceId() {
        return provinces.get(npProvince.getValue()).code;
    }

    public String getProvinceName() {
        return provinces.get(npProvince.getValue()).name;
    }

    public int getCityId() {
        return selectedCities.get(npCity.getValue()).code;
    }

    public String getCityName() {
        return selectedCities.get(npCity.getValue()).name;
    }
}
