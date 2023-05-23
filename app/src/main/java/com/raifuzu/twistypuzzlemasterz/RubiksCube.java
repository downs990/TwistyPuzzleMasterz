package com.raifuzu.twistypuzzlemasterz;


import android.graphics.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface RubiksCube {

	enum SurfaceName {
		U, L, F, R, B, D
	}

	enum Rotation {
		CLOCKWISE, COUNTER_CLOCKWISE
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


	ArrayList<SurfaceName> findLocationOfCubie(List<Integer> stickers);

	List<Integer> getCubieAtLocation(List<SurfaceName> intersection);

	String getCubeOrientation();

	void rotate(CubeLayer layerToRotate, Rotation directionOfRotation);

	// String algorithm can be either solution or scramble
	void executeAlgorithm(String algorithm);

	void resetCube();

	String generateScrambleAlgorithm(int size);

}
