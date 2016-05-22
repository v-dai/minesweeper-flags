package com.vdai.minesweeperflags;

import android.app.Activity;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vivian on 2016-05-22.
 */
public class GameBoardAdapter extends BaseAdapter {

    private GameScreenActivity activity;

    private LayoutInflater layoutInflater;
    private List<Integer> changedTiles = new ArrayList<>();
    private String playerColor = "red"; // placeholder

    public GameBoardAdapter(GameScreenActivity activity) {
        this.activity = activity;

        layoutInflater = activity.getLayoutInflater();

    }

    public List<Integer> getChangedTiles() {
        return changedTiles;
    }

    public void addToChangedTiles(int position) {
        changedTiles.add(position);
    }

    public int getCount() {
        return activity.gridSize * activity.gridSize;
    }

    public Tile getItem(int position) {
        return activity.tilesView.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("getView", "position: " + position);
        TileImageView imageView = null;

        if(convertView == null) {
            Log.i("getView", "convertView is null");
            imageView = (TileImageView) layoutInflater.inflate(R.layout.grid_tile, null).findViewById(R.id.tile);
            imageView.setImageResource(R.drawable.square_unrevealed);
        } else if(changedTiles.contains(position)) {
            imageView = (TileImageView) convertView;
            Tile tile = activity.tilesActual.get(position);
            changedTiles.remove(Integer.valueOf(position));
            if(tile.getMine()) { // if position is a mine
                setFlagImage(imageView, playerColor);
                tile.setRevealed(playerColor);
            } else { // if position is a number/blank
                setNumberImage(imageView, tile.getNumber());
                tile.setRevealed();
            }
        } else {
            imageView = (TileImageView) convertView;
            Tile tile = activity.tilesView.get(position);
            if(tile.getState().equals("flagged")) {
                setFlagImage(imageView, tile.getFlagColor());
            } else if(tile.getState().equals("unrevealed")) {
                imageView.setImageResource(R.drawable.square_unrevealed);
            } else {
                setNumberImage(imageView, tile.getNumber());
            }

        }


        return imageView;
    }

    public void setNumberImage(ImageView imageView, int number) {
        switch (number) {
            case 0:
                imageView.setImageResource(R.drawable.square_revealed);
                break;
            case 1:
                imageView.setImageResource(R.drawable.number_1);
                break;
            case 2:
                imageView.setImageResource(R.drawable.number_2);
                break;
            case 3:
                imageView.setImageResource(R.drawable.number_3);
                break;
            case 4:
                imageView.setImageResource(R.drawable.number_4);
                break;
            case 5:
                imageView.setImageResource(R.drawable.number_5);
                break;
            case 6:
                imageView.setImageResource(R.drawable.number_6);
                break;
            case 7:
                imageView.setImageResource(R.drawable.number_7);
                break;
            case 8:
                imageView.setImageResource(R.drawable.number_8);
                break;
        }
    }

    public void setFlagImage(ImageView imageView, String color) {
        switch (color) {
            case "red":
                imageView.setImageResource(R.drawable.flag_red);
                break;
            case "blue":
                imageView.setImageResource(R.drawable.flag_blue);
                break;
        }
    }

}
