package com.raifuzu.twistypuzzlemasterz;

import java.util.ArrayList;
import com.raifuzu.twistypuzzlemasterz.MainActivity.PlaceholderFragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class RecordedTimesFragment extends Fragment {

	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";
	private View rootView;
	private CustomTimesListAdapter adapter;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static RecordedTimesFragment newInstance(int sectionNumber) {
		RecordedTimesFragment fragment = new RecordedTimesFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public RecordedTimesFragment() {

	}
 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_recorded_times, container, false);

		
		String cube = getCurrentCubeType();
		Toast.makeText(getActivity(), "Cube Type: " + cube, Toast.LENGTH_SHORT).show();

		// Gets the correct times from the last selected cube type
		MainActivity.timesDB.setSpecificData(cube);
		// TODO - read from the DB
		final ArrayList<String> timesList = MainActivity.timesDB.getTimesList();
		final ArrayList<String> algList = MainActivity.timesDB.getScrambleList();
		int numOfTimes = timesList.size();
		final ArrayList<String> countsList = new ArrayList<String>();
		for (int i = 1; i <= numOfTimes ; i++) {
			countsList.add(Integer.toString(i));
		}

		adapter = new CustomTimesListAdapter(getActivity(), countsList, timesList, algList);
		final ListView list = (ListView) rootView.findViewById(R.id.times_list);
		list.setAdapter(adapter);
		
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String Slecteditem = timesList.get(+position);
				Toast.makeText(getActivity(), Slecteditem, Toast.LENGTH_SHORT).show();

			}
		});

		list.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {

				String time = timesList.get(+pos);
				String alg = algList.get(+pos);
				
				showLongClickOptions(list, time, alg);
				return true;
			}
		});
		
		 
		
		return rootView;
	}

	private CubeType stringToCubeType(String cube) {
		CubeType result = CubeType.STANDARD_3x3;

		switch (cube) {
		case "STANDARD_2x2":
			result = CubeType.STANDARD_2x2;
			break;
		case "STANDARD_3x3":
			result = CubeType.STANDARD_3x3;
			break;
		case "STANDARD_4x4":
			result = CubeType.STANDARD_4x4;
			break;
		default:
			break;
		}

		return result;
	}

	private void showLongClickOptions(final ListView list, final String time, final String alg) {
		final String[] items = { "Delete Time", "Delete All", "Use This Scramble Algorithm" };
		AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
		builder.setTitle("Options");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				// Do something with the selection
				String text = items[item];
				if (item == 0) {
					// delete current time
					MainActivity.timesDB.delete(time, alg);
					refreshFragment(list);
				} else if (item == 1) {
					// delete all times
					MainActivity.timesDB.deleteAllTimes(stringToCubeType(getCurrentCubeType()));
					refreshFragment(list);
				}

				Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
			}
		});
		 

		 
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void refreshFragment(final ListView list){
		;
		// Refresh Fragment
		int position = 0 ;// WARNING:: This value might change if you remove or add fragments
		getFragmentManager().beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
		.commit();
		 position = 2 ; 
		 
		getFragmentManager().beginTransaction().replace(R.id.container, RecordedTimesFragment.newInstance(position + 1))
		.commit();
	}
	

	private String getCurrentCubeType() {
		SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
		String defaultValue = "STANDARD_3x3";
		String type = sharedPref.getString("CUBE_KEY", defaultValue);
		return type;

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
	}

}
