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
    private Integer[] surfaceColorIndexes;        // Length = 9
    private Integer[] surfaceBackColorIndexes;    // Length = 3
    private Integer[] surfaceRightColorIndexes;   // Length = 3
    private Integer[] surfaceFrontColorIndexes;   // Length = 3
    private Integer[] surfaceLeftColorIndexes;    // Length = 3


    private Integer[] w1, w2, w3, w4;


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
        Cubie cubie1 = new Cubie(locations[0], toColor(surfaceColorIndexes[0]), toColor(surfaceBackColorIndexes[0]), toColor(surfaceLeftColorIndexes[2]) );
        Cubie cubie2 = new Cubie(locations[1], toColor(surfaceColorIndexes[1]), toColor(surfaceBackColorIndexes[1]));
        Cubie cubie3 = new Cubie(locations[2], toColor(surfaceColorIndexes[2]), toColor(surfaceBackColorIndexes[2]), toColor(surfaceRightColorIndexes[0]));
        Cubie cubie4 = new Cubie(locations[3], toColor(surfaceColorIndexes[3]), toColor(surfaceLeftColorIndexes[1]));
        Cubie cubie5 = new Cubie(locations[4], toColor(surfaceColorIndexes[4]) );
        Cubie cubie6 = new Cubie(locations[5], toColor(surfaceColorIndexes[5]), toColor(surfaceRightColorIndexes[1]));
        Cubie cubie7 = new Cubie(locations[6], toColor(surfaceColorIndexes[6]), toColor(surfaceLeftColorIndexes[0]), toColor(surfaceFrontColorIndexes[2]));
        Cubie cubie8 = new Cubie(locations[7], toColor(surfaceColorIndexes[7]), toColor(surfaceFrontColorIndexes[1]));
        Cubie cubie9 = new Cubie(locations[8], toColor(surfaceColorIndexes[8]), toColor(surfaceRightColorIndexes[2]), toColor(surfaceFrontColorIndexes[0]));

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
        this.surfaceColorIndexes = this.surfaceAndBorder.get(0);
        this.surfaceBackColorIndexes = this.surfaceAndBorder.get(1);
        this.surfaceRightColorIndexes = this.surfaceAndBorder.get(2);
        this.surfaceFrontColorIndexes = this.surfaceAndBorder.get(3);
        this.surfaceLeftColorIndexes = this.surfaceAndBorder.get(4);

        // Init W
        this.w1 = this.surfaceAndBorder.get(5);
        this.w2 = this.surfaceAndBorder.get(6);
        this.w3 = this.surfaceAndBorder.get(7);
        this.w4 = this.surfaceAndBorder.get(8);
    }


    public Integer[] getW1(){return this.w1;}
    public Integer[] getW2(){return this.w2;}
    public Integer[] getW3(){return this.w3;}
    public Integer[] getW4(){return this.w4;}



    public Integer getCenterColor(){
        return this.getAllCubies().get(4).getStickerColors().get(0);
    }

    public Integer[] getSurfaceColorIndexes() {
        return this.surfaceColorIndexes;
    }

    public void setSurfaceColorIndexes(Integer[] surfaceColorIndexes) {
        this.surfaceColorIndexes = surfaceColorIndexes;
    }

    public Integer[] getSurfaceBackColorIndexes() {
        return this.surfaceBackColorIndexes;
    }
 

    public Integer[] getSurfaceRightColorIndexes() {
        return this.surfaceRightColorIndexes;
    }
 

    public Integer[] getSurfaceFrontColorIndexes() {
        return this.surfaceFrontColorIndexes;
    }
 

    public Integer[] getSurfaceLeftColorIndexes() {
        return this.surfaceLeftColorIndexes;
    }


	public ArrayList<String[]> getAllColorsOnLayer(){

		ArrayList<String[]> result = new ArrayList<>();
 
		Integer[] surfaceColorsIndexes       = this.getSurfaceColorIndexes();
		
		Integer[] surfaceBackColorsIndexes   = this.getSurfaceBackColorIndexes();
		Integer[] surfaceRightColorsIndexes  = this.getSurfaceRightColorIndexes();
		Integer[] surfaceFrontColorsIndexes  = this.getSurfaceFrontColorIndexes();
		Integer[] surfaceLeftColorsIndexes   = this.getSurfaceLeftColorIndexes();


		ArrayList<Integer[]> allIndexes = new ArrayList<>( Arrays.asList(
                surfaceColorsIndexes, surfaceBackColorsIndexes, surfaceRightColorsIndexes,
                surfaceFrontColorsIndexes, surfaceLeftColorsIndexes
        ));


		for(int i = 0; i < allIndexes.size(); i++){

			Integer[] layerSection = allIndexes.get(i);
			String[] colorLettersList = new String[ layerSection.length ];

			for(int j = 0; j < layerSection.length; j++){
				char colorLetter = this.cubeAsString.charAt(layerSection[j] - 1);
                colorLettersList[j] = Character.toString(colorLetter);
			}

			result.add(colorLettersList);
		}


		return result;
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
            String color = colorIntToString(toColor( listOfColor));
            output.append(" ").append( color );
        }
        return output.toString();
    }


    @Override
    public String toString() {
        String output = "";

        String surfaceString = arrayToString(this.surfaceColorIndexes);
        output += "\n\nSurface: " + this.surfaceName.toString() + "\n" + surfaceString;
        String surfaceBackString = arrayToString(this.surfaceBackColorIndexes);
        output += "\nSurface Back: \n" + surfaceBackString;
        String surfaceRightString = arrayToString(this.surfaceRightColorIndexes);
        output += "\nSurface Right: \n" + surfaceRightString;
        String surfaceFrontString = arrayToString(this.surfaceFrontColorIndexes);
        output += "\nSurface Front: \n" + surfaceFrontString;
        String surfaceLeftString = arrayToString(this.surfaceLeftColorIndexes);
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
    public class Cubie {

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


        // TODO: Use for F2L, OLL, and PLL
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
            StringBuilder output = new StringBuilder("{");
            for (int i = 0; i < this.stickerColorsList.size(); i++) {
                // Converts the sticker list into a string with sticker color names
                Integer currentColor = this.stickerColorsList.get(i); 
                String stickerColorName = colorIntToString(currentColor);
                output.append(" ").append(stickerColorName);
            }

            output.append("}");
            return output.toString();
        }

    }

}
