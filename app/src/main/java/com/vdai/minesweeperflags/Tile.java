package com.vdai.minesweeperflags;

/**
 * Created by Vivian on 2016-05-22.
 */
public class Tile {
    private String state; // flagged, revealed, unrevealed
    private int number = 0;
    private String flagColor = null;

    public Tile() {
        state = "unrevealed";
    }

    public void setState(int number) {
        state = "revealed";
        this.number = number;
    }

    public void setState(String flagColor) {
        state = "flagged";
        this.flagColor = flagColor;
    }

    public String getState() {
        return state;
    }

    public int getNumber() {
        return number;
    }

    public String getFlagColor() {
        return flagColor;
    }

}
