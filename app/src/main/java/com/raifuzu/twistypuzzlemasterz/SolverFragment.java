package com.raifuzu.twistypuzzlemasterz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class SolverFragment extends Fragment {
	
	private Integer RED = Color.RED;
	private Integer YELLOW = Color.YELLOW;
	private Integer BLUE = Color.BLUE;
	private Integer WHITE = Color.LTGRAY;
	private Integer GREEN = Color.GREEN;
	private Integer ORANGE = Color.rgb(255, 165, 0);
	private Integer selectedColor = WHITE;
	private RubiksCubeStructure rubiksCube;
	private RubiksCubeStructure rubiksCubeProper;

	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static SolverFragment newInstance(int sectionNumber) {
		SolverFragment fragment = new SolverFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public SolverFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_solver, container, false);

		// U side of the cube
		Button[] upFace = idsToButtons(rootView, //
				R.id.cubie1, R.id.cubie2, R.id.cubie3, //
				R.id.cubie4, R.id.cubie5, R.id.cubie6, //
				R.id.cubie7, R.id.cubie8, R.id.cubie9);//
		// L side of the cube
		Button[] leftFace = idsToButtons(rootView, //
				R.id.cubie10, R.id.cubie11, R.id.cubie12, //
				R.id.cubie13, R.id.cubie14, R.id.cubie15, //
				R.id.cubie16, R.id.cubie17, R.id.cubie18);//
		// F side of the cube
		Button[] frontFace = idsToButtons(rootView, //
				R.id.cubie19, R.id.cubie20, R.id.cubie21, //
				R.id.cubie22, R.id.cubie23, R.id.cubie24, //
				R.id.cubie25, R.id.cubie26, R.id.cubie27);//
		// R side of the cube
		Button[] rightFace = idsToButtons(rootView, //
				R.id.cubie28, R.id.cubie29, R.id.cubie30, //
				R.id.cubie31, R.id.cubie32, R.id.cubie33, //
				R.id.cubie34, R.id.cubie35, R.id.cubie36);//
		// B side of the cube
		Button[] backFace = idsToButtons(rootView, //
				R.id.cubie37, R.id.cubie38, R.id.cubie39, //
				R.id.cubie40, R.id.cubie41, R.id.cubie42, //
				R.id.cubie43, R.id.cubie44, R.id.cubie45);//
		// D side the cube
		Button[] downFace = idsToButtons(rootView, //
				R.id.cubie46, R.id.cubie47, R.id.cubie48, //
				R.id.cubie49, R.id.cubie50, R.id.cubie51, //
				R.id.cubie52, R.id.cubie53, R.id.cubie54);//

		// Format: cubesideBoarderName ------------------------------- CubieIds
		// U side borders
		Button[] upBack = { backFace[2], backFace[1], backFace[0] }; // 39,38,37
		Button[] upRight = { rightFace[2], rightFace[1], rightFace[0] }; // 30,29,28
		Button[] upFront = { frontFace[2], frontFace[1], frontFace[0] }; // 21,20,19
		Button[] upLeft = { leftFace[2], leftFace[1], leftFace[0] }; // 12,11,10
		// L side borders
		Button[] leftBack = { upFace[0], upFace[3], upFace[6] }; // 1,4,7
		Button[] leftRight = { frontFace[0], frontFace[3], frontFace[6] }; // 19,22,25
		Button[] leftFront = { downFace[0], downFace[3], downFace[6] }; // 46,49,52
		Button[] leftLeft = { backFace[8], backFace[5], backFace[2] }; // 45,42,39
		// F side borders
		Button[] frontBack = { upFace[6], upFace[7], upFace[8] }; // 7,8,9
		Button[] frontRight = { rightFace[0], rightFace[3], rightFace[6] }; // 28,31,34
		Button[] frontFront = { downFace[2], downFace[1], downFace[0] }; // 48,47,46
		Button[] frontLeft = { leftFace[8], leftFace[5], leftFace[2] }; // 18,15,12
		// R side borders
		Button[] rightBack = { upFace[8], upFace[5], upFace[2] }; // 9,6,3
		Button[] rightRight = { backFace[0], backFace[3], backFace[6] }; // 37,40,43
		Button[] rightFront = { downFace[8], downFace[5], downFace[2] }; // 54,51,48
		Button[] rightLeft = { frontFace[8], frontFace[5], frontFace[2] }; // 27,24,21
		// B side borders
		Button[] backBack = { upFace[2], upFace[1], upFace[0] }; // 3,2,1
		Button[] backRight = { leftFace[0], leftFace[3], leftFace[6] }; // 10,13,16
		Button[] backFront = { downFace[6], downFace[7], downFace[8] }; // 52,53,54
		Button[] backLeft = { rightFace[8], rightFace[5], rightFace[2] }; // 36,33,30
		// D side borders
		Button[] downBack = { frontFace[6], frontFace[7], frontFace[8] }; // 25,26,27
		Button[] downRight = { rightFace[6], rightFace[7], rightFace[8] }; // 34,35,36
		Button[] downFront = { backFace[6], backFace[7], backFace[8] }; // 43,44,45
		Button[] downLeft = { leftFace[6], leftFace[7], leftFace[8] }; // 16,17,18

		// These are all of the layers as list of buttons
		AdvancedArrayList<Button[]> UFaceAndBorder = new AdvancedArrayList<Button[]>(upFace, upBack, upRight, upFront,
				upLeft);
		AdvancedArrayList<Button[]> LFaceAndBorder = new AdvancedArrayList<Button[]>(leftFace, leftBack, leftRight,
				leftFront, leftLeft);
		AdvancedArrayList<Button[]> FFaceAndBorder = new AdvancedArrayList<Button[]>(frontFace, frontBack, frontRight,
				frontFront, frontLeft);
		AdvancedArrayList<Button[]> RFaceAndBorder = new AdvancedArrayList<Button[]>(rightFace, rightBack, rightRight,
				rightFront, rightLeft);
		AdvancedArrayList<Button[]> BFaceAndBorder = new AdvancedArrayList<Button[]>(backFace, backBack, backRight,
				backFront, backLeft);
		AdvancedArrayList<Button[]> DFaceAndBorder = new AdvancedArrayList<Button[]>(downFace, downBack, downRight,
				downFront, downLeft);

		// !!!!! THIS LIST IS ONLY USED TO ADD FUNCTIONALITY TO ALL BUTTONS !!
		AdvancedArrayList<Button> allCubieButtons = new AdvancedArrayList<Button>(upFace, leftFace, frontFace,
				rightFace, backFace, downFace);

		Button color1 = (Button) rootView.findViewById(R.id.color1);
		selectColor(color1);
		Button color2 = (Button) rootView.findViewById(R.id.color2);
		selectColor(color2);
		Button color3 = (Button) rootView.findViewById(R.id.color3);
		selectColor(color3);
		Button color4 = (Button) rootView.findViewById(R.id.color4);
		selectColor(color4);
		Button color5 = (Button) rootView.findViewById(R.id.color5);
		selectColor(color5);
		Button color6 = (Button) rootView.findViewById(R.id.color6);
		selectColor(color6);

		setSelectableColors(color1, color2, color3, color4, color5, color6);
		setCubieFunctionality(allCubieButtons);

		// Dynamic Cube Data Structure
		rubiksCube = new RubiksCubeStructure(rootView, UFaceAndBorder, LFaceAndBorder, FFaceAndBorder, RFaceAndBorder,
				BFaceAndBorder, DFaceAndBorder);


		// TODO: Use this cube instead.
//		rubiksCubeProper = new RubiksCubeStructure(
//				rootView,
//				"ORYBYBRYRWWOROWGOWBGBRGGYYYOORWWBWGGBYYRRWOOGBYWGBBROG");

		// Cool pattern
		// rubiksCube.executeAlgorithm("U F B' L2 U2 L2 F' B U2 L2 U");
		Button solveButton = (Button) rootView.findViewById(R.id.solve_button);
		solveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				rubiksCube.finalizeColors();
				CubeSolver mySolver = new CubeSolver(rootView, rubiksCube);
				mySolver.solveCube();
//				displayAlert("Missing Cubies:", solutionAlg);


			}
		});

		Button setButton = (Button) rootView.findViewById(R.id.reset_button);
		setButton.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("ShowToast")
			@Override
			public void onClick(View arg0) {
				rubiksCube.resetCube();
				Toast.makeText(getActivity(), "Cube is reset!", Toast.LENGTH_SHORT).show();
			}
		});

		Button scrambleButton = (Button) rootView.findViewById(R.id.scramble_button);
		scrambleButton.setOnClickListener(new View.OnClickListener() {

			@SuppressLint("ShowToast")
			@Override
			public void onClick(View arg0) {

				// Even number
				String scrambleAlg = rubiksCube.generateScrambleAlgorithm(26);
				rubiksCube.executeAlgorithm(scrambleAlg);
				Toast.makeText(getActivity(), "Scramble Algorithm: " + scrambleAlg, Toast.LENGTH_SHORT).show();
			}
		});

		return rootView;

	}

	public void setSelectableColors(Button color1, Button color2, Button color3, Button color4, Button color5,
			Button color6) {

		color1.setBackgroundColor(RED);
		color1.setTextColor(RED);
		color2.setBackgroundColor(YELLOW);
		color2.setTextColor(YELLOW);
		color3.setBackgroundColor(BLUE);
		color3.setTextColor(BLUE);
		color4.setBackgroundColor(WHITE);
		color4.setTextColor(WHITE);
		color5.setBackgroundColor(GREEN);
		color5.setTextColor(GREEN);
		color6.setBackgroundColor(ORANGE);
		color6.setTextColor(ORANGE);
	}

	public Button[] idsToButtons(View rootView, Integer... ids) {

		Button[] newButtonList = new Button[ids.length];

		for (int i = 0; i < ids.length; i++) {
			Button newButton = (Button) rootView.findViewById(ids[i]);
			newButtonList[i] = newButton;
		}

		return newButtonList;
	}

	public void selectColor(final Button button) {

		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String name = "";

				if (button.getId() == R.id.color1) {
					selectedColor = RED;
					name = "red";
				}
				else if (button.getId() == R.id.color2) {
					selectedColor = YELLOW;
					name = "yellow";
				}else if (button.getId() == R.id.color3) {
					selectedColor = BLUE;
					name = "blue";
				}else if (button.getId() == R.id.color4) {
					selectedColor = WHITE;
					name = "white";
				}else if (button.getId() == R.id.color5) {
					selectedColor = GREEN;
					name = "green";
				}else if (button.getId() == R.id.color6) {
					selectedColor = ORANGE;
					name = "orange";
				}

				Toast.makeText(getActivity(), "COLOR: " + name, Toast.LENGTH_LONG).show();
			}
		});
	}

	public void clickedFunction(final Button view) {
		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				view.setBackgroundColor(selectedColor);
				view.setTextColor(selectedColor);
			}
		});
	}

	public void setCubieFunctionality(AdvancedArrayList<Button> allCubieButtons) {
		for (int i = 0; i < allCubieButtons.size(); i++) {
			Button newButton = allCubieButtons.get(i);
			clickedFunction(newButton);
		}
	}

	public void displayAlert(String title, String message) {
		new AlertDialog.Builder(getActivity()).setTitle(title).setMessage(message)
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// continue with delete
					}
				}).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// do nothing
					}
				}).setIcon(android.R.drawable.ic_dialog_alert).show();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
	}

}
