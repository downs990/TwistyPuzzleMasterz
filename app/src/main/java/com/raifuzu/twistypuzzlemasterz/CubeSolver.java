package com.raifuzu.twistypuzzlemasterz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.view.View;
import android.widget.Toast;
import com.raifuzu.twistypuzzlemasterz.RubiksCube.SurfaceName;


/**
 * NOTE: This class solves the Rubik's Cube using the CFOP method.
 * @author downs
 */
public class CubeSolver {



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
     * @param stickersToSolve
     * @return
     */
    public String crossSolutionSteps(  Integer[][] stickersToSolve){

        StringBuilder solutionAlgorithm = new StringBuilder();

        for(Integer[] cubie : stickersToSolve){
            List<Integer> cubieAsList = Arrays.asList(cubie);
            ArrayList<SurfaceName> intersectingLayers = this.rubiksCube.findLocationOfCubie(cubieAsList);

            // If cubie on 'up' layer
            if(intersectingLayers.contains(SurfaceName.U)){
                if(intersectingLayers.contains(SurfaceName.R)){
                    solutionAlgorithm.append("R2 ");
                }
                else if(intersectingLayers.contains(SurfaceName.F)){
                    solutionAlgorithm.append("F2 ");
                }
                else if(intersectingLayers.contains(SurfaceName.L)){
                    solutionAlgorithm.append("L2 ");
                }
                else if(intersectingLayers.contains(SurfaceName.B)){
                    solutionAlgorithm.append("B2 ");
                }
            }
            // If the cubie on 'down' layer
            else if(intersectingLayers.contains(SurfaceName.D)){
                // LOGIC:
                // - Maintain location on down layer while putting other cubies down here.
                // - Rotate down layer such that there is no white cubie on the spot
                //      that intersects with down and the chosen layer to move.
            }

            // If the cubie on 'middle' layer
            else{
                // [L, F] ->   L  or F'
                // [R, F] ->   R' or F
                // [L, B] ->   L' or B
                // [R, B] ->   R  or B'

                // LOGIC:
                // - Prefer the move that puts the cubie in the correct location.
                //                or
                // - Move that wont kick out an already positioned white cross cubie on down layer.
            }

        }



        return solutionAlgorithm.toString();
    }






    public void solveCross(){

        Integer[][] stickersToSolve = {
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

        boolean cubeIsValid = rubiksCube.isValidState();

        if( cubeIsValid ){
            solveCross();
            solveF2L();
            solveOLL();
            solvePLL();

        }else{
            Toast.makeText(rootView.getContext(), "Invalid State! ", Toast.LENGTH_LONG).show();

        }

    }

}