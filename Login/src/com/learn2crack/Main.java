package com.learn2crack;

/**
 * Author :Raj Amal
 * Email  :raj.amalw@learn2crack.com
 * Website:www.learn2crack.com
 **/

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.BitmapFactory;

import com.learn2crack.PullToRefreshListView;
import com.learn2crack.PullToRefreshListView.OnRefreshListener;
import com.learn2crack.FlyOutContainer;
import com.learn2crack.library.UserFunctions;
import com.learn2crack.library.DatabaseHandler;
import com.learn2crack.mainscreenload.GetDataTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;



public class Main extends ListActivity {
    Button btnLogout;
    Button changepas;
    Button camera;
    Bitmap picture;
    ImageView Uploadedphoto;
	private static final int RESULT_LOAD_IMAGE = 1;
    final String upLoadServerUri = "http://foodobjectorienteddesign.com/upload_pic.php";
    String uploadFilePath = "";
    TextView messageText;
    private Intent pictureActionIntent = null;
    FlyOutContainer root;
    PullToRefreshListView listItems;
    //mainscreenload homepage = new mainscreenload();
	private List<String> mListItems;
	ArrayAdapterCustom adapter;
	XMLHandler reader;
	int counter=0;
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
	 Button upolad;
		String Dishname,Restaurant,Address,Description = "";
		Intent info;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);

        Intent get = getIntent();
        Intentgrabber(get);
        
        this.root = (FlyOutContainer) this.getLayoutInflater().inflate(R.layout.newsfeed, null);
		this.setContentView(root);
		
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
        listItems.setOnItemClickListener(new ListViewItemListener(Main.this));
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

        changepas = (Button) findViewById(R.id.slideoutb1);
        btnLogout = (Button) findViewById(R.id.logoutn);
        camera = (Button)findViewById(R.id.camera);
        upolad = (Button)findViewById(R.id.btnLogin);
        //Uploadedphoto = (ImageView) findViewById(R.id.);

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        /**
         * Hashmap to load data from the Sqlite database
         **/
         HashMap<String,String> user = new HashMap<String, String>();
         user = db.getUserDetails();


        /**
         * Change Password Activity Started
         **/
        changepas.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){

                Intent chgpass = new Intent(getApplicationContext(), ChangePassword.class);

                startActivity(chgpass);
            }

        });

       /**
        *Logout from the User Panel which clears the data in Sqlite database
        **/
        btnLogout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                UserFunctions logout = new UserFunctions();
                logout.logoutUser(getApplicationContext());
                Intent login = new Intent(getApplicationContext(), Login.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
                finish();
            }
          });
        camera.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
    	        startDialog(); 	
            }
          });
/**
 * Sets user first name and last name in text view.
 **/
       // final TextView login = (TextView) findViewById(R.id.textwelcome);
       // login.setText("Welcome  "+user.get("fname"));
       // final TextView lname = (TextView) findViewById(R.id.lname);
       // lname.setText(user.get("lname"));


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
    	super.onActivityResult(requestCode, resultCode, data);
    	if (requestCode == REQUEST_TAKE_PHOTO){
    		if(resultCode == RESULT_OK){
    			startDialogForm(data);
    		}
    	}else if(requestCode == RESULT_LOAD_IMAGE){
       		if(resultCode == RESULT_OK){
       			if( data.getExtras() != null){
       				startDialogForm(data);
       			}else{
                	Toast.makeText(getApplicationContext(),
                            "ERROR: Pick photo from Gallery", Toast.LENGTH_SHORT).show();
       			}
    		}
    	}
    	
    	
     }
	
	public void toggleMenu(View v){
		this.root.toggleMenu();
	}
	
	String mCurrentPhotoPath;

	static final int REQUEST_TAKE_PHOTO = 0;

	public Uri getImageUri(Context inContext, Bitmap inImage) {
	    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
	    String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
	    return Uri.parse(path);
	}
	public String getRealPathFromURI(Uri uri) {
	    Cursor cursor = getContentResolver().query(uri, null, null, null, null); 
	    cursor.moveToFirst(); 
	    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
	    return cursor.getString(idx); 
	}
	 private void startDialog() {
		    AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
		    myAlertDialog.setTitle("Upload Pictures Option");
		    myAlertDialog.setMessage("Choose a Photo or Take a Photo");

		    myAlertDialog.setPositiveButton("Gallery",
		            new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface arg0, int arg1) {
		                    pictureActionIntent = new Intent(
		                            Intent.ACTION_GET_CONTENT, null);
		                    pictureActionIntent.setType("image/*");
		                    pictureActionIntent.putExtra("return-data", true);
		                    startActivityForResult(pictureActionIntent,
		                    		RESULT_LOAD_IMAGE);
		                }
		            });

		    myAlertDialog.setNegativeButton("Camera",
		            new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface arg0, int arg1) {
		                    pictureActionIntent = new Intent(
		                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		                    startActivityForResult(pictureActionIntent,
		                    		REQUEST_TAKE_PHOTO);

		                }
		            });
		    myAlertDialog.show();
		}
	 private void startDialogForm(final Intent data) {
	     pictureActionIntent = new Intent(this, Uploadform.class);
	     pictureActionIntent.putExtra("Data",putToserver(data));
	     startActivity(pictureActionIntent);    
	}

		public String putToserver(Intent data){
			Bitmap bp = (Bitmap) data.getExtras().get("data");
	    	//Uploadedphoto.setImageBitmap(bp);
	    	//Uri tempUri = getImageUri(getApplicationContext(), bp);
	    	Uri _uri = data.getData();
	    	String imageFilePath = "";
	        if (_uri != null) {
	            //User had pick an image.
	            Cursor cursor = getContentResolver().query(_uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
	            cursor.moveToFirst();

	            //Link to the image
	            imageFilePath = cursor.getString(0);
	            Log.v("imageFilePath", imageFilePath);
	            File photos= new File(imageFilePath);
	            long length = photos.length();

	            byte[] imgData = new byte[(int) length];

	            FileInputStream pdata = null;
	               try {
	                   pdata = new FileInputStream(photos);


	               } catch (FileNotFoundException e1) {
	                   // TODO Auto-generated catch block
	                   e1.printStackTrace();
	               }
	                try {
	                   pdata.read(imgData);//imgdata is an array where you get byte data for selected image from gallery and ready to upload.
	               } catch (IOException e) {
	                   // TODO Auto-generated catch block
	                   e.printStackTrace();
	               }




	            cursor.close();
	        }
	    	uploadFilePath = imageFilePath;
	         return uploadFilePath;
	    	//uploadFilePath = data.getDataString();  
			
		}
		
		//Detects when the popup is clicked so it can be closed
		public void onPopupClick(View v)
		{ 
			PopupWindow pop = (PopupWindow)(v.getTag());
			pop.dismiss();
			v.setVisibility(View.GONE);
			System.err.println("View : boobop");
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
		           description = reader.getDescription();
		           names = reader.getNames();
		           //address = reader.getAddress();
		           //resname = reader.getResname();
		           time = reader.getTime();
		           //description = reader.getDescription();
		           uid= reader.getUID();
		        }
		        reader.resetFlag();
				return mStrings;
			}

			@Override
			protected void onPostExecute(String[] result) {
				//mListItems.add(0, "Added new item after refresh...");
				// Call onRefreshComplete when the list has been refreshed.
				// Call onRefreshComplete when the list has been refreshed.
				for(int x=0;x<uid.size();x++)
			    	  System.err.println("UID is: "+uid.get(x));
				if(links==null || ids==null || smashes==null || passes==null)	
				{
					((PullToRefreshListView) getListView()).onRefreshComplete();
					super.onPostExecute(result);
				}
				else
				{
					adapter.clear();
					for(int x=0;x<links.size();x++)
		            {
						counter++;
		            	adapter.add(new NewsFeedItem(links.get(x),ids.get(x),smashes.get(x),passes.get(x),names.get(x),"",time.get(x),description.get(x),uid.get(x)));//,resname.get(x),time.get(x),description.get(x),uid.get(x)));
		            }
		    		adapter.notifyDataSetChanged();
		    		listItems.setCount(counter);
		    		((PullToRefreshListView) getListView()).onRefreshComplete();
					super.onPostExecute(result);
				}
			}
		}

		private String[] mStrings = { "Andaman and Nicobar Islands",
				"Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar",
				"Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh",
				"Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala",
				"Madhya Pradesh", "Maharashtra", "Manipur" };
		
		public void Intentgrabber(Intent get){
	        if(get.hasExtra("Data")){
		        if(get.hasExtra("Dish")){
		        	Dishname = get.getStringExtra("Dish");
		        }
		        if(get.hasExtra("Description")){
		        	Description = get.getStringExtra("Description");
		        }
		        if(get.hasExtra("Address")){
		        	Address = get.getStringExtra("Address");
		        }
		        if(get.hasExtra("Restaurant")){		        	
		        	Restaurant = get.getStringExtra("Restaurant");
		        }
		        uploadFilePath = get.getStringExtra("Data");
		        final Uploader put = new Uploader(uploadFilePath,upLoadServerUri);
		        new Thread(new Runnable() {
		            public void run() {                                
		                 put.uploadFile(uploadFilePath,Dishname,Restaurant,Description);
		            }
		          }).start(); 
	        	Toast.makeText(getApplicationContext(),
	        			Restaurant+Address+ Description + Dishname + ":::" + uploadFilePath, Toast.LENGTH_SHORT).show();
	        }
			
		}

}