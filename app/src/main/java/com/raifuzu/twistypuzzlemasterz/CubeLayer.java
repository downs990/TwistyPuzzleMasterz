package com.raifuzu.twistypuzzlemasterz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import com.raifuzu.twistypuzzlemasterz.RubiksCube.SurfaceName;
import android.view.View;


public class CubeLayer {

    private SurfaceName surfaceName;
    private Integer[] surfaceColors;        // Length = 9
    private Integer[] surfaceBackColors;    // Length = 3
    private Integer[] surfaceRightColors;   // Length = 3
    private Integer[] surfaceFrontColors;   // Length = 3
    private Integer[] surfaceLeftColors;    // Length = 3
    private AdvancedArrayList<Integer[]> surfaceAndBorder;
    public AdvancedArrayList<Cubie> myCubies;

    private String cubeAsString;

    private View rootView;// Keep this. You might need to Toast.


    public CubeLayer(View rootView, String cubeAsString, AdvancedArrayList<Integer[]> surfaceAndBorder, SurfaceName surfaceName) {

        this.rootView = rootView;
        this.cubeAsString = cubeAsString;
        this.surfaceName = surfaceName;


        this.surfaceAndBorder = surfaceAndBorder;
        setAllLayerColors();

        initializeCubies();
    }


    public Integer toColor(int indexOfCubeAsString) {
        char colorLetter = cubeAsString.charAt(indexOfCubeAsString - 1);
        return RubiksCubeStructure.colorLetterToIntegerColor(colorLetter);
    }


    private void initializeCubies() {

        // Correctly set the locations of all cubies on this layer
        String[] locations = RubiksCube.cubieLocationsMap.get(this.surfaceName.name());

        // TODO: The order of each of these buttons for each sticker should automatically create the
        //     location String for each cubie. Instead of having to create RubiksCube.cubieLocationMap
        //     and managing the order of each location manually.

        // TODO: Use '.this' everywhere ?
        Cubie cubie1 = new Cubie(locations[0], toColor(surfaceColors[0]), toColor(surfaceBackColors[0]), toColor(surfaceLeftColors[2]) );
        Cubie cubie2 = new Cubie(locations[1], toColor(surfaceColors[1]), toColor(surfaceBackColors[1]));
        Cubie cubie3 = new Cubie(locations[2], toColor(surfaceColors[2]), toColor(surfaceBackColors[2]), toColor(surfaceRightColors[0]));
        Cubie cubie4 = new Cubie(locations[3], toColor(surfaceColors[3]), toColor(surfaceLeftColors[1]));
        Cubie cubie5 = new Cubie(locations[4], toColor(surfaceColors[4]) );
        Cubie cubie6 = new Cubie(locations[5], toColor(surfaceColors[5]), toColor(surfaceRightColors[1]));
        Cubie cubie7 = new Cubie(locations[6], toColor(surfaceColors[6]), toColor(surfaceLeftColors[0]), toColor(surfaceFrontColors[2]));
        Cubie cubie8 = new Cubie(locations[7], toColor(surfaceColors[7]), toColor(surfaceFrontColors[1]));
        Cubie cubie9 = new Cubie(locations[8], toColor(surfaceColors[8]), toColor(surfaceRightColors[2]), toColor(surfaceFrontColors[0]));

        // Initialize myCubies
        this.myCubies = new AdvancedArrayList<>(cubie1, cubie2, cubie3, cubie4, cubie5, cubie6, cubie7, cubie8, cubie9);
    }

    public AdvancedArrayList<Cubie> getAllCubies() {
        return this.myCubies;
    }

    public AdvancedArrayList<Integer[]> getSurfaceAndBorderColors() {
        return this.surfaceAndBorder;
    }

    public void setAllLayerColors() {
        this.surfaceColors      = this.surfaceAndBorder.get(0);
        this.surfaceBackColors  = this.surfaceAndBorder.get(1);
        this.surfaceRightColors = this.surfaceAndBorder.get(2);
        this.surfaceFrontColors = this.surfaceAndBorder.get(3);
        this.surfaceLeftColors  = this.surfaceAndBorder.get(4);
    }

    public Cubie getCenterCubie(){
         return null; // TODO: Implement me !
    }

    public Integer[] getSurfaceColors() {
        return this.surfaceColors;
    }

    public void setSurfaceColors(Integer[] surfaceColors) {
        this.surfaceColors = surfaceColors;
    }

    public Integer[] getSurfaceBackColors() {
        return this.surfaceBackColors;
    }
 

    public Integer[] getSurfaceRightColors() {
        return this.surfaceRightColors;
    }
 

    public Integer[] getSurfaceFrontColors() {
        return this.surfaceFrontColors;
    }
 

    public Integer[] getSurfaceLeftColors() {
        return this.surfaceLeftColors;
    }
 

    /**
     * This method updates all of the colors of each layer in the cubes data
     * structure to a solved state. It also updates the UI to display the UI
     * cube in a solved state.
     *
//     * @param surfaceColor
//     * @param surfaceBackColor
//     * @param surfaceRightColor
//     * @param surfaceFrontColor
//     * @param surfaceLeftColor
     */
//    public void initializeSolvedColors(Integer surfaceColor, Integer surfaceBackColor, Integer surfaceRightColor,
//                                       Integer surfaceFrontColor, Integer surfaceLeftColor) {
//
//
//        // lengths =                9            3                  3                   3               3
//        Integer[] layerColors = {surfaceColor, surfaceBackColor, surfaceRightColor, surfaceFrontColor, surfaceLeftColor};
//
//        // surfaceAndBorder includes the following arrays:
//        // surface[], s back[], s right[], s front[], s left[]
//        int index = 0;
//        for (Integer[] stickersList : this.surfaceAndBorder) {
//            Arrays.fill(stickersList, layerColors[index]);
//            index++;
//        }
//
//        initializeCubies();
//    }

    // TODO: Test me! Does this.surfaceAndBorder actually get updated???
    @SuppressWarnings("unchecked")
    public void initializeScrambledColors(AdvancedArrayList<Integer>... colorLists) {

        // Setting surfaceAndBorder with what's passed as an argument.
        int surfaceBorderIndex = 0;
        for (Integer[] stickersList : this.surfaceAndBorder) {
            for (int i = 0; i < stickersList.length; i++) {
                stickersList[i] = colorLists[surfaceBorderIndex].get(i);
            }
            surfaceBorderIndex++;
        }

        setAllLayerColors();
        initializeCubies();
    }

    public SurfaceName getSideName() {
        return this.surfaceName;
    }

    public static String colorIntToString(Integer colorNum) {
        String colorString = "unknown_color"; // impossible, hopefully.

        Map<String, Integer> colorsMap = RubiksCube.colorsMap;

        Object[] keys = colorsMap.keySet().toArray();
        Iterator<Integer> values = colorsMap.values().iterator();

        int i = 0;
        while (values.hasNext()) {
            Integer value = values.next();

            if (Objects.equals(value, colorNum)) {
                colorString = keys[i].toString();
            }
            i++;
        }

        return colorString;
    }


    private String arrayToString(Integer[] listOfColors) {
        StringBuilder output = new StringBuilder();

        for (Integer listOfColor : listOfColors) {
            output.append(" ").append(colorIntToString(listOfColor));
        }
        return output.toString();
    }


    @Override
    public String toString() {
        String output = "";

        String surfaceString = arrayToString(this.surfaceColors);
        output += "\n\nSurface: " + this.surfaceName.toString() + "\n" + surfaceString;
        String surfaceBackString = arrayToString(this.surfaceBackColors);
        output += "\nSurface Back: \n" + surfaceBackString;
        String surfaceRightString = arrayToString(this.surfaceRightColors);
        output += "\nSurface Right: \n" + surfaceRightString;
        String surfaceFrontString = arrayToString(this.surfaceFrontColors);
        output += "\nSurface Front: \n" + surfaceFrontString;
        String surfaceLeftString = arrayToString(this.surfaceLeftColors);
        output += "\nSurface Left: \n" + surfaceLeftString;

        return output;
    }



    /**
     * The purpose of the Cubie class is to make it easier to check if the user
     * input into the UI cube was valid. It also helps to make the process of
     * solving the cube more clean.
     *
     * @author downs
     */
    public static class Cubie {

        // Location is a string representation of all faces that intersects this cubie
        private final String location; // Ex. "F R U" or "B L"

        private Map<String, String> cubieOrientation;

 
        private ArrayList<Integer> stickerColorsList;

        private RubiksCube.CubieType type;
        public Cubie(String location, Integer... stickers) {


            this.cubieOrientation = new HashMap<>(); 
            this.stickerColorsList = new ArrayList<>(Arrays.asList( stickers) );



            this.location = location; 

            if(stickers.length == 1){
                this.type = RubiksCube.CubieType.CENTER;
            }else if(stickers.length == 2){
                this.type = RubiksCube.CubieType.EDGE;
            }else if(stickers.length == 3){
                this.type = RubiksCube.CubieType.CORNER;
            }
        }

        public void setCubieOrientation(Map<String, String> newOrientation){
            this.cubieOrientation = newOrientation;
        }

        public Map<String, String> getCubieOrientation(){
            return this.cubieOrientation;
        }


        // TODO: Use for F2L, PLL, and OLL
        public boolean isOrientationCorrect(){

            boolean isCorrectOrientation = true;
            Set<String> keys = this.cubieOrientation.keySet();

            // Make sure that each key is equal to it's corresponding value.
            for(String key : keys){

                String value =  this.cubieOrientation.get(key);
                if( ! value.equals(key)){
                    isCorrectOrientation = false;
                }
            }

            return isCorrectOrientation;
        }
 

        public String getLocation(){
            return this.location;
        }


        public ArrayList<Integer> getStickerColors() {
            return this.stickerColorsList;
        }

        public void setStickersColors(ArrayList<Integer> newStickerColors){
            this.stickerColorsList  = newStickerColors;
        }

        public ArrayList<String> getStickerColorsStrings(){

            ArrayList<String> result = new ArrayList<>();
            for(Integer value : this.stickerColorsList){

                String color = CubeLayer.colorIntToString(value);
                result.add(color);
            }



            return result;
        } 

        public RubiksCube.CubieType getCubieType(){
            return type;
        }


        @Override
        public String toString() {
            StringBuilder output = new StringBuilder("");
            for (int i = 0; i < this.stickerColorsList.size(); i++) {
                // Converts the sticker list into a string with sticker color names
                Integer currentColor = this.stickerColorsList.get(i); 
                String stickerColorName = colorIntToString(currentColor);
                output.append(" ").append(stickerColorName);
            }

            return output.toString();
        }

    }

}
