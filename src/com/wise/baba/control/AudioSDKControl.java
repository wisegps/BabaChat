package com.wise.baba.control;

import android.content.Context;


public class AudioSDKControl {

	private AVContextControl contextControl;
	private AVRoomControl roomControl;
	public AudioSDKControl(Context context) {
		super();
		contextControl = new AVContextControl(context);
		roomControl = new AVRoomControl(context);
	}
	
	
	public void startSdkContext(){
		contextControl.startContext();
	}
	
	public void stopSdkContext(){
		exitRoom();
		contextControl.stopContext();
	}
	
	public void enterRoom(){
		roomControl.enterRoom(1);
	}
	
	public void exitRoom(){
		roomControl.exitRoom();
	}


	public AVContextControl getContextControl() {
		return contextControl;
	}


	public void setContextControl(AVContextControl contextControl) {
		this.contextControl = contextControl;
	}


	public AVRoomControl getRoomControl() {
		return roomControl;
	}


	public void setRoomControl(AVRoomControl roomControl) {
		this.roomControl = roomControl;
	}
	
	
	
}
