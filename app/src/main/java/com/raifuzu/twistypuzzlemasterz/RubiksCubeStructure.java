package com.raifuzu.twistypuzzlemasterz;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// THERE IS NO OTHER WAY TO GET THE BACKGROUND COLOR OF A BUTTON.
//
// Example:
// button.setTextColor(color1); button.setBackgroundColor(color1);
// button.getCurrentTextColor();

public class RubiksCubeStructure implements RubiksCube {

	private CubeLayer up, left, front, right, back, down;
	AdvancedArrayList<CubeLayer> rubiksCube = new AdvancedArrayList<>();
	private View rootView;

	@SafeVarargs
	public RubiksCubeStructure(View rootView, AdvancedArrayList<Button[]>... layerList) {

		// Initialize rootView for setting up buttons
		this.rootView = rootView;

		// Gives each layer it's buttons
		up = new CubeLayer(rootView, layerList[0], Side.U);
		left = new CubeLayer(rootView, layerList[1], Side.L);
		front = new CubeLayer(rootView, layerList[2], Side.F);
		right = new CubeLayer(rootView, layerList[3], Side.R);
		back = new CubeLayer(rootView, layerList[4], Side.B);
		down = new CubeLayer(rootView, layerList[5], Side.D);

		rubiksCube.addMultiple(up, left, front, right, back, down);
		resetCube();
	}


	@Override
	public ArrayList<CubeLayer> findLocationOfCubie(Color[] stickers) {
		return null;
	}

	@Override
	public String getCubeOrientation() {
		String result = "UP: " + up.getCenterColor() + "RIGHT: " + right.getCenterColor() + "FRONT: "
				+ front.getCenterColor();
		return result;
	}

	/**
	 * Puts the colors on the cube into a solved state.
	 */
	@Override
	public void resetCube() {

		// Valid representation.
		//                       U ,     L  ,   F ,    R  ,    B  ,    D
		//	 			      { RED, YELLOW, BLUE, WHITE, GREEN, ORANGE };

//		up.initializeSolvedColors(RED, GREEN, WHITE, BLUE, YELLOW);
//		left.initializeSolvedColors(YELLOW, RED, BLUE, ORANGE, GREEN);
//		front.initializeSolvedColors(BLUE, RED, WHITE, ORANGE, YELLOW);
//		right.initializeSolvedColors(WHITE, RED, GREEN, ORANGE, BLUE);
//		back.initializeSolvedColors(GREEN, RED, YELLOW, ORANGE, WHITE);
//		down.initializeSolvedColors(ORANGE, BLUE, WHITE, GREEN, YELLOW);

		// Valid representation.
		//                       U ,     L  ,   F ,    R  ,    B  ,    D
        //				     { YELLOW, ORANGE, BLUE, RED, GREEN, WHITE };
		up.initializeSolvedColors(YELLOW, GREEN, RED, BLUE, ORANGE);
		left.initializeSolvedColors(ORANGE, YELLOW, BLUE, WHITE, GREEN);
		front.initializeSolvedColors(BLUE, YELLOW, RED, WHITE, ORANGE);
		right.initializeSolvedColors(RED, YELLOW, GREEN, WHITE, BLUE);
		back.initializeSolvedColors(GREEN, YELLOW, ORANGE, WHITE, RED);
		down.initializeSolvedColors(WHITE, BLUE, RED, GREEN, ORANGE);

	}

	@Override
	public void executeAlgorithm(String algorithm) {
		// Example Algorithm: R' D' R D

		String[] moves = algorithm.split(" ");
		for (int i = 0; i < moves.length; i++) {

			String individualMove = moves[i];
			String actualMove = individualMove.replace("'", "");
			actualMove.trim();

			Rotation direction = Rotation.CLOCKWISE;
			if (!individualMove.equals(actualMove)) {
				// inverted move
				direction = Rotation.COUNTER_CLOCKWISE;
			}

			if (individualMove.contains("2")) {
				// Actual rotation and UI update
				actualMove = individualMove.replace("2", "");
				CubeLayer currentLayer = findCubeLayer(actualMove);
				// Rotate 2 times
				rotate(currentLayer, direction);
				rotate(currentLayer, direction);
			} else {
				// Actual rotation and UI update
				CubeLayer currentLayer = findCubeLayer(actualMove);
				rotate(currentLayer, direction);// Moves colors on structure
			}

		}
	}

	private CubeLayer findCubeLayer(String actualMove) {
		CubeLayer resultLayer = null;
		for (int i = 0; i < rubiksCube.size(); i++) {
			Side sideName = rubiksCube.get(i).getSideName();

			if (sideName.toString().equals(actualMove)) {
				resultLayer = rubiksCube.get(i);
				break;
			}
		}

		return resultLayer;
	}

	@Override
	public void rotate(CubeLayer layerToRotate, Rotation directionOfRotation) {

		if (layerToRotate.equals(null)) {
			// This means that the actualMove variable in the executeAlgorithm
			// method was invalid. One of the moves in the algorithm did not
			// match any moves in the Side enumeration. This should never
			// happen.
			Toast.makeText(rootView.getContext(), "Layer to rotate is null", Toast.LENGTH_LONG).show();
		} else {

			if (directionOfRotation == Rotation.CLOCKWISE) {
				rotateClockwise(layerToRotate);
			} else {
				rotateCounterClockwise(layerToRotate);
			}
		}
	}

	private AdvancedArrayList<Integer> buttonsToColors(Button[] buttons) {
		AdvancedArrayList<Integer> colors = new AdvancedArrayList<>();

		for (int buttonIndex = 0; buttonIndex < buttons.length; buttonIndex++) {
			colors.add(buttons[buttonIndex].getCurrentTextColor());
		}

		return colors;
	}

	private Integer getStickerColor(CubeLayer layer, int index) {
		Button button = layer.getSurfaceButtons()[index];
		Integer color = button.getCurrentTextColor();
		return color;
	}

	@SuppressWarnings("unchecked")
	private void rotateClockwise(CubeLayer layer) {

		// Different from white board picture
		AdvancedArrayList<Integer> newSurfaceList = new AdvancedArrayList<Integer>(getStickerColor(layer, 6),
				getStickerColor(layer, 3), getStickerColor(layer, 0), getStickerColor(layer, 7),
				getStickerColor(layer, 4), getStickerColor(layer, 1), getStickerColor(layer, 8),
				getStickerColor(layer, 5), getStickerColor(layer, 2));

		// Edges and Corners
		AdvancedArrayList<Integer> newsurfaceBack = new AdvancedArrayList<Integer>(
				buttonsToColors(layer.getSurfaceLeftButtons()));
		AdvancedArrayList<Integer> newsurfaceRight = new AdvancedArrayList<Integer>(
				buttonsToColors(layer.getSurfaceBackButtons()));
		AdvancedArrayList<Integer> newsurfaceFront = new AdvancedArrayList<Integer>(
				buttonsToColors(layer.getSurfaceRightButtons()));
		AdvancedArrayList<Integer> newsurfaceLeft = new AdvancedArrayList<Integer>(
				buttonsToColors(layer.getSurfaceFrontButtons()));

		layer.initializeScrambledColors(newSurfaceList, newsurfaceBack, newsurfaceRight, newsurfaceFront,
				newsurfaceLeft);

	}

	@SuppressWarnings("unchecked")
	private void rotateCounterClockwise(CubeLayer layer) {
		// Different from white board picture
		AdvancedArrayList<Integer> newSurfaceList = new AdvancedArrayList<Integer>(getStickerColor(layer, 2),
				getStickerColor(layer, 5), getStickerColor(layer, 8), getStickerColor(layer, 1),
				getStickerColor(layer, 4), getStickerColor(layer, 7), getStickerColor(layer, 0),
				getStickerColor(layer, 3), getStickerColor(layer, 6));

		// Edges and Corners
		AdvancedArrayList<Integer> newsurfaceBack = new AdvancedArrayList<Integer>(
				buttonsToColors(layer.getSurfaceRightButtons()));
		AdvancedArrayList<Integer> newsurfaceRight = new AdvancedArrayList<Integer>(
				buttonsToColors(layer.getSurfaceFrontButtons()));
		AdvancedArrayList<Integer> newsurfaceFront = new AdvancedArrayList<Integer>(
				buttonsToColors(layer.getSurfaceLeftButtons()));
		AdvancedArrayList<Integer> newsurfaceLeft = new AdvancedArrayList<Integer>(
				buttonsToColors(layer.getSurfaceBackButtons()));

		layer.initializeScrambledColors(newSurfaceList, newsurfaceBack, newsurfaceRight, newsurfaceFront,
				newsurfaceLeft);
	}


	// Size must always be even (26)
	public String generateScrambleAlgorithm(int size) {

		String output = "";
		for (int i = 0; i < size; i++) {
			String newSymbol = generateRandomSymbol();
			if (i == size - 1) {
				output += newSymbol;
			} else {
				output += newSymbol + " ";
			}
		}

		return improveScrambleAlg(output);
	}

	private String generateRandomSymbol() {
		String notation3x3 = "U' F L' D F' U2 B R B' F2 R' R2 U L D' B2 D2 L2";
		String[] movesFor3x3 = notation3x3.split(" ");
		int min = 0;
		int max = movesFor3x3.length - 1;

		// Generates random numbers between minutes and max,
		// inclusive on both ends
		int randomMove = min + (int) (Math.random() * (max - (min - 1)));
		return movesFor3x3[randomMove];
	}

	// TODO - if you want you could use a regular expression instead of if
	// statements to generate your scramble algs. 

	// remove all occurrences of exactly 2 identical symbols that are
	// adjacent.
	private String improveScrambleAlg(String initialAlg) {
		String[] movesFor3x3 = initialAlg.split(" ");
		AdvancedArrayList<String> cubeSymbols = new AdvancedArrayList<String>(movesFor3x3);

		for (int i = 0; i < cubeSymbols.size() - 2; i += 2) {
			String symbol1 = cubeSymbols.get(i);
			String symbol2 = cubeSymbols.get(i + 1);
			String symbol3 = cubeSymbols.get(i + 2);

			// CharAt(0) Gets the letter value of the move
			// Checks the following: 0 (1 2) (3 4) (5 6) (7 8) 9
			// and
			// Checks the following: (0 1) (2 3) (4 5) (6 7) (8 9)
			while (symbol1.charAt(0) == symbol2.charAt(0) || symbol2.charAt(0) == symbol3.charAt(0)) {
				symbol2 = generateRandomSymbol();
				cubeSymbols.remove(i + 1);
				cubeSymbols.add(i + 1, symbol2);
			}

			// Checks the last two moves of the algorithm
			while (cubeSymbols.get(cubeSymbols.size() - 1).charAt(0) == (cubeSymbols.get(cubeSymbols.size() - 2))
					.charAt(0)) {
				String newLastSymbol = generateRandomSymbol();
				cubeSymbols.add(cubeSymbols.size() - 1, newLastSymbol);
			}
		}

		return cubeSymbols.toString();
	}


    /**
     * The purpose of this method is to initialize all of the colors for
     * each cubie in each layer of the cube. This can not be done in any
     * constructor therefore it was made into a seperate method that can
     * be triggered when desired.
     */
	public void finalizeColors(){
		for (CubeLayer layer: rubiksCube) {
			layer.setAllCubieColors();
		}
	}

	public AdvancedArrayList<CubeLayer> getLayersList(){
		return this.rubiksCube;
	}

	@Override
	public String toString() {
		String output = "";

		for (int i = 0; i < rubiksCube.size(); i++) {
			output += rubiksCube.get(i).toString();
		}

		return output;
	}
}
