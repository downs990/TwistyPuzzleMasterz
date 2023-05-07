package com.raifuzu.twistypuzzlemasterz;

import android.graphics.Color;

import java.util.ArrayList;

public interface RubiksCube {

	enum Side {
		U, L, F, R, B, D
	};

	enum Rotation {
		CLOCKWISE, COUNTER_CLOCKWISE
	}

	Integer RED = Color.RED;
	Integer YELLOW = Color.YELLOW;
	Integer BLUE = Color.BLUE;
	Integer WHITE = Color.LTGRAY;
	Integer GREEN = Color.GREEN;
	Integer ORANGE = Color.rgb(255, 165, 0);

	//                       U ,     L  ,   F ,    R  ,    B  ,    D
	Integer[] colorsArray = { RED, YELLOW, BLUE, WHITE, GREEN, ORANGE };


	/**
	 * The purpose of this method is to find the exact location of a cubie
	 * by just searching for it's unique colors in the whole cube. This will
	 * be helpful for determining if the cubie is on the top, middle, or down
	 * level of the cube.
	 *
	 * @param stickers Array of stickers is the cubies unique identifier
	 * @return List of Layers that contains that cubie. The intersection of those layers is
	 * the cubie's location.
	 */
	ArrayList<CubeLayer> findLocationOfCubie(Color[] stickers);


	String getCubeOrientation();

	void rotate(CubeLayer layerToRotate, Rotation directionOfRotation);

	// String algorithm can be either solution or scramble
	void executeAlgorithm(String algorithm);

	void resetCube();

	String generateScrambleAlgorithm(int size);

}
