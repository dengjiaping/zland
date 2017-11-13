package com.zhisland.lib.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.view.NumberPicker.Drawer;
import com.zhisland.lib.view.NumberPicker.Formatter;
import com.zhisland.lib.view.NumberPicker.OnScrollListener;
import com.zhisland.lib.view.NumberPicker.OnValueChangeListener;

public class ADateTimePicker extends LinearLayout {

	static final String TAG = "adate";
	static int START_YEAR = 1900, END_YEAR = 2050;
	static final int BASE_MONTH = START_YEAR * 12;
	static final int MONTH_COUNT = (END_YEAR - START_YEAR) * 12;
	static final int START_WEEK = 1;// 1900年1月1日是周1
	static final String[] WEEK_STRINGS = new String[] { "星期一", "星期二", "星期三",
			"星期四", "星期五", "星期六", "星期日" };
	static final int MAX_MONTH_WIDTH = DensityUtil.dip2px(31);
	static final int MAX_DAY_WIDTH = DensityUtil.dip2px(28);
	static final int[] MON_DAYS = new int[] { 31, 28, 31, 30, 31, 30, 31, 31,
			30, 31, 30, 31 };// 每个月对应的天数，二月份单独处理
	static final SparseArray<String> MONTH_STRINGS = new SparseArray<String>();
	static final SparseArray<String> DAY_STRINGS = new SparseArray<String>();

	private int[] monthDays;// 每个月有多少天
	private int[] monthDayOffset;// 每个月距离1900年有多少天
	private int maxDayIndex;
	private DayListener dayFormatter;
	private MonthListener monthFormatter;
	private NumberPicker npMonth, npDay, npHour, npMinute;

	public ADateTimePicker(Context context) {
		super(context);
		initView(context);
	}

	public ADateTimePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public ADateTimePicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	/**
	 * 1: date,2:time,3 datetime
	 */
	public void setDateType(int type) {
		switch (type) {
		case 1: {
			npMonth.setVisibility(View.VISIBLE);
			npDay.setVisibility(View.VISIBLE);
			npHour.setVisibility(View.GONE);
			npMinute.setVisibility(View.GONE);
			break;
		}
		case 2: {
			npMonth.setVisibility(View.GONE);
			npDay.setVisibility(View.GONE);
			npHour.setVisibility(View.VISIBLE);
			npMinute.setVisibility(View.VISIBLE);
			break;
		}
		case 3: {
			npMonth.setVisibility(View.VISIBLE);
			npDay.setVisibility(View.VISIBLE);
			npHour.setVisibility(View.VISIBLE);
			npMinute.setVisibility(View.VISIBLE);
			break;
		}
		}
	}

	/**
	 * set time
	 */
	public void setTime(Time time) {
		npMonth.setValue(diffMonth(time.year, time.month));
		npDay.setValue(diffDay(time.year, time.month, time.monthDay));
		npHour.setValue(time.hour);
		npMinute.setValue((time.minute / 10) % 6);
	}

	public int getYear() {
		String monthyear = MONTH_STRINGS.get(npMonth.getValue());
		int year = Integer.parseInt(monthyear.substring(0, 4));
		return year;
	}

	public int getMonth() {
		String monthyear = MONTH_STRINGS.get(npMonth.getValue());
		int month = Integer.parseInt(monthyear.substring(5,
				monthyear.length() - 1));
		return month;
	}

	public int getDay() {
		String dayofMonth = DAY_STRINGS.get(npDay.getValue());
		int day = Integer
				.parseInt(dayofMonth.substring(0, dayofMonth.length()==5 ? 1 : 2));
		return day;

	}

	public int getHour() {
		return npHour.getValue();
	}

	public int getMin() {
		return npMinute.getValue() * 10;
	}

	private void initView(Context context) {

		buildMonthArray();

		setGravity(Gravity.CENTER_HORIZONTAL);
		npMonth = new NumberPicker(context);
		npDay = new NumberPicker(context);
		npHour = new NumberPicker(context);
		npMinute = new NumberPicker(context);
		int width = (DensityUtil.getWidth() * 9) / 10;
		this.addView(npMonth, width / 3, LayoutParams.WRAP_CONTENT);
		this.addView(npDay, width / 3, LayoutParams.WRAP_CONTENT);
		this.addView(npHour, width / 6, LayoutParams.WRAP_CONTENT);
		this.addView(npMinute, width / 6, LayoutParams.WRAP_CONTENT);

		int curMonth = (1900 - 1900) * 12 + 1 - 1;
		int curDay = 0;

		dayFormatter = new DayListener();
		monthFormatter = new MonthListener();

		npDay.setDrawer(dayFormatter);
		npDay.setMaxValue(maxDayIndex);
		npDay.setMinValue(0);
		int curValue = monthDayOffset[curMonth] + curDay;
		npDay.setValue(curValue);
		npDay.setEditOnTouch(false);
		npDay.setFocusable(true);
		npDay.setFocusableInTouchMode(true);
		npDay.setOnValueChangedListener(dayFormatter);

		npMonth.setDrawer(monthFormatter);
		npMonth.setMaxValue(MONTH_COUNT - 1);
		npMonth.setMinValue(0);
		npMonth.setValue(curMonth);
		npMonth.setOnValueChangedListener(monthFormatter);
		npMonth.setEditOnTouch(false);
		npMonth.setFocusable(true);
		npMonth.setFocusableInTouchMode(true);

		npMonth.setFormatter(monthFormatter);
		npDay.setFormatter(dayFormatter);

		//
		npHour.setMaxValue(23);
		npHour.setMinValue(0);

		npMinute.setMaxValue(5);
		npMinute.setMinValue(0);
		npMinute.setFormatter(new Formatter() {

			@Override
			public String format(int value) {
				return value + "0";
			}
		});

		Calendar calendar = Calendar.getInstance();
		int nowYear = calendar.get(Calendar.YEAR);
		int nowMonth = calendar.get(Calendar.MONDAY);
		int nowDayofMonth = calendar.get(Calendar.DAY_OF_MONTH);
		int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
		int nowMin = calendar.get(Calendar.MINUTE);

		npMonth.setValue(diffMonth(nowYear, nowMonth));
		npDay.setValue(diffDay(nowYear, nowMonth, nowDayofMonth));
		npHour.setValue(nowHour);
		npMinute.setValue((nowMin / 10) % 6);
	}
	
	public NumberPicker getMonthNumPicker() {
		return npMonth;
	}
	
	public NumberPicker getDayNumPicker() {
		return npDay;
	}
	
	public NumberPicker getHourNumPicker() {
		return npHour;
	}
	
	public NumberPicker getMinuteNumPicker() {
		return npMinute;
	}

	public int diffMonth(int year, int month) {
		int difMonth = (year - START_YEAR) * 12 + month;
		return difMonth;

	}

	public int diffDay(int year, int month, int day) {
		int whichDay = 0;
		String before = START_YEAR + "-01-01";
		String now = year + "-" + (month + 1) + "-" + day;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d1;
		try {
			d1 = sdf.parse(before);
			Date d2 = sdf.parse(now);
			long daysBetween = (d2.getTime() - d1.getTime())
					/ (3600 * 24 * 1000);
			whichDay = (int) daysBetween;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return whichDay;

	}

	/**
	 * 初始化月份和月份的偏移植
	 */
	private void buildMonthArray() {
		monthDays = new int[MONTH_COUNT];
		monthDayOffset = new int[MONTH_COUNT];
		int totalOffset = 0;
		for (int i = 0; i < MONTH_COUNT; i++) {
			int monthIndex = i % 12;
			int yearIndex = i / 12 + START_YEAR;
			if (monthIndex == 1 && isLeapYear(yearIndex)) {
				monthDays[i] = 29;
			} else {
				monthDays[i] = MON_DAYS[monthIndex];
			}
			monthDayOffset[i] = totalOffset;
			totalOffset += monthDays[i];
		}
		maxDayIndex = monthDayOffset[MONTH_COUNT - 1]
				+ monthDays[MONTH_COUNT - 1] - 1;

		for (int i = 0; i < 3; i++) {
			// 初始化最开始的3天和最后面的3天
			String weekString = WEEK_STRINGS[(i) % 7];
			String s = weekString + (i + 1);
			DAY_STRINGS.put(i, s);

			int ldi = maxDayIndex - i;
			int lastDay = ldi - monthDayOffset[MONTH_COUNT - 1];
			weekString = WEEK_STRINGS[ldi % 7];
			s = weekString + (lastDay + 1);
			DAY_STRINGS.put(ldi, s);
		}
	}

	/**
	 * 是不是闰年
	 */
	private boolean isLeapYear(int year) {
		return (0 == year % 4 && 0 != year % 100)
				|| ((0 == year % 100) && (0 == year % 400));
	}

	class DayListener implements Formatter, Drawer, OnValueChangeListener {

		int monthCur;

		@Override
		public void draw(Canvas canvas, String text, float x, float y,
				Paint large, Paint largeSmall) {
			canvas.drawText(text, x , y, large);
		}

		@Override
		public String format(int value) {

			// String dayString = DAY_STRINGS.get(value);
			// if (dayString != null)
			// return dayString;

			int curMonth = npMonth.getValue();

			int chechRes = checkValue(value);
			int day;
			if (chechRes > 0) {
				day = value - monthDayOffset[curMonth + 1];
			} else if (chechRes < 0) {
				day = value - monthDayOffset[curMonth - 1];
			} else {
				day = value - monthDayOffset[curMonth];
			}
			String weekString = WEEK_STRINGS[(value) % 7];
			String s = (day + 1)+"日"+weekString;
			DAY_STRINGS.put(value, s);
			MLog.d(TAG, String.format("day format %d to %s", value, s));
			return s;
		}

		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
			MLog.d(TAG, "day change " + oldVal + " " + newVal);
			int chechRes = checkValue(newVal);
			if (chechRes > 0) {
				npMonth.setOnValueChangedListener(null);
				if (oldVal == 0 && newVal == maxDayIndex) {
					npMonth.setValue(MONTH_COUNT - 1);
				} else {
					npMonth.setValue(npMonth.getValue() + 1);
				}
				npMonth.setOnValueChangedListener(monthFormatter);
			} else if (chechRes < 0) {
				npMonth.setOnValueChangedListener(null);
				if (oldVal == maxDayIndex && newVal == 0) {
					npMonth.setValue(0);
				} else {
					npMonth.setValue(npMonth.getValue() - 1);
				}
				npMonth.setOnValueChangedListener(monthFormatter);
			}
		}

		/**
		 * >0 next month; = 0 current month;<0 pre month
		 * 
		 * @param value
		 * @return
		 */
		private int checkValue(int value) {
			int curMonthMax = monthDayOffset[npMonth.getValue()]
					+ monthDays[npMonth.getValue()];
			int curMonthMin = monthDayOffset[npMonth.getValue()];
			if (value >= curMonthMax) {
				return 1;
			} else if (value < curMonthMin) {
				return -1;
			}
			return 0;
		}

	}

	class MonthListener implements Formatter, Drawer, OnValueChangeListener {

		@Override
		public void draw(Canvas canvas, String text, float x, float y,
				Paint large, Paint largeSmall) {
			canvas.drawText(text, x, y, large);
		}

		@Override
		public String format(int value) {
			String monthString = MONTH_STRINGS.get(value);
			if (monthString != null)
				return monthString;
			int monthCount = value + BASE_MONTH;
			int year = monthCount / 12;
			int month = monthCount % 12;
			String s = String.format("%d年%d月", year, month + 1);
			MLog.d(TAG, "month format: " + s);
			MONTH_STRINGS.put(value, s);
			return s;
		}

		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
			int value = npDay.getValue();
			int dayIndex = value - monthDayOffset[oldVal];
			MLog.d(TAG, "第" + dayIndex);
			npDay.setOnValueChangedListener(null);
			npDay.setValue(monthDayOffset[newVal] + dayIndex);
			npDay.setOnValueChangedListener(dayFormatter);
		}
	}

}
