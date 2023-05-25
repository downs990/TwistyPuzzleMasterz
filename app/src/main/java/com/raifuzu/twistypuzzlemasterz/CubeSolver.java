package com.raifuzu.twistypuzzlemasterz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
     * @return string representation of the solution algorithm to put those cubies in the correct places.
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
                // Continue to next cubie to solve.
            }

            // If the cubie on 'middle' layer
            else{
                // [L, F] ->   L  or F'                     <-- left
                // [L, B] ->   L' or B
                // [R, F] ->   R' or F                      <-- right
                // [R, B] ->   R  or B'

                if(intersectingLayers.contains(SurfaceName.R)){


                    String[] intersection = {SurfaceName.R.name() ,  SurfaceName.D.name()};
                    // Check if a white cubie already exists at the location [R,D]
                    // If it does, then do avoidance maneuver.
                    List<Integer> currentCubie = this.rubiksCube.getCubieAtLocation( intersection );

                    if(currentCubie.contains(RubiksCube.WHITE)){
                        this.avoidanceManeuverForCross();
                    }else{

                        // R'
                        if(intersectingLayers.contains(SurfaceName.F)){
                            solutionAlgorithm.append("R' ");
                        }
                        // R
                        else if(intersectingLayers.contains(SurfaceName.B)){
                            solutionAlgorithm.append("R ");
                        }
                    }




                }else if(intersectingLayers.contains(SurfaceName.L)){
                    // Check if a white cubie already exists at the location [L,D]
                    // If it does, then do avoidance maneuver. Else, just do L or L'
                }

            }

        }

        return solutionAlgorithm.toString();
    }


    private void avoidanceManeuverForCross(){

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
        // TODO: Complete solution implementation
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

    public String ollSolutionSteps(  Integer[][] stickersToSolve){
        // TODO: Complete solution implementation
        return null;
    }

    public void solveOLL(){
        String ollSolutionAlgorithm = ollSolutionSteps( topCubies );
        this.rubiksCube.executeAlgorithm(ollSolutionAlgorithm);
    }



    public String pllSolutionSteps(  Integer[][] stickersToSolve){
        // TODO: Complete solution implementation
        return null;
    }

    public void solvePLL(){
        String pllSolutionAlgorithm = pllSolutionSteps( topCubies );
        this.rubiksCube.executeAlgorithm(pllSolutionAlgorithm);
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
