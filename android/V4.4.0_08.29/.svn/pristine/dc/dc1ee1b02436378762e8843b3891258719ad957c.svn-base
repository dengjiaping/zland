package com.zhisland.android.blog.common.app;

import java.util.List;
import java.util.UUID;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.zhisland.android.blog.common.uri.UriBrowseActivity;
import com.zhisland.lib.util.MD5;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.Tools;

/**
 * the util class for helping app, device's informaiton
 *
 * @author arthur
 */
public class AppUtil {

    private static final String TAG = "apputil";
    private static AppUtil instance;
    private static final String PREF_DEVICE_ID = "pref_device_id";

    private String deviceId;
    private String platformString;
    private String siminfo;

    private AppUtil() {

    }

    public static AppUtil Instance() {
        if (instance == null) {
            synchronized (AppUtil.class) {
                if (instance == null) {
                    instance = new AppUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 获取当前应用的版本号
     *
     * @return
     */
    public int getVersionCode() {
        PackageInfo pInfo;
        try {
            pInfo = ZhislandApplication.APP_CONTEXT
                    .getPackageManager()
                    .getPackageInfo(
                            ZhislandApplication.APP_CONTEXT.getPackageName(), 0);
            return pInfo.versionCode;
        } catch (NameNotFoundException e) {
            MLog.e(TAG, e.getMessage(), e);
        }
        return -1;

    }

    /**
     * 获取当前应用的版本号
     *
     * @return
     */
    public String getVersionName() {
        PackageInfo pInfo;
        try {
            pInfo = ZhislandApplication.APP_CONTEXT
                    .getPackageManager()
                    .getPackageInfo(
                            ZhislandApplication.APP_CONTEXT.getPackageName(), 0);
            return pInfo.versionName;
        } catch (NameNotFoundException e) {
            MLog.e(TAG, e.getMessage(), e);
        }
        return "";

    }

    /**
     * 获取设备的wi-fi mac-address
     *
     * @return
     */
    public String getDeviceId() {

        if (deviceId == null) {
            deviceId = PrefUtil.Instance().getByKey(PREF_DEVICE_ID, null);

            if (deviceId == null) {

                WifiManager wifi = (WifiManager) ZhislandApplication.APP_CONTEXT
                        .getSystemService(Context.WIFI_SERVICE);
                String macAddress = wifi.getConnectionInfo().getMacAddress();

                if (macAddress == null) {
                    UUID deviceUuid = UUID.randomUUID();
                    macAddress = deviceUuid.toString();
                }

                String code = macAddress;
                deviceId = MD5.getDecMD5(code);
                PrefUtil.Instance().setKeyValue(PREF_DEVICE_ID, deviceId);
            }
        }
        return deviceId;
    }

    /**
     * 获取设备名称
     *
     * @return
     */
    public String getDeviceName() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取系统名称
     *
     * @return
     */
    public String getSysName() {
        return "android";
    }

    /**
     * 获取系统版本号
     */
    public String getSysVersion() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取蓝牙地址信息
     */
    public String getBlueTooth() {
        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
        if (bluetooth != null) {
            return bluetooth.getAddress();
        }
        return null;
    }

    /**
     * 获取mode字段
     *
     * @return samsung GT-I9508
     */
    public String getMode() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取平台信息
     *
     * @return
     */
    private String getPlatformString() {
        if (platformString != null) {
            return platformString;
        }

        // PLAT: ANDROID#*#samsung
        // GT-I9508#*#4.3#*#1#*#opcode:46000;opname:CHINA
        // MOBILE;nettype:NETWORK_TYPE_UNKNOWN;phonetype:PHONE_TYPE_GSM#*#versioncode
        // 16

        StringBuilder sb = new StringBuilder();
        sb.append("PLAT: ANDROID");
        sb.append("#*#" + android.os.Build.BRAND + " " + android.os.Build.MODEL);
        sb.append("#*#" + android.os.Build.VERSION.RELEASE);
        sb.append("#*#"
                + Tools.isNetworkAvailable(ZhislandApplication.APP_CONTEXT));

        String siminfo = checkSimInfo();
        if (!StringUtil.isNullOrEmpty(siminfo)) {
            sb.append("#*#" + siminfo);
        }

        String versionCode = getVersionCode() + "";
        if (!StringUtil.isNullOrEmpty(versionCode)) {
            sb.append("#*#versioncode " + versionCode);
        }

        platformString = sb.toString();

        return platformString;

    }

    private String checkSimInfo() {
        if (siminfo != null) {
            return siminfo;
        }

        StringBuilder sb = new StringBuilder();

        try {
            TelephonyManager telManager = (TelephonyManager) ZhislandApplication.APP_CONTEXT
                    .getSystemService(Context.TELEPHONY_SERVICE);

            try {
                String operator = telManager.getSimOperator();
                sb.append("opcode:" + operator);
            } catch (Exception ex) {

            }

            try {
                String operatorName = telManager.getNetworkOperatorName();
                sb.append(";opname:" + operatorName);
            } catch (Exception ex) {

            }

            String netTypeString = null;
            try {
                int netType = telManager.getNetworkType();

                switch (netType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                        netTypeString = "NETWORK_TYPE_GPRS";
                        break;
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                        netTypeString = "NETWORK_TYPE_CDMA";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        netTypeString = "NETWORK_TYPE_EDGE";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                        netTypeString = "NETWORK_TYPE_UMTS";
                        break;
                    default:
                        netTypeString = "NETWORK_TYPE_UNKNOWN";
                        break;
                }
                if (!StringUtil.isNullOrEmpty(netTypeString)) {
                    sb.append(";nettype:" + netTypeString);
                }
            } catch (Exception ex) {

            }

            String phoneTypeString = null;
            try {
                int phoneType = telManager.getPhoneType();

                switch (phoneType) {
                    case TelephonyManager.PHONE_TYPE_CDMA:
                        phoneTypeString = "PHONE_TYPE_CDMA";
                        break;
                    case TelephonyManager.PHONE_TYPE_GSM:
                        phoneTypeString = "PHONE_TYPE_GSM";
                        break;
                    default:
                        phoneTypeString = "PHONE_TYPE_UNKNOWN";
                        break;
                }

                if (!StringUtil.isNullOrEmpty(phoneTypeString)) {
                    sb.append(";phonetype:" + phoneTypeString);
                }

            } catch (Exception ex) {

            }

        } catch (Exception ex) {

        }
        return sb.toString();
    }

    public int getChannelId() {
        return 1;
    }

    /**
     * 程序是否运行
     *
     * @param excludeClassName 排除的Activity， 场景：推送的时候，UriBrowseActivity一定运行，所以需要排除此activity
     * @return true: 程序正在运行。false： 程序没有运行
     */
    public static boolean appIsRunning(String excludeClassName) {
        boolean isRunning = false;
        ActivityManager am = (ActivityManager) ZhislandApplication.APP_CONTEXT
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(ZhislandApplication.APP_CONTEXT.getPackageName()) &&
                    info.baseActivity.getPackageName().equals(ZhislandApplication.APP_CONTEXT.getPackageName())) {
                if (StringUtil.isNullOrEmpty(excludeClassName)) {
                    isRunning = true;
                } else {
                    String topName = info.topActivity.getClassName();
                    if (!topName.contains(excludeClassName)) {
                        isRunning = true;
                    }
                }
            }
        }
        return isRunning;
    }

}
