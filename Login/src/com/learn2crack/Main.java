package com.learn2crack;

/**
 * Author :Raj Amal
 * Email  :raj.amalw@learn2crack.com
 * Website:www.learn2crack.com
 **/

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import com.learn2crack.FlyOutContainer;
import com.learn2crack.library.UserFunctions;
import com.learn2crack.library.DatabaseHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;



public class Main extends Activity {
    Button btnLogout;
    Button changepas;
    Button camera;
    Bitmap picture;
    ImageView Uploadedphoto;
	private static final int RESULT_LOAD_IMAGE = 1;
    final String upLoadServerUri = "http://foodobjectorienteddesign.com/imageupload/wes/poopers.php";
    String uploadFilePath = "";
    TextView messageText;
    private Intent pictureActionIntent = null;
    FlyOutContainer root;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);

        this.root = (FlyOutContainer) this.getLayoutInflater().inflate(R.layout.newsfeed, null);
		this.setContentView(root);
        
        changepas = (Button) findViewById(R.id.slideoutb1);
        btnLogout = (Button) findViewById(R.id.logoutn);
        camera = (Button)findViewById(R.id.camera);
        Uploadedphoto = (ImageView)findViewById(R.id.img1);
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
    			putToserver(data);
    		}
    	}else if(requestCode == RESULT_LOAD_IMAGE){
       		if(resultCode == RESULT_OK){
       			if( data.getExtras() != null){
       				putToserver(data);
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

		public void putToserver(Intent data){
			Bitmap bp = (Bitmap) data.getExtras().get("data");
	    	Uploadedphoto.setImageBitmap(bp);
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
	    	Toast.makeText(getApplicationContext(),
	    			imageFilePath, Toast.LENGTH_LONG).show();
	        final Uploader put = new Uploader(imageFilePath,upLoadServerUri);
	        uploadFilePath = imageFilePath;
	    	//uploadFilePath = data.getDataString();
	        new Thread(new Runnable() {
	            public void run() {                                
	                 put.uploadFile(uploadFilePath);
	            }
	          }).start();   
			
		}

}