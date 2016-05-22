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

    public GameBoardAdapter(GameScreenActivity activity) {
        this.activity = activity;

        layoutInflater = activity.getLayoutInflater();

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

    public View getView(int position, View convertView, ViewGroup parent) {
        TileImageView imageView;

        if(convertView == null) {
            imageView = (TileImageView) layoutInflater.inflate(R.layout.grid_tile, null).findViewById(R.id.tile);
            imageView.setImageResource(R.drawable.square_unrevealed);
        } else {
            imageView = (TileImageView) convertView;
        }

        if(convertView != null && changedTiles.contains(position)) {
            Tile tile = activity.tilesActual.get(position);
            changedTiles.remove(position);
            String state = tile.getState();
            if (state.equals("revealed")) {
                setNumberImage(imageView, tile.getNumber());
            } else if (state.equals("flagged")) {
                setFlagImage(imageView, tile.getFlagColor());
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
