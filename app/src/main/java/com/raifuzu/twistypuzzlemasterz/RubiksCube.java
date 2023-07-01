package com.raifuzu.twistypuzzlemasterz;


import android.graphics.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface RubiksCube {

	enum CubieType {
		EDGE,
		CORNER,
		CENTER
	}

	enum SurfaceName {
		U, L, F, R, B, D
	}

	enum Rotation {
		CLOCKWISE, COUNTER_CLOCKWISE
	}


	// The Rubiks Cube should record all movements that it makes to an algorithm
	// string that can be displayed on screen and passed to the website to show solve animation.
	enum RecordAlgorithm{
		YES, NO
	}

	Integer RED = Color.RED;
	Integer YELLOW = Color.YELLOW;
	Integer BLUE = Color.BLUE;
	Integer WHITE = Color.LTGRAY;
	Integer GREEN = Color.GREEN;
	Integer ORANGE = Color.rgb(255, 165, 0);

	Map<String, Integer> colorsMap = new HashMap<String, Integer>() {{
		put("RED",    RED);     // U
		put("YELLOW", YELLOW);  // L
		put("BLUE",   BLUE);	// F
		put("WHITE",  WHITE);   // R
		put("GREEN",  GREEN);   // B
		put("ORANGE", ORANGE);  // D
	}};

	// Using "Double Brace Initialization"
	Map<String, String[]> cubieLocationsMap = new HashMap<String, String[]>() {{
		// TODO: These location's orders must match the order that they are passed to the Cubie() constructors
		//    in CubeLayer.java  initializeCubies() method. 

		// NOTE: Orient the physical cube such that the current layer (layer x) is facing you. (on F layer)
		put("U", new String[]{"U B L" , "U B" , "U B R" , "U L" , "U" , "U R" , "U L F" , "U F" , "U R F" });
		put("L", new String[]{"L U B" , "L U" , "L U F" , "L B" , "L" , "L F" , "L B D", "L D", "L F D" });
		put("F", new String[]{"F U L" , "F U" , "F U R" , "F L" , "F" , "F R" , "F L D", "F D", "F R D" });
		put("R", new String[]{"R U F" , "R U" , "R U B" , "R F" , "R" , "R B" , "R F D", "R D", "R B D" });
		put("B", new String[]{"B U R" , "B U" , "B U L" , "B R" , "B" , "B L" , "B R D", "B D", "B L D" });
		put("D", new String[]{"D F L" , "D F" , "D F R" , "D L" , "D" , "D R" , "D L B", "D B", "D R B" });
	}};

	ArrayList<SurfaceName> findLocationOfCubie(List<Integer> stickers);
	SurfaceName findLayerByColor(Integer color);

	CubeLayer.Cubie getCubieAtLocation(String[] intersection);

	CubeLayer.Cubie getCubieByColorStickers(ArrayList<Integer> stickersList);

	void rotate(CubeLayer layerToRotate, Rotation directionOfRotation);

	// String algorithm can be either solution or scramble
	void executeAlgorithm(String algorithm, RecordAlgorithm yesOrNo);


	ArrayList<Integer> getCenterColorsOfLocation(String[] location);
	boolean isCorrectCubieAtThisLocation(String[] location);

	boolean isCubieAtLocationSolved(String[] location);

	List<SurfaceName> correctLocationOfCubie(Integer[] cubieColors );

	void resetCube();

	String generateScrambleAlgorithm(int size);

	CubeLayer getLayerByLetter(String layerLetter);
	ArrayList<CubeLayer> getLayersList();


}
