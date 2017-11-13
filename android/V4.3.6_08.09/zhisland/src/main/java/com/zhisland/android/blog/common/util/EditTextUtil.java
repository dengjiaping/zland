package com.zhisland.android.blog.common.util;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.zhisland.lib.util.StringUtil;

/**
 * @author zhangxiang
 */
public class EditTextUtil {
    public static final int ID_NEXT = EditorInfo.IME_ACTION_NEXT;
    public static final int ID_DONE = EditorInfo.IME_ACTION_DONE;
    public static final int ID_GO = EditorInfo.IME_ACTION_GO;
    public static final int ID_NONE = EditorInfo.IME_ACTION_NONE;
    public static final int ID_PREVIOUS = EditorInfo.IME_ACTION_PREVIOUS;
    public static final int ID_SEND = EditorInfo.IME_ACTION_SEND;
    public static final int ID_SEARCH = EditorInfo.IME_ACTION_SEARCH;
    public static final int ID_UNSPECIFIED = EditorInfo.IME_ACTION_UNSPECIFIED;

    /**
     * 实现这个接口的方法action，可以在点击软键盘右下角的自定义按扭时触发业务逻辑的处理
     *
     * @author zhangxiang
     */
    public interface IKeyBoardAction {
        /**
         * 点击定制的键盘右下角键时，触发的方法，都写在这里面吧
         */
        public void action();
    }

    /**
     * 为EditText 自定义软键盘右下角按键，并产生联动
     *
     * @param et       要用的EditText
     * @param type     定制的键盘的类型，比如下一步，搜索，发送什么的
     * @param kbAction 实现IKeyBoardAction中的接口方法，以完成具体的逻辑业务
     */
    public static void setKeyBoard(EditText et, final int type,
                                   final IKeyBoardAction kbAction) {
        et.setImeOptions(type);
        et.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == type
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    kbAction.action();
                    return true;

                }
                return false;
            }
        });
    }

    /**
     * 限制EditText的长度。
     *
     * @param editText      需要设置长度限制的EditText
     * @param lengthChinese 最大可以输入多少字符。无论中文英文符号，都是1位。
     * @param tvCount       显示剩余可以输入的字数。可以传null。
     */
    public static void limitEditTextLength(final EditText editText,
                                           final int maxCount, final TextView tvCount) {
        tvCount.setText(String.valueOf(maxCount));
        String intoStr = "";
        tvCount.setText(String.valueOf(maxCount));
        if (StringUtil.isNullOrEmpty(intoStr = editText.getText().toString())) {
            if (intoStr.length() > maxCount) {
                intoStr.substring(0, maxCount);
                editText.setText(intoStr);
                tvCount.setText(String.valueOf(maxCount - intoStr.length()));
            }
        }
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                maxCount)});

        editText.addTextChangedListener(new TextWatcher() {

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
                int len = s.toString().length();
                int remainLen = maxCount - len;
                tvCount.setText(String.valueOf(remainLen));
            }
        });
    }

    /**
     * 限制EditText的长度。（中文占1位，英文占0.5位）
     *
     * @param editText      需要设置长度限制的EditText
     * @param lengthChinese 最大长度。中文占1位，英文占0.5位，如20，可以输入20个中文，40个英文
     * @param tvCount       显示剩余可以输入的字数。可以传null。
     */
    public static void limitEditTextLengthChinese(final EditText editText,
                                                  final int lengthChinese, final TextView tvCount) {
        limitEditTextLengthChinese(editText, lengthChinese, tvCount, true);
    }

    /**
     * 限制EditText的长度。（中文占1位，英文占0.5位）
     *
     * @param editText      需要设置长度限制的EditText
     * @param lengthChinese 最大长度。中文占1位，英文占0.5位，如20，可以输入20个中文，40个英文
     * @param tvCount       显示剩余可以输入的字数。可以传null。
     */
    public static void limitEditTextLengthChinese(final EditText editText,
                                                  final int lengthChinese, final TextView tvCount, final boolean filterEnter) {
        editText.addTextChangedListener(new TextWatcher() {

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
                String content = editText.getText().toString();
                if (filterEnter) {
                    content = content.replace("\n", "");
                }
                content = StringUtil.subString(content, lengthChinese * 2);
                int selectionPos = editText.getSelectionStart();
                if (!editText.getText().toString().equals(content)) {
                    editText.setText(content);
                    if (selectionPos <= content.length()) {
                        editText.setSelection(selectionPos);
                    } else {
                        editText.setSelection(content.length());
                    }
                } else {
                    if (tvCount != null) {
                        tvCount.setText(""
                                + (lengthChinese * 2 - StringUtil
                                .getLength2(content)) / 2);
                    }
                }
            }
        });
    }
}
