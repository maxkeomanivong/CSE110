package com.learn2crack;

/**
 * Author :Raj Amal
 * Email  :raj.amalw@learn2crack.com
 * Website:www.learn2crack.com
 **/

import android.app.Activity;
import android.content.Context;
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
    private static final int REQUEST_CODE = 1;
    final String upLoadServerUri = "http://foodobjectorienteddesign.com/imageupload/wes/poopers.php";
    String uploadFilePath = "";
    TextView messageText;

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
            	Toast.makeText(getApplicationContext(),
                        "Camera Button", Toast.LENGTH_SHORT).show();
            	/*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            	if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_CODE);
                }*/
            	dispatchTakePictureIntent();
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
    	Bitmap bp = (Bitmap) data.getExtras().get("data");
    	Uploadedphoto.setImageBitmap(bp);
    	Uri tempUri = getImageUri(getApplicationContext(), bp);
        File finalFile = new File(getRealPathFromURI(tempUri));
    	uploadFilePath = finalFile.getAbsolutePath();//finalFile.getAbsolutePath();
    	Toast.makeText(getApplicationContext(),
    			uploadFilePath, Toast.LENGTH_LONG).show();
        new Thread(new Runnable() {
            public void run() {                     
               Uploader put = new Uploader(uploadFilePath,upLoadServerUri);
                 put.uploadFile(upLoadServerUri);
                                          
            }
          }).start(); 
     }
	
	public void toggleMenu(View v){
		this.root.toggleMenu();
	}
	
	String mCurrentPhotoPath;

	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + "_";
	    File storageDir = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES);
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );

	    // Save a file: path for use with ACTION_VIEW intents
	    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
	    return image;
	}
	
	static final int REQUEST_TAKE_PHOTO = 0;

	private void dispatchTakePictureIntent() {
	    Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

	}
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

}