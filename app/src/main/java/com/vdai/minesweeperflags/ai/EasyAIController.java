package com.vdai.minesweeperflags.ai;

import com.vdai.minesweeperflags.Tile;

import java.util.List;
import java.util.Random;

/**
 * Created by Vivian on 2017-06-11.
 */

public class EasyAIController extends AIController {

    public int getNextClick(List<Tile> tiles) {
        int numEmpty = 0;
        for(Tile tile : tiles) {
            if (tile.getState().equals("unrevealed")) numEmpty++;
        }

        Random rand = new Random();
        int n = rand.nextInt(numEmpty);

        for(int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).getState().equals("unrevealed")) {
                if (n == 0) {
                    return i;
                }
                n--;
            }
        }

        return 0;
    }
}
