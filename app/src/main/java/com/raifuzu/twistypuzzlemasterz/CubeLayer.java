package com.raifuzu.twistypuzzlemasterz;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import com.raifuzu.twistypuzzlemasterz.RubiksCube.Side;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// THERE IS NO OTHER WAY TO GET THE BACKGROUND COLOR OF A BUTTON. 
//
// Example:
// button.setTextColor(color1); button.setBackgroundColor(color1);
// button.getCurrentTextColor();

public class CubeLayer {

    private Side sideName;
    private Button[] surfaceButtons;
    private Button[] surfaceBackButtons;
    private Button[] surfaceRightButtons;
    private Button[] surfaceFrontButtons;
    private Button[] surfaceLeftButtons;
    private AdvancedArrayList<Button[]> surfaceAndBorder;
    private AdvancedArrayList<Cubie> myCubies;

    private View rootView;// Keep this. You might need to Toast.

    public CubeLayer(View rootView, AdvancedArrayList<Button[]> surfaceAndBorder, Side sideName) {
        this.rootView = rootView;
        this.sideName = sideName;
        setAllLayerButtons(surfaceAndBorder);
        this.surfaceAndBorder = surfaceAndBorder;
        initializeCubies(surfaceButtons, surfaceBackButtons, surfaceRightButtons, surfaceFrontButtons,
                surfaceLeftButtons);
    }

    @SuppressWarnings("unchecked")
    public CubeLayer(Side sideName, AdvancedArrayList<Integer>... colorLists) {
        this.sideName = sideName;
        // surface, surface back, surface right, surface front, surface left.
        initializeScrambledColors(colorLists[0], colorLists[1], colorLists[2], colorLists[3], colorLists[4]);
        initializeCubies(surfaceButtons, surfaceBackButtons, surfaceRightButtons, surfaceFrontButtons,
                surfaceLeftButtons);
    }

    public void setAllCubieColors() {
        for (Cubie currentCubie : myCubies) {
            currentCubie.setCubieStickerColors();
        }
    }


    /**
     *  This function gives each layer the ability to search for a specific
     *  cubie.
     *
     * @param cubieID - A string representation of the unique colors that make up this cubie
     * @return - A string representation of all surfaces/sides that intersect/contain this cubie
     */
    public String findCubie(String cubieID){
        return null;
    }

    private void initializeCubies(Button[] surfaceStickers, Button[] surfaceBStickers, Button[] surfaceRStickers,
                                  Button[] surfaceFStickers, Button[] surfaceLStickers) {

        // Create cubies for this layer <3 <3
        Cubie cubie1 = new Cubie(CubieType.CORNER, surfaceButtons[0], surfaceBackButtons[0], surfaceLeftButtons[2]);
        Cubie cubie2 = new Cubie(CubieType.EDGE, surfaceButtons[1], surfaceBackButtons[1]);
        Cubie cubie3 = new Cubie(CubieType.CORNER, surfaceButtons[2], surfaceBackButtons[2], surfaceRightButtons[0]);
        Cubie cubie4 = new Cubie(CubieType.EDGE, surfaceButtons[3], surfaceLeftButtons[1]);
        Cubie cubie5 = new Cubie(CubieType.CENTER, surfaceButtons[4]);
        Cubie cubie6 = new Cubie(CubieType.EDGE, surfaceButtons[5], surfaceRightButtons[1]);
        Cubie cubie7 = new Cubie(CubieType.CORNER, surfaceButtons[6], surfaceLeftButtons[0], surfaceFrontButtons[2]);
        Cubie cubie8 = new Cubie(CubieType.EDGE, surfaceButtons[7], surfaceFrontButtons[1]);
        Cubie cubie9 = new Cubie(CubieType.CORNER, surfaceButtons[8], surfaceRightButtons[2], surfaceFrontButtons[0]);

        // Initialize myCubies
        myCubies = new AdvancedArrayList<>(cubie1, cubie2, cubie3, cubie4, cubie5, cubie6, cubie7, cubie8, cubie9);
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
        return surfaceButtons;
    }

    public void setSurfaceButtons(Button[] surfaceButtons) {
        this.surfaceButtons = surfaceButtons;
    }

    public Button[] getSurfaceBackButtons() {
        return surfaceBackButtons;
    }

    public void setSurfaceBackButtons(Button[] surfaceBackButtons) {
        this.surfaceBackButtons = surfaceBackButtons;
    }

    public Button[] getSurfaceRightButtons() {
        return surfaceRightButtons;
    }

    public void setSurfaceRightButtons(Button[] surfaceRightButtons) {
        this.surfaceRightButtons = surfaceRightButtons;
    }

    public Button[] getSurfaceFrontButtons() {
        return surfaceFrontButtons;
    }

    public void setSurfaceFrontButtons(Button[] surfaceFrontButtons) {
        this.surfaceFrontButtons = surfaceFrontButtons;
    }

    public Button[] getSurfaceLeftButtons() {
        return surfaceLeftButtons;
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
        for (Button[] buttons : surfaceAndBorder) {
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
        for (Button[] buttons : surfaceAndBorder) {
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
        Integer centerColor = surfaceButtons[CENTER_INDEX].getCurrentTextColor();
        return colorIntToString(centerColor);
    }

    public Side getSideName() {
        return sideName;
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
        String output = "";

        for (int i = 0; i < listOfColors.length; i++) {
            output += " " + colorIntToString(listOfColors[i]);
        }
        return output;
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

        Integer[] surfaceColors = getButtonColors(surfaceButtons);
        Integer[] surfaceBackColors = getButtonColors(surfaceBackButtons);
        Integer[] surfaceRightColors = getButtonColors(surfaceRightButtons);
        Integer[] surfaceFrontColors = getButtonColors(surfaceFrontButtons);
        Integer[] surfaceLeftColors = getButtonColors(surfaceLeftButtons);

        String surfaceString = arrayToString(surfaceColors);
        output += "\n\nSurface: " + sideName.toString() + "\n" + surfaceString;
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
    public class Cubie {

        // Location is a string representation of all faces that intersects this cubie
        private String location; // Ex. "FRU" or "BL"
        private boolean correctOrientation = false;


        private ArrayList<Button> stickers = new ArrayList<>();
        private CubieType cubieType = null;//TODO - find out if you even need this.
        private ArrayList<Integer> stickerColorsList = new ArrayList<>();

        public Cubie(CubieType cubieType, Button... stickers) {

            this.cubieType = cubieType;

            for (Button sticker : stickers) {
                this.stickers.add(sticker);
            }
        }

        public void setCubieStickerColors() {
            stickerColorsList.clear();
            for (Button sticker : stickers) {
                stickerColorsList.add(sticker.getCurrentTextColor());
            }
        }

        public ArrayList<Integer> getStickerColors() {
            return this.stickerColorsList;
        }

        public ArrayList<Button> getStickers() {
            return this.stickers;
        }

        public CubieType getCubieType() {
            return this.cubieType;
        }

        @Override
        public String toString() {
            String output = "" + cubieType + ": ";
            for (int i = 0; i < stickers.size(); i++) {
                // Converts the sticker list into a string with sticker color
                // names
                Integer currentColor = stickers.get(i).getCurrentTextColor();
                String stickerColorName = colorIntToString(currentColor);
                output = output + " " + stickerColorName;
            }

            return output;
        }

    }

}
