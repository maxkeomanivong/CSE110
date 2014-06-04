package com.learn2crack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Uploadform extends Activity{
	 Button upolad;
	 Intent image;  
	 EditText Description;
	 EditText Dish;
	 EditText Restaurant;
	 EditText Address;
	 Intent form;
	 
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        form = new Intent(this, Main.class);
		    Intent old = getIntent();
		    if(old.hasExtra("Data")){
		    	form.putExtra("Data", old.getStringExtra("Data"));
		    }
	        setContentView(R.layout.form);
	        upolad = (Button)findViewById(R.id.btnLogin);
	        Dish = (EditText)findViewById(R.id.Dish);
	        Restaurant = (EditText)findViewById(R.id.restuarant);
	        Address = (EditText)findViewById(R.id.address);
	        Description = (EditText)findViewById(R.id.descr);
	        upolad.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View view) {
	            String dishname =  Dish.getText().toString();
	            String descript =  Description.getText().toString();
	            String address =  Address.getText().toString();
	            String restaur =  Restaurant.getText().toString();	            
	            form.putExtra("Dish", dishname);
	            form.putExtra("Description", descript);
	            form.putExtra("Address", address);
	            form.putExtra("Restaurant", restaur);
	            startActivity(form);
	            finish();
	            }});
		    }
	        
	        
}
	

