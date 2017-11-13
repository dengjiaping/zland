package com.zhisland.android.blog.common.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.aa.dto.Country;
import com.zhisland.android.blog.common.dto.Dict;
import com.zhisland.android.blog.common.util.EditTextUtil;
import com.zhisland.android.blog.common.util.EditTextUtil.IKeyBoardAction;
import com.zhisland.android.blog.common.util.MobileUtil;
import com.zhisland.android.blog.common.util.SoftInputUtil;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;

import java.util.List;

/**
 * 带国家码的手机输入View
 * */
public class InternationalPhoneView extends LinearLayout {

	private TextView tvInternationalCode;
	private EditText etMobileNum;

	private TextView tvDictPickerOk;
	// 国家编号列表选择对话框
	private Dialog dictDialog;

	private Country currentDict;

	private boolean needSpace = false;

	public InternationalPhoneView(Context context) {
		this(context, null);
	}

	public InternationalPhoneView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * 设置国家码
	 * */
	public void setCountry(Country country) {
		if (country == null) {
			return;
		}
		this.currentDict = country;
		tvInternationalCode.setText("+" + currentDict.showCode);
	}

    /**
     * 获取输入手机号EditText
     */
	public EditText getEditText() {
		return etMobileNum;
	}

    /**
     * 获取国家码 TextView
     */
    public TextView getTvInternationalCode(){
        return  tvInternationalCode;
    }

	/**
	 * 设置国家码
	 * */
	public void setCountryByCode(String code) {
		this.currentDict = getCountryByCode(code);
		tvInternationalCode.setText("+" + currentDict.showCode);
	}

	/**
	 * 设置电话号码
	 * */
	public void setMobileNum(String mobileNum) {
		if (!StringUtil.isNullOrEmpty(mobileNum)) {
			etMobileNum.setText(mobileNum);
		}
	}

	/**
	 * 文本输入框，请求焦点
	 * */
	public void editRequestFocus() {
		etMobileNum.requestFocus();
	}
	
	public void setEditHint(String hint){
		etMobileNum.setHint(hint);
	}

	/**
	 * 获取输入的手机号。 中国的去除非数字。
	 * */
	public String getMobileNum() {
		if (currentDict != null
				&& !currentDict.showCode.equals(Country.CHINA_SHOW_CODE)) {
			return etMobileNum.getText().toString().trim();
		} else {
			return MobileUtil.getRelleayMobileNumber(etMobileNum.getText()
					.toString().trim());
		}
	}

	/**
	 * 获取当前国家码
	 * */
	public Country getCurrentDict() {
		return currentDict;
	}

	/**
	 * 设置国家码和电话
	 * 
	 * @param dictMobile
	 *            包含国家码的电话，国家码和电话用“-”分隔，如0086-13264101314
	 **/
	public void setDictMobile(String dictMobile) {
		if (StringUtil.isNullOrEmpty(dictMobile)) {
			return;
		}
		String phone = "";
		Country country = null;
		if (dictMobile.contains("-")) {
			String code = dictMobile.substring(0, dictMobile.indexOf("-"));
			country = getCountryByCode(code);
			if(dictMobile.length() > dictMobile.indexOf("-") + 1){
				phone = dictMobile.substring(dictMobile.indexOf("-") + 1,
						dictMobile.length());
			}
		} else {
			country = getDefaultCountry();
			phone = dictMobile;
		}
		setCountry(country);
		setMobileNum(phone);
	}

	/**
	 * 获取国家码和电话
	 * 
	 * @return 返回包含国家码的电话，国家码和电话用“-”分隔，如0086-13264101314
	 **/
	public String getDictMobile() {
		String code = currentDict.code;
		String phone = MobileUtil.getRelleayMobileNumber(etMobileNum.getText()
				.toString().trim());
		return code + "-" + phone;
	}

	/**
	 * 检查当前输入的是否合法，中国时检查电话 1开头11位。非中国不空就合法
	 * */
	public boolean checkInput() {
		if (currentDict.showCode.equals(Country.CHINA_SHOW_CODE)) {
			if (!StringUtil.phoneFormatCheck(MobileUtil
					.getRelleayMobileNumber(etMobileNum.getText().toString()
							.trim()))) {
				return false;
			}
		} else {
			if (StringUtil.isNullOrEmpty(etMobileNum.getText().toString()
					.trim())) {
				return false;
			}
		}
		return true;
	}
	
	private Country getDefaultCountry(){
		Country country = new Country();
		country.code = "0086";
		country.name = "中国";
		country.showCode = "86";
		return country;
	}
	
	private void init() {
		setOrientation(LinearLayout.HORIZONTAL);

		// 设置默认国家为中国
		currentDict = getDefaultCountry();

		// 设置国家码View属性
		tvInternationalCode = new TextView(getContext());
		setCountry(currentDict);
		tvInternationalCode.setTextSize(16);
		tvInternationalCode.setGravity(Gravity.CENTER_VERTICAL);
		Drawable drawable = getResources().getDrawable(
				R.drawable.btn_arrow_down_grey);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		tvInternationalCode.setCompoundDrawables(null, null, drawable, null);
		tvInternationalCode.setCompoundDrawablePadding(DensityUtil.dip2px(4));
		tvInternationalCode.setTextColor(getResources().getColor(
				R.color.color_f3));
		tvInternationalCode.setOnClickListener(clickListener);

		// 设置手机输入框View属性
		etMobileNum = new EditText(getContext());
		etMobileNum.setInputType(InputType.TYPE_CLASS_NUMBER);
		etMobileNum.setBackgroundColor(getResources().getColor(
				R.color.transparent));
		etMobileNum.setSingleLine(true);
		int padding = DensityUtil.dip2px(10);
		etMobileNum.setPadding(padding, padding, padding, padding);
		etMobileNum.setTextSize(16);
		etMobileNum.setTextColor(getResources().getColor(R.color.color_f1));
		etMobileNum.setHintTextColor(getResources().getColor(R.color.color_f3));
		etMobileNum.addTextChangedListener(watcher);

		addView(tvInternationalCode, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		addView(etMobileNum, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
	}

	public void setEnterCallBack(final int type, final IKeyBoardAction kbAction) {
		EditTextUtil.setKeyBoard(etMobileNum, type, kbAction);
	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			SoftInputUtil.hideInput(((Activity) getContext()));
			showCountryCodeView();
		}
	};

	TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (currentDict != null
					&& !currentDict.showCode
							.equals(Country.CHINA_SHOW_CODE)) {
				if (!StringUtil.isNullOrEmpty(etMobileNum.getText().toString()
						.trim())) {
					if (callBack != null) {
						callBack.onInputQualified();
					}
					return;
				}
			} else {
				if (needSpace) {
					String lastMobile = s.toString();
					int sel = etMobileNum.getSelectionEnd();
					String cursorLeft = s.toString().substring(0, sel);
					String formatCursorLeft = MobileUtil
							.getFormatMobileNumber(cursorLeft);
					String formatPhoneNumber = MobileUtil
							.getFormatMobileNumber(s.toString());
					if (!StringUtil.isEquals(lastMobile, formatPhoneNumber)) {
						etMobileNum.setText(formatPhoneNumber);
					}
					etMobileNum.setSelection(formatCursorLeft.length());
				}
				String phoneNum = MobileUtil.getRelleayMobileNumber(etMobileNum
						.getText().toString().trim());
				if (StringUtil.phoneFormatCheck(phoneNum)) {
					if (callBack != null) {
						callBack.onInputQualified();
					}
					return;
				}
			}
			if (callBack != null) {
				callBack.onInputSubstandard();
			}
		}
	};

	/**
	 * 是否在输入时，用空格分隔
	 * */
	public void setNeedSpace(boolean needSpace) {
		this.needSpace = needSpace;
	}
	
	/**
	 * 根据code取Country，没取到返回中国
	 * */
	private Country getCountryByCode(String code){
		if(code != null){
			final List<Country> dicts = Dict.getInstance().getCountryCode();
			for (int i = 0; i < dicts.size(); i++) {
				if (dicts.get(i).code.equals(code)) {
					return dicts.get(i);
				}
			}
		}
		return getDefaultCountry();
	}

	/**
	 * 初始化国家编号列表选择器的界面
	 */
	private void showCountryCodeView() {
		final List<Country> dicts = Dict.getInstance().getCountryCode();
		if (dicts == null || dicts.size() <= 0) {
			return;
		}
		View countryPickView = LayoutInflater.from(getContext()).inflate(
				R.layout.country_picker, null);
		final CountryCodePicker dictPicker = (CountryCodePicker) countryPickView
				.findViewById(R.id.startDictPicker);
		dictPicker.initData(dicts);

		String showcode = currentDict.showCode;
		for (int i = 0; i < dicts.size(); i++) {
			if (dicts.get(i).showCode.equals(showcode)) {
				dictPicker.dictSelector(i);
				break;
			}
		}

		tvDictPickerOk = (TextView) countryPickView.findViewById(R.id.btnDictOk);
		dictDialog = new Dialog(getContext(), R.style.ActionDialog);
		dictDialog.setContentView(countryPickView);
		WindowManager.LayoutParams wmlp = dictDialog.getWindow()
				.getAttributes();
		wmlp.gravity = Gravity.BOTTOM;
		wmlp.width = DensityUtil.getWidth(); // 宽度
		tvDictPickerOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dictDialog.dismiss();

				currentDict = dictPicker.getCurrentDict();
				// 做判断 刷新view
				if (currentDict.showCode.equals(Country.CHINA_SHOW_CODE)) {
					etMobileNum.setInputType(InputType.TYPE_CLASS_NUMBER);
					if (!StringUtil.phoneFormatCheck(MobileUtil
							.getRelleayMobileNumber(etMobileNum.getText()
									.toString()))) {
						if (callBack != null) {
							callBack.onInputSubstandard();
						}
					}
				} else {
					etMobileNum.setInputType(InputType.TYPE_CLASS_TEXT);
					if (!StringUtil.isNullOrEmpty(etMobileNum.getText()
							.toString())) {
						if (callBack != null) {
							callBack.onInputQualified();
						}
					}
				}
				tvInternationalCode.setText("+" + currentDict.showCode);
				if (callBack != null) {
					callBack.onDictPickerOkClick();
				}
			}
		});
		dictDialog.show();
	}

	CallBack callBack;

	public void setCallBack(CallBack callBack) {
		this.callBack = callBack;
	}

	public interface CallBack {

		/**
		 * 国家码选择器OK回调
		 * */
		public void onDictPickerOkClick();

		/**
		 * 回调，输入的符合格式
		 * */
		public void onInputQualified();

		/**
		 * 回调，输入的不符合格式
		 * */
		public void onInputSubstandard();

	}
}
