package com.vdai.minesweeperflags.ai;

import android.util.Log;

import com.vdai.minesweeperflags.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Vivian on 2017-06-11.
 */

public class RegularAIController extends AIController {
    private List<Integer> toClick = new ArrayList<>();

    public int getNextClick(List<Tile> tiles, List<Tile> actual) {
        if (!toClick.isEmpty()) {
            return toClick.remove(0);
        }

        List<Integer> temp = new ArrayList<>();
        Log.i("tiles size", String.valueOf(tiles.size()));


        for(int i = 0; i < tiles.size(); i++) {
            Log.i("Loop for tile", String.valueOf(i));
            if (tiles.get(i).getState().equals("revealed")) {
                Log.i("FOUND REVEALED TILE", String.valueOf(i));
                List<Integer> surrounding = findSurroundingIndices(tiles, i);

                int counter = 0;
                for(Integer j : surrounding) {
                    String state = tiles.get(j).getState();
                    if (state.equals("unrevealed")) {
                        temp.add(j);
                        counter++;
                    } else if (state.equals("flagged")) {
                        counter++;
                    }
                }

                Log.i("counter", String.valueOf(counter));
                Log.i("temp", Arrays.toString(temp.toArray()));
                if (!temp.isEmpty() && counter != 0 && counter == tiles.get(i).getNumber()) {
                    int nextClick = temp.remove(0);
                    toClick = temp;
                    Log.i("====RETURNING TILE====", String.valueOf(nextClick));

                    return nextClick;
                } else {
                    temp = new ArrayList<>();
                }
            }
        }
        Log.i("====click====", "clicking random tile");
        return clickRandomTile(tiles);
    }
}
