package com.wise.baba.control;

import java.util.ArrayList;

import com.tencent.av.sdk.AVAudioCtrl;
import com.tencent.av.sdk.AVContext;
import com.tencent.av.sdk.AVEndpoint;
import com.tencent.av.sdk.AVRoom;
import com.tencent.av.sdk.AVRoomMulti;
import com.wise.baba.app.Action;
import com.wise.baba.app.App;
import com.wise.baba.entity.MemberInfo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AVRoomControl {
	private static final int TYPE_MEMBER_CHANGE_IN = 0;
	private static final int TYPE_MEMBER_CHANGE_OUT = TYPE_MEMBER_CHANGE_IN + 1;
	private static final int TYPE_MEMBER_CHANGE_UPDATE = TYPE_MEMBER_CHANGE_OUT + 1;
	private ArrayList<MemberInfo> mMemberList = new ArrayList<MemberInfo>();
	private Context context;
	public AVRoomControl(Context context) {
		super();
		this.context = context;
	}
	
	public void createRoom(){
		
	}
	
	/**
	 * 创建房间
	 * 
	 * @param relationId
	 *            讨论组号
	 */
	public void enterRoom(int relationId){
		App app = (App) context;
		AudioSDKControl sdkControl = app.getSDKControl();
		AVContext avContext = sdkControl.getContextControl().getmAVContext();
		int roomType = AVRoom.AV_ROOM_MULTI;
		int roomId = 0;

		long authBits = AVRoom.AUTH_BITS_DEFUALT;//权限位；默认值是拥有所有权限。TODO：请业务侧填根据自己的情况填上权限位。
		byte[] authBuffer = null;//权限位加密串；TODO：请业务侧填上自己的加密串。
		int authBufferSize = 0;//权限位加密串长度；TODO：请业务侧填上自己的加密串长度。
		String controlRole = "";//角色名；多人房间专用。该角色名就是web端音视频参数配置工具所设置的角色名。TODO：请业务侧填根据自己的情况填上自己的角色名。
		AVRoom.Info roomInfo = new AVRoom.Info(roomType, roomId, relationId, AVRoom.AV_MODE_AUDIO, "", authBits, authBuffer, authBufferSize, controlRole);
		// create room
		avContext.enterRoom(roomDelegate, roomInfo);
	}
	public void exitRoom(){
		App app = (App) context;
		AudioSDKControl sdkControl = app.getSDKControl();
		AVContext avContext = sdkControl.getContextControl().getmAVContext();
		avContext.exitRoom();
	
	}
	
	
	private AVRoomMulti.Delegate roomDelegate = new AVRoomMulti.Delegate(){

		@Override
		protected void OnPrivilegeDiffNotify(int privilege) {
			super.OnPrivilegeDiffNotify(privilege);
			Log.i("AVRoomControl", "OnPrivilegeDiffNotify");
		}

		@Override
		protected void onEndpointsEnterRoom(int endpointCount,
				AVEndpoint[] endpointList) {
			super.onEndpointsEnterRoom(endpointCount, endpointList);
			onMemberChange(TYPE_MEMBER_CHANGE_IN, endpointList, endpointCount);
			Log.i("AVRoomControl", "onEndpointsEnterRoom");
		}

		@Override
		protected void onEndpointsExitRoom(int endpointCount,
				AVEndpoint[] endpointList) {
			// TODO Auto-generated method stub
			super.onEndpointsExitRoom(endpointCount, endpointList);
			onMemberChange(TYPE_MEMBER_CHANGE_OUT, endpointList, endpointCount);
			Log.i("AVRoomControl", "onEndpointsExitRoom");
		}

		@Override
		protected void onEndpointsUpdateInfo(int endpointCount,
				AVEndpoint[] endpointList) {
			super.onEndpointsUpdateInfo(endpointCount, endpointList);
			onMemberChange(TYPE_MEMBER_CHANGE_UPDATE, endpointList, endpointCount);
			Log.i("AVRoomControl", "onEndpointsUpdateInfo");
		}

		@Override
		protected void onEnterRoomComplete(int result) {
			super.onEnterRoomComplete(result);
			Log.i("AVRoomControl", "onEnterRoomComplete");
			Intent intent = new Intent(Action.ACTION_ENTER_ROOM_SUCCESS);
			context.sendBroadcast(intent);
			
			
			
		}

		@Override
		protected void onExitRoomComplete(int result) {
			super.onExitRoomComplete(result);
			Log.i("AVRoomControl", "onExitRoomComplete");
		}
		
	};
	
	
	/**
	 * 成员列表变化
	 * 
	 * @param type
	 *            类型
	 * @param endpointList
	 *            成员列表
	 * @param endpointCount
	 *            成员总数
	 */
	private void onMemberChange(int type, AVEndpoint endpointList[], int endpointCount) {

		if (TYPE_MEMBER_CHANGE_IN == type) {
			for (int i = 0; i < endpointCount; i++) {
				AVEndpoint.Info userInfo = endpointList[i].getInfo();		
				String identifier = userInfo.openId;	
				
				
				Log.i("AVRoomControl", "进来一个"+identifier);
				boolean bExist = false;
				for(int j = 0; j < mMemberList.size(); j++) {
					if (mMemberList.get(j).identifier.equals(identifier)) {
						bExist = true;
						Log.i("AVRoomControl", "但是他已经在里面了"+identifier);
						break;
					}	
				}

				if (!bExist) {
					MemberInfo info = new MemberInfo();
					info.identifier = userInfo.openId;
					mMemberList.add(info);	
					Log.i("AVRoomControl", "他是新来的"+identifier);
				}
				
				
				
			}
		} else if (TYPE_MEMBER_CHANGE_OUT == type) {
			for (int i = 0; i < endpointCount; i++) {
				AVEndpoint.Info userInfo = endpointList[i].getInfo();
				String identifier = userInfo.openId;
				Log.i("AVRoomControl", "出去一个"+identifier);
				for (int j = 0; j < mMemberList.size(); j++) {
					if (mMemberList.get(j).identifier.equals(identifier)) {
						mMemberList.remove(j);
					
						break;
					}
				}
			}
		} else if (TYPE_MEMBER_CHANGE_UPDATE == type) {
			for (int i = 0; i < endpointCount; i++) {
				AVEndpoint.Info userInfo = endpointList[i].getInfo();
				String identifier = userInfo.openId;
				Log.i("AVRoomControl", "更新"+identifier);
				boolean identifierExist = false;
				for (int j = 0; j < mMemberList.size(); j++) {
					if (mMemberList.get(j).identifier.equals(identifier)) {
						mMemberList.remove(j);
						MemberInfo info = new MemberInfo();
						info.identifier = userInfo.openId;
						mMemberList.add(j, info);
						identifierExist = true;
						break;
					}
				}
				
				if (!identifierExist) {
					MemberInfo info = new MemberInfo();
					info.identifier = userInfo.openId;
					mMemberList.add(info);	
				}
			}
			
		}

		context.sendBroadcast(new Intent(Action.ACTION_MEMBER_CHANGE));
	}

	public ArrayList<MemberInfo> getmMemberList() {
		return mMemberList;
	}

}
