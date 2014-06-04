package com.learn2crack;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.learn2crack.ArrayAdapterCustom.ViewHolderItem;
public class ListViewItemListener implements OnItemClickListener{
	Main context;
	Display display;
	Point size;
	int width;
	int height;
    PopupWindow popup;
	int trigCount = 0;
	ArrayList <String> names = new ArrayList<String>();
	ArrayList <String> address = new ArrayList<String>();
	ArrayList <String> phone = new ArrayList<String>();
	ArrayList <String> callfor = new ArrayList<String>();
	ArrayList <String> description = new ArrayList<String>();
	ArrayList <String> uid = new ArrayList<String>();
	
	XMLHandler reader;
	int counter=1;
	String RSSfeed = "http://foodobjectorienteddesign.com/feed/restaurantinfo.php?foodId=";
	View layout;
	public ListViewItemListener(Activity context)
	{
		this.context=(Main) context;
		display = context.getWindowManager().getDefaultDisplay();
		size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
	}
	
	public void onPopupClick(View v)
	{
		System.err.println("View : boobop");
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		// TODO Auto-generated method stub
		int popupWidth = width-80;
		   int popupHeight = 1100;
		 ViewHolderItem item = (ViewHolderItem)view.getTag();
		 System.err.println("The Id is: "+item.getItem().id);
		   // Inflate the popup_layout.xml
		   LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
		   LayoutInflater layoutInflater = (LayoutInflater) context
		     .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		   layout = layoutInflater.inflate(R.layout.popup_layout, viewGroup);
		 
		   // Creating the PopupWindow
		   popup = new PopupWindow(context);
		   popup.setContentView(layout);
		   popup.setWidth(popupWidth);
		   popup.setHeight(popupHeight);
		   popup.setFocusable(true);
		   layout.findViewById(R.id.popup).setTag(popup);
		   ((TextView) layout.findViewById(R.id.textView1)).setTypeface(Typeface.createFromAsset(context.getAssets(),"qsandbold.otf"));
		   ((TextView) layout.findViewById(R.id.textView2)).setTypeface(Typeface.createFromAsset(context.getAssets(),"qsandbold.otf"));
		   ((TextView) layout.findViewById(R.id.textView3)).setTypeface(Typeface.createFromAsset(context.getAssets(),"qsandbold.otf"));
		   ((TextView) layout.findViewById(R.id.textView4)).setTypeface(Typeface.createFromAsset(context.getAssets(),"qsandbold.otf"));
		   ((TextView) layout.findViewById(R.id.textView5)).setTypeface(Typeface.createFromAsset(context.getAssets(),"qsandbold.otf"));

		   // Some offset to align the popup a bit to the right, and a bit downs, relative to button's position.
		   int OFFSET_X = 30;
		   int OFFSET_Y = 30;
		 
		   new GetDataTask69().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,item.getItem().id);
		   
		   // Clear the default translucent background
		   popup.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.textboxpopup));
		 
		   // Displaying the popup at the specified location, + offsets.
		   popup.showAtLocation(context.getListView(), Gravity.CENTER,0,0);
		 /*
		   // Getting a reference to Close button, and close the popup when clicked.
		   Button close = (Button) layout.findViewById(R.id.close);
		   close.setOnClickListener(new OnClickListener() {
		 
		     @Override
		     public void onClick(View v) {
		       popup.dismiss();
		     }
		   });*/
		}
	
	private class GetDataTask69 extends AsyncTask<String, Void, String[]> {

		@Override
		protected String[] doInBackground(String... params) {
			// Simulates a background job.
			int trig=0;
			while(trig==0 && trigCount<50)
			{
			reader = new XMLHandler();
	        reader.execPHP(RSSfeed+params[0]);
	        while(reader.getFlag()==0)
	        {	          
	           names = reader.getNames();
	           address = reader.getAddress();
	           phone = reader.getNumbers();
	           callfor = reader.getCallfor();
	           description = reader.getDescription();
	           
	        }
	        trigCount++;
	        if(names!=null && address!=null && phone!=null && callfor!=null && description!=null)
	        	trig=1;
	        reader.resetFlag();
			}
			trig=0;
			System.err.println("Out of the while loop");
			//adapter.add(new NewsFeedItem(links.get(3)));
    		/*
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {

			}*/
			//adapter.clear();
    		/*
    		for(int x=0;x<links.size();x++)
            {
            	Log.d(links.get(x), "LINKS ADDED --------MAIN ACTIVITY");
            	adapter.add(new NewsFeedItem(links.get(x)));
            }
    		adapter.notifyDataSetChanged();*/
			return mStrings;
		}

		@Override
		protected void onPostExecute(String[] result) {
			//mListItems.add(0, "Added new item after refresh...");
			// Call onRefreshComplete when the list has been refreshed.
			System.err.println("The name is "+ names.get(0));
			try{
				((TextView)layout.findViewById(R.id.textView1)).setText("Restaurant Name >> "+names.get(0));
				((TextView)layout.findViewById(R.id.textView2)).setText("Address >> "+address.get(0));
				((TextView)layout.findViewById(R.id.textView3)).setText("Phone Number >> "+phone.get(0));
				((TextView)layout.findViewById(R.id.textView4)).setText("Call for "+callfor.get(0));
				((TextView)layout.findViewById(R.id.textView5)).setText(""+description.get(0));
				}
			catch(NullPointerException e)
			{
				System.err.println("ITSNULLLLLLYOYYOOYYOYOYOYO1");

			}
			
				super.onPostExecute(result);
				
			
		}
	}

	private String[] mStrings = { "Andaman and Nicobar Islands",
			"Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar",
			"Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh",
			"Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala",
			"Madhya Pradesh", "Maharashtra", "Manipur" };
	
	
	}
