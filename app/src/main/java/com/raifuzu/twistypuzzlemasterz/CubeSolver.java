package com.raifuzu.twistypuzzlemasterz;

import java.util.ArrayList;
import java.util.Arrays;
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
     *   2. Rotate down layer until at least 2 pieces are in correct location
     *   3. Set the cubies that are in correct locations to their correct orientation
     *   4. Iff 2 cubies are in incorrect locations then swap them
     *   5. Iff needed, set those 2 cubies to correct orientation
     *
     * @param stickersToSolve Unique stickers list of the specific cubies to solve
     */
    public void crossSolutionSteps(  Integer[][] stickersToSolve){

        for(Integer[] cubie : stickersToSolve){

            // Length of cubieAtList and intersectingLayers will always be 2
            List<Integer> cubieAsList = Arrays.asList(cubie);
            ArrayList<SurfaceName> intersectingLayers = this.rubiksCube.findLocationOfCubie(cubieAsList);

            int indexOfU = intersectingLayers.indexOf(SurfaceName.U);
            int indexOfNonU = (indexOfU == 0) ? 1 : 0;
            SurfaceName nonUSurface = intersectingLayers.get(indexOfNonU);
//

            // If cubie on 'up' layer
            if(intersectingLayers.contains(SurfaceName.U)){
                // Check if there is a white cubie on the D layer directly under U cubie.
                String[] locationDirectlyBelow = {nonUSurface.name(), SurfaceName.D.name()};
                CubeLayer.Cubie cubieDirectlyBelow = this.rubiksCube.getCubieAtLocation(locationDirectlyBelow);

                if(cubieDirectlyBelow.stickerColorsList.contains(RubiksCube.WHITE)) {
                    // avoidance maneuver
                    avoidanceManeuverForCross(locationDirectlyBelow);
                }else{
                    String sideToRotate = nonUSurface.name() + "2";
                    this.rubiksCube.executeAlgorithm( sideToRotate );
                }
            }

            // If the cubie on 'middle' layer
            if( ! intersectingLayers.contains(SurfaceName.D) && ! intersectingLayers.contains(SurfaceName.U) ){


                Map<String, String[]> avoidanceManeuverMap = new HashMap<>();
                // [L, F] ->   L  or F'               // Check {  {L,D}, {F,D}  }
                // [L, B] ->   L' or B                // Check {  {L,D}, {B,D}  }
                // [R, F] ->   R' or F                // Check {  {R,D}, {F,D}  }
                // [R, B] ->   R  or B'               // Check {  {R,D}, {B,D}  }

                // TODO: Create map to check avoidance maneuver


                String[] intersection1 = {SurfaceName.R.name() ,  SurfaceName.D.name()};
                // Check if a white cubie already exists at the location [R,D]
                // If it does, then do avoidance maneuver.
                CubeLayer.Cubie currentCubie1 = this.rubiksCube.getCubieAtLocation( intersection1 );
                ArrayList<Integer> currentCubie = currentCubie1.getStickerColors();

                String s = Arrays.toString(intersection1) + " = " + currentCubie1 + "\n";
//                Toast.makeText(rootView.getContext(), s, Toast.LENGTH_LONG).show();

                if(currentCubie.contains(RubiksCube.WHITE)){
                    this.avoidanceManeuverForCross( intersection1 );
                }else{


                    if(intersectingLayers.contains(SurfaceName.F)){
                        this.rubiksCube.executeAlgorithm("R'");// R inverted
                    }
                    else if(intersectingLayers.contains(SurfaceName.B)){
                        this.rubiksCube.executeAlgorithm("R");// R
                    }
                }




            }

        }
    }


    // TODO: Test this
    private void avoidanceManeuverForCross(String[] locationDirectlyBelow){

        CubeLayer.Cubie cubieDirectlyBelow = this.rubiksCube.getCubieAtLocation(locationDirectlyBelow);
        ArrayList<Integer> stickers = cubieDirectlyBelow.getStickerColors();

        while(stickers.contains(RubiksCube.WHITE) ){
            cubieDirectlyBelow = this.rubiksCube.getCubieAtLocation(locationDirectlyBelow);
            stickers = cubieDirectlyBelow.getStickerColors();

            // Actually Execute a D rotation
            this.rubiksCube.executeAlgorithm("D");
        }



    }


    public void solveCross(){

        Integer[][] stickersToSolve = {
                {RubiksCube.WHITE, RubiksCube.GREEN},
                {RubiksCube.WHITE, RubiksCube.BLUE},
                {RubiksCube.WHITE, RubiksCube.ORANGE},
                {RubiksCube.WHITE, RubiksCube.RED},
        };

        crossSolutionSteps(   stickersToSolve  );

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
