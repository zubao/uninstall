package com.uninstall.browser.dao;

import android.content.Context;
import android.content.SharedPreferences;

public class ConfigDao {
	
	private static ConfigDao mInstance = null;
	
	private SharedPreferences mSharedPreferences;
	
	private ConfigDao(Context context) {
		mSharedPreferences = context.getSharedPreferences("pid_info", Context.MODE_PRIVATE);
	}
	
	public static ConfigDao getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new ConfigDao(context);
		}
		return mInstance;
	}
	
	private final String MONITOR_PID = "monitor_pid";	// 监控进程 PID
	
	public void setMonitorPid(int pid) {
		mSharedPreferences.edit().putInt(MONITOR_PID, pid).commit();
	}
	
	public int getMonitorPid() {
		return mSharedPreferences.getInt(MONITOR_PID, 0);
	}
}
