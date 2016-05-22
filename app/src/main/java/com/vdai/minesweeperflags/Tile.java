package com.vdai.minesweeperflags;

/**
 * Created by Vivian on 2016-05-22.
 */
public class Tile {
    private String state; // flagged, revealed, unrevealed
    private int number = 0;
    private boolean mine = false;
    private String flagColor = null;

    public Tile() {
        state = "unrevealed";
    }

    public void setState(int number) {
        this.number = number;
    }

    public void setRevealed() {
        state = "revealed";
    }

    public void setRevealed(String flagColor) {
        state = "flagged";
        this.flagColor = flagColor;
    }

    public void putMine() {
        mine = true;
    }

    public String getState() {
        return state;
    }

    public int getNumber() {
        return number;
    }

    public boolean getMine() {
        return mine;
    }

    public String getFlagColor() {
        return flagColor;
    }

}
