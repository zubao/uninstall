package com.uninstall.browser.sdk;

import java.lang.reflect.Method;

import com.uninstall.browser.dao.ConfigDao;

import android.content.Context;
import android.util.Log;

public class UninstallBrowserSDK {

	private static UninstallBrowserSDK mInstance;

	private UninstallBrowserSDK() {

	}

	public static UninstallBrowserSDK getInstance() {
		if (mInstance == null) {
			mInstance = new UninstallBrowserSDK();
		}
		return mInstance;
	}

	private native int init(String packageName, String url, String userSerial);

	private native String getNameByPid(int pid);

	static {
		System.loadLibrary("native-lib");
	}

	/**
	 * 设置软件卸载时弹出网页的URL
	 */
	public void setUninstallWebUrl(Context context, String url) {
		if (url == null || url.length() == 0) {
			return;
		}
		int mMonitorPid = ConfigDao.getInstance(context).getMonitorPid();
		if (mMonitorPid > 0 && !getNameByPid(mMonitorPid).equals("!")) {
			Log.i("stefanli", "监控进程存在");
			return;
		} else {
			int mPid = init("/data/data/" + context.getPackageName(), url, getUserSerial(context));
			Log.i("stefanli", "监控进程ID:" + mPid);
			Log.i("stefanli", "监控进程名称:" + getNameByPid(mPid));
			ConfigDao.getInstance(context).setMonitorPid(mPid);
		}
	}
	
	/**
	 * 获取 userSerialNumber
	 * @param context
	 * @return
	 */
	private String getUserSerial(Context context) {
		Object userManager = context.getSystemService("user");
		if (userManager == null) {
			return null;
		}
		try {
			Method myUserHandleMethod = android.os.Process.class.getMethod(
					"myUserHandle", (Class<?>[]) null);
			Object myUserHandle = myUserHandleMethod.invoke(
					android.os.Process.class, (Object[]) null);

			Method getSerialNumberForUser = userManager.getClass().getMethod(
					"getSerialNumberForUser", myUserHandle.getClass());
			long userSerial = (Long) getSerialNumberForUser.invoke(userManager,
					myUserHandle);
			return String.valueOf(userSerial);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
