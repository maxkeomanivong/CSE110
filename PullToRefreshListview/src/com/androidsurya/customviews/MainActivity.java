package com.androidsurya.customviews;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.androidsurya.pulltorefresh.R;
import com.androidsurya.pulltorefresh.R.layout;
import com.androidsurya.customviews.*;
import com.androidsurya.customviews.PullToRefreshListView.OnRefreshListener;


public class MainActivity extends ListActivity {
	private List<String> mListItems;
	ArrayAdapterCustom adapter;
	PullToRefreshListView listItems;
	RssReader reader;
	String RSSfeed = "http://foodobjectorienteddesign.com/feed/datesort.php";
	ArrayList <String> links = new ArrayList<String>();
	List<NewsFeedItem> item = new ArrayList<NewsFeedItem>();
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pulltorefresh);

		reader = new RssReader();
        links = reader.getLinks(RSSfeed);
       
        //initializes the first news feed contents
        for(int x=0;x<links.size();x++)
        {
        	Log.d(links.get(x), "LINKS ADDED --------MAIN ACTIVITY");
        	item.add(new NewsFeedItem(links.get(x)));
        }
        
        //the adapter for the listview,passing in this, the layout for each row in
        //the list view and the array with the newsfeeditems.
        adapter = new ArrayAdapterCustom (this, R.layout.listview_row,item);
    
        //create the ListView and attach the adapter and listener
        listItems = (PullToRefreshListView) getListView();
        listItems.setAdapter(adapter);
        listItems.setOnScrollListener(listItems);
        listItems.setOnItemClickListener(new ListViewItemListener());
        
		// Set a listener to be invoked when the list should be refreshed.
		
				listItems.setOnRefreshListener(new OnRefreshListener() {
					@Override
					public void onRefresh() {
						// Do work to refresh the list here.
						new GetDataTask().execute();
						adapter.clear();
						reader = new RssReader();
				        links = reader.getLinks(RSSfeed);
						//adapter.add(new NewsFeedItem(links.get(3)));
			    		for(int x=0;x<links.size();x++)
			            {
			            	//Log.d(links.get(x), "LINKS ADDED --------MAIN ACTIVITY");
			            	adapter.add(new NewsFeedItem(links.get(x)));
			            }
			    		adapter.notifyDataSetChanged();
					}
				});

	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {

			}
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
			((PullToRefreshListView) getListView()).onRefreshComplete();

			super.onPostExecute(result);
		}
	}

	private String[] mStrings = { "Andaman and Nicobar Islands",
			"Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar",
			"Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh",
			"Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala",
			"Madhya Pradesh", "Maharashtra", "Manipur" };
}
