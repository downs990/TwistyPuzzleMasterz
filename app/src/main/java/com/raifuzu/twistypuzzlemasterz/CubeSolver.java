package com.raifuzu.twistypuzzlemasterz;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import android.view.View;
import android.widget.Toast;

import com.raifuzu.twistypuzzlemasterz.RubiksCube.SurfaceName;


/**
 * NOTE: This class solves the Rubik's Cube using the CFOP method.
 *
 * @author downs
 */
public class CubeSolver {


    Integer[][] topCubies = {
            {RubiksCube.YELLOW, RubiksCube.BLUE, RubiksCube.ORANGE},
            {RubiksCube.YELLOW, RubiksCube.BLUE},
            {RubiksCube.YELLOW, RubiksCube.BLUE, RubiksCube.RED},
            {RubiksCube.YELLOW, RubiksCube.RED},
            {RubiksCube.YELLOW, RubiksCube.RED, RubiksCube.GREEN},
            {RubiksCube.YELLOW, RubiksCube.GREEN},
            {RubiksCube.YELLOW, RubiksCube.GREEN, RubiksCube.ORANGE},
            {RubiksCube.YELLOW, RubiksCube.ORANGE}
    };

    private final RubiksCubeStructure rubiksCube;
    private final View rootView;


    public CubeSolver(View rootView, RubiksCubeStructure rubiksCube) {
        this.rootView = rootView;
        this.rubiksCube = rubiksCube;
    }

    /**
     * 1. Get all pieces on down layer
     *
     * @param stickersToSolve Unique stickers list of the specific cubies to solve
     */
    public void crossStep1(Integer[][] stickersToSolve) {

        for (Integer[] cubie : stickersToSolve) {

            // Length of cubieAtList and intersectingLayers will always be 2
            List<Integer> cubieAsList = Arrays.asList(cubie);
            ArrayList<SurfaceName> intersectingLayers = this.rubiksCube.findLocationOfCubie(cubieAsList);

            int indexOfU = intersectingLayers.indexOf(SurfaceName.U);
            int indexOfNonU = (indexOfU == 0) ? 1 : 0;
            SurfaceName nonUSurface = intersectingLayers.get(indexOfNonU);


            // If cubie on 'up' layer
            if (intersectingLayers.contains(SurfaceName.U)) {

                // Check if there is a white cubie on the D layer directly under U cubie.
                String[] locationDirectlyBelow = {nonUSurface.name(), SurfaceName.D.name()};
                CubeLayer.Cubie cubieDirectlyBelow = this.rubiksCube.getCubieAtLocation(locationDirectlyBelow);

                if (cubieDirectlyBelow.getStickerColors().contains(RubiksCube.WHITE)) {

                    // avoidance maneuver
                    avoidanceManeuverForCross(locationDirectlyBelow);
                }

                String sideToRotate = nonUSurface.name() + "2";
                this.rubiksCube.executeAlgorithm(sideToRotate, RubiksCube.RecordAlgorithm.YES);
            }

            // If the cubie on 'middle' layer
            else if (!intersectingLayers.contains(SurfaceName.D) && !intersectingLayers.contains(SurfaceName.U)) {

                ArrayList<String> locationAsString = new ArrayList<>();
                locationAsString.add(intersectingLayers.get(0).name());
                locationAsString.add(intersectingLayers.get(1).name());
                Collections.sort(locationAsString);

                // Possible locations to insert 'cubie' if it's location is case1
                String[][] locationsToCheck = {{locationAsString.get(0), "D"},
                        {locationAsString.get(1), "D"}};


                // locationsAsString   ->  possibleMoves          {  locationsToCheck }
                // [F, L] ->   F' or L                       // Check {  {F,D}, {L,D}  }
                // [B, L] ->   B  or L'                      // Check {  {B,D}, {L,D}  }
                // [F, R] ->   F  or R'                      // Check {  {F,D}, {R,D}  }
                // [B, R] ->   B' or R                       // Check {  {B,D}, {R,D}  }

                String keyToPossibleMovesMap = locationsToCheck[0][0] + " " + locationsToCheck[0][1]
                        + " , " + locationsToCheck[1][0] + " " + locationsToCheck[1][1];
                Map<String, String[]> possibleMovesMap = new HashMap<String, String[]>() {{
                    put("F D , L D", new String[]{"F'", "L"});
                    put("B D , L D", new String[]{"B",  "L'"});
                    put("F D , R D", new String[]{"F",  "R'"});
                    put("B D , R D", new String[]{"B'", "R"});
                }};


                List<Boolean> whitePieceExist = Arrays.asList(false, false);

                for (int i = 0; i < locationsToCheck.length; i++) {

                    String[] checkingLocation = locationsToCheck[i];

                    CubeLayer.Cubie currentCubie1 = this.rubiksCube.getCubieAtLocation(checkingLocation);
                    ArrayList<Integer> currentCubie = currentCubie1.getStickerColors();

                    // Check if a white cubie already exists at the checking location. If yes, do avoidance.
                    if (currentCubie.contains(RubiksCube.WHITE)) {
                        whitePieceExist.set(i, true);
                    }
                }


                // Check if both possible moves have white pieces at their locations. If yes, do avoidance.
                String[] possibleMoves = possibleMovesMap.get(keyToPossibleMovesMap);
                if (whitePieceExist.get(0) && whitePieceExist.get(1)) {

                    this.avoidanceManeuverForCross(locationsToCheck[0]);
                    String moveToExecute = possibleMoves[0];
                    this.rubiksCube.executeAlgorithm(moveToExecute, RubiksCube.RecordAlgorithm.YES);
                }
                // Else, perform correct insertion.
                else {

                    int indexOfFalse = whitePieceExist.indexOf(false);
                    String moveToExecute = possibleMoves[indexOfFalse];
                    this.rubiksCube.executeAlgorithm(moveToExecute, RubiksCube.RecordAlgorithm.YES);

                }

            }
        }
    }


    private void avoidanceManeuverForCross(String[] locationDirectlyBelow) {

        CubeLayer.Cubie cubieDirectlyBelow = this.rubiksCube.getCubieAtLocation(locationDirectlyBelow);
        ArrayList<Integer> stickers = cubieDirectlyBelow.getStickerColors();

        while (stickers.contains(RubiksCube.WHITE)) {

            // Actually Execute a D rotation
            this.rubiksCube.executeAlgorithm("D", RubiksCube.RecordAlgorithm.YES);

            cubieDirectlyBelow = this.rubiksCube.getCubieAtLocation(locationDirectlyBelow);
            stickers = cubieDirectlyBelow.getStickerColors();
        }
    }

    private int crossPiecesInCorrectLocation() {
        String[] location1 = {SurfaceName.F.name(), SurfaceName.D.name()};
        String[] location2 = {SurfaceName.R.name(), SurfaceName.D.name()};
        String[] location3 = {SurfaceName.B.name(), SurfaceName.D.name()};
        String[] location4 = {SurfaceName.L.name(), SurfaceName.D.name()};
        String[][] locations = {location1, location2, location3, location4};

        int numOfCorrectLocations = 0;
        for (String[] location : locations) {
            boolean isCorrect = this.rubiksCube.isCorrectCubieAtThisLocation(location);
            if (isCorrect) {
                numOfCorrectLocations++;
            }
        }
        return numOfCorrectLocations;
    }


    public void crossStep2() {

        int numOfCorrectLocations = crossPiecesInCorrectLocation();
        while (numOfCorrectLocations < 2) {

            this.rubiksCube.executeAlgorithm("D", RubiksCube.RecordAlgorithm.YES);
            numOfCorrectLocations = crossPiecesInCorrectLocation();
        }
    }


    private void cubieOrientationCorrection(CubeLayer.Cubie currentCubie) {
        if (currentCubie.getLocation().contains(SurfaceName.R.name())) {
            this.rubiksCube.executeAlgorithm("R2 U F R' F'", RubiksCube.RecordAlgorithm.YES);
        } else if (currentCubie.getLocation().contains(SurfaceName.F.name())) {
            this.rubiksCube.executeAlgorithm("F2 U L F' L'", RubiksCube.RecordAlgorithm.YES);
        } else if (currentCubie.getLocation().contains(SurfaceName.L.name())) {
            this.rubiksCube.executeAlgorithm("L2 U B L' B'", RubiksCube.RecordAlgorithm.YES);
        } else if (currentCubie.getLocation().contains(SurfaceName.B.name())) {
            this.rubiksCube.executeAlgorithm("B2 U R B' R'", RubiksCube.RecordAlgorithm.YES);
        }
    }


    // Check the 4 cross pieces to see if their orientations are correct.
    // If a piece is not correct, then use the orientation correction algorithm.
    public ArrayList<CubeLayer.Cubie> crossStep3() {

        ArrayList<CubeLayer.Cubie> crossCubiesInWrongLocations = new ArrayList<>();

        String[] location1 = new String[]{"F", "D"};
        String[] location2 = new String[]{"R", "D"};
        String[] location3 = new String[]{"B", "D"};
        String[] location4 = new String[]{"L", "D"};
        String[][] locationsOfCrossPieces = {location1, location2, location3, location4};

        for (String[] location : locationsOfCrossPieces) {
            CubeLayer.Cubie crossCubie = this.rubiksCube.getCubieAtLocation(location);
            Map<String, String> orientationOfCubie = crossCubie.getCubieOrientation();

            // If the white sticker on this cubie is not on the white side.
            if (!orientationOfCubie.get("WHITE").equals("WHITE")) {
                cubieOrientationCorrection(crossCubie);
            }

            // IMPORTANT: You must query the rubiks cube for a new instance of this cubie (i.e call
            // .getCubieAtLocation() ) because cubieOrientationCorrection() calls executeAlgorithm,
            // which changes the state of the cube.
            crossCubie = this.rubiksCube.getCubieAtLocation(location);
            orientationOfCubie = crossCubie.getCubieOrientation();
            Set<String> keys = orientationOfCubie.keySet();
            for (String sticker : keys) {

                if (!orientationOfCubie.get(sticker).equals(sticker)) {
                    crossCubiesInWrongLocations.add(crossCubie);
                    break;
                }
            }


        }


        return crossCubiesInWrongLocations;
    }


    // TODO:
    //  IMPORTANT:
    //      This implementation will have to change to something more dynamic if you
    //      ever decide to change the initial orientation of the cube. Or if you add whole
    //      cube rotations to the CubeSolver. Specifically, the way that you're checking for
    //      parallel pieces is the problem.
    public void crossStep4(ArrayList<CubeLayer.Cubie> crossCubiesToSwap) {

        // Find the pair of cross pieces that need to be swapped.
        // Size is either 2 or 0.
        if (crossCubiesToSwap.size() > 0) {

            CubeLayer.Cubie cubie1 = crossCubiesToSwap.get(0);
            CubeLayer.Cubie cubie2 = crossCubiesToSwap.get(1);

            int whiteIndex = cubie1.getStickerColorsStrings().indexOf("WHITE"); //TODO: Use RubiksCube.WHITE
            final String nonWhiteColor = (whiteIndex == 0) ?
                    cubie1.getStickerColorsStrings().get(1) :
                    cubie1.getStickerColorsStrings().get(0);

            int whiteIndex2 = cubie2.getStickerColorsStrings().indexOf("WHITE");
            final String nonWhiteColor2 = (whiteIndex2 == 0) ?
                    cubie2.getStickerColorsStrings().get(1) :
                    cubie2.getStickerColorsStrings().get(0);

            ArrayList<String> nonWhiteColors = new ArrayList<String>() {{
                addAll(Arrays.asList(nonWhiteColor, nonWhiteColor2));
            }};

            // Check if the two remaining cross cubies are parallel pieces
            if (nonWhiteColors.contains("GREEN") && nonWhiteColors.contains("BLUE")) {
                this.rubiksCube.executeAlgorithm("F2 B2 U2 F2 B2", RubiksCube.RecordAlgorithm.YES);
            } else if (nonWhiteColors.contains("RED") && nonWhiteColors.contains("ORANGE")) {
                this.rubiksCube.executeAlgorithm("R2 L2 U2 R2 L2", RubiksCube.RecordAlgorithm.YES);
            }

            // Check if the two remaining cross cubies are adjacent pieces
            else {
                final ArrayList<SurfaceName> location1 = this.rubiksCube.findLocationOfCubie(cubie1.getStickerColors());
                final ArrayList<SurfaceName> location2 = this.rubiksCube.findLocationOfCubie(cubie2.getStickerColors());

                final ArrayList<String> location1AsStrings = new ArrayList<String>() {{
                    add(location1.get(0).name());
                    add(location1.get(1).name());
                }};
                final ArrayList<String> location2AsStrings = new ArrayList<String>() {{
                    add(location2.get(0).name());
                    add(location2.get(1).name());
                }};
                Collections.sort(location1AsStrings);
                Collections.sort(location2AsStrings);

                ArrayList<String> bothCubieLocations = new ArrayList<String>() {{
                    add(location1AsStrings.get(0) + " " + location1AsStrings.get(1));
                    add(location2AsStrings.get(0) + " " + location2AsStrings.get(1));
                }};
                Collections.sort(bothCubieLocations);
                String keyOfLocations = bothCubieLocations.get(0) + " , " + bothCubieLocations.get(1);
                Map<String, String> solutionMap = new HashMap<String, String>() {{
                    put("D F , D R", "R2 U F2 U' R2");
                    put("D F , D L", "F2 U L2 U' F2");
                    put("B D , D L", "L2 U B2 U' L2");
                    put("B D , D R", "B2 U R2 U' B2");
                }};

                // Once the two cubies to swap are in the correct locations, then perform swap alg.
                String algorithm = solutionMap.get(keyOfLocations);
                this.rubiksCube.executeAlgorithm(algorithm, RubiksCube.RecordAlgorithm.YES);

            }

        }
    }


    public void solveCross() {

        Integer[][] stickersToSolve = {
                {RubiksCube.WHITE, RubiksCube.GREEN},
                {RubiksCube.WHITE, RubiksCube.BLUE},
                {RubiksCube.WHITE, RubiksCube.ORANGE},
                {RubiksCube.WHITE, RubiksCube.RED},
        };

        // Get all cross pieces on down layer
        crossStep1(stickersToSolve);
        // Rotate bottom layer until at least 2 pieces are in correct locations
        crossStep2();
        //Set the cubies that are in correct locations to their correct orientations
        ArrayList<CubeLayer.Cubie> crossCubiesToSwap = crossStep3();

        // Iff 2 cubies are in incorrect locations then swap them.
        //  (set those 2 cubies to correct orientations IFF need to)
        crossStep4(crossCubiesToSwap);
    }


    // TODO: Test me!   (8/10 tested)
    private void f2lStep1(Integer[][] f2lPairCubies) {

        // Locate both cubies of the current f2l pair
        List<Integer> cubie1 = Arrays.asList(f2lPairCubies[0]);
        List<Integer> cubie2 = Arrays.asList(f2lPairCubies[1]);
        ArrayList<SurfaceName> cubie1Location = this.rubiksCube.findLocationOfCubie(cubie1);
        ArrayList<SurfaceName> cubie2Location = this.rubiksCube.findLocationOfCubie(cubie2);
        Collections.sort(cubie1Location);
        Collections.sort(cubie2Location);

        List<SurfaceName> cubie1CorrectLocation = this.rubiksCube.correctLocationOfCubie(f2lPairCubies[0]);
        List<SurfaceName> cubie2CorrectLocation = this.rubiksCube.correctLocationOfCubie(f2lPairCubies[1]);
        Collections.sort(cubie1CorrectLocation);
        Collections.sort(cubie1CorrectLocation);

        // If one or both are slotted incorrectly, then remove them from that location
        // using the 'removal maneuver'

        // If the corner cubie is in the incorrect spot on the D layer.
        if (!cubie1Location.equals(cubie1CorrectLocation) && cubie1Location.contains(SurfaceName.D)) {
            removalManeuver(cubie1Location);
        }

        // If the edge cubie is in the incorrect spot on the middle layer.
        if (!cubie2Location.equals(cubie2CorrectLocation)
                && !cubie2Location.contains(SurfaceName.U)
                && !cubie2Location.contains(SurfaceName.D)) {
            removalManeuver(cubie2Location);
        }

    }


    private JSONObject getF2L_Config() {

        JSONObject result = null;
        JSONParser parser = new JSONParser();
        try {

            InputStream fileStream = rootView.getContext().getAssets().open("F2L_Config.json");
            Object obj = parser.parse(new BufferedReader(new InputStreamReader(fileStream)));
            result = (JSONObject) obj;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }




    // TODO: Test Me!
    private void f2lStep2(Integer[][] f2lPair) {

        JSONObject f2lJSON = getF2L_Config();

        JSONArray fullF2L = (JSONArray) f2lJSON.get("F2L");
//        String case1 = fullF2L.get(0).toString();

        ArrayList<Integer> cubie1AsColors = new ArrayList<> (Arrays.asList(f2lPair[0]) );
        ArrayList<Integer> cubie2AsColors = new ArrayList<> (Arrays.asList(f2lPair[1]) );

        CubeLayer.Cubie cubie1 = this.rubiksCube.getCubieByColorStickers( cubie1AsColors );
        CubeLayer.Cubie cubie2 = this.rubiksCube.getCubieByColorStickers( cubie2AsColors );


        // Rotate the top layer to correctly align the F2L pair iff:
        //      1. Both of the F2L cubies are in the U layer.
        //      2. One F2L cubie is in the U layer and the other is in the correct location.
        if( ( cubie1.getLocation().contains("U") && cubie2.getLocation().contains("U") )
                || (cubie1.getLocation().contains("U") && this.rubiksCube.isCorrectCubieAtThisLocation(cubie2.getLocation().split(" ")))
                || (cubie2.getLocation().contains("U") && this.rubiksCube.isCorrectCubieAtThisLocation(cubie1.getLocation().split(" ")))
        ){

            boolean correctAlignment = false;
            while(! correctAlignment){
                this.rubiksCube.executeAlgorithm("U", RubiksCube.RecordAlgorithm.YES);

                String algorithmToExecute = "";
                for(Object currentF2LCase : fullF2L){

                    // Compare current orientation to the current f2l pair in the json file.
                    correctAlignment = correctF2LFound(currentF2LCase, cubie1, cubie2);
                    if(correctAlignment){
                        algorithmToExecute = (String) ((JSONObject)currentF2LCase).get("SolutionAlgorithm");
                        break;
                    }
                }

                ArrayList<SurfaceName> locationOfCubie1 = this.rubiksCube.findLocationOfCubie(cubie1AsColors);
                algorithmToExecute = orientationTransform(algorithmToExecute, locationOfCubie1);

                // Execute the corresponding algorithm using the (orientation transform ) function.
                this.rubiksCube.executeAlgorithm(algorithmToExecute, RubiksCube.RecordAlgorithm.YES);
            }
        }

    }

    // TODO: Test me !
    private ArrayList< Map<String, String> > convertFileOrientation(Object currentF2LCase){


        ArrayList< Map<String, String> > result = new ArrayList<>();

        JSONObject cubie1FromFile = (JSONObject) ((JSONObject)currentF2LCase).get("Cubie1");
        JSONObject cubie2FromFile = (JSONObject) ((JSONObject)currentF2LCase).get("Cubie2");
        JSONObject[] bothCubies = {cubie1FromFile, cubie2FromFile};


        Integer color1 = this.rubiksCube.getLayerByLetter("D").getCenterColor();
        Integer color2 = this.rubiksCube.getLayerByLetter("F").getCenterColor();
        Integer color3 = this.rubiksCube.getLayerByLetter("R").getCenterColor();
        Integer color4 = this.rubiksCube.getLayerByLetter("U").getCenterColor();
        Integer color5 = this.rubiksCube.getLayerByLetter("L").getCenterColor();
        Integer color6 = this.rubiksCube.getLayerByLetter("B").getCenterColor();
        ArrayList<String> colorsList = new ArrayList<>(Arrays.asList(
                CubeLayer.colorIntToString( color1 ) ,
                CubeLayer.colorIntToString( color2 ) ,
                CubeLayer.colorIntToString( color3 ) ,
                CubeLayer.colorIntToString( color4 ) ,
                CubeLayer.colorIntToString( color5 ) ,
                CubeLayer.colorIntToString( color6 )

        ));


        for (JSONObject currentCubie : bothCubies) {

            Map<String, String> coloredOrientation = new HashMap<>();

            Map<String, String> orientation = new HashMap<>((JSONObject) currentCubie.get("Orientation"));
            Object[] keys = orientation.keySet().toArray();
            Iterator<String> values = orientation.values().iterator();

            // Loop through keys() and values() lists
            int keyIndex = 0;
            while(values.hasNext() ){

                String key = keys[keyIndex].toString();
                String value = values.next();

                // Check if each string contains 1, 2, 3, or 4
                // Based on the number that is found create a new HashMap<> with the colorsX replacing X
                String newKey = getColorByNumber(key, colorsList);
                String newValue = getColorByNumber(value, colorsList);

                coloredOrientation.put(newKey, newValue);


                keyIndex++;
            }

            result.add(coloredOrientation);
        }


        return result;

    }

    private String getColorByNumber(String currentString, ArrayList<String> colorsList){

        // *_COLOR_1 = color1   *_COLOR_2 = color2   *_COLOR_3 = color3   *_COLOR_4 = color4

        String result = "";
        if(currentString.contains("1")){
            result = colorsList.get(0);
        }else if(currentString.contains("2")){
            result = colorsList.get(1);
        }else if(currentString.contains("3")){
            result = colorsList.get(2);
        }else if(currentString.contains("4")){
            result = colorsList.get(3);
        }else if(currentString.contains("5")){
            result = colorsList.get(4);
        }else if(currentString.contains("6")){
            result = colorsList.get(5);
        }

        return result;
    }


    // TODO: Test me !
    private boolean correctF2LFound(Object currentF2LCase, CubeLayer.Cubie cubie1, CubeLayer.Cubie cubie2){

        ArrayList< Map<String, String> > bothCubies = convertFileOrientation(currentF2LCase); // TODO: Not sure if you need to convert to colors
        Map<String, String> cubie1OrientationFromFile = bothCubies.get(0);
        Map<String, String> cubie2OrientationFromFile = bothCubies.get(1);


        boolean result = false;

        Map<String, String> cubie1Orientation = cubie1.getCubieOrientation();
        Map<String, String> cubie2Orientation = cubie2.getCubieOrientation();

        // TODO: Check if the locations of cubie1 and cubie2 match the location at currentF2LCase
        // TODO: Check if the orientations of cubie1 and cubie2 match that of currentF2LCase (without comparing colors)
        // TODO: Proper way to check if two HashMaps have equal key value pairs (not sure if this is needed ? )
//        boolean isEqual = hashMap1.equals(hashMap2) && hashMap2.equals(hashMap1) &&
//                hashMap1.entrySet().containsAll(hashMap2.entrySet()) &&
//                hashMap2.entrySet().containsAll(hashMap1.entrySet());

        if(cubie1OrientationFromFile.equals(cubie1Orientation)
                && cubie2OrientationFromFile.equals(cubie2Orientation) ){
            result = true;
        }


        return result;
    }


    // TODO: Test me!     (8/10 tested)
    private String orientationTransform(String originalAlgorithm, ArrayList<SurfaceName> cubiesCurrentLocation) {

        //   PAPER EXPECTED  -> [F, R, B, L] [F, R, B, L] [F, R, B, L] [F, R, B, L]
        //   REPLACEMENT     -> [F, R, B, L] [R, B, L, F] [B, L, F, R] [L, F, R, B]
        //                       _  _         _  _         _  _         _  _


        String[] paperExpected = {};
        if (cubiesCurrentLocation.contains(SurfaceName.F) && cubiesCurrentLocation.contains(SurfaceName.R)) {
            paperExpected = new String[]{"F", "R", "B", "L"};
        } else if (cubiesCurrentLocation.contains(SurfaceName.R) && cubiesCurrentLocation.contains(SurfaceName.B)) {
            paperExpected = new String[]{"R", "B", "L", "F"};
        } else if (cubiesCurrentLocation.contains(SurfaceName.B) && cubiesCurrentLocation.contains(SurfaceName.L)) {
            paperExpected = new String[]{"B", "L", "F", "R"};
        } else if (cubiesCurrentLocation.contains(SurfaceName.L) && cubiesCurrentLocation.contains(SurfaceName.F)) {
            paperExpected = new String[]{"L", "F", "R", "B"};
        }

        // Pre-programmed orientation (rubiks cube data structure)
        final String[] finalPaperExpected = paperExpected;
        Map<String, String> transformMap = new HashMap<String, String>() {
            {
                put(SurfaceName.F.name(), finalPaperExpected[0]);
                put(SurfaceName.R.name(), finalPaperExpected[1]);
                put(SurfaceName.B.name(), finalPaperExpected[2]);
                put(SurfaceName.L.name(), finalPaperExpected[3]);
            }
        };

        char[] originalAlgAsArray = originalAlgorithm.toCharArray();
        for (int i = 0; i < originalAlgAsArray.length; i++) {

            char currentMove = originalAlgAsArray[i];
            String replacementChar = transformMap.get(Character.toString(currentMove));

            // If the current char is one of the following 3 [ '  , 2  , SPACE]
            if (replacementChar != null) {
                originalAlgAsArray[i] = replacementChar.charAt(0);
            }

        }

        return new String(originalAlgAsArray);
    }


    // TODO: Test me!      (8/10 tested)
    private void removalManeuver(ArrayList<SurfaceName> cubiesCurrentLocation) {


        String removalAlgorithm = "";

        // Corner Cubie
        if (cubiesCurrentLocation.size() == 3) {
            removalAlgorithm = "R U R'";
        }
        // Edge Cubie
        else if (cubiesCurrentLocation.size() == 2) {
            removalAlgorithm = "U R U' R' U' F' U F";
        }


        // Only want the F or R or L or B
        cubiesCurrentLocation.remove(SurfaceName.D);

        // Convert the removal algorithm into the correct orientation.
        removalAlgorithm = orientationTransform(
                removalAlgorithm, cubiesCurrentLocation);

        this.rubiksCube.executeAlgorithm(removalAlgorithm, RubiksCube.RecordAlgorithm.YES);

    }


    public void f2lSolutionSteps(Integer[][][] stickersToSolve) {

        for (Integer[][] f2lPair : stickersToSolve) {
            f2lStep1(f2lPair);
            f2lStep2(f2lPair);
        }

    }

    public void solveF2L() {
        Integer[][][] stickersToSolve = {
                {{RubiksCube.WHITE, RubiksCube.BLUE, RubiksCube.ORANGE}, {RubiksCube.BLUE, RubiksCube.ORANGE}},
                {{RubiksCube.WHITE, RubiksCube.BLUE, RubiksCube.RED}, {RubiksCube.BLUE, RubiksCube.RED}},
                {{RubiksCube.WHITE, RubiksCube.RED, RubiksCube.GREEN}, {RubiksCube.RED, RubiksCube.GREEN}},
                {{RubiksCube.WHITE, RubiksCube.GREEN, RubiksCube.ORANGE}, {RubiksCube.GREEN, RubiksCube.ORANGE}},
        };

        f2lSolutionSteps(stickersToSolve);


    }

    public void ollSolutionSteps(Integer[][] stickersToSolve) {
        // TODO: Complete solution implementation
    }

    public void solveOLL() {
        ollSolutionSteps(topCubies);
    }


    public void pllSolutionSteps(Integer[][] stickersToSolve) {
        // TODO: Complete solution implementation
    }

    public void solvePLL() {
        pllSolutionSteps(topCubies);
    }


    public void solveCube() {

//        boolean cubeIsValid = rubiksCube.isValidState();

//        if( cubeIsValid ){
        solveCross();
        solveF2L();
        solveOLL();
        solvePLL();

//        }else{
//            Toast.makeText(rootView.getContext(), "Invalid State! ", Toast.LENGTH_LONG).show();
//
//        }

    }

}
