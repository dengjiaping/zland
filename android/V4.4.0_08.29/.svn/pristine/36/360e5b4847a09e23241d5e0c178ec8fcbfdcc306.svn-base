package com.zhisland.android.blog.aa.controller;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.view.InternationalPhoneView;
import com.zhisland.android.blog.common.view.TimeCountUtil;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;
import com.zhisland.lib.view.dialog.AProgressDialog;

/**
 * 登录注册公共工具类
 */
public class AAUtil {

    //验证码最小长度
    private static final int VERIFY_CODE_LEN = 4;
    //密码最小长度
    private static final int PWD_MIN_LEN = 6;
    //密码最大长度
    private static final int PWD_MAX_LEN = 16;

    private static AAUtil aaUtil;
    private static final Object sysObj = new Object();

    private AAUtil() {
    }

    public static AAUtil getInstance() {
        if (aaUtil == null) {
            synchronized (sysObj) {
                if (aaUtil == null) {
                    aaUtil = new AAUtil();
                }
            }
        }
        return aaUtil;
    }

    /**
     * 检测手机号是否合法
     */
    public boolean checkMobile(InternationalPhoneView phoneView) {
        boolean ret = false;
        if (StringUtil.isNullOrEmpty(phoneView.getMobileNum())) {
            ToastUtil.showShort("手机号码不能为空");
        } else if (!phoneView.checkInput()) {
            ToastUtil.showShort("手机号码格式有误");
        } else {
            ret = true;
        }
        return ret;
    }

    /**
     * 检测验证码是否合法
     */
    public boolean checkVerifyCode(String verifyCode) {
        boolean ret = false;
        if (StringUtil.isNullOrEmpty(verifyCode)) {
            ToastUtil.showShort("验证不能为空");
        } else if (verifyCode.length() < VERIFY_CODE_LEN) {
            ToastUtil.showShort("验证码不能小于4位");
        } else {
            ret = true;
        }
        return ret;
    }

    /**
     * 检测密码是否合法
     */
    public boolean checkPwd(String pwd) {
        boolean ret = false;
        if (StringUtil.isNullOrEmpty(pwd)) {
            ToastUtil.showShort("密码不能为空");
        } else if (pwd.length() < PWD_MIN_LEN
                || pwd.length() > PWD_MAX_LEN) {
            ToastUtil.showShort("密码格式错误");
        } else {
            ret = true;
        }
        return ret;
    }

    /**
     * 获取验证码
     */
    public void getVerifyCodeTask(Context context, String phoneNum, String countryCode, final TimeCountUtil timeCountUtil) {

        final AProgressDialog progressDialog = new AProgressDialog(context);
        progressDialog.show();
        ZHApis.getAAApi().getVerifyCode(context, phoneNum, countryCode, new TaskCallback<Object>() {

                            @Override
                            public void onSuccess(Object content) {
                                if (timeCountUtil != null) {
                                    timeCountUtil.start();
                                }
                            }

                            @Override
                            public void onFailure(Throwable error) {
                            }

                            @Override
                            public void onFinish() {
                                super.onFinish();
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                            }
                        }

                );
    }

    /**
     * 监听 验证码按钮状态，当手机号不为空且验证码大于等于4位时按钮为enable状态
     *
     * @param etPhoneNum   手机号输入框
     * @param etVerifyCode 验证码输入框
     * @param btnLogin     登录按钮
     */
    public void addVerifyCodeTextChangeListener(final EditText etPhoneNum, final EditText etVerifyCode, final Button btnLogin) {
        if (etPhoneNum == null || etVerifyCode == null || btnLogin == null) {
            return;
        }
        etPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phoneNum = etPhoneNum.getText().toString().trim();
                String verifyCode = etVerifyCode.getText().toString().trim();
                if (!StringUtil.isNullOrEmpty(phoneNum) && verifyCode.length() >= VERIFY_CODE_LEN) {
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
                }
            }
        });
        etVerifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phoneNum = etPhoneNum.getText().toString().trim();
                String verifyCode = etVerifyCode.getText().toString().trim();
                if (!StringUtil.isNullOrEmpty(phoneNum) && verifyCode.length() >= VERIFY_CODE_LEN) {
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
                }
            }
        });
    }

    /**
     * 监听 登录按钮状态，当手机号不为空且密码大于等于6位且小于16位时按钮为enable状态
     *
     * @param etPhoneNum 手机号输入框
     * @param etPwd      密码输入框
     * @param btnLogin   登录按钮
     */
    public void addPwdTextChangeListener(final EditText etPhoneNum, final EditText etPwd, final Button btnLogin) {
        if (etPhoneNum == null || etPwd == null || btnLogin == null) {
            return;
        }
        etPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phoneNum = etPhoneNum.getText().toString().trim();
                String pwd = etPwd.getText().toString().trim();
                if (!StringUtil.isNullOrEmpty(phoneNum) && pwd.length() >= PWD_MIN_LEN) {
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
                }
            }
        });
        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phoneNum = etPhoneNum.getText().toString().trim();
                String pwd = etPwd.getText().toString().trim();
                if (!StringUtil.isNullOrEmpty(phoneNum) && pwd.length() >= PWD_MIN_LEN) {
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
                }
            }
        });
    }
}
