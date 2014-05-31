package com.learn2crack;

import android.content.Context;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Container {
	
	Button smash;
	Button pass; 
	ImageView img; 
	TextView txt1; 
	TextView txt2;
	
	public Container(Context con)
	{
		Button smash = new Button(con);
		Button pass = new Button(con);
		ImageView img = new ImageView(con);
		TextView txt1 = new TextView(con);
		TextView txt2 = new TextView(con);
		
		//set the button attributes
		smash.setBackgroundResource(R.drawable.smash);
		pass.setBackgroundResource(R.drawable.pass);
		txt1.setText("Baws");
		txt1.setText("BigBaws");
	}
	public void setSrc(String x)
	{		
		//img.setBackgroundResource(resid);
	}
	public void changeLocation(View view)
	{
		
	}
}
