package com.androidsurya.customviews;

import java.util.ArrayList;
import java.util.List;

import com.androidsurya.pulltorefresh.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ArrayAdapterCustom extends ArrayAdapter<NewsFeedItem> {
	Context myContext;
	int layoutID;
	List<NewsFeedItem> data = null;
	int position;
	XMLHandler reader;
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
	        viewHolder.view=convertView;
	        viewHolder.imgViewItem = (ImageView) convertView.findViewById(R.id.imgView);
	        viewHolder.s=(TextView) convertView.findViewById(R.id.tbox3);
	        viewHolder.pos=position;
	         
	        viewHolder.imgViewItem.setTag(position);
	        // store the holder with the view.
	        viewHolder.getButtonSmash().setOnClickListener(new SmashListener());
	        viewHolder.getButtonPass().setOnClickListener(new SmashListener());
	        convertView.setTag(viewHolder);
	        viewHolder.getButtonSmash().setTag(viewHolder);
	        viewHolder.getButtonPass().setTag(viewHolder);
		}
		else{
	        // we've just avoided calling findViewById() on resource everytime
	        // just use the viewHolder
	        viewHolder = (ViewHolderItem) convertView.getTag();
	    }
		viewHolder.pos=position;
		viewHolder.s.setText(""+data.get(position).getSmash()+" Smashes | "+data.get(position).getPass()+" Passes");
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
		View view;
	    ImageView imgViewItem;
	    int pos;
	    Button smash;
	    Button pass;
	    TextView s;
	    public Button getButtonSmash()
	    {
	    	smash=(Button) view.findViewById(R.id.smash2);
	    	return smash;
	    }
	    public Button getButtonPass()
	    {
	    	pass=(Button) view.findViewById(R.id.pass2);
	    	return pass;
	    }
	}
	
	public class SmashListener implements OnClickListener{
		@Override
		public void onClick(View myView) {
			ViewHolderItem v = (ViewHolderItem) myView.getTag();

			switch(myView.getId())
			{
			case R.id.smash2:
				new GetDataTask3().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://foodobjectorienteddesign.com/feed/like.php?Id="+data.get(v.pos).getId());
				Toast.makeText(myView.getContext(), "ID number is "+data.get(v.pos).getId()+". ", Toast.LENGTH_SHORT).show();
				v.s.setText(""+(data.get(v.pos).getSmash()+1)+" Smashes | "+data.get(v.pos).getPass()+" Passes");
				data.get(v.pos).smash=""+(data.get(v.pos).getSmash()+1);
			break;
			case R.id.pass2:
				new GetDataTask3().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://foodobjectorienteddesign.com/feed/dislike.php?Id="+data.get(v.pos).getId());
				Toast.makeText(myView.getContext(), "ID number is "+R.id.pass2+". ", Toast.LENGTH_SHORT).show();
				v.s.setText(""+data.get(v.pos).getSmash()+" Smashes | "+(data.get(v.pos).getPass()+1)+" Passes");
				data.get(v.pos).pass=""+(data.get(v.pos).getPass()+1);
			break;
			}
			
		}


	}
	
	private class GetDataTask3 extends AsyncTask<String, Void, String[]> {

		@Override
		protected String[] doInBackground(String... params) {
			// Simulates a background job.
			String par= (String)params[0];
			reader = new XMLHandler();
	        reader.execPHP(par);
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
			
			super.onPostExecute(result);
		}
	}

	private String[] mStrings = { "Andaman and Nicobar Islands",
			"Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar",
			"Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh",
			"Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala",
			"Madhya Pradesh", "Maharashtra", "Manipur" };
}
