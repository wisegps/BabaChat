package com.wise.baba.control;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.tencent.TIMUser;
import com.tencent.av.sdk.AVContext;
import com.tencent.av.sdk.AVContext.Config;
import com.wise.baba.app.Action;
import com.wise.baba.app.App;

import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

/**
 * 语音聊天sdk上下文对象
 * 
 * @author c
 * 
 */
public class AVContextControl {

	private Context mContext;
	private Config mConfig;
	private static String UID_TYPE = "875";
	private static String APP_ID = "1400000817";
	private String identifier = "";
	private String usersig = "";
	private AVContext mAVContext = null;

	public AVContextControl(Context context) {
		mContext = context;
	}

	public void startContext() {
		if (usersig.length() < 1) {
			requestUsersig();
		} 
	}

	/**
	 * @描述：首先获取用户签名
	 */
	public void requestUsersig() {
		// http://web.wisegps.cn/app/get_sig?account_type=875&identifier=13316891158&sdk_appid=1400000817&expiry_after=2592000&private_key_path=/wise/tls/tools/ec_key.pem
		TelephonyManager tm = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		identifier = ((App) mContext).getMemberInfo().getIdentifier();
		String url = "http://web.wisegps.cn/app/get_sig?account_type="
				+ UID_TYPE
				+ "&identifier="
				+ identifier
				+ "&sdk_appid="
				+ APP_ID
				+ "&expiry_after=2592000&private_key_path=/wise/tls/tools/ec_key.pem";
		System.out.println(url);

		RequestQueue mQueue = Volley.newRequestQueue(mContext);
		mQueue.add(new JsonObjectRequest(Method.GET, url, null,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							usersig = response.getString("sig");
							login();
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, null));
		mQueue.start();
	}

	/**
	 * 然后登陆
	 */
	private void login() {
		mConfig = new AVContext.Config(UID_TYPE, APP_ID, identifier, usersig,
				APP_ID);

		// 请确保TIMManager.getInstance().init()一定执行在主线程
		TIMManager.getInstance().init(mContext);
		TIMUser userId = new TIMUser();
		userId.setAccountType(UID_TYPE);
		userId.setAppIdAt3rd(APP_ID);
		userId.setIdentifier(identifier);

		TIMManager.getInstance().login(Integer.parseInt(mConfig.sdkAppId),
				userId, mConfig.userSig, new TIMCallBack() {

					@Override
					public void onSuccess() {
						/*
						 * 创建一个AVContext对象
						 */

						if (mAVContext == null) {
							mAVContext = AVContext.createContext(mConfig);
							mAVContext.startContext(mContext,
									mStartContextCompleteCallback);
						}

					}

					@Override
					public void onError(int code, String desc) {

						System.out.println("创建AVContext失败" + desc);
						mAVContext.onDestroy();
						mAVContext = null;
					}
				});
	}

	/**
	 * 关闭SDK系统
	 */
	public void stopContext() {
		if (mAVContext != null) {
			mAVContext.stopContext(mStopContextCompleteCallback);
		}
	}

	/**
	 * 启动SDK系统的回调函数
	 */
	private AVContext.StartContextCompleteCallback mStartContextCompleteCallback = new AVContext.StartContextCompleteCallback() {
		public void OnComplete(int result) {
			System.out.println("创建AVContext成功");
			Intent intent = new Intent(Action.ACTION_START_CONTEXT_COMPLETE);
			mContext.sendBroadcast(intent);
		}
	};

	/**
	 * 关闭SDK系统的回调函数
	 */
	private AVContext.StopContextCompleteCallback mStopContextCompleteCallback = new AVContext.StopContextCompleteCallback() {
		public void OnComplete() {
			System.out.println("退出销毁");
			TIMManager.getInstance().logout();
			mAVContext.onDestroy();
			mAVContext = null;
		}
	};

	public AVContext getmAVContext() {
		return mAVContext;
	}

}
