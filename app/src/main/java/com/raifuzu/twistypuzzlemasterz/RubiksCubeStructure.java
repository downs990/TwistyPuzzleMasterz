package com.raifuzu.twistypuzzlemasterz;

import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.json.JSONObject;

public class RubiksCubeStructure implements RubiksCube {


	private Map<String, CubeLayer> rubiksCube;

	private String validCubieDisplay = "";

	// TODO: Should be setting these ArrayLists equal to new in the constructor. Init all member vars there.
	private final ArrayList<CubeLayer.Cubie> validCubiesFoundList = new ArrayList<>();
	private final ArrayList<CubeLayer.Cubie> invalidCubiesFoundList = new ArrayList<>();
	private final ArrayList<String> missingCubiesList = new ArrayList<>();


	private String solutionAlgorithm;
	private String cubeAsString;

	private View rootView;



	public RubiksCubeStructure(View rootView, String cubeAsString){

		// Initialize rootView for setting up buttons
		this.rootView = rootView;
		this.rubiksCube = new HashMap<>();
		this.cubeAsString = cubeAsString;
		this.solutionAlgorithm = "";


		initLayers();

	}


	private void initLayers(){
		AdvancedArrayList<Integer[]> surfacesColorsLists = convertCubeStringToColorsLists();


		SurfaceName[] surfaceNames = {
				SurfaceName.U, SurfaceName.L, SurfaceName.F,
				SurfaceName.R, SurfaceName.B, SurfaceName.D
		};


		// TODO: Update all variables from buttons, colors, to indexes.
		for(int i = 0; i < surfacesColorsLists.size(); i++){
			Integer[] surfaceColors = surfacesColorsLists.get(i);
			SurfaceName nameOfSurface = surfaceNames[i];

			AdvancedArrayList<Integer[]> surfaceAndBorderColors = new AdvancedArrayList<>();
			AdvancedArrayList<Integer[]> border = initBorder(nameOfSurface, surfacesColorsLists);

			assert border != null;
			surfaceAndBorderColors.add(surfaceColors);
			surfaceAndBorderColors.addAll(border);

			CubeLayer currentLayer = new CubeLayer(
					this.rootView, this.cubeAsString, surfaceAndBorderColors, nameOfSurface);

			this.rubiksCube.put(nameOfSurface.name(), currentLayer);
		}
	}


	public static AdvancedArrayList<Integer[]> convertCubeStringToColorsLists(){

		// Orientation mapping to get the OpenCV app's cube string to correctly map to
		// my RubiksCubeStructure. These are indexes of the cubeAsString variable.
		Integer[][] colorMapping = {
				// UP
				{9,8,7,6,5,4,3,2,1} ,
				// LEFT
				{10,11,12,13,14,15,16,17,18},
				// FRONT
				{46,47,48,49,50,51,52,53,54},
				// RIGHT
				{37,38,39,40,41,42,43,44,45},
				// BACK
				{19,20,21,22,23,24,25,26,27},
				// DOWN
				{36,35,34,33,32,31,30,29,28},
		};

		AdvancedArrayList<Integer[]> surface = new AdvancedArrayList<>();

		for (Integer[] currentLayer : colorMapping){
			// Color lists
			Integer[] surfaceColors = new Integer[9];
			for (int i = 0; i < currentLayer.length; i++){
				surfaceColors[i] = currentLayer[i];
			}

			surface.addMultiple(surfaceColors);
		}

		return surface;
	}


	private ArrayList< AdvancedArrayList<Integer[]> > createWBorders(
			final Integer[] u, final Integer[] l, final Integer[] f, final Integer[] r,
			final Integer[] b, final Integer[] d){


		// [L, R] => X      [U,D] => Y     [F,B] => Z
		ArrayList< AdvancedArrayList<Integer[]> >  result = new ArrayList<>();

		// W layer for -> Left, Right
		AdvancedArrayList<Integer[]> xW = new AdvancedArrayList<Integer[]>(){{
			add( new Integer[]{  u[7] ,  u[4] , u[1]} );
			add( new Integer[]{  b[1] ,  b[4] , b[7]} );
			add( new Integer[]{  d[7] ,  d[4] , d[1]} );
			add( new Integer[]{  f[7] ,  f[4] , f[1]} );
		}};
		// W layer for -> Up, Down
		final AdvancedArrayList<Integer[]> yW = new AdvancedArrayList<Integer[]>(){{
			add( new Integer[]{  f[3] ,  f[4] ,  f[5]} );
			add( new Integer[]{  r[3] ,  r[4] ,  r[5]} );
			add( new Integer[]{  b[3] ,  b[4] ,  b[5]} );
			add( new Integer[]{  l[3] ,  l[4] ,  l[5]} );
		}};
		// W layer for -> Front, Back
		AdvancedArrayList<Integer[]> zW = new AdvancedArrayList<Integer[]>(){{
			add( new Integer[]{  u[3] ,  u[4] ,  u[5]} );
			add( new Integer[]{  r[1] ,  r[4] ,  r[7]} );
			add( new Integer[]{  d[5] ,  d[4] ,  d[3]} );
			add( new Integer[]{  l[7] ,  l[4] ,  l[1]} );
		}};

		result.add(xW);
		result.add(yW);
		result.add(zW);

		return result;
	}



	// Just pass around indexes of locations in the masterColorsList/colorMapping in SolvingFragment.java
	private AdvancedArrayList<Integer[]> initBorder(SurfaceName layerName,
													AdvancedArrayList<Integer[]> surfacesColorsLists){

		final Integer[] u = surfacesColorsLists.get(0);
		final Integer[] l = surfacesColorsLists.get(1);
		final Integer[] f = surfacesColorsLists.get(2);
		final Integer[] r = surfacesColorsLists.get(3);
		final Integer[] b = surfacesColorsLists.get(4);
		final Integer[] d = surfacesColorsLists.get(5);


		ArrayList< AdvancedArrayList<Integer[]> > wBorders = createWBorders(u,l,f,r,b,d);
		final AdvancedArrayList<Integer[]> xW = wBorders.get(0);
		final AdvancedArrayList<Integer[]> yW = wBorders.get(1);
		final AdvancedArrayList<Integer[]> zW = wBorders.get(2);

		AdvancedArrayList<Integer[]> upSurfaceBorder = new AdvancedArrayList<Integer[]>(){{
			add( new Integer[]{  b[2] ,  b[1] , b[0]} );
			add( new Integer[]{  r[2] ,  r[1] , r[0]} );
			add( new Integer[]{  f[2] ,  f[1] , f[0]} );
			add( new Integer[]{  l[2] ,  l[1] , l[0]} );
			addAll( yW );
		}};
		AdvancedArrayList<Integer[]> leftSurfaceBorder = new AdvancedArrayList<Integer[]>(){{
			add( new Integer[]{  u[0] ,  u[3] ,  u[6]} );
			add( new Integer[]{  f[0] ,  f[3] ,  f[6]} );
			add( new Integer[]{  d[0] ,  d[3] ,  d[6]} );
			add( new Integer[]{  b[8] ,  b[5] ,  b[2]} );
			addAll( xW );
		}};
		AdvancedArrayList<Integer[]> frontSurfaceBorder = new AdvancedArrayList<Integer[]>(){{
			add( new Integer[]{  u[6] ,  u[7] ,  u[8]} );
			add( new Integer[]{  r[0] ,  r[3] ,  r[6]} );
			add( new Integer[]{  d[2] ,  d[1] ,  d[0]} );
			add( new Integer[]{  l[8] ,  l[5] ,  l[2]} );
			addAll( zW );
		}};
		AdvancedArrayList<Integer[]> rightSurfaceBorder = new AdvancedArrayList<Integer[]>(){{
			add( new Integer[]{  u[8] ,  u[5] ,  u[2]} );
			add( new Integer[]{  b[0] ,  b[3] ,  b[6]} );
			add( new Integer[]{  d[8] ,  d[5] ,  d[2]} );
			add( new Integer[]{  f[8] ,  f[5] ,  f[2]} );
			addAll( xW );
		}};
		AdvancedArrayList<Integer[]> backSurfaceBorder = new AdvancedArrayList<Integer[]>(){{
			add( new Integer[]{  u[2] ,  u[1] ,  u[0]} );
			add( new Integer[]{  l[0] ,  l[3] ,  l[6]} );
			add( new Integer[]{  d[6] ,  d[7] ,  d[8]} );
			add( new Integer[]{  r[8] ,  r[5] ,  r[2]} );
			addAll( zW );
		}};
		AdvancedArrayList<Integer[]> downSurfaceBorder = new AdvancedArrayList<Integer[]>(){{
			add( new Integer[]{  f[6] ,  f[7] ,  f[8]} );
			add( new Integer[]{  r[6] ,  r[7] ,  r[8]} );
			add( new Integer[]{  b[6] ,  b[7] ,  b[8]} );
			add( new Integer[]{  l[6] ,  l[7] ,  l[8]} );
			addAll( yW );
		}};

 		switch (layerName){
			case U:
				return upSurfaceBorder;
			case L:
				return leftSurfaceBorder;
			case F:
				return frontSurfaceBorder;
			case R:
				return rightSurfaceBorder;
			case B:
				return backSurfaceBorder;
			case D:
				return downSurfaceBorder;

			default:
				// IMPORTANT: This should never happen. If does then will crash.
				return new AdvancedArrayList<>();//empty list

		 }

	}


	public static Integer colorLetterToIntegerColor(char colorLetter){

		Integer value = Color.BLUE;
		// R      W      B      O      Y      G
		// RED   WHITE  BLUE   ORANGE YELLOW GREEN

		Set<String> keys =  RubiksCube.colorsMap.keySet();
		for(String fullNameOfColor : keys){
			if(fullNameOfColor.charAt(0) == colorLetter){
				value = RubiksCube.colorsMap.get(fullNameOfColor);
			}
		}

		return value;
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

		for(CubeLayer layer : this.rubiksCube.values()){
			boolean successfullyFoundCubie = false;
			for(CubeLayer.Cubie cubie : layer.myCubies){

				// Same type of cubie (i.e Edge, Corner, Center)
				if(stickers.size() == cubie.getStickerColors().size()){
					ArrayList<Boolean> foundList = new ArrayList<>();

					for(Integer color : stickers){
						boolean stickerFound = cubie
								.getStickerColors()
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


	// IMPORTANT: If you ever change the pre-programmed orientation of the rubikscube
	// then you'll need to use this as well.
	@Override
	public SurfaceName findLayerByColor(Integer color) {

		if(color.equals(RubiksCube.YELLOW)){
			return SurfaceName.U;
		}else if(color.equals(RubiksCube.ORANGE)){
			return SurfaceName.L;
		}else if(color.equals(RubiksCube.BLUE)){
			return SurfaceName.F;
		}else if(color.equals(RubiksCube.RED)){
			return SurfaceName.R;
		}else if(color.equals(RubiksCube.GREEN)){
			return SurfaceName.B;
		}else if(color.equals(RubiksCube.WHITE)){
			return SurfaceName.D;
		}



		return null;
	}

	@Override
	public CubeLayer.Cubie getCubieAtLocation(String[] intersection){

		CubeLayer.Cubie correctCubie = null;

		// You don't need to loop through each location now that each cubie has a location.
		String currentSurface = intersection[0];
		CubeLayer currentLayer = this.rubiksCube.get( currentSurface );
		ArrayList<CubeLayer.Cubie> cubies = currentLayer.getAllCubies();

		// Get the cubie from the cubies list that has it's location
		//       that matches intersection
		for(CubeLayer.Cubie currentCubie : cubies){

			String[] currentCubieLocation = currentCubie.getLocation().split(" ");
			Arrays.sort(currentCubieLocation);
			Arrays.sort(intersection);

			if(Arrays.equals( intersection, currentCubieLocation )){
				correctCubie = currentCubie;
				break;
			}
		}

		// return the colors of the cubie
		return correctCubie;
	}

 
	@Override
	public CubeLayer.Cubie getCubieByColorStickers(ArrayList<Integer> stickersList){

		CubeLayer.Cubie result = null;

		for(CubeLayer currentLayer : this.rubiksCube.values()){

			ArrayList<CubeLayer.Cubie> cubiesFromThisLayer = currentLayer.getAllCubies();
			for(CubeLayer.Cubie currentCubie : cubiesFromThisLayer){

				ArrayList<Integer> a = stickersList;
				ArrayList<Integer> b = currentCubie.getStickerColors();

				// Check if the two array list have the same contents despite of order.
				boolean isEqual = a.containsAll(b)
									&& b.containsAll(a)
									&& a.size() == b.size();

				if(isEqual){
					result = currentCubie;
					break;
				}
			}

		}

		return result;
	}



	public String getSolutionAlgorithm(){
		return this.solutionAlgorithm;
	}

	/**
	 * Puts the colors on the cube into a solved state.
	 */
	@Override
	public void resetCube() {


		this.cubeAsString = "YYYYYYYYYOOOOOOOOOGGGGGGGGGWWWWWWWWWRRRRRRRRRBBBBBBBBB";
		initLayers();


		// TODO: Clear all other member variables to make sure that unwanted data
		// 	doesn't persist between scrambles and clears.
		solutionAlgorithm = "";

	}

	public String getCubeAsString(){
		return this.cubeAsString;
	}

 
	@Override
	public void executeAlgorithm(String algorithm, RecordAlgorithm yesOrNo) {
		// Example Algorithm: R' D' R D

		if(yesOrNo.equals(RecordAlgorithm.YES)){
			this.solutionAlgorithm += " " + algorithm;
		}

		String[] moves = algorithm.split(" ");
		for (String individualMove : moves) {

			String actualMove = individualMove.replace("'", "");
			Rotation direction = Rotation.CLOCKWISE;
			boolean isRotateW = false;

			if (!individualMove.equals(actualMove)) {
				// inverted move
				direction = Rotation.COUNTER_CLOCKWISE;
			}



			if(individualMove.contains("M")){
				if(individualMove.contains("2")){

					// M2
					rotateSliceM(direction);
					rotateSliceM(direction);
				}else{
					// M
					rotateSliceM(direction);
				}	

			}else{

				if(individualMove.contains("w")){
					isRotateW = true;
					individualMove = actualMove.replace("w", "");
					actualMove = individualMove;
				}


				if (individualMove.contains("2")) {
					// Actual rotation and UI update
					actualMove = individualMove.replace("2", "");
					CubeLayer currentLayer = this.rubiksCube.get(actualMove);
					// Rotate 2 times
					rotate(currentLayer, direction, isRotateW);
					rotate(currentLayer, direction, isRotateW);
				} else {
					// Actual rotation and UI update
					CubeLayer currentLayer = this.rubiksCube.get(actualMove);
					rotate(currentLayer, direction, isRotateW);// Moves colors on structure
				}

			}



			 
		}
	}


	private void rotateSliceM(Rotation directionOfRotation){


		Integer[] u = this.getLayerByLetter("U").getSurfaceColors();
		Integer[] b = this.getLayerByLetter("B").getSurfaceColors();
		Integer[] d = this.getLayerByLetter("D").getSurfaceColors();
		Integer[] f = this.getLayerByLetter("F").getSurfaceColors();

		int[] m1 = {u[7], u[4], u[1]};
		int[] m2 = {b[1], b[4], b[7]};
		int[] m3 = {d[7], d[4], d[1]};
		int[] m4 = {f[7], f[4], f[1]};


		int[][] currentSliceIndexes = {m1, m2, m3, m4};
		int[][] newSlicesIndexes;
		if(directionOfRotation == Rotation.CLOCKWISE){
			// Clockwise
			newSlicesIndexes = new int[][]{m4, m1, m2, m3};
		}else{
			// Counter Clockwise
			newSlicesIndexes = new int[][]{m2, m3, m4, m1};
		}


		String tempCubeString = new String(cubeAsString); // deep copy.
		char[] newCharArray = tempCubeString.toCharArray();
		char[] originalCharArray = cubeAsString.toCharArray();


		// Char at oldIndex now equals char at newIndex
		for(int i = 0; i < newSlicesIndexes.length; i++){
			for(int j = 0; j < newSlicesIndexes[i].length; j++){

				int oldIndex = currentSliceIndexes[i][j] - 1;
				int newIndex = newSlicesIndexes[i][j] - 1;
				char c = originalCharArray[newIndex];
				newCharArray[oldIndex] = c;
			}
		}

		// Updates cubeAsAString to match changes after rotation.
		cubeAsString = new String(newCharArray);


		initLayers();
		finalizeOrientation();	// Update all cubie orientations
	}







	// TODO: Add rotations and notations for    M M' M2
	@Override
	public void rotate(CubeLayer layerToRotate, Rotation directionOfRotation, boolean isRotateW) {

		if (layerToRotate == null) {
			// This means that the actualMove variable in the executeAlgorithm
			// method was invalid. One of the moves in the algorithm did not
			// match any moves in the Side enumeration.
			Toast.makeText(rootView.getContext(),
					"Invalid algorithm notation character. Can't find layer",
					Toast.LENGTH_LONG).show();
		} else {

			if (directionOfRotation == Rotation.CLOCKWISE) {
				rotateClockwise(layerToRotate, isRotateW);
			} else {
				rotateCounterClockwise(layerToRotate, isRotateW);
			}


			initLayers();

			// Update all cubie orientations
			finalizeOrientation();
		}
	}


	private void rotateClockwise(CubeLayer layer, boolean isRotateW) {

		String layerName = layer.getSideName().name();

		// Different from white board picture
		AdvancedArrayList<Integer> newSurfaceList = new AdvancedArrayList<>(
				getStickerColor(layer, 6), getStickerColor(layer, 3), getStickerColor(layer, 0),
				getStickerColor(layer, 7), getStickerColor(layer, 4), getStickerColor(layer, 1),
				getStickerColor(layer, 8), getStickerColor(layer, 5), getStickerColor(layer, 2));

		ArrayList<Integer[]> normalBorder = new ArrayList<>(
			Arrays.asList(
												// Current locations      // New Locations:
					layer.getSurfaceLeftColors(),		// <-            	 // back
					layer.getSurfaceBackColors(), 		// <-             	 // right
					layer.getSurfaceRightColors(),		// <-            	 // front
					layer.getSurfaceFrontColors()));	// <-           	 // left



		// Don't rotate w by default.
		ArrayList<Integer[]> wBorder = new ArrayList<>(Arrays.asList( layer.getW1(), layer.getW2(),layer.getW3(),layer.getW4() ));


		if(isRotateW){

			wBorder = new ArrayList<>( Arrays.asList( layer.getW4(), layer.getW1(),layer.getW2(),layer.getW3() ));


			// L and R share the same w Axis of x. Therefore, L is the same as R' on the w layer.
			// This is why we have to invert rotations on each axis from L, U, and B
			if(layerName.equals("L") || layerName.equals("U")  || layerName.equals("B")){

				wBorder = new ArrayList<>(Arrays.asList( layer.getW2(), layer.getW3(),layer.getW4(),layer.getW1() ));
			}
		}

		updateCubeAsString(layer, newSurfaceList, normalBorder, wBorder);



	}


	private void rotateCounterClockwise(CubeLayer layer, boolean isRotateW) {

		String layerName = layer.getSideName().name();

		AdvancedArrayList<Integer> newSurfaceList = new AdvancedArrayList<>(
				getStickerColor(layer, 2), getStickerColor(layer, 5), getStickerColor(layer, 8),
				getStickerColor(layer, 1), getStickerColor(layer, 4), getStickerColor(layer, 7),
				getStickerColor(layer, 0), getStickerColor(layer, 3), getStickerColor(layer, 6));


		ArrayList<Integer[]> normalBorder = new ArrayList<>(
				Arrays.asList( 							// Current locations      // New Locations:
						layer.getSurfaceRightColors(), 			// <-            	 // back
						layer.getSurfaceFrontColors(), 			// <-            	 // right
						layer.getSurfaceLeftColors(), 			// <-            	 // front
						layer.getSurfaceBackColors() 			// <-            	 // left
				));


		// Don't rotate w by default.
		ArrayList<Integer[]> wBorder = new ArrayList<>(Arrays.asList( layer.getW1(), layer.getW2(),layer.getW3(),layer.getW4() ));


		if(isRotateW){
			wBorder = new ArrayList<>( Arrays.asList( layer.getW2(), layer.getW3(),layer.getW4(),layer.getW1() ));

			// L and R share the same w Axis of x. Therefore, L is the same as R' on the w layer.
			// This is why we have to invert rotations on each axis from L, U, and B
			if(layerName.equals("L") || layerName.equals("U") || layerName.equals("B")){
				wBorder = new ArrayList<>( Arrays.asList(layer.getW4(), layer.getW1(),layer.getW2(),layer.getW3() ));
			}
		}

		updateCubeAsString(layer, newSurfaceList, normalBorder, wBorder);
	}


	// Keeps cubeAsString up to date after each rotation change.
	public final void updateCubeAsString(CubeLayer layer, AdvancedArrayList<Integer> newSurfaceList,
										 ArrayList<Integer[]> newSurfaceBorder, ArrayList<Integer[]> newWBorder){

		Integer[] oldSurfaceBack  =  layer.getSurfaceBackColors();
		Integer[] oldSurfaceRight =  layer.getSurfaceRightColors();
		Integer[] oldSurfaceFront =  layer.getSurfaceFrontColors();
		Integer[] oldSurfaceLeft  =  layer.getSurfaceLeftColors();
		Integer[] newSurfaceBack  =  newSurfaceBorder.get(0);
		Integer[] newSurfaceRight =  newSurfaceBorder.get(1);
		Integer[] newSurfaceFront =  newSurfaceBorder.get(2);
		Integer[] newSurfaceLeft  =  newSurfaceBorder.get(3);


		Integer[] oldW1 = layer.getW1();
		Integer[] oldW2 = layer.getW2();
		Integer[] oldW3 = layer.getW3();
		Integer[] oldW4 = layer.getW4();
		Integer[] newW1 =  newWBorder.get(0);
		Integer[] newW2 =  newWBorder.get(1);
		Integer[] newW3 =  newWBorder.get(2);
		Integer[] newW4 =  newWBorder.get(3);



		String tempCubeString = new String(cubeAsString); // deep copy. preserve original cube while changing it.
		char[] newCharArray = tempCubeString.toCharArray();
		char[] originalCharArray = cubeAsString.toCharArray();

		for(int i = 0; i < newSurfaceBack.length; i++){

			int[] oldBorderIndexes = {

					oldSurfaceBack[i] - 1, 	oldSurfaceRight[i] - 1, oldSurfaceFront[i] - 1, oldSurfaceLeft[i] - 1,
					oldW1[i] - 1, 			oldW2[i] - 1, 			oldW3[i] - 1, 			oldW4[i] - 1
			};

			int[] newBorderIndexes = {
					newSurfaceBack[i] - 1, 	newSurfaceRight[i] - 1, newSurfaceFront[i] - 1, newSurfaceLeft[i] - 1,
					newW1[i] - 1,	 		newW2[i] - 1, 			newW3[i] - 1, 			newW4[i] - 1
			};


			for(int j = 0; j < newBorderIndexes.length; j++){
				int newIndex = newBorderIndexes[j];
				int oldIndex = oldBorderIndexes[j];
				char c = originalCharArray[ newIndex ];

				newCharArray[oldIndex]  = c;
			}
		}

		Integer[] currentSurfaceList = layer.getSurfaceColors();



		// Char at oldIndex now equals char at newIndex
		for(int i = 0; i < newSurfaceList.size(); i++){

			int oldIndex = currentSurfaceList[i] - 1;
			int newIndex = newSurfaceList.get(i) - 1;
			char c = originalCharArray[newIndex];
			newCharArray[oldIndex] = c;
		}

		// Updates cubeAsAString to match changes after rotation.
		cubeAsString = new String(newCharArray);
	}







	private Integer getStickerColor(CubeLayer layer, int index) {
		return layer.getSurfaceColors()[index];
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
		String[] movesFor3x3x3 = initialAlg.split(" ");
		AdvancedArrayList<String> cubeSymbols = new AdvancedArrayList<>(movesFor3x3x3);

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


	@Override
	public ArrayList<Integer> getCenterColorsOfLocation(String[] location){

		ArrayList<Integer> centerColors = new ArrayList<>();

		for(String letter : location){
			CubeLayer.Cubie centerCubieOfLayer = this.rubiksCube
					.get(letter)
					.getAllCubies()
					.get(4);
			Integer colorOfCenterCubie = centerCubieOfLayer.getStickerColors().get(0);
			centerColors.add(colorOfCenterCubie);
		}

		return centerColors;
	}

	public void finalizeOrientation(){


		for (CubeLayer layer: this.rubiksCube.values()) {
			ArrayList<CubeLayer.Cubie> cubiesList = layer.getAllCubies();
			for(CubeLayer.Cubie cubie : cubiesList){
				Map<String, String> newOrientation = new HashMap<>();


				ArrayList<String> cubieStickersColors = cubie.getStickerColorsStrings();
				String[] cubieLocation = cubie.getLocation().split(" ");

				for(int i = 0; i < cubieLocation.length; i++){

					// Find the color that this layer should be
					String surfaceLetter = cubieLocation[i];
					CubeLayer cubieLayer = this.rubiksCube.get(surfaceLetter);
					CubeLayer.Cubie centerCubie = cubieLayer.getAllCubies().get(4);
					Integer centerColor = centerCubie.getStickerColors().get(0);
					String layerCenterColor = CubeLayer.colorIntToString(centerColor);


					// Find the sticker on this cubie that is on that layer
					String stickerColor = cubieStickersColors.get(i);


					// Store those two colors as a pair. If they are the same then the orientation is correct.
					newOrientation.put(stickerColor, layerCenterColor);

				}

				cubie.setCubieOrientation(newOrientation);
			}
		}

	}

	@Override
	public CubeLayer getLayerByLetter(String layerLetter){
		return this.rubiksCube.get(layerLetter);
	}


	@Override
	public ArrayList<CubeLayer> getLayersList(){

		// Forces this function to return the layers in the same order that the
		// cube is initialized with.
		String[] layerNames = {
				SurfaceName.U.name(), SurfaceName.L.name(), SurfaceName.F.name(),
				SurfaceName.R.name(), SurfaceName.B.name(), SurfaceName.D.name()
		};

		ArrayList<CubeLayer> allLayers = new ArrayList<>();
		for(String layer : layerNames){
			allLayers.add (this.rubiksCube.get( layer ) );
		}

		return allLayers;
	}

 

//	public String test(String xLayer){
//
//		StringBuilder output = new StringBuilder();
//		// Print all cubies at x layer
//		ArrayList<CubeLayer.Cubie> allCubies = this.rubiksCube.get(xLayer).getAllCubies();
//		for(CubeLayer.Cubie cubie : allCubies){
////			output.append(cubie.toString());
//
//			Map<String, String> orientation = cubie.getCubieOrientation();
//			output.append(orientation.toString());
//		}
//
//		return output.toString();
//	}



	// Check if the cubie at this location is in the correct location. (orientation might still be off)
	@Override
	public boolean isCorrectCubieAtThisLocation(String[] location){

		CubeLayer.Cubie cubieAtLocation = this.getCubieAtLocation(location);
		ArrayList<String> colorsOfCubie = cubieAtLocation.getStickerColorsStrings();

		ArrayList<String> centerColorsAsStrings = new ArrayList<>();
		ArrayList<Integer> centerColorsOfLayers = getCenterColorsOfLocation(location);
		for(Integer color : centerColorsOfLayers){
			String colorAsString = CubeLayer.colorIntToString(color);
			centerColorsAsStrings.add(colorAsString);
		}

		Collections.sort(colorsOfCubie);
		Collections.sort(centerColorsAsStrings);

		return colorsOfCubie.equals(centerColorsAsStrings);

	}

	@Override
	public boolean isCubieAtLocationSolved(String[] location){
		boolean correctOrientation = true;
		boolean correctLocation = isCorrectCubieAtThisLocation(location);

		CubeLayer.Cubie cubieAtLocation = this.getCubieAtLocation(location);
		Map<String, String> orientation = cubieAtLocation.getCubieOrientation();

		Set<String> keySet = orientation.keySet();

		for(String key : keySet){
			if( ! orientation.get(key).equals(key) ){
				correctOrientation = false;
				break;
			}
		}

		return correctLocation && correctOrientation;
	}

	@Override
	public List<SurfaceName> correctLocationOfCubie(Integer[] cubieColors) {

		ArrayList<SurfaceName> correctLocation = new ArrayList<>();

		for(Integer stickerColor : cubieColors){
			for(CubeLayer currentLayer : this.rubiksCube.values()){
				Integer colorOfSurface = currentLayer.getCenterColor();

				if(Objects.equals(stickerColor, colorOfSurface)){
					correctLocation.add( currentLayer.getSideName() );
					break;
				}
			}
		}

		return correctLocation;
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
				for (CubeLayer.Cubie cubie : layer.getAllCubies()) {

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
			for (CubeLayer.Cubie cubie : layer.getAllCubies()) {

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
					validCubies.add(cubie.getStickerColors());
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

	private void getMissingCubies(ArrayList<ArrayList<Integer>> validPieces, ArrayList<CubeLayer.Cubie> validCubiesFound) {

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

		for (CubeLayer layer : this.rubiksCube.values()) {
			output.append(layer.toString());
		}

		return output.toString();
	}
}
