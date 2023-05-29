package com.raifuzu.twistypuzzlemasterz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.raifuzu.twistypuzzlemasterz.RubiksCube.SurfaceName;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;


public class CubeLayer {

    private SurfaceName surfaceName;
    private Button[] surfaceButtons;
    private Button[] surfaceBackButtons;
    private Button[] surfaceRightButtons;
    private Button[] surfaceFrontButtons;
    private Button[] surfaceLeftButtons;
    private AdvancedArrayList<Button[]> surfaceAndBorder;
    public AdvancedArrayList<Cubie> myCubies;

    private View rootView;// Keep this. You might need to Toast.


    public CubeLayer(View rootView, String initialLayerColors) {
        this.rootView = rootView;
        this.surfaceName = SurfaceName.F; // TODO: First char in initialLayerColors
        setAllLayerButtons(surfaceAndBorder);
//        this.surfaceAndBorder = surfaceAndBorder;
//        initializeCubies(surfaceButtons, surfaceBackButtons, surfaceRightButtons, surfaceFrontButtons,
//                surfaceLeftButtons);
    }


    public CubeLayer(View rootView, AdvancedArrayList<Button[]> surfaceAndBorder, SurfaceName surfaceName) {
        this.rootView = rootView;
        this.surfaceName = surfaceName;
        setAllLayerButtons(surfaceAndBorder);
        this.surfaceAndBorder = surfaceAndBorder;
        initializeCubies();
    }

//    @SuppressWarnings("unchecked")
//    public CubeLayer(SurfaceName surfaceName, AdvancedArrayList<Integer>... colorLists) {
//        this.surfaceName = surfaceName;
//        // surface, surface back, surface right, surface front, surface left.
//        initializeScrambledColors(colorLists[0], colorLists[1], colorLists[2], colorLists[3], colorLists[4]);
//        initializeCubies(surfaceButtons, surfaceBackButtons, surfaceRightButtons, surfaceFrontButtons,
//                surfaceLeftButtons);
//    }

    public void setAllCubieColors() {
        for (Cubie currentCubie : this.myCubies) {
            currentCubie.setCubieStickerColors();
        }
    }




    private void initializeCubies() {

        // Correctly set the locations of all cubies on this layer
        String[] locations = RubiksCube.cubieLocationsMap.get(this.surfaceName.name());

        // Create cubies for this layer <3 <3
        Cubie cubie1 = new Cubie(locations[0], this.surfaceButtons[0], this.surfaceBackButtons[0], this.surfaceLeftButtons[2]);
        Cubie cubie2 = new Cubie(locations[1], this.surfaceButtons[1], this.surfaceBackButtons[1]);
        Cubie cubie3 = new Cubie(locations[2], this.surfaceButtons[2], this.surfaceBackButtons[2], this.surfaceRightButtons[0]);
        Cubie cubie4 = new Cubie(locations[3], this.surfaceButtons[3], this.surfaceLeftButtons[1]);
        Cubie cubie5 = new Cubie(locations[4], this.surfaceButtons[4]);
        Cubie cubie6 = new Cubie(locations[5], this.surfaceButtons[5], this.surfaceRightButtons[1]);
        Cubie cubie7 = new Cubie(locations[6], this.surfaceButtons[6], this.surfaceLeftButtons[0], this.surfaceFrontButtons[2]);
        Cubie cubie8 = new Cubie(locations[7], this.surfaceButtons[7], this.surfaceFrontButtons[1]);
        Cubie cubie9 = new Cubie(locations[8], this.surfaceButtons[8], this.surfaceRightButtons[2], this.surfaceFrontButtons[0]);

        // Initialize myCubies
        this.myCubies = new AdvancedArrayList<>(cubie1, cubie2, cubie3, cubie4, cubie5, cubie6, cubie7, cubie8, cubie9);
    }

    public AdvancedArrayList<Cubie> getAllCubies() {
        return this.myCubies;
    }

    public AdvancedArrayList<Button[]> getAllLayerButtons() {
        return this.surfaceAndBorder;
    }

    public void setAllLayerButtons(AdvancedArrayList<Button[]> allLayerButtons) {
        this.surfaceButtons = allLayerButtons.get(0);
        this.surfaceBackButtons = allLayerButtons.get(1);
        this.surfaceRightButtons = allLayerButtons.get(2);
        this.surfaceFrontButtons = allLayerButtons.get(3);
        this.surfaceLeftButtons = allLayerButtons.get(4);
    }

    public Button[] getSurfaceButtons() {
        return this.surfaceButtons;
    }

    public void setSurfaceButtons(Button[] surfaceButtons) {
        this.surfaceButtons = surfaceButtons;
    }

    public Button[] getSurfaceBackButtons() {
        return this.surfaceBackButtons;
    }

    public void setSurfaceBackButtons(Button[] surfaceBackButtons) {
        this.surfaceBackButtons = surfaceBackButtons;
    }

    public Button[] getSurfaceRightButtons() {
        return this.surfaceRightButtons;
    }

    public void setSurfaceRightButtons(Button[] surfaceRightButtons) {
        this.surfaceRightButtons = surfaceRightButtons;
    }

    public Button[] getSurfaceFrontButtons() {
        return this.surfaceFrontButtons;
    }

    public void setSurfaceFrontButtons(Button[] surfaceFrontButtons) {
        this.surfaceFrontButtons = surfaceFrontButtons;
    }

    public Button[] getSurfaceLeftButtons() {
        return this.surfaceLeftButtons;
    }

    public void setSurfaceLeftButtons(Button[] surfaceLeftButtons) {
        this.surfaceLeftButtons = surfaceLeftButtons;
    }

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
        int index = 0;
        for (Button[] buttons : this.surfaceAndBorder) {
            for (Button button : buttons) {
                button.setBackgroundColor(layerColors[index]);
                button.setTextColor(layerColors[index]);
            }
            index++;
        }

    }

    @SuppressWarnings("unchecked")
    public void initializeScrambledColors(AdvancedArrayList<Integer>... colorLists) {

        // surfaceAndBorder includes the following button arrays:
        // surface[], s back[], s right[], s front[], s left[]
        int surfaceBorderIndex = 0;
        int stickerIndex = 0;
        for (Button[] buttons : this.surfaceAndBorder) {
            for (Button button : buttons) {
                button.setBackgroundColor(colorLists[surfaceBorderIndex].get(stickerIndex));
                button.setTextColor(colorLists[surfaceBorderIndex].get(stickerIndex));
                stickerIndex++;
            }
            stickerIndex = 0;
            surfaceBorderIndex++;
        }

    }

    public String getCenterColor() {
        final int CENTER_INDEX = 4;
        Integer centerColor = this.surfaceButtons[CENTER_INDEX].getCurrentTextColor();
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

    private Integer[] getButtonColors(Button[] buttons) {
        Integer[] colors = new Integer[buttons.length];

        for (int buttonIndex = 0; buttonIndex < buttons.length; buttonIndex++) {
            colors[buttonIndex] = buttons[buttonIndex].getCurrentTextColor();
        }

        return colors;
    }


    @Override
    public String toString() {
        String output = "";

        Integer[] surfaceColors = getButtonColors(this.surfaceButtons);
        Integer[] surfaceBackColors = getButtonColors(this.surfaceBackButtons);
        Integer[] surfaceRightColors = getButtonColors(this.surfaceRightButtons);
        Integer[] surfaceFrontColors = getButtonColors(this.surfaceFrontButtons);
        Integer[] surfaceLeftColors = getButtonColors(this.surfaceLeftButtons);

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
        private final String location; // Ex. "F,R,U" or "B,L"


        // TODO: use center pieces and sticker colors as reference for orientations.
        private final boolean correctOrientation = false;


        private final ArrayList<Button> stickers = new ArrayList<>();
        private final ArrayList<Integer> stickerColorsList = new ArrayList<>();

        private RubiksCube.CubieType type;
        public Cubie(String location, Button... stickers) {
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

        public void setCubieOrientation(){
            // TODO: finish this!
        }

        public void setCubieStickerColors() {
            this.stickerColorsList.clear();
            for (Button sticker : this.stickers) {
                this.stickerColorsList.add(sticker.getCurrentTextColor());
            }

            setCubieOrientation();
        }

        public String getLocation(){
            return this.location;
        }


        public ArrayList<Integer> getStickerColors() {
            return this.stickerColorsList;
        }

        public ArrayList<Button> getStickers() {
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
                Integer currentColor = this.stickers.get(i).getCurrentTextColor();
                String stickerColorName = colorIntToString(currentColor);
                output.append(" ").append(stickerColorName);
            }

            return output.toString();
        }

    }

}
