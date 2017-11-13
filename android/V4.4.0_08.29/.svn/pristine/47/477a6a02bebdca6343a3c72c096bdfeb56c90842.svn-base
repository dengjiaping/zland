package com.zhisland.android.blog.im.view;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

import com.zhisland.lib.util.MLog;

public class IMEditText extends EditText {

    private static final int ID_SELECT_ALL = android.R.id.selectAll;
    private static final int ID_COPY = android.R.id.copy;
    private static final int ID_PASTE = android.R.id.paste;
    private SessBottomController.HideOther hideOther;

    public IMEditText(Context context) {
        super(context, null);
    }

    public IMEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setHideOtherListener(SessBottomController.HideOther hideOther) {
        this.hideOther = hideOther;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        MLog.d("bkey", event.toString());
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        boolean res = super.onTextContextMenuItem(id);
        switch (id) {
            case ID_SELECT_ALL:
            case ID_COPY: {
                if (hideOther != null) {
                    hideOther.hideToolsbar();
                }
                break;
            }
            case ID_PASTE: {
                Editable tmp = super.getText();
                String text = tmp.toString();
                CharSequence sequence = ChatViewUtil.instance()
                        .formatText(getContext(), text, null, getLineHeight());
                this.setText(sequence);
                if (sequence != null && sequence.length() > 0) {
                    this.setSelection(sequence.length());
                }
                break;
            }
        }
        return res;

    }

}