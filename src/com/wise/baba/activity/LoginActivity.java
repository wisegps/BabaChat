package com.wise.baba.activity;

import com.wise.baba.R;
import com.wise.baba.app.App;
import com.wise.baba.entity.MemberInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
	}

	public void onClick(View view) {

		switch (view.getId()) {
		
		case R.id.imgBtn:
			
			String name = ((EditText) findViewById(R.id.et_name)).getText()
					.toString().trim();
			if (name != null && name.length() > 0) {
				App app = (App) this.getApplication();
				MemberInfo memberInfo = new MemberInfo();
				memberInfo.setIdentifier(name);
				app.setMemberInfo(memberInfo);
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent);
			}
			break;
		}
	}

}
