package com.raifuzu.twistypuzzlemasterz;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class CustomListAdapter extends ArrayAdapter<String> {

	private final Activity context;
	private final Integer[] imgid;
	private final ArrayList<String> itemname;
	
	
	public CustomListAdapter(Activity context, Integer[] imgid, ArrayList<String> itemname) {
		super(context, R.layout.fridrich_listview, itemname);
		// TODO Auto-generated constructor stub

		this.context = context;
		this.imgid = imgid;
		this.itemname = itemname;
	}

	@SuppressLint("ViewHolder")
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.fridrich_listview, null, true);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
		TextView algorithm = (TextView) rowView.findViewById(R.id.algorithm_tv);
		imageView.setImageResource(imgid[position]);
		algorithm.setText(itemname.get(position));
		
		algorithm.setTypeface(null, Typeface.BOLD);
		final int F2L_TITLE = 0;
		final int OLL_TITLE = 42;
		
		if(position == F2L_TITLE || position == OLL_TITLE){
			rowView.setBackgroundColor(Color.rgb(245,245,245));
			algorithm.setPadding(0, 20, 20, 20);
			algorithm.setGravity(Gravity.CENTER);
			imageView.getLayoutParams().height = 1;
			imageView.getLayoutParams().width = 1;
		}else if(position != F2L_TITLE || position != OLL_TITLE){
			if(position > OLL_TITLE){
				position = position - OLL_TITLE;
			}
			algorithm.setText("(" + (position) + ") - " +  algorithm.getText().toString());
			
		}
		

		return rowView;

	};
}