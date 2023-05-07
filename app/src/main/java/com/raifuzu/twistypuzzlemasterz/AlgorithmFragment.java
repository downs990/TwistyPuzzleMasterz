package com.raifuzu.twistypuzzlemasterz;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class AlgorithmFragment extends Fragment {

	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static AlgorithmFragment newInstance(int sectionNumber) {
		AlgorithmFragment fragment = new AlgorithmFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public AlgorithmFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_algorithm, container, false);

		final AdvancedArrayList<String> algsList = new AdvancedArrayList<String>(//
				"First Two Layers - (F2L)", // F2L - http://speedcubing.com/
				"R2 U2 F R2 F' U2 R' U R'", "R' U2 B' R B U2 R", "R' U2 B' R' B U2 R", //
				"R2 U2 R' U' R U' R' U2 R'", "F' L' U2 L F R U R'", "R U' R' U R U' R'", //
				"U' R U' R' F' U' F", "F2 U2 F U F' U F U2 F", "R U' R' F' L' U2 L F", //
				"F' U F U' F' U F", "U' R U R' U' R U R'", "U F' U F U F' U2 F", //
				"U F' U' F U' R U R'", "U' F' U F", "R' U2 R2 U R2 U R", //
				"R2 B U B' U' R2", "U' R U R' U R U R'", "R U R'", //
				"F' U' L' U2 L U' F", "U' R U' R' U R U R'", "F' L' B' U B L F", //
				"B' D B U' B' D' B", "U' R U R' U F' U' F", "U F' U F U' F' U' F", //
				"F' U F U2 R U R'", "F' U' F", "F2 U2 R' F2 R U2 F2", //
				"F2 L' U' L U F2", "R2 U B U' B' R2", "U R U' R'", //
				"F U2 F2 U' F2 U' F'", "R U R' U' R U R' U' R U R'", "F' U F R U2 R'", //
				"F' U2 F U F' U' F", "U2 R2 U2 R' U' R U' R2", "U' F' U2 F U' F' U F", //
				"U2 R U R' U R U' R'", "U R U2 R' U R U' R'", "U2 F' U' F U' F' U F", //
				"R U2 R' U' R U R'", "F' U' F' L F L' U2 F",
				// F2L Above
				"Orientation Last Layers - (OLL)",
				// OLL -
				// https://ruwix.com/the-rubiks-cube/advanced-cfop-fridrich/orient-the-last-layer-oll/
				"R' F R U R' F' R y L U' L'", "L F' L' U' L F L' y' R' U R", "R B R' L U L' U' R B' R'", //
				"L' B' L R' U' R U L' B L", "R U x' R U' R' U x U' R'", "(R U R' U') x D' R' U R E'", //
				"L' (R U R' U') L R' F R F'", "M' U' M U2' M' U' M", "R' U2 (R U R' U) R", //
				"L' U R U' L U R'", "L U' R' U L' U (R U R' U) R", "(R U R' U) R U' R' U R U2 R'", //
				"R' F' L F R F' L' F", "R' F' L' F R F' L F", "R2 D R' U2 R D' R' U2 R'", //
				"R U B' l U l2' x' U' R' F R F'", "R' F R F' U2 R' F R y' R2 U2 R", "y L' R2 B R' B L U2' L' B M'", //
				"R' U2 x R' U R U' y R' U' R' U R' F", "R' U2 F (R U R' U') y' R2 U2 x' R U",
				"F (R U R' U) y' R' U2 (R' F R F')", //
				"(R U R' U) R' F R F' U2 R' F R F'", "M' U2 M U2 M' U M U2 M' U2 M", "F (R U R' U') (R U R' U') F'", //
				"F' L' U' L U L' U' L U F", "L U' y' R' U2' R' U R U' R U2 R d' L'", "R' F R' F' R2 U2 x' U' R U R'", //
				"R' F R F' U2 R2 y R' F' R F'", "L F' L' F U2 L2 y' L F L' F", "L F R' F R F2 L'", //
				"L' B' L U' R' U R L' B L", "U2 r R2' U' R U' R' U2 R U' M", "U2 l' L2 U L' U L U2 L' U M", //
				"x' U' R U' R2' F x (R U R' U') R B2", "R' U' R y' x' R U' R' F R U R'", "R U R' y R' F R U' R' F' R", //
				"R2' U R' B R U' R2' U l U l'", "U' R U2' R' U' R U' R2 y' R' U' R U B",
				"U' R' U2 (R U R' U) R2 y (R U R' U') F'", //
				"R' U2 l R U' R' U l' U2 R", "F R' F' R U R U' R'", "r' U2 (R U R' U) r", //
				"r U2 R' U' R U' r'", "R U' y R2 D R' U2 R D' R2 d R'", "R' U' y L' U L' y' L F L' F R", //
				"F U R U' R' U R U' R' F'", "L' B' L U' R' U R U' R' U R L' B L", "L d R' d' L' U L F L'", //
				"R' d' L d R U' R' F' R", "F U R U' R' F'", "F' U' L' U L F", //
				"F (R U R' U') F'", "(R U R' U') R' F R F'", "L U L' U L U' L' U' y2' R' F R F'", //
				"R' U' R U' R' U R U y F R' F' R", "R' F (R U R' U') y L' d R", "L F' L' U' L U y' R d' L'"//
		);

		final Integer[] imgidList = { R.drawable.invisible_image, //
				R.drawable.f2l_1, R.drawable.f2l_2, R.drawable.f2l_3, //
				R.drawable.f2l_4, R.drawable.f2l_5, R.drawable.f2l_6, //
				R.drawable.f2l_7, R.drawable.f2l_8, R.drawable.f2l_9, //
				R.drawable.f2l_10, R.drawable.f2l_11, R.drawable.f2l_12, //
				R.drawable.f2l_13, R.drawable.f2l_14, R.drawable.f2l_15, //
				R.drawable.f2l_16, R.drawable.f2l_17, R.drawable.f2l_18, //
				R.drawable.f2l_19, R.drawable.f2l_20, R.drawable.f2l_21, //
				R.drawable.f2l_22, R.drawable.f2l_23, R.drawable.f2l_24, //
				R.drawable.f2l_25, R.drawable.f2l_26, R.drawable.f2l_27, //
				R.drawable.f2l_28, R.drawable.f2l_29, R.drawable.f2l_30, //
				R.drawable.f2l_31, R.drawable.f2l_32, R.drawable.f2l_33, //
				R.drawable.f2l_34, R.drawable.f2l_35, R.drawable.f2l_36, //
				R.drawable.f2l_37, R.drawable.f2l_38, R.drawable.f2l_39, //
				R.drawable.f2l_40, R.drawable.f2l_41, // F2L Above
				R.drawable.invisible_image, //
				R.drawable.oll_01, R.drawable.oll_02, R.drawable.oll_03, R.drawable.oll_04, R.drawable.oll_05, //
				R.drawable.oll_06, R.drawable.oll_07, R.drawable.oll_08, R.drawable.oll_09, R.drawable.oll_10, //
				R.drawable.oll_11, R.drawable.oll_12, R.drawable.oll_13, R.drawable.oll_14, R.drawable.oll_15, //
				R.drawable.oll_16, R.drawable.oll_17, R.drawable.oll_18, R.drawable.oll_19, R.drawable.oll_20, //
				R.drawable.oll_21, R.drawable.oll_22, R.drawable.oll_23, R.drawable.oll_24, R.drawable.oll_25, //
				R.drawable.oll_26, R.drawable.oll_27, R.drawable.oll_28, R.drawable.oll_29, R.drawable.oll_30, //
				R.drawable.oll_31, R.drawable.oll_32, R.drawable.oll_33, R.drawable.oll_34, R.drawable.oll_35, //
				R.drawable.oll_36, R.drawable.oll_37, R.drawable.oll_38, R.drawable.oll_39, R.drawable.oll_40, //
				R.drawable.oll_41, R.drawable.oll_42, R.drawable.oll_43, R.drawable.oll_44, R.drawable.oll_45, //
				R.drawable.oll_46, R.drawable.oll_47, R.drawable.oll_48, R.drawable.oll_49, R.drawable.oll_50, //
				R.drawable.oll_51, R.drawable.oll_52, R.drawable.oll_53, R.drawable.oll_54, R.drawable.oll_55, //
				R.drawable.oll_56, R.drawable.oll_57, };//

		CustomListAdapter adapter = new CustomListAdapter(getActivity(), imgidList, algsList);
		ListView list = (ListView) rootView.findViewById(R.id.algorithms_list);
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				String Slecteditem = algsList.get(+position);
				Toast.makeText(getActivity(), Slecteditem, Toast.LENGTH_SHORT).show();

			}
		});

		return rootView;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
	}

}
