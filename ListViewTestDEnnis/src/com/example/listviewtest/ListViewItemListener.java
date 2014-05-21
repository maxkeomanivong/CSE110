package com.example.listviewtest;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ListViewItemListener implements OnItemClickListener{

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		// TODO Auto-generated method stub
		Context context = view.getContext();
		Toast.makeText(context, "ID number is "+id+". ", Toast.LENGTH_SHORT).show();
		System.out.println("in listener");
	}

}
