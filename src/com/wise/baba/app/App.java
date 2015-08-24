package com.wise.baba.app;
import android.app.Application;
import com.wise.baba.control.AVContextControl;
import com.wise.baba.control.AudioSDKControl;
import com.wise.baba.entity.MemberInfo;

public class App extends Application{

	private AudioSDKControl sdkControl;
	
	private MemberInfo memberInfo;
	
	@Override
	public void onCreate() {
		super.onCreate();
		sdkControl = new AudioSDKControl(this);
	}

	public AudioSDKControl getSDKControl() {
		return sdkControl;
	}

	public MemberInfo getMemberInfo() {
		return memberInfo;
	}

	public void setMemberInfo(MemberInfo memberInfo) {
		this.memberInfo = memberInfo;
	}
	
	
}
