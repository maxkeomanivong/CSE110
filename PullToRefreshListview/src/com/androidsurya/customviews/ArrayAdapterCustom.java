package com.androidsurya.customviews;

import java.util.ArrayList;
import java.util.List;

import com.androidsurya.pulltorefresh.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class ArrayAdapterCustom extends ArrayAdapter<NewsFeedItem> {
	Context myContext;
	int layoutID;
	List<NewsFeedItem> data = null;
	int position;
	
	public ArrayAdapterCustom(Context myContext, int layoutID, List<NewsFeedItem> data)
	{
		super(myContext,layoutID,data);
		
		this.myContext= myContext;
		this.layoutID=layoutID;
		this.data=data;
		
		BitmapManager.INSTANCE.setPlaceholder(BitmapFactory.decodeResource(
			    myContext.getResources(), R.drawable.kabob));
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		this.position=position;
	    ViewHolderItem viewHolder;
		
		//if its not null then we dont want to re-inflate it.
		if(convertView==null)
		{
			LayoutInflater inflater = ((Activity)myContext).getLayoutInflater();
			convertView = inflater.inflate(layoutID, parent,false);
			
			// well set up the ViewHolder
	        viewHolder = new ViewHolderItem();
	        viewHolder.imgViewItem = (ImageView) convertView.findViewById(R.id.imgView);
	        viewHolder.pos=position;
	         
	        viewHolder.imgViewItem.setTag(position);
	        // store the holder with the view.
	        convertView.setTag(viewHolder);
		}
		else{
	        // we've just avoided calling findViewById() on resource everytime
	        // just use the viewHolder
	        viewHolder = (ViewHolderItem) convertView.getTag();
	    }
		//gets the items based on the position of the row
		NewsFeedItem newsfeedItem = data.get(position);		
		/*if(newsfeedItem.newBitmap==null)
		{
		viewHolder.imgViewItem.setImageResource(R.drawable.kabob);
		newsfeedItem.setBitMap(viewHolder.imgViewItem,this);
		}
		else
		viewHolder.imgViewItem.setImageBitmap(newsfeedItem.newBitmap);
			*/
		BitmapManager.INSTANCE.loadBitmap(newsfeedItem.getImg(), viewHolder.imgViewItem, 950,
			    700);
		return convertView;
	}
	
	// our ViewHolder.
	// caches our TextView
	static class ViewHolderItem {
	    ImageView imgViewItem;
	    int pos;
	}
}
