package com.raifuzu.twistypuzzlemasterz;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class CustomTimesListAdapter extends ArrayAdapter<String> {

	private final Activity context;
	private final ArrayList<String> count;
	private final ArrayList<String> time;
	private final ArrayList<String> algorithm;

	public CustomTimesListAdapter(Activity context, ArrayList<String> count,ArrayList<String> time, ArrayList<String> algorithm) {
		super(context, R.layout.fragment_listview, algorithm);

		this.context = context;
		this.count = count;
		this.time = time;
		this.algorithm = algorithm;
	}

	@SuppressLint("ViewHolder")
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.fragment_listview, null, true);

		TextView countTV = (TextView) rowView.findViewById(R.id.count_tv);
		TextView timeTV = (TextView) rowView.findViewById(R.id.time_tv);
		TextView algorithmTV = (TextView) rowView.findViewById(R.id.algorithm_tv);

		countTV.setText(count.get(position));
		timeTV.setText(time.get(position));
		algorithmTV.setText(algorithm.get(position));
		return rowView;

	};
}