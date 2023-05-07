package com.raifuzu.twistypuzzlemasterz;

import java.lang.reflect.Array;
import java.util.ArrayList;

import com.raifuzu.twistypuzzlemasterz.CubeLayer.Cubie;

import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

/**
 * Research for Optimal Solution with AI:
 * <p>
 * https://github.com/BobNisco/RubiksCube https://github.com/stuartsoft/RSolver
 * https://github.com/johnpaulwelsh/AI-Rubiks-Cube
 * https://en.wikipedia.org/wiki/Optimal_solutions_for_Rubik%27s_Cube#Korf.27s_Algorithm
 * <p>
 * NOTE: This class solves the Rubik's Cube using the CFOP method.
 *
 * @author downs
 */
public class CubeSolver {
    private RubiksCubeStructure rubiksCube;
    private View rootView;

    private Integer RED = Color.RED;
    private Integer YELLOW = Color.YELLOW;
    private Integer BLUE = Color.BLUE;
    private Integer WHITE = Color.LTGRAY;
    private Integer GREEN = Color.GREEN;
    private Integer ORANGE = Color.rgb(255, 165, 0);
    private String validCubieDisplay = "";
    private ArrayList<Cubie> validCubiesFoundList = new ArrayList<>();
    private ArrayList<Cubie> invalidCubiesFoundList = new ArrayList<>();
    private ArrayList<String> missingCubiesList = new ArrayList<>();

    // TODO - this class will need access to all advanced CFOP algorithms.
    public CubeSolver(View rootView, RubiksCubeStructure rubiksCube) {
        this.rubiksCube = rubiksCube;
        boolean valid = isValidState();
        Toast.makeText(rootView.getContext(), "Valid State: " + valid, Toast.LENGTH_LONG).show();
    }

    public RubiksCube getRubiksCube() {
        return this.rubiksCube;
    }

    private Integer convertToIntegerColor(String stringColor) {
        switch (stringColor) {
            case "RED":
                return RED;
            case "WHITE":
                return WHITE;
            case "BLUE":
                return BLUE;
            case "ORANGE":
                return ORANGE;
            case "YELLOW":
                return YELLOW;
            case "GREEN":
                return GREEN;
            default:
                return null;

        }
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
            for (CubeLayer layer : rubiksCube.getLayersList()) {
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
        for (CubeLayer layer : rubiksCube.getLayersList()) {
            for (Cubie cubie : layer.getAllCubies()) {

                ArrayList<Integer> cubieStickers = cubie.getStickerColors();
                boolean isValidPiece = false;
                int index = 0;
                while (index < validCubies.size() && isValidPiece == false) {
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
                if (isValidPiece == false) {
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

            if (found == false) {
                missingCubiesList.add(arrayListToCubieString(piece));
            }
        }
    }

    private boolean isValidState() {
        boolean isValid = false;
        String validEdgesStr = "RED WHITE,RED BLUE,RED YELLOW,RED GREEN,"// Top
                + "BLUE WHITE,BLUE YELLOW,YELLOW GREEN,GREEN WHITE, "// Middle
                + "ORANGE WHITE, ORANGE BLUE, ORANGE YELLOW, ORANGE GREEN";// Botton

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

    public String generateSolveAlgorithm() {






        return validCubieDisplay;
    }

}
