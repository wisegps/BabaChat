package com.wise.baba.view.adapter;

import java.util.ArrayList;
import java.util.List;

import com.wise.baba.R;
import com.wise.baba.entity.MemberInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UserListAdapter extends BaseAdapter{

	private Context context;
	private List<MemberInfo>  memberList = new ArrayList<MemberInfo>();
	public UserListAdapter(Context context) {
		super();
		this.context = context;
	}

	@Override
	public int getCount() {
		return 10;
		//return memberList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int index, View view, ViewGroup arg2) {
		if(view == null){
			view = LayoutInflater.from(context).inflate(com.wise.baba.R.layout.user_list_item, null);
		}
		TextView textView = (TextView) view.findViewById(R.id.tv_name);
		//textView.setText(memberList.get(index).getIdentifier());
		return view;
	}

	
	public void notify(List<MemberInfo>  memberList){
		this.memberList = memberList;
		this.notifyDataSetChanged();
	}
}
