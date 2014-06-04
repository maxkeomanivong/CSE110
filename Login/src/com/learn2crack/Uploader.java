package com.learn2crack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;







import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Uploader {
	TextView messageText;
    Button uploadButton;
    int serverResponseCode = 0;
    int dialog = 0;
    String upLoadServerUri = null;
    String uploadToFileManager = "http://foodobjectorienteddesign.com/poopers.php";
    String uploadFilePath = "";
	public Uploader(String path, String Serverpath) {
		// TODO Auto-generated constructor stub
		uploadFilePath = path;
		upLoadServerUri = Serverpath;
	}

        
    
     
    /**********  File Path *************/
   
      
    public int uploadFile(String sourceFileUri, String Dish, String Descritption, String Restaurant) {
           
           
          String fileName = sourceFileUri;
  
          HttpURLConnection conn = null;
          DataOutputStream dos = null;  
          String lineEnd = "\r\n";
          String twoHyphens = "--";
          String boundary = "*****";
          int bytesRead, bytesAvailable, bufferSize;
          byte[] buffer;
          String nameOfFile = null;
          int maxBufferSize = 1 * 1024 * 1024; 
          File sourceFile = new File(sourceFileUri); 
          nameOfFile = sourceFile.getName();
          //Herbert Bitmap resizie and compresssion, prperation for upload
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
		  Bitmap bitmap = BitmapFactory.decodeFile(sourceFile.getPath());
		  Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 900, 700, false);
		  scaled.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		  //load the resized bitmap from the output stream to the input stream
		  ByteArrayInputStream bis = new ByteArrayInputStream(baos.toByteArray());
		  //End Herbert
		  
          dialog = ProgressDialog.STYLE_SPINNER; 
          postData(upLoadServerUri,Dish,Descritption,Restaurant,nameOfFile);
          upLoadServerUri = uploadToFileManager;

          if (!sourceFile.isFile()) { 
                
               Log.e("uploadFile", "Source File not exist :"
                                   +uploadFilePath);
                
               new Thread(new Runnable() {
                   public void run() {
                       messageText.setText("Source File not exist :"
                               +uploadFilePath );
                   }
               }); 
                
               return 0;
            
          }
          else
          {
               try { 
                    
                     // open a URL connection to the Servlet
                   FileInputStream fileInputStream = new FileInputStream(sourceFile);
                   URL url = new URL(upLoadServerUri);
                   // Open a HTTP  connection to  the URL
                   conn = (HttpURLConnection) url.openConnection(); 
                   conn.setDoInput(true); // Allow Inputs
                   conn.setDoOutput(true); // Allow Outputs
                   conn.setUseCaches(false); // Don't use a Cached Copy
                   conn.setRequestMethod("POST");
                   conn.setRequestProperty("Connection", "Keep-Alive");
                   conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                   conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                   conn.setRequestProperty("upload_file", fileName); 
                   //conn.setRequestProperty("Title",);
                    
                   dos = new DataOutputStream(conn.getOutputStream());
          
                   dos.writeBytes(twoHyphens + boundary + lineEnd); 
				dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\" ;filename=\""
                                             + fileName + "\"" + lineEnd);
                    
                   dos.writeBytes(lineEnd);
          
                   // create a buffer of  maximum size
                   bytesAvailable = bis.available(); 
          
                   bufferSize = Math.min(bytesAvailable, maxBufferSize);
                   buffer = new byte[bufferSize];
          
                   // read file and write it into form...
                   bytesRead = bis.read(buffer, 0, bufferSize);  
                      
                   while (bytesRead > 0) {
                        
                     dos.write(buffer, 0, bufferSize);
                     bytesAvailable = bis.available();
                     bufferSize = Math.min(bytesAvailable, maxBufferSize);
                     bytesRead = bis.read(buffer, 0, bufferSize);   
                      
                    }
          
                   // send multipart form data necesssary after file data...
                   dos.writeBytes(lineEnd);
                   dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
          
                   // Responses from the server (code and message)
                   serverResponseCode = conn.getResponseCode();
                   String serverResponseMessage = conn.getResponseMessage();
                     
                   Log.i("uploadFile", "HTTP Response is : "
                           + serverResponseMessage + ": " + serverResponseCode);
                    
                   if(serverResponseCode == 200){
                        
                	   new Thread(new Runnable() {
                            public void run() {
                                 
                                String msg = "File Upload Completed.\n\n";
                                 
                                messageText.setText(msg);
                            }
                        });                
                   }    
                    
                   //close the streams //
                   baos.close();
                   bis.close();
                   fileInputStream.close();
                   dos.flush();
                   dos.close();
                     
              } catch (MalformedURLException ex) {
                   
   
                  ex.printStackTrace();
                   
                  new Thread(new Runnable() {
                      public void run() {
                          messageText.setText("MalformedURLException Exception : check script url.");
                      }
                  });
                   
                  Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
              } catch (Exception e) {
                   
 
                  e.printStackTrace();
                   
                  new Thread(new Runnable() {
                      public void run() {
                          messageText.setText("Got Exception : see logcat ");
                      }
                  });
                  Log.e("Upload file to server Exception", "Exception : "
                                                   + e.getMessage(), e);  
              }
      
              return serverResponseCode; 
               
           } // End else block 
         } 
    
    public void postData(String sourceFileUri, String Dish, String Descritption, String Restaurant, String Filename) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(sourceFileUri);

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("Title", Dish));
            nameValuePairs.add(new BasicNameValuePair("Location", Restaurant));
            nameValuePairs.add(new BasicNameValuePair("Description", Descritption));
            nameValuePairs.add(new BasicNameValuePair("Filename", Filename));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    }

}
