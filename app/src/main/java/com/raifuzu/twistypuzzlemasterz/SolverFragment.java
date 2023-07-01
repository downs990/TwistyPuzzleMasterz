package com.raifuzu.twistypuzzlemasterz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;


public class SolverFragment extends Fragment {

	private Integer selectedColor = RubiksCube.WHITE;
	private RubiksCubeStructure rubiksCube;

	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";


	// TODO: Reset doesn't clear this scrambled cubeAsString for some reason???
	private String cubeAsString = "ORYBYBRYRWWOROWGOWBGBRGGYYYOORWWBWGGBYYRRWOOGBYWGBBROG";
//private String cubeAsString = "YYYYYYYYYOOOOOOOOOGGGGGGGGGWWWWWWWWWRRRRRRRRRBBBBBBBBB";






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



		final AdvancedArrayList<Button[]> surfacesAsButtonLists = new AdvancedArrayList<>();
		surfacesAsButtonLists.addMultiple(upFace, leftFace,frontFace,rightFace,backFace,downFace);

		final AdvancedArrayList<Button> allSurfaceButtons = new AdvancedArrayList<>(upFace, leftFace, frontFace,
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
		setButtonsFunctionality(allSurfaceButtons);


		final TextView debuggingText = (TextView)rootView.findViewById(R.id.debugging_textview);

		initRubiksCube(rootView, surfacesAsButtonLists);






		Button solveButton = (Button) rootView.findViewById(R.id.solve_button);
		solveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// TODO: Only needed when you want to init cube by setting colors on interface manually.
				// Grabs the current colors from the interface buttons.
				// This needs to be in the solve listener for the case of the user setting colors manually on phone.
//				initRubiksCube(rootView,UFaceAndBorder, LFaceAndBorder, FFaceAndBorder, RFaceAndBorder, BFaceAndBorder, DFaceAndBorder);



				// Solve cube
				CubeSolver mySolver = new CubeSolver(rootView, rubiksCube);
				mySolver.solveCube();

				String entireSolution = rubiksCube.getSolutionAlgorithm();

//				displayAlert("Solution Algorithm:", rubiksCube.toString());

				// Show solve algorithm
				String currentDisplayingText = debuggingText.getText().toString();
				String newMessage = currentDisplayingText + "\nSolution: " + entireSolution;
				debuggingText.setText(newMessage);

				// Update buttons to match the cube's state
				setButtonsToCurrentCube(surfacesAsButtonLists);



				// TODO: Use for testing
				// displayAlert("Solution Algorithm:", entireSolution);
				// Toast.makeText(getActivity(), "Cube is reset!", Toast.LENGTH_SHORT).show();


				// TODO: Remove after testing
//				 String x = rubiksCube.test("R");
//				 displayAlert("All Cubies - Right:", x);


			}
		});

		Button resetButton = (Button) rootView.findViewById(R.id.reset_button);
		resetButton.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("ShowToast")
			@Override
			public void onClick(View arg0) {

				cubeAsString = "YYYYYYYYYOOOOOOOOOGGGGGGGGGWWWWWWWWWRRRRRRRRRBBBBBBBBB";
				initRubiksCube(rootView, surfacesAsButtonLists);

//				rubiksCube.resetCube();
				String message = "Cube is reset!";
				debuggingText.setText(message);

//				setButtonsToCurrentCube(surfacesAsButtonLists);


				// TODO: Remove this after testing !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//				rubiksCube.executeAlgorithm("R U R'", RubiksCube.RecordAlgorithm.NO);
//				setButtonsToCurrentCube(surfacesAsButtonLists);
				// TODO: !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


			}
		});


		Button scrambleButton = (Button) rootView.findViewById(R.id.scramble_button);
		scrambleButton.setOnClickListener(new View.OnClickListener() {

			@SuppressLint("ShowToast")
			@Override
			public void onClick(View arg0) {

				// Even number
				String scrambleAlg = rubiksCube.generateScrambleAlgorithm(18);
				rubiksCube.executeAlgorithm(scrambleAlg , RubiksCube.RecordAlgorithm.NO );

 				String text = "Scramble: " + scrambleAlg;
				debuggingText.setText(text);

				setButtonsToCurrentCube(surfacesAsButtonLists);
			}
		});

		return rootView;

	}

	private void setButtonsToCurrentCube(AdvancedArrayList<Button[]> layerList){
		AdvancedArrayList<Integer[]> surfaceColors = new AdvancedArrayList<>();

		ArrayList<CubeLayer> allCubeLayers = rubiksCube.getLayersList();
		for (CubeLayer layer : allCubeLayers){
			surfaceColors.add( layer.getSurfaceColors() ) ;
		}

		// Update the interface buttons with solution algorithm movements
		updateButtonsWithColors(surfaceColors, layerList);
	}


	private void initRubiksCube(View rootView, AdvancedArrayList<Button[]> layerList){

		// Getting all colors from all buttons.
//		initColorsFromButtons(rootView, layerList);

		// Top: B ; Front: Y ; ORYBYBRYR                      O | R | Y
		// Top: Y ; Front: O ; WWOROWGOW                     -----------
		// Top: Y ; Front: G ; BGBRGGYYY                      B | Y | B
		// Top: G ; Front: W ; OORWWBWGG                     -----------
		// Top: Y ; Front: R ; BYYRRWOOG                      R | Y | R
		// Top: Y ; Front: B ; BYWGBBROG


		rubiksCube = new RubiksCubeStructure(rootView, cubeAsString);
		setButtonsToCurrentCube(layerList);
	}

	/**
	 * Precondition - this.allColorsList and layersList parameter
	 * 		MUST BE EXACT ONE TO ONE MATCH
	 *
	 * @param layerList list of all interface buttons
	 * @param colors list of all surface colors to set for those buttons.
	 */
	private void updateButtonsWithColors(AdvancedArrayList<Integer[]> colors,
										 AdvancedArrayList<Button[]> layerList){

		for(int i = 0; i < layerList.size(); i++){

			Button[] layer = layerList.get(i);
			Integer[] surfaceColorIndexes = colors.get(i);

			for(int k = 0; k < layer.length; k++){
				Button currentButton = layer[k];
				Integer currentColorIndex = surfaceColorIndexes[k];

				// Converting indexes back to colors.
				char colorLetter = rubiksCube.getCubeAsString().charAt(currentColorIndex - 1);
				Integer colorAsInteger = RubiksCubeStructure.colorLetterToIntegerColor(colorLetter);

				currentButton.setBackgroundColor(colorAsInteger);
				currentButton.setTextColor(colorAsInteger);
			}
		}

	}



	// TODO: Keep this if you still want to be able to setup a cube by clicking stickers on screen
	@SafeVarargs
	private final void initColorsFromButtons(View rootView, AdvancedArrayList<Button[]>... layerList){


//		for(int i = 0; i < layerList.length; i++){
//
//			Integer[] colorsOfLayer = surfacesColorsLists.get(i);
//			AdvancedArrayList<Button[]> layersButtons = layerList[i];
//			for(Button[] buttonsList : layersButtons){
//
//				Integer[] colorsList = new Integer[buttonsList.length];
//				for(int j = 0; j < buttonsList.length; j++){
//
//					Button button = buttonsList[j];
//					int currentButtonColor = button.getCurrentTextColor();
//					colorsList[j] = currentButtonColor;
//				}
//
//				colorsOfLayer.add(colorsList);
//			}
//		}


	}

	public void setSelectableColors(Button color1, Button color2, Button color3, Button color4, Button color5,
			Button color6) {

		color1.setBackgroundColor(RubiksCube.RED);
		color1.setTextColor(RubiksCube.RED);
		color2.setBackgroundColor(RubiksCube.YELLOW);
		color2.setTextColor(RubiksCube.YELLOW);
		color3.setBackgroundColor(RubiksCube.BLUE);
		color3.setTextColor(RubiksCube.BLUE);
		color4.setBackgroundColor(RubiksCube.WHITE);
		color4.setTextColor(RubiksCube.WHITE);
		color5.setBackgroundColor(RubiksCube.GREEN);
		color5.setTextColor(RubiksCube.GREEN);
		color6.setBackgroundColor(RubiksCube.ORANGE);
		color6.setTextColor(RubiksCube.ORANGE);
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


		// TODO: Update this to use RubiksCube.colorsMap instead of more hardcoded strings.
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String name = "";

				if (button.getId() == R.id.color1) {
					selectedColor = RubiksCube.RED;
					name = "red";
				}
				else if (button.getId() == R.id.color2) {
					selectedColor = RubiksCube.YELLOW;
					name = "yellow";
				}else if (button.getId() == R.id.color3) {
					selectedColor = RubiksCube.BLUE;
					name = "blue";
				}else if (button.getId() == R.id.color4) {
					selectedColor = RubiksCube.WHITE;
					name = "white";
				}else if (button.getId() == R.id.color5) {
					selectedColor = RubiksCube.GREEN;
					name = "green";
				}else if (button.getId() == R.id.color6) {
					selectedColor = RubiksCube.ORANGE;
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

	public void setButtonsFunctionality(AdvancedArrayList<Button> allCubieButtons) {
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
