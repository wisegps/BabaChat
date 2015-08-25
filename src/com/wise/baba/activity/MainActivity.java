package com.wise.baba.activity;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Window;
import android.widget.ImageView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.tencent.av.sdk.AVAudioCtrl;
import com.wise.baba.R;
import com.wise.baba.app.Action;
import com.wise.baba.app.App;
import com.wise.baba.control.AudioSDKControl;
import com.wise.baba.view.ChatView;
import com.wise.baba.view.UserListView;

public class MainActivity extends Activity {
	private UserListView userListView;
	private MapView mMapView;
	private ChatView chatView;
	private AVBroadcastReceiver mBroadcastReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.activity_main);
		 // configure the SlidingMenu
        SlidingMenu menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        // 设置滑动菜单视图的宽度  
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);  
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setShadowWidth(0);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.activity_left);
		
        
       
     	userListView = new UserListView(this);
		chatView = new ChatView(this);
		mMapView = (MapView) findViewById(R.id.bmapView);
//		((App) this.getApplication()).getSDKControl().startSdkContext();
//		IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction(Action.ACTION_START_CONTEXT_COMPLETE);
//		intentFilter.addAction(Action.ACTION_CLOSE_CONTEXT_COMPLETE);	
//		
//		intentFilter.addAction(Action.ACTION_ENTER_ROOM_SUCCESS);
//		
//		intentFilter.addAction(Action.ACTION_MEMBER_CHANGE);
//		mBroadcastReceiver = new AVBroadcastReceiver();
//		registerReceiver(mBroadcastReceiver, intentFilter);
	}

	
	public class AVBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Action.ACTION_START_CONTEXT_COMPLETE)){
				((App) context.getApplicationContext()).getSDKControl().enterRoom();
			}else if (intent.getAction().equals(Action.ACTION_ENTER_ROOM_SUCCESS)){
				boolean openMic = ((App) context.getApplicationContext()).getSDKControl().getContextControl().getmAVContext().getAudioCtrl().enableMic(true);
				if(openMic == true){
					System.out.println("打开麦克风");
				}else {
					System.out.println("打开麦克风,失败");
				}
				
				((App) context.getApplicationContext()).getSDKControl().getContextControl().getmAVContext().getAudioCtrl().setAudioOutputMode(AVAudioCtrl.OUTPUT_MODE_SPEAKER);
			
				System.out.println("打开扬声器");
			}else if (intent.getAction().equals(Action.ACTION_MEMBER_CHANGE)){
				List memberList = ((App) context.getApplicationContext()).getSDKControl().getRoomControl().getmMemberList();
				userListView.onMemberChanged(memberList);
			}
		}

	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mBroadcastReceiver);
		System.out.println("onDestroy");
		AudioSDKControl sdkControl = ((App) this.getApplication()).getSDKControl();
		if(sdkControl!=null){
			sdkControl.stopSdkContext();
		}
		
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("onResume");
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}

}
