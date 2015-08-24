package com.wise.baba.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wise.baba.R;
import com.wise.baba.entity.MemberInfo;
import com.wise.baba.view.adapter.UserListAdapter;

public class UserListView implements OnRefreshListener2<ListView> {
	
	private Context context;
	private PullToRefreshListView listView;
	private UserListAdapter userListAdapter;
	public UserListView(Context context){
		this.context = context;
		init();
	}
	
	public void init(){
		listView = (PullToRefreshListView) ((Activity) context).findViewById(R.id.listUser);
		listView.setMode(Mode.PULL_FROM_END);
		listView.setOnRefreshListener(this);
		userListAdapter = new UserListAdapter(context);
		listView.setAdapter(userListAdapter);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				System.out.println("onPullDownToRefresh");
				userListAdapter.notifyDataSetChanged();
				listView.onRefreshComplete();
			}
			
		}, 1000);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				System.out.println("onPullUpToRefresh");
				userListAdapter.notifyDataSetChanged();
				listView.onRefreshComplete();
			}
			
		}, 1000);
	}
	
	
	public void onMemberChanged(List<MemberInfo>  memberList){
		userListAdapter.notify(memberList);
	}
	

}
