package com.load.third.jqm.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.load.third.jqm.MyApp;

public class MySharedPreference {
	private Context context;
	private SharedPreferences mPerferences;

	public MySharedPreference(Context context) {
		this.context = MyApp.getContext();
		if (context != null) {
			mPerferences = PreferenceManager
					.getDefaultSharedPreferences(context);
		}
	}

	public String getKeyStr(String key) {
        if (mPerferences == null) {
            mPerferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
        }
		return mPerferences.getString(key, "");
	}

	public Boolean getKeyBoolean(String key) {
        if (mPerferences == null) {
            mPerferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
        }
		return mPerferences.getBoolean(key, false);
	}

	public int getKeyInt(String key) {
        if (mPerferences == null) {
            mPerferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
        }
		return mPerferences.getInt(key, -1);
	}

	public Long getKeyLong(String key) {
        if (mPerferences == null) {
            mPerferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
        }
		return mPerferences.getLong(key, 0);
	}

	public Boolean setKeyBoolean(String key, Boolean bl) {
		if (mPerferences == null) {
			mPerferences = PreferenceManager
					.getDefaultSharedPreferences(context);
		}
		SharedPreferences.Editor mEditor = mPerferences.edit();
		mEditor.putBoolean(key, bl);
		mEditor.commit();
		return true;
	}

	public Boolean setKeyInt(String key, int info) {
		if (mPerferences == null) {
			mPerferences = PreferenceManager
					.getDefaultSharedPreferences(context);
		}
		SharedPreferences.Editor mEditor = mPerferences.edit();
		mEditor.putInt(key, info);
		mEditor.commit();
		return true;
	}

	public Boolean setKeyLong(String key, long info) {
		if (mPerferences == null) {
			mPerferences = PreferenceManager
					.getDefaultSharedPreferences(context);
		}
		SharedPreferences.Editor mEditor = mPerferences.edit();
		mEditor.putLong(key, info);
		mEditor.commit();
		return true;
	}

	public Boolean setKeyFloat(String key, float info) {
		if (mPerferences == null) {
			mPerferences = PreferenceManager
					.getDefaultSharedPreferences(context);
		}
		SharedPreferences.Editor mEditor = mPerferences.edit();
		mEditor.putFloat(key, info);
		mEditor.commit();
		return true;
	}

	public float getKeyFloat(String key) {
		if (mPerferences == null) {
			mPerferences = PreferenceManager
					.getDefaultSharedPreferences(context);
		}
		SharedPreferences.Editor mEditor = mPerferences.edit();
		return mPerferences.getFloat(key, 0);

	}

	public Boolean setKeyStr(String key, String info) {
		if (mPerferences == null) {
			mPerferences = PreferenceManager
					.getDefaultSharedPreferences(context);
		}
		if (info == null) {
			info = "";
		}
		SharedPreferences.Editor mEditor = mPerferences.edit();
		mEditor.putString(key, info);
		mEditor.commit();
		Log.d("mPerferences", key + "=" + info);
		return true;
	}

	public void removeKey(String key) {
		if (mPerferences == null) {
			mPerferences = PreferenceManager
					.getDefaultSharedPreferences(context);
		}
		SharedPreferences.Editor mEditor = mPerferences.edit();
		mEditor.remove(key);
		mEditor.commit();
	}
}
