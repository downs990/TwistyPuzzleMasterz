package com.raifuzu.twistypuzzlemasterz;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.raifuzu.twistypuzzlemasterz.CubeLayer.Cubie;

import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

/**
 * Research for Optimal Solution with AI:
 * github.com/BobNisco/RubiksCube
 * github.com/stuartsoft/RSolver
 * github.com/johnpaulwelsh/AI-Rubiks-Cube
 * en.wikipedia.org/wiki/Optimal_solutions_for_Rubik%27s_Cube#Korf.27s_Algorithm
 * NOTE: This class solves the Rubik's Cube using the CFOP method.
 *
 * @author downs
 */
public class CubeSolver {



    private RubiksCubeStructure rubiksCube;
    private View rootView;

    private String validCubieDisplay = "";
    private ArrayList<Cubie> validCubiesFoundList = new ArrayList<>();
    private ArrayList<Cubie> invalidCubiesFoundList = new ArrayList<>();
    private ArrayList<String> missingCubiesList = new ArrayList<>();



    public CubeSolver(View rootView, RubiksCubeStructure rubiksCube) {
        this.rubiksCube = rubiksCube;
        boolean valid = isValidState();
        Toast.makeText(rootView.getContext(), "Valid State: " + valid, Toast.LENGTH_LONG).show();
    }

    public RubiksCube getRubiksCube() {
        return this.rubiksCube;
    }



    public String crossSolutionSteps(  Integer[][] stickersToSolve){


            // 1. Get all pieces on down layer
            Map< Integer[], ArrayList<CubeLayer>> cubieLocationsMap = new HashMap<>();

            for(Integer[] cubie : stickersToSolve){
                ArrayList<CubeLayer> intersectingLayers = this.rubiksCube.findLocationOfCubie(cubie);
                cubieLocationsMap.put(cubie, intersectingLayers);
            }


            // 2, Rotate down layer until at least 2 pieces are in correct location
            // 3. Set the cubies that are in correct locations to their correct orientation
            // 4. Iff 2 cubies are in incorrect locations then swap them
            // 5. Iff needed, set those 2 cubies to correct orientation
        return null;
    }

    public void solveCross(){

        Integer[][] stickersToSolve =  {
                {RubiksCube.WHITE, RubiksCube.GREEN},
                {RubiksCube.WHITE, RubiksCube.BLUE},
                {RubiksCube.WHITE, RubiksCube.ORANGE},
                {RubiksCube.WHITE, RubiksCube.RED},
        };

        String crossSolutionAlgorithm = crossSolutionSteps(   stickersToSolve  );
        this.rubiksCube.executeAlgorithm(crossSolutionAlgorithm);
    }


    public String f2lSolutionSteps(  Integer[][][] stickersToSolve){

        return null;
    }

    public void solveF2L(){
        Integer[][][] stickersToSolve = {
                {{RubiksCube.WHITE, RubiksCube.BLUE, RubiksCube.ORANGE}, { RubiksCube.BLUE, RubiksCube.ORANGE}},
                {{RubiksCube.WHITE, RubiksCube.BLUE, RubiksCube.RED}, { RubiksCube.BLUE, RubiksCube.RED}},
                {{RubiksCube.WHITE, RubiksCube.RED, RubiksCube.GREEN}, { RubiksCube.RED, RubiksCube.GREEN}},
                {{RubiksCube.WHITE, RubiksCube.GREEN, RubiksCube.ORANGE}, { RubiksCube.GREEN, RubiksCube.ORANGE}},
        };

        String f2lSolutionAlgorithm = f2lSolutionSteps( stickersToSolve );
        this.rubiksCube.executeAlgorithm(f2lSolutionAlgorithm);
    }
    public void solveOLL(){
//        String ollSolutionAlgorithm = solveStickers(
//                SolutionSteps.OLL,
//                "[" +
//                "TOP_SURFACE"+
//                "]");
//
//        this.rubiksCube.executeAlgorithm(ollSolutionAlgorithm);
    }
    public void solvePLL(){
//        String pllSolutionAlgorithm = solveStickers(
//                SolutionSteps.PLL,
//                "[" +
//                "TOP_BORDER"+
//                "]");
//
//        this.rubiksCube.executeAlgorithm(pllSolutionAlgorithm);
    }




    public void solveCube(){
        solveCross();
        solveF2L();
        solveOLL();
        solvePLL();
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

    private boolean isValidState() {
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

}
