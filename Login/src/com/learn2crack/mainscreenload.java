package com.learn2crack;

import java.util.ArrayList;
import java.util.List;

import com.learn2crack.*;
import com.learn2crack.PullToRefreshListView.OnRefreshListener;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.util.Arrays;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

public class mainscreenload extends ListActivity {
	private List<String> mListItems;
	ArrayAdapterCustom adapter;
	PullToRefreshListView listItems;
	XMLHandler reader;
	int counter=1;
	String RSSfeed = "http://foodobjectorienteddesign.com/feed/wesfeed/feed.php";
	ArrayList <String> links = new ArrayList<String>();
	ArrayList <String> ids = new ArrayList<String>();
	ArrayList <String> smashes = new ArrayList<String>();
	ArrayList <String> passes = new ArrayList<String>();
	ArrayList <String> names = new ArrayList<String>();
	ArrayList <String> address = new ArrayList<String>();
	ArrayList <String> resname = new ArrayList<String>();
	ArrayList <String> time = new ArrayList<String>();
	ArrayList <String> description = new ArrayList<String>();
	ArrayList <String> uid = new ArrayList<String>();
	List<NewsFeedItem> item = new ArrayList<NewsFeedItem>();
public void set(){
	
	if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
		    new GetDataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} 
        
        //initializes the first news feed contents
        
		for(int x=0;x<links.size();x++)
        {
        	Log.d(links.get(x), "LINKS ADDED --------MAIN ACTIVITY");
        	item.add(new NewsFeedItem(links.get(x),ids.get(x),smashes.get(x),passes.get(x),names.get(x),resname.get(x),time.get(x),description.get(x),uid.get(x)));
        }
        
        //the adapter for the listview,passing in this, the layout for each row in
        //the list view and the array with the newsfeeditems.
        adapter = new ArrayAdapterCustom (this, R.layout.listview_row, item);
    
        //create the ListView and attach the adapter and listener
        listItems = (PullToRefreshListView) getListView();
        listItems.setAdapter(adapter);
        listItems.setOnScrollListener(listItems);
        listItems.setOnItemClickListener(new ListViewItemListener(mainscreenload.this));
        System.err.println("THINGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
		// Set a listener to be invoked when the list should be refreshed.
		
				listItems.setOnRefreshListener(new OnRefreshListener() {
					@Override
					public void onRefresh() {
						// Do work to refresh the list here.
						//new GetDataTask().execute();
						if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
						    new GetDataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						} else {
						    new GetDataTask().execute();
						}
					}
				}); 

	}
class GetDataTask extends AsyncTask<Void, Void, String[]> {

	@Override
	protected String[] doInBackground(Void... params) {
		// Simulates a background job.
		counter=0;
		String count= ""+counter;
		reader = new XMLHandler();
        reader.execPHP(RSSfeed+"?start="+count);
        while(reader.getFlag()==0)
        {
           links = reader.getURLS();
           ids = reader.getIDs();
           smashes = reader.getSmashes();
           passes = reader.getPasses();
        }
        reader.resetFlag();
	      
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
		
		adapter.clear();
		for(int x=0;x<links.size();x++)
        {
			counter++;
        	//Log.d(links.get(x), "LINKS ADDED --------MAIN ACTIVITY");
			adapter.add(new NewsFeedItem(links.get(x),ids.get(x),smashes.get(x),passes.get(x),names.get(x),"","","",uid.get(x)));//,resname.get(x),time.get(x),description.get(x),uid.get(x)));
        }
		adapter.notifyDataSetChanged();
		listItems.setCount(counter);
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



