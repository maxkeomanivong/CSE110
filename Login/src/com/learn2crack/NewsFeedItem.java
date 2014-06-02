package com.learn2crack;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class NewsFeedItem {
		public String link;
		public String name;
		public String smash;
		public String pass;
		public Bitmap imgMap = null;
		public Bitmap newBitmap = null;
		Bitmap scaledBitmap;
		public ImageView iv;
		String id;
		float maxImageSize = 650;
		boolean filter = true;
		ArrayAdapterCustom adapter;
		public NewsFeedItem(String img,String id,String s, String p)
		{
			this.link=img;		
			this.id=id;
			this.smash=s;
			this.pass=p;
		}
		
		public String getImg()
		{
			return link;
		}
		public String getId()
		{
			return id;
		}
		public int getSmash()
		{
			return Integer.parseInt(smash);
		}
		public int getPass()
		{
			return Integer.parseInt(pass);
		}
		public void setBitMap(ImageView imgview,ArrayAdapterCustom adapter)
		{
			this.adapter=adapter;
			iv=imgview;
			//if(newBitmap==null)
			new DownloadImageTask(imgview,adapter.position).execute(link);
		}
		
		private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	        ImageView bmImage;
	        int positio;
	        public DownloadImageTask(ImageView bmImage, int position) {
	        	positio=position;
	            this.bmImage = bmImage;
	        }

	        protected Bitmap doInBackground(String... urls) {
	            String urldisplay = urls[0];
	            Bitmap realImage = null;
	            try {
	                InputStream in = new java.net.URL(urldisplay).openStream();
	                realImage = BitmapFactory.decodeStream(in);
	            } catch (Exception e) {
	                Log.e("Error", e.getMessage());
	                e.printStackTrace();
	            }
	            
	            float ratio = Math.min(
	                    (float) maxImageSize / realImage.getWidth(),
	                    (float) maxImageSize / realImage.getHeight());
	            int width = Math.round((float) ratio * realImage.getWidth());
	            int height = Math.round((float) ratio * realImage.getHeight());

	            newBitmap = Bitmap.createScaledBitmap(realImage, width,
	                    height, filter);
	            realImage.recycle();
	            return newBitmap;
	            
	        }

	        protected void onPostExecute(Bitmap result) {
	        
	        	
	            bmImage.setImageBitmap(result);
	            
	        }
	    }
}
