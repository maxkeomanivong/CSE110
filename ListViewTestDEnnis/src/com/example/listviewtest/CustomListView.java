package com.example.listviewtest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;

public class CustomListView  extends ListView implements OnScrollListener{

	View footer;
	boolean loading=false;
	ArrayAdapterCustom adap;
	List<NewsFeedItem> item = new ArrayList<NewsFeedItem>();
	ArrayList <String> links = new ArrayList<String>(Arrays.asList(new String[] {"http://www.foodobjectorienteddesign.com/imageupload/wes/img/123021.jpg","http://www.foodobjectorienteddesign.com/imageupload/wes/img/CUhouv9.jpg","http://www.foodobjectorienteddesign.com/imageupload/wes/img/155597.jpg","http://www.foodobjectorienteddesign.com/imageupload/wes/img/Horse.jpg","http://www.foodobjectorienteddesign.com/imageupload/wes/img/Koala.jpg","http://www.foodobjectorienteddesign.com/imageupload/wes/img/tumblr_mykp0lkFiX1ruj0bpo1_1280.jpg","http://www.foodobjectorienteddesign.com/imageupload/wes/img/zljUGM0.jpg","http://lh5.ggpht.com/_mrb7w4gF8Ds/TCpetKSqM1I/AAAAAAAAD2c/Qef6Gsqf12Y/s144-c/_DSC4374%20copy.jpg",
			   "http://lh5.ggpht.com/_Z6tbBnE-swM/TB0CryLkiLI/AAAAAAAAVSo/n6B78hsDUz4/s144-c/_DSC3454.jpg",
			   "http://lh3.ggpht.com/_GEnSvSHk4iE/TDSfmyCfn0I/AAAAAAAAF8Y/cqmhEoxbwys/s144-c/_MG_3675.jpg",
			   "http://lh6.ggpht.com/_Nsxc889y6hY/TBp7jfx-cgI/AAAAAAAAHAg/Rr7jX44r2Gc/s144-c/IMGP9775a.jpg",
			   "http://lh3.ggpht.com/_lLj6go_T1CQ/TCD8PW09KBI/AAAAAAAAQdc/AqmOJ7eg5ig/s144-c/Juvenile%20Gannet%20despute.jpg"}));
	public CustomListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onScroll(AbsListView view, int firstVisible, int visibleCount, int totalItems) {
		// TODO Auto-generated method stub
		Log.d("Like a baws","BAWSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSs");
		if(getAdapter()==null)
			return;
		else if(getAdapter().getCount() == 0)
			return;
		
		int last = firstVisible+visibleCount;
		if(last>= totalItems && !loading)
		{
			//this.addFooterView(footer);
			for(int x=0;x<links.size();x++)
	        {
	        	item.add(new NewsFeedItem(links.get(x)));
	        }
			Log.d("Like a baws","BAWSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSs");
			adap = (ArrayAdapterCustom) getAdapter();
			adap.addAll(item);
			adap.notifyDataSetChanged();
			
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public void setLoadIndicater (int resourceID)
	{
		LayoutInflater inflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footer = (View) inflater.inflate(resourceID,null);
		this.addFooterView(footer);
	}

}
