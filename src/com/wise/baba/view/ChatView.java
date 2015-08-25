package com.wise.baba.view;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.wise.baba.R;
import com.wise.baba.view.adapter.ChatListAdapter;
import com.wise.baba.view.adapter.UserListAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ChatView {

	private Context context;
	private ImageView imgShow;
	private RelativeLayout rlytChat;
	private View listUser;
	private PullToRefreshListView listMsg;
	private ChatListAdapter chatListAdapter;
	
	
	public ChatView(Context context) {
		this.context = context;
		init();
	}

	public void init() {
		imgShow = (ImageView) ((Activity) context).findViewById(R.id.imgShow);
		rlytChat = (RelativeLayout) ((Activity) context)
				.findViewById(R.id.rlytChat);
		rlytChat.setVisibility(View.INVISIBLE);
		listUser = ((Activity) context).findViewById(R.id.listUser);
		listUser.setVisibility(View.VISIBLE);
		listMsg = (PullToRefreshListView) ((Activity) context).findViewById(R.id.listMsg);
		listMsg.setMode(Mode.PULL_FROM_START);
		
		listMsg.setOnRefreshListener(onRefreshMsgListener);
		
		chatListAdapter = new ChatListAdapter(context);
		
		listMsg.setAdapter(chatListAdapter);
		
		
		imgShow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				onShow();
			}
		});
	}
	
	public void onShow(){
		if (listUser.getVisibility() == View.INVISIBLE) {
			listUser.setVisibility(View.VISIBLE);
			rlytChat.setVisibility(View.INVISIBLE);
			imgShow.setImageResource(R.drawable.icon_friends);
		} else {
			listUser.setVisibility(View.INVISIBLE);
			rlytChat.setVisibility(View.VISIBLE);
			imgShow.setImageResource(R.drawable.icon_msg);
		}
	}
	
	
	public PullToRefreshBase.OnRefreshListener  onRefreshMsgListener = new PullToRefreshBase.OnRefreshListener(){

		@Override
		public void onRefresh(PullToRefreshBase refreshView) {
			refreshView.onRefreshComplete();
		}
		
		
	};
	
	
}
