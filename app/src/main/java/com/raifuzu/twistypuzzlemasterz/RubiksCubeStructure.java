package com.raifuzu.twistypuzzlemasterz;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.raifuzu.twistypuzzlemasterz.CubeLayer.Cubie;



public class RubiksCubeStructure implements RubiksCube {

	private final CubeLayer up;
	private final CubeLayer left;
	private final CubeLayer front;
	private final CubeLayer right;
	private final CubeLayer back;
	private final CubeLayer down;

	AdvancedArrayList<CubeLayer> rubiksCube = new AdvancedArrayList<>();



	private String validCubieDisplay = "";
	private ArrayList<Cubie> validCubiesFoundList = new ArrayList<>();
	private ArrayList<Cubie> invalidCubiesFoundList = new ArrayList<>();
	private ArrayList<String> missingCubiesList = new ArrayList<>();




	private final View rootView;


	// TODO: Figure out how to remove buttons from this implementation.
	// 		( However, I'd like to keep this in as an optional enable/disable. )
	public RubiksCubeStructure(View rootView, String initialCubeState){
		this.rootView = rootView;

		ArrayList<String> surfaces = new ArrayList<>();

		int start = 0;
		int end = 0;
		for(int i = 0; i <= initialCubeState.length(); i++){
			if(i % 9 == 0 && i > 0){
				end = i;
				start = end - 9;

				// TODO: Make sure you're using the correct cube orientations that match
				// 		the opencv app.

				// Take 9 chars from initialCubeState at a time, left to right.
				String currentSurface = initialCubeState.substring(start, end);
				surfaces.add( currentSurface );
			}
		}

		// TODO: Update CubeLayer constructor to init layers from string of colors
		up = new CubeLayer(rootView, 	surfaces.get(0) );
		left = new CubeLayer(rootView,  surfaces.get(0) );
		front = new CubeLayer(rootView, surfaces.get(0) );
		right = new CubeLayer(rootView, surfaces.get(0) );
		back = new CubeLayer(rootView,  surfaces.get(0) );
		down = new CubeLayer(rootView,  surfaces.get(0) );
		rubiksCube.addMultiple(up, left, front, right, back, down);

	}

	@SafeVarargs
	public RubiksCubeStructure(View rootView, AdvancedArrayList<Button[]>... layerList) {

		// Initialize rootView for setting up buttons
		this.rootView = rootView;

		up = new CubeLayer(rootView, layerList[0], SurfaceName.U);
		left = new CubeLayer(rootView, layerList[1], SurfaceName.L);
		front = new CubeLayer(rootView, layerList[2], SurfaceName.F);
		right = new CubeLayer(rootView, layerList[3], SurfaceName.R);
		back = new CubeLayer(rootView, layerList[4], SurfaceName.B);
		down = new CubeLayer(rootView, layerList[5], SurfaceName.D);

		rubiksCube.addMultiple(up, left, front, right, back, down);
		resetCube();
	}

	/**
	 * The purpose of this method is to find the exact location of a cubie
	 * by just searching for it's unique colors in the whole cube. This will
	 * be helpful for determining if the cubie is on the top, middle, or down
	 * level of the cube.
	 *
	 * @param stickers - Array of stickers is the cubies unique identifier
	 * @return - List of Layers that contains that cubie. The intersection of those layers is
	 * the cubie's location.
	 */
	@Override
	public ArrayList<SurfaceName> findLocationOfCubie(List<Integer> stickers) {

		ArrayList<SurfaceName> intersectingSurfaces = new ArrayList<>();

		for(CubeLayer layer : rubiksCube){
			boolean successfullyFoundCubie = false;
			for(Cubie cubie : layer.myCubies){

				// Same type of cubie (i.e Edge, Corner, Center)
				if(stickers.size() == cubie.stickerColorsList.size()){
					ArrayList<Boolean> foundList = new ArrayList<>();

					for(Integer color : stickers){
						boolean stickerFound = cubie
								.stickerColorsList
								.contains(color);

						foundList.add(stickerFound);
					}
					successfullyFoundCubie = !foundList.contains(false);

					if(successfullyFoundCubie){
						intersectingSurfaces.add(layer.getSideName());
					}
				}
			}
		}
		return intersectingSurfaces;
	}



	@Override
	public String getCubeOrientation() {
		return "UP: " + up.getCenterColor() + "RIGHT: " + right.getCenterColor() + "FRONT: "
				+ front.getCenterColor();
	}

	/**
	 * Puts the colors on the cube into a solved state.
	 */
	@Override
	public void resetCube() {

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
		for (String individualMove : moves) {

			String actualMove = individualMove.replace("'", "");
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
			SurfaceName surfaceName = rubiksCube.get(i).getSideName();

			if (surfaceName.toString().equals(actualMove)) {
				resultLayer = rubiksCube.get(i);
				break;
			}
		}

		return resultLayer;
	}

	@Override
	public void rotate(CubeLayer layerToRotate, Rotation directionOfRotation) {

		if (layerToRotate == null) {
			// This means that the actualMove variable in the executeAlgorithm
			// method was invalid. One of the moves in the algorithm did not
			// match any moves in the Side enumeration.
			Toast.makeText(rootView.getContext(),
					"Invalid algorithm notation character. Can't find layer",
					Toast.LENGTH_LONG).show();
		} else {

			if (directionOfRotation == Rotation.CLOCKWISE) {
				rotateClockwise(layerToRotate);
			} else {
				rotateCounterClockwise(layerToRotate);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void rotateClockwise(CubeLayer layer) {

		// Different from white board picture
		AdvancedArrayList<Integer> newSurfaceList = new AdvancedArrayList<Integer>(getStickerColor(layer, 6),
				getStickerColor(layer, 3), getStickerColor(layer, 0), getStickerColor(layer, 7),
				getStickerColor(layer, 4), getStickerColor(layer, 1), getStickerColor(layer, 8),
				getStickerColor(layer, 5), getStickerColor(layer, 2));

		// Edges and Corners
		AdvancedArrayList<Integer> newSurfaceBack = new AdvancedArrayList<>(
				buttonsToColors(layer.getSurfaceLeftButtons()));
		AdvancedArrayList<Integer> newSurfaceRight = new AdvancedArrayList<>(
				buttonsToColors(layer.getSurfaceBackButtons()));
		AdvancedArrayList<Integer> newSurfaceFront = new AdvancedArrayList<>(
				buttonsToColors(layer.getSurfaceRightButtons()));
		AdvancedArrayList<Integer> newSurfaceLeft = new AdvancedArrayList<>(
				buttonsToColors(layer.getSurfaceFrontButtons()));

		layer.initializeScrambledColors(newSurfaceList, newSurfaceBack, newSurfaceRight, newSurfaceFront,
				newSurfaceLeft);

	}

	@SuppressWarnings("unchecked")
	private void rotateCounterClockwise(CubeLayer layer) {
		// Different from white board picture
		AdvancedArrayList<Integer> newSurfaceList = new AdvancedArrayList<Integer>(getStickerColor(layer, 2),
				getStickerColor(layer, 5), getStickerColor(layer, 8), getStickerColor(layer, 1),
				getStickerColor(layer, 4), getStickerColor(layer, 7), getStickerColor(layer, 0),
				getStickerColor(layer, 3), getStickerColor(layer, 6));

		// Edges and Corners
		AdvancedArrayList<Integer> newSurfaceBack = new AdvancedArrayList<>(
				buttonsToColors(layer.getSurfaceRightButtons()));
		AdvancedArrayList<Integer> newSurfaceRight = new AdvancedArrayList<>(
				buttonsToColors(layer.getSurfaceFrontButtons()));
		AdvancedArrayList<Integer> newSurfaceFront = new AdvancedArrayList<>(
				buttonsToColors(layer.getSurfaceLeftButtons()));
		AdvancedArrayList<Integer> newSurfaceLeft = new AdvancedArrayList<>(
				buttonsToColors(layer.getSurfaceBackButtons()));

		layer.initializeScrambledColors(newSurfaceList, newSurfaceBack, newSurfaceRight, newSurfaceFront,
				newSurfaceLeft);
	}

	private AdvancedArrayList<Integer> buttonsToColors(Button[] buttons) {
		AdvancedArrayList<Integer> colors = new AdvancedArrayList<>();

		for (Button button : buttons) {
			colors.add(button.getCurrentTextColor());
		}

		return colors;
	}

	private Integer getStickerColor(CubeLayer layer, int index) {
		Button button = layer.getSurfaceButtons()[index];
		return button.getCurrentTextColor();
	}

	// Size must always be even (26)
	public String generateScrambleAlgorithm(int size) {

		StringBuilder output = new StringBuilder();
		for (int i = 0; i < size; i++) {
			String newSymbol = generateRandomSymbol();
			if (i == size - 1) {
				output.append(newSymbol);
			} else {
				output.append(newSymbol).append(" ");
			}
		}

		return improveScrambleAlg(output.toString());
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
	// 	statements to generate your scramble algorithm.

	// remove all occurrences of exactly 2 identical symbols that are
	// adjacent.
	private String improveScrambleAlg(String initialAlg) {
		String[] movesFor3x3 = initialAlg.split(" ");
		AdvancedArrayList<String> cubeSymbols = new AdvancedArrayList<>(movesFor3x3);

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
     * constructor therefore it was made into a separate method that can
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












	private Integer convertToIntegerColor(String stringColor) {

		Map<String, Integer> colorsMap = RubiksCube.colorsMap;
		return colorsMap.get(stringColor);
	}

	/**
	 * This method converts a string of colors names into an actual list of
	 * color pairs of type Integer.
	 *
	 * @param colorString
	 * @return
	 */
	private ArrayList<ArrayList<Integer>> createColorPairs(String colorString) {

		ArrayList<ArrayList<Integer>> resultColorPairs = new ArrayList<>();

		String[] individualCubie = colorString.split(",");
		for (String cubie : individualCubie) {
			cubie = cubie.trim();
			String[] listOfStickers = cubie.split(" ");
			ArrayList<Integer> integerColorPair = new ArrayList<>();
			for (String sticker : listOfStickers) {
				Integer integerColor = convertToIntegerColor(sticker);
				integerColorPair.add(integerColor);
			}
			resultColorPairs.add(integerColorPair);
		}

		return resultColorPairs;
	}

	private void getValidCubies(ArrayList<ArrayList<Integer>> validCubies, CubieType validType) {

		final Integer INVALID_STICKER = 999;

		for (ArrayList<Integer> validStickers : validCubies) {
			for (CubeLayer layer : this.getLayersList()) {
				for (Cubie cubie : layer.getAllCubies()) {

					CubieType cubieType = cubie.getCubieType();
					ArrayList<Integer> cubieStickers = cubie.getStickerColors();

					//NOTE: this method finds all of the pieces that are valid.
					if ((validType == cubieType) && cubieStickers.containsAll(validStickers)) {
						validStickers.add(INVALID_STICKER);
						validCubiesFoundList.add(cubie);
						break;
					}
				}
			}

			validStickers.remove(INVALID_STICKER);
		}
	}

	private void getInvalidCubies(ArrayList<ArrayList<Integer>> validCubies, CubieType validType) {
		//final Integer INVALID_STICKER = 999;

		//NOTE: this method finds all of the pieces that are invalid.
		for (CubeLayer layer : this.getLayersList()) {
			for (Cubie cubie : layer.getAllCubies()) {

				ArrayList<Integer> cubieStickers = cubie.getStickerColors();
				boolean isValidPiece = false;
				int index = 0;
				while (index < validCubies.size() && !isValidPiece) {
					ArrayList<Integer> validCubie = validCubies.get(index);
					//validCubie.remove(INVALID_STICKER);

					if ((validType == cubie.getCubieType())
							&& cubieStickers.containsAll(validCubie)) {
						isValidPiece = true;
					}
					//Different types automatically go through here because there is no way to tell yet.
					if (validType != cubie.getCubieType()) {
						isValidPiece = true;
					}
					index++;
				}

				//This cubie was not found in the valid cubie list
				if (!isValidPiece) {
					invalidCubiesFoundList.add(cubie);
					validCubies.add(cubie.getStickerColors());//TODO- figure out where to remove this?
				}

			}
		}
	}

	private String arrayListToCubieString(ArrayList<Integer> cubieStickers) {
		String cubieString = "";

		switch (cubieStickers.size()) {
			case 1:
				cubieString += CubieType.CENTER + ": ";
				break;
			case 2:
				cubieString += CubieType.EDGE + ": ";
				break;
			case 3:
				cubieString += CubieType.CORNER + ": ";
				break;
			default:
				break;
		}

		for (Integer color : cubieStickers) {
			String colorStr = CubeLayer.colorIntToString(color);
			cubieString += colorStr + " ";
		}

		return cubieString;
	}

	private void getMissingCubies(ArrayList<ArrayList<Integer>> validPieces, ArrayList<Cubie> validCubiesFound) {

		for (ArrayList<Integer> piece : validPieces) {

			int index = 0;
			boolean found = false;
			while ((index < validCubiesFound.size()) && !found) {
				ArrayList<Integer> cubieStickers = validCubiesFound.get(index).getStickerColors();

				//If type is same
				if (piece.size() == validCubiesFound.get(index).getStickerColors().size()) {
					if (cubieStickers.containsAll(piece)) {
						found = true;
					}
				}

				index++;
			}

			if (!found) {
				missingCubiesList.add(arrayListToCubieString(piece));
			}
		}
	}

	public boolean isValidState() {
		boolean isValid = false;
		String validEdgesStr = "RED WHITE,RED BLUE,RED YELLOW,RED GREEN,"// Top
				+ "BLUE WHITE,BLUE YELLOW,YELLOW GREEN,GREEN WHITE, "// Middle
				+ "ORANGE WHITE, ORANGE BLUE, ORANGE YELLOW, ORANGE GREEN";// Bottom

		String validCornersStr = "RED WHITE BLUE, RED BLUE YELLOW, RED YELLOW GREEN, RED GREEN WHITE,"// Top
				+ "BLUE WHITE ORANGE, BLUE ORANGE YELLOW, YELLOW ORANGE GREEN, GREEN ORANGE WHITE";// Bottom

		String validCentersStr = "RED, WHITE, BLUE, ORANGE, YELLOW, GREEN";// All

		ArrayList<ArrayList<Integer>> validEdges = createColorPairs(validEdgesStr);
		ArrayList<ArrayList<Integer>> validCorners = createColorPairs(validCornersStr);
		ArrayList<ArrayList<Integer>> validCenters = createColorPairs(validCentersStr);

		//NOTE: Initializes validCubiesFoundList
		getValidCubies(validEdges, CubieType.EDGE);
		getValidCubies(validCorners, CubieType.CORNER);
		getValidCubies(validCenters, CubieType.CENTER);

		//NOTE: Initializes missingCubiesList
		getMissingCubies(validEdges, validCubiesFoundList);
		getMissingCubies(validCorners, validCubiesFoundList);
		getMissingCubies(validCenters, validCubiesFoundList);

		final int CORRECT_PIECES = 26;
		validCubieDisplay += "Valid Cubies: " + validCubiesFoundList.size() + " of " + CORRECT_PIECES + "\n\n";

		//NOTE: If you want to use validEdges, validCorners, or validCenters below this code then
		//  you might need to clear them and recreate them because after getInvalidCubies() uses them
		//  it modifies their values.
		getInvalidCubies(validEdges, CubieType.EDGE);
		getInvalidCubies(validCorners, CubieType.CORNER);
		getInvalidCubies(validCenters, CubieType.CENTER);

		//NOTE: change to
		//   - validCubiesFoundList<Cubie>
		//   - invalidCubiesFoundList<Cubie>
		//   - missingCubiesList<String>
		for (String cubie : missingCubiesList) {
			validCubieDisplay += cubie + "\n";//TODO- add .toString() if using Cubie Object
		}

		if (validCubiesFoundList.size() == CORRECT_PIECES) {
			isValid = true;
		}

		return isValid;
	}










	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();

		for (int i = 0; i < rubiksCube.size(); i++) {
			output.append(rubiksCube.get(i).toString());
		}

		return output.toString();
	}
}