package com.vdai.minesweeperflags.ai;

import com.vdai.minesweeperflags.Tile;

import java.util.List;
import java.util.Random;

/**
 * Created by Vivian on 2017-06-11.
 */

public class EasyAIController extends AIController {

    public int getNextClick(List<Tile> tiles, List<Tile> actual) {
        return clickRandomTile(tiles);
    }
}
