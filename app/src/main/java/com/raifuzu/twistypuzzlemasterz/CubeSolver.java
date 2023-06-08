package com.raifuzu.twistypuzzlemasterz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.Surface;
import android.view.View;
import android.widget.Toast;
import com.raifuzu.twistypuzzlemasterz.RubiksCube.SurfaceName;


/**
 * NOTE: This class solves the Rubik's Cube using the CFOP method.
 * @author downs
 */
public class CubeSolver {


    Integer[][] topCubies = {
            {RubiksCube.YELLOW, RubiksCube.BLUE, RubiksCube.ORANGE},
            {RubiksCube.YELLOW, RubiksCube.BLUE} ,
            {RubiksCube.YELLOW, RubiksCube.BLUE, RubiksCube.RED} ,
            {RubiksCube.YELLOW, RubiksCube.RED},
            {RubiksCube.YELLOW, RubiksCube.RED, RubiksCube.GREEN},
            {RubiksCube.YELLOW, RubiksCube.GREEN} ,
            {RubiksCube.YELLOW, RubiksCube.GREEN, RubiksCube.ORANGE} ,
            {RubiksCube.YELLOW, RubiksCube.ORANGE}
    };

    private final RubiksCubeStructure rubiksCube;
    private final View rootView;


    public CubeSolver(View rootView, RubiksCubeStructure rubiksCube) {
        this.rootView = rootView;
        this.rubiksCube = rubiksCube;
    }

    /**
     *   1. Get all pieces on down layer
     *
     * @param stickersToSolve Unique stickers list of the specific cubies to solve
     */
    public void crossStep1(  Integer[][] stickersToSolve){

        for(Integer[] cubie : stickersToSolve){

            // Length of cubieAtList and intersectingLayers will always be 2
            List<Integer> cubieAsList = Arrays.asList(cubie); 
            ArrayList<SurfaceName> intersectingLayers = this.rubiksCube.findLocationOfCubie(cubieAsList);

            int indexOfU = intersectingLayers.indexOf(SurfaceName.U);
            int indexOfNonU = (indexOfU == 0) ? 1 : 0;
            SurfaceName nonUSurface = intersectingLayers.get(indexOfNonU);



            // If cubie on 'up' layer
            if(intersectingLayers.contains(SurfaceName.U)){

                // Check if there is a white cubie on the D layer directly under U cubie.
                String[] locationDirectlyBelow = {nonUSurface.name(), SurfaceName.D.name()};
                CubeLayer.Cubie cubieDirectlyBelow = this.rubiksCube.getCubieAtLocation(locationDirectlyBelow);

                if(cubieDirectlyBelow.getStickerColors().contains(RubiksCube.WHITE)) {

                    // avoidance maneuver
                    avoidanceManeuverForCross(locationDirectlyBelow);
                }

                String sideToRotate = nonUSurface.name() + "2";
                this.rubiksCube.executeAlgorithm( sideToRotate , RubiksCube.RecordAlgorithm.YES);
            }

            // If the cubie on 'middle' layer
            else if( ! intersectingLayers.contains(SurfaceName.D) && ! intersectingLayers.contains(SurfaceName.U) ){

                ArrayList<String> locationAsString = new ArrayList<>();
                locationAsString.add( intersectingLayers.get(0).name() );
                locationAsString.add( intersectingLayers.get(1).name() );
                Collections.sort( locationAsString );

                // Possible locations to insert 'cubie' if it's location is case1
                String[][] locationsToCheck = { {locationAsString.get(0), "D"},
                                                {locationAsString.get(1), "D"}   };


                // locationsAsString   ->  possibleMoves          {  locationsToCheck }
                // [F, L] ->   F' or L                       // Check {  {F,D}, {L,D}  }
                // [B, L] ->   B  or L'                      // Check {  {B,D}, {L,D}  }
                // [F, R] ->   F  or R'                      // Check {  {F,D}, {R,D}  }
                // [B, R] ->   B' or R                       // Check {  {B,D}, {R,D}  }

                String keyToPossibleMovesMap = locationsToCheck[0][0] + " " + locationsToCheck[0][1]
                        + " , " + locationsToCheck[1][0] + " " + locationsToCheck[1][1];
                Map<String , String[]> possibleMovesMap = new HashMap<String , String[]>(){{
                        put("F D , L D"      , new String[]{"F'" , "L"});
                        put("B D , L D"      , new String[]{"B"  , "L'"});
                        put("F D , R D"      , new String[]{"F"  , "R'"});
                        put("B D , R D"      , new String[]{"B'" , "R"});
                }};


                List<Boolean> whitePieceExist = Arrays.asList(false, false);

                for(int i = 0; i < locationsToCheck.length; i++){

                    String[] checkingLocation = locationsToCheck[i];

                    CubeLayer.Cubie currentCubie1 = this.rubiksCube.getCubieAtLocation( checkingLocation );
                    ArrayList<Integer> currentCubie = currentCubie1.getStickerColors();

                    // Check if a white cubie already exists at the checking location. If yes, do avoidance.
                    if(currentCubie.contains(RubiksCube.WHITE)){
                        whitePieceExist.set(i, true);
                    }
                }


                // Check if both possible moves have white pieces at their locations. If yes, do avoidance.
                String[] possibleMoves = possibleMovesMap.get( keyToPossibleMovesMap );
                if( whitePieceExist.get(0) && whitePieceExist.get(1)){

                    this.avoidanceManeuverForCross( locationsToCheck[0] );
                    String moveToExecute = possibleMoves[0];
                    this.rubiksCube.executeAlgorithm(moveToExecute, RubiksCube.RecordAlgorithm.YES);
                }
                // Else, perform correct insertion.
                else{

                    int indexOfFalse = whitePieceExist.indexOf(false);
                    String moveToExecute = possibleMoves[indexOfFalse];
                    this.rubiksCube.executeAlgorithm(moveToExecute, RubiksCube.RecordAlgorithm.YES);

                }

            }
        }
    }


    private void avoidanceManeuverForCross(String[] locationDirectlyBelow){

        CubeLayer.Cubie cubieDirectlyBelow = this.rubiksCube.getCubieAtLocation(locationDirectlyBelow);
        ArrayList<Integer> stickers = cubieDirectlyBelow.getStickerColors();

        while(stickers.contains(RubiksCube.WHITE) ){

            // Actually Execute a D rotation
            this.rubiksCube.executeAlgorithm("D", RubiksCube.RecordAlgorithm.YES);

            cubieDirectlyBelow = this.rubiksCube.getCubieAtLocation(locationDirectlyBelow);
            stickers = cubieDirectlyBelow.getStickerColors();
        }
    }

    private int crossPiecesInCorrectLocation(){
        String[] location1 = {SurfaceName.F.name(), SurfaceName.D.name()};
        String[] location2 = {SurfaceName.R.name(), SurfaceName.D.name()};
        String[] location3 = {SurfaceName.B.name(), SurfaceName.D.name()};
        String[] location4 = {SurfaceName.L.name(), SurfaceName.D.name()};
        String[][] locations = {location1, location2, location3, location4};

        int numOfCorrectLocations = 0;
        for(String[] location : locations){
            boolean isCorrect = this.rubiksCube.isCorrectCubieAtThisLocation(location);
            if(isCorrect){
                numOfCorrectLocations++;
            }
        }
        return numOfCorrectLocations;
    }


    public void crossStep2(){

        int numOfCorrectLocations = crossPiecesInCorrectLocation();
        while(numOfCorrectLocations < 2){

            this.rubiksCube.executeAlgorithm("D", RubiksCube.RecordAlgorithm.YES);
            numOfCorrectLocations = crossPiecesInCorrectLocation();
        }
    }




    public void crossStep3(){

    }

    public void crossStep4(){

    }


    public void solveCross(){

        Integer[][] stickersToSolve = {
                {RubiksCube.WHITE, RubiksCube.GREEN},
                {RubiksCube.WHITE, RubiksCube.BLUE},
                {RubiksCube.WHITE, RubiksCube.ORANGE},
                {RubiksCube.WHITE, RubiksCube.RED},
        };

        // Get all cross pieces on down layer
        crossStep1(   stickersToSolve  );
        // Rotate bottom layer until at least 2 pieces are in correct locations
       crossStep2(); 
        //Set the cubies that are in correct locations to their correct orientations
        crossStep3();

        // Iff 2 cubies are in incorrect locations then swap them.
        //  (set those 2 cubies to correct orientations IFF need to)
        crossStep4();



//        Toast.makeText(rootView.getContext(), "Solution: " + this.rubiksCube.getSolutionAlgorithm(), Toast.LENGTH_LONG).show();
    }


    public void f2lSolutionSteps(  Integer[][][] stickersToSolve){
        // TODO: Complete solution implementation
    }

    public void solveF2L(){
        Integer[][][] stickersToSolve = {
                {{RubiksCube.WHITE, RubiksCube.BLUE, RubiksCube.ORANGE}, { RubiksCube.BLUE, RubiksCube.ORANGE}},
                {{RubiksCube.WHITE, RubiksCube.BLUE, RubiksCube.RED}, { RubiksCube.BLUE, RubiksCube.RED}},
                {{RubiksCube.WHITE, RubiksCube.RED, RubiksCube.GREEN}, { RubiksCube.RED, RubiksCube.GREEN}},
                {{RubiksCube.WHITE, RubiksCube.GREEN, RubiksCube.ORANGE}, { RubiksCube.GREEN, RubiksCube.ORANGE}},
        };

        f2lSolutionSteps( stickersToSolve );
    }

    public void ollSolutionSteps(  Integer[][] stickersToSolve){
        // TODO: Complete solution implementation
    }

    public void solveOLL(){
        ollSolutionSteps( topCubies );
    }



    public void pllSolutionSteps(  Integer[][] stickersToSolve){
        // TODO: Complete solution implementation
    }

    public void solvePLL(){
        pllSolutionSteps( topCubies );
    }




    public void solveCube(){

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
