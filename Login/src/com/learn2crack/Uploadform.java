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
	 EditText Description;
	 EditText Dish;
	 EditText Restaurant;
	 EditText Address;
	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.form);
	        upolad = (Button)findViewById(R.id.btnLogin);
        	Toast.makeText(getApplicationContext(),
                    "We made it to Uploadform", Toast.LENGTH_SHORT).show();
	        upolad.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View view) {
	            Intent myIntent = new Intent(view.getContext(), Main.class);
	            startActivityForResult(myIntent, 0);
	            finish();
	            }});
	   }

}
