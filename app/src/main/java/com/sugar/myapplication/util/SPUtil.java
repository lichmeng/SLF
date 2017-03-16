package com.sugar.myapplication.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtil {
	private static final String SP_PATH = "config";
	private SPUtil(){}
	
	public static void putString(Context ctx,String key,String value) {
		SharedPreferences sp = ctx.getSharedPreferences(SP_PATH, Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}
	
	public static String getString(Context ctx,String key,String defValue) {
		SharedPreferences sp = ctx.getSharedPreferences(SP_PATH, Context.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}
	
	public static void putBoolean(Context ctx,String key,boolean value) {
		SharedPreferences sp = ctx.getSharedPreferences(SP_PATH, Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}
	public static boolean getBoolean(Context ctx,String key,boolean defValue) {
		SharedPreferences sp = ctx.getSharedPreferences(SP_PATH, Context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);
	}
	
	public static void putInt(Context ctx,String key,int value) {
		SharedPreferences sp = ctx.getSharedPreferences(SP_PATH, Context.MODE_PRIVATE);
		sp.edit().putInt(key, value).commit();
	}
	public static int getInt(Context ctx,String key,int defValue) {
		SharedPreferences sp = ctx.getSharedPreferences(SP_PATH, Context.MODE_PRIVATE);
		return sp.getInt(key, defValue);
	}
	
}
