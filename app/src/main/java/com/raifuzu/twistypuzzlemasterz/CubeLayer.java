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
    private Integer[] surfaceColors;
    private Integer[] surfaceBackColors;
    private Integer[] surfaceRightColors;
    private Integer[] surfaceFrontColors;
    private Integer[] surfaceLeftColors;
    private AdvancedArrayList<Integer[]> surfaceAndBorder;
    public AdvancedArrayList<Cubie> myCubies;

    private View rootView;// Keep this. You might need to Toast.


    public CubeLayer(View rootView, AdvancedArrayList<Integer[]> surfaceAndBorder, SurfaceName surfaceName) {
        this.rootView = rootView;
        this.surfaceName = surfaceName;
        setAllLayerButtons(surfaceAndBorder);
        this.surfaceAndBorder = surfaceAndBorder;
        initializeCubies();
    }


    public void setAllCubieColors() {
        for (Cubie currentCubie : this.myCubies) {
            currentCubie.setCubieStickerColors();
        }
    }


    private void initializeCubies() {

        // Correctly set the locations of all cubies on this layer
        String[] locations = RubiksCube.cubieLocationsMap.get(this.surfaceName.name());

        // TODO: The order of each of these buttons for each sticker should automatically create the
        //     location String for each cubie. Instead of having to create RubiksCube.cubieLocationMap
        //     and managing the order of each location manually.
        Cubie cubie1 = new Cubie(locations[0], this.surfaceColors[0], this.surfaceBackColors[0], this.surfaceLeftColors[2]);
        Cubie cubie2 = new Cubie(locations[1], this.surfaceColors[1], this.surfaceBackColors[1]);
        Cubie cubie3 = new Cubie(locations[2], this.surfaceColors[2], this.surfaceBackColors[2], this.surfaceRightColors[0]);
        Cubie cubie4 = new Cubie(locations[3], this.surfaceColors[3], this.surfaceLeftColors[1]);
        Cubie cubie5 = new Cubie(locations[4], this.surfaceColors[4]);
        Cubie cubie6 = new Cubie(locations[5], this.surfaceColors[5], this.surfaceRightColors[1]);
        Cubie cubie7 = new Cubie(locations[6], this.surfaceColors[6], this.surfaceLeftColors[0], this.surfaceFrontColors[2]);
        Cubie cubie8 = new Cubie(locations[7], this.surfaceColors[7], this.surfaceFrontColors[1]);
        Cubie cubie9 = new Cubie(locations[8], this.surfaceColors[8], this.surfaceRightColors[2], this.surfaceFrontColors[0]);

        // Initialize myCubies
        this.myCubies = new AdvancedArrayList<>(cubie1, cubie2, cubie3, cubie4, cubie5, cubie6, cubie7, cubie8, cubie9);
    }

    public AdvancedArrayList<Cubie> getAllCubies() {
        return this.myCubies;
    }

    public AdvancedArrayList<Integer[]> getAllLayerButtons() {
        return this.surfaceAndBorder;
    }

    public void setAllLayerButtons(AdvancedArrayList<Integer[]> allLayerButtons) {
        this.surfaceColors = allLayerButtons.get(0);
        this.surfaceBackColors = allLayerButtons.get(1);
        this.surfaceRightColors = allLayerButtons.get(2);
        this.surfaceFrontColors = allLayerButtons.get(3);
        this.surfaceLeftColors = allLayerButtons.get(4);
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

//    public void setSurfaceBackButtons(Integer[] surfaceBackButtons) {
//        this.surfaceBackButtons = surfaceBackButtons;
//    }

    public Integer[] getSurfaceRightColors() {
        return this.surfaceRightColors;
    }

//    public void setSurfaceRightButtons(Integer[] surfaceRightButtons) {
//        this.surfaceRightButtons = surfaceRightButtons;
//    }

    public Integer[] getSurfaceFrontColors() {
        return this.surfaceFrontColors;
    }

//    public void setSurfaceFrontButtons(Integer[] surfaceFrontButtons) {
//        this.surfaceFrontButtons = surfaceFrontButtons;
//    }

    public Integer[] getSurfaceLeftColors() {
        return this.surfaceLeftColors;
    }

//    public void setSurfaceLeftButtons(Integer[] surfaceLeftButtons) {
//        this.surfaceLeftButtons = surfaceLeftButtons;
//    }

    /**
     * This method updates all of the colors of each layer in the cubes data
     * structure to a solved state. It also updates the UI to display the UI
     * cube in a solved state.
     *
     * @param surfaceColor
     * @param surfaceBackColor
     * @param surfaceRightColor
     * @param surfaceFrontColor
     * @param surfaceLeftColor
     */
    public void initializeSolvedColors(Integer surfaceColor, Integer surfaceBackColor, Integer surfaceRightColor,
                                       Integer surfaceFrontColor, Integer surfaceLeftColor) {

        Integer[] layerColors = {surfaceColor, surfaceBackColor, surfaceRightColor, surfaceFrontColor,
                surfaceLeftColor};

        // surfaceAndBorder includes the following button arrays:
        // surface[], s back[], s right[], s front[], s left[]

        // TODO: Test me! (this seems off. really think about it)
        this.surfaceAndBorder.add(layerColors);
//        int index = 0;
//        for (Integer[] buttons : this.surfaceAndBorder) {
//            for (Integer button : buttons) {
//                button.setBackgroundColor(layerColors[index]);
//                button.setTextColor(layerColors[index]);
//            }
//            index++;
//        }

    }

    @SuppressWarnings("unchecked")
    public void initializeScrambledColors(AdvancedArrayList<Integer>... colorLists) {

        // surfaceAndBorder includes the following button arrays:
        // surface[], s back[], s right[], s front[], s left[]
        int surfaceBorderIndex = 0;
        int stickerIndex = 0;
        for (Integer[] buttons : this.surfaceAndBorder) {
            for (Integer button : buttons) {

                // TODO: What goes here instead of commented out two lines below???
//                button.setBackgroundColor(colorLists[surfaceBorderIndex].get(stickerIndex));
//                button.setTextColor(colorLists[surfaceBorderIndex].get(stickerIndex));
                stickerIndex++;
            }
            stickerIndex = 0;
            surfaceBorderIndex++;
        }

    }

    public String getCenterColor() {
        final int CENTER_INDEX = 4;
        Integer centerColor = this.surfaceColors[CENTER_INDEX];//.getCurrentTextColor();
        return colorIntToString(centerColor);
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

    private Integer[] getButtonColors(Integer[] buttons) {
        Integer[] colors = new Integer[buttons.length];

        for (int buttonIndex = 0; buttonIndex < buttons.length; buttonIndex++) {
            colors[buttonIndex] = buttons[buttonIndex];//.getCurrentTextColor();
        }

        return colors;
    }


    @Override
    public String toString() {
        String output = "";

        Integer[] surfaceColors = getButtonColors(this.surfaceColors);
        Integer[] surfaceBackColors = getButtonColors(this.surfaceBackColors);
        Integer[] surfaceRightColors = getButtonColors(this.surfaceRightColors);
        Integer[] surfaceFrontColors = getButtonColors(this.surfaceFrontColors);
        Integer[] surfaceLeftColors = getButtonColors(this.surfaceLeftColors);

        String surfaceString = arrayToString(surfaceColors);
        output += "\n\nSurface: " + this.surfaceName.toString() + "\n" + surfaceString;
        String surfaceBackString = arrayToString(surfaceBackColors);
        output += "\nSurface Back: \n" + surfaceBackString;
        String surfaceRightString = arrayToString(surfaceRightColors);
        output += "\nSurface Right: \n" + surfaceRightString;
        String surfaceFrontString = arrayToString(surfaceFrontColors);
        output += "\nSurface Front: \n" + surfaceFrontString;
        String surfaceLeftString = arrayToString(surfaceLeftColors);
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


        private final ArrayList<Integer> stickers;
        private final ArrayList<Integer> stickerColorsList;

        private RubiksCube.CubieType type;
        public Cubie(String location, Integer... stickers) {


            this.cubieOrientation = new HashMap<>();
            this.stickers = new ArrayList<>();
            this.stickerColorsList = new ArrayList<>();



            this.location = location;
            this.stickers.addAll(Arrays.asList(stickers));

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

        public void setCubieStickerColors() {
            this.stickerColorsList.clear();
            this.stickerColorsList.addAll(this.stickers);

        }

        public String getLocation(){
            return this.location;
        }


        public ArrayList<Integer> getStickerColors() {
            return this.stickerColorsList;
        }

        public ArrayList<String> getStickerColorsStrings(){

            ArrayList<String> result = new ArrayList<>();
            for(Integer value : this.stickerColorsList){

                String color = CubeLayer.colorIntToString(value);
                result.add(color);
            }



            return result;
        }

        public ArrayList<Integer> getStickers() {
            return this.stickers;
        }


        public RubiksCube.CubieType getCubieType(){
            return type;
        }


        @Override
        public String toString() {
            StringBuilder output = new StringBuilder( " : ");
            for (int i = 0; i < this.stickers.size(); i++) {
                // Converts the sticker list into a string with sticker color
                // names
                Integer currentColor = this.stickers.get(i);//.getCurrentTextColor();
                String stickerColorName = colorIntToString(currentColor);
                output.append(" ").append(stickerColorName);
            }

            return output.toString();
        }

    }

}
