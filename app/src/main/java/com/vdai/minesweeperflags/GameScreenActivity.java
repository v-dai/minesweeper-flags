package com.vdai.minesweeperflags;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Vivian on 2016-05-22.
 */
public class GameScreenActivity extends AppCompatActivity {
    private int DEFAULT_GRID_SIZE = 15;
    private int DEFAULT_NUM_MINES = 31;

    public int gridSize;
    public int totalSize;
    public int numMines;
    public List<Tile> tilesView = new ArrayList<>();
    public List<Tile> tilesActual = new ArrayList<>();
    public List<Integer> mines = new ArrayList<>();

    public GridView gameBoard;
    public GameBoardAdapter gameBoardAdapter;

    public TextView redScore;
    public TextView blueScore;
    public TextView minesLeft;
    public TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        minesLeft = (TextView) findViewById(R.id.mines_left);
        redScore = (TextView) findViewById(R.id.red_score);
        blueScore = (TextView) findViewById(R.id.blue_score);
        message = (TextView) findViewById(R.id.message);

        createGrid();
    }


    public void createGrid() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        gridSize = sharedPreferences.getInt(Integer.toString(R.string.grid_size_key), DEFAULT_GRID_SIZE);
        numMines = sharedPreferences.getInt(Integer.toString(R.string.num_mines_key), DEFAULT_NUM_MINES);
        totalSize = gridSize * gridSize;

        for(int i = 0; i < totalSize; i++) {
            Tile tile = new Tile();
            tilesView.add(tile);
            tilesActual.add(tile);
        }

        populateGrid();

        gameBoard = (GridView) findViewById(R.id.game_board);

        gameBoard.setNumColumns(gridSize);

        gameBoardAdapter = new GameBoardAdapter(this);
        gameBoard.setAdapter(gameBoardAdapter);

        gameBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                changeTile(position);
                changeText(position);
                Toast.makeText(GameScreenActivity.this, "Position of click is: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // populates mines list with the indices of the tiles that have mines, in numerical order
    public void generateMines() {
        List<Integer> allNums = new ArrayList<>();
        for(int i = 0; i < totalSize; i++) {
            allNums.add(Integer.valueOf(i));
        }
        Collections.shuffle(allNums);

        for(int j = 0; j < numMines; j++) {
            int position = allNums.get(j);
            mines.add(position);
            tilesActual.get(position).putMine();
        }
        Collections.sort(mines);
    }

    // populate tilesActual with number tiles that represent the number of adjacent mines
    public void generateNumbers() {
        for(int i = 0; i < totalSize; i++) {
            if(tilesActual.get(i).getMine()) continue;

            int mineCounter = 0;

            for(Integer index : findSurroundingIndices(i)) {
                if(tilesActual.get(index).getMine()) mineCounter++;
            }

            tilesActual.get(i).setState(mineCounter);

        }
    }

    public void populateGrid() {
        generateMines();
        generateNumbers();
    }

    // returns a list of all the valid indices surrounding a position
    public List<Integer> findSurroundingIndices(int position) {
        List<Integer> indices = new ArrayList<>();

        int checkPosition = position - gridSize - 1;
        if((checkPosition + 1) % gridSize != 0) { // if it is not at the left edge of the screen
            for(int i = 0; i < 3; i++) {
                if(checkPosition >= 0 && checkPosition < totalSize) {
                    indices.add(checkPosition);
                }
                checkPosition = checkPosition + gridSize;
            }
        }

        checkPosition = position - gridSize + 1;
        if(checkPosition % gridSize != 0) { // if it is not at the right edge of the screen
            for(int i = 0; i < 3; i++) {
                if(checkPosition >= 0 && checkPosition < totalSize) {
                    indices.add(checkPosition);
                }
                checkPosition = checkPosition + gridSize;
            }
        }

        checkPosition = position - gridSize;
        if(checkPosition >= 0 && checkPosition < totalSize) {
            indices.add(checkPosition);
        }

        checkPosition = position + gridSize;
        if(checkPosition >= 0 && checkPosition < totalSize) {
            indices.add(checkPosition);
        }

        return indices;
    }

    public List<Integer> findAdjacentIndices(int position) {
        List<Integer> indices = new ArrayList<>();
        int checkPosition = position - gridSize;
        if(checkPosition >= 0 && checkPosition < totalSize) {
            indices.add(checkPosition);
        }

        checkPosition = position + gridSize;
        if(checkPosition >= 0 && checkPosition < totalSize) {
            indices.add(checkPosition);
        }

        checkPosition = position - 1;
        if(checkPosition >= 0 && checkPosition < totalSize) {
            indices.add(checkPosition);
        }

        checkPosition = position + 1;
        if(checkPosition >= 0 && checkPosition < totalSize) {
            indices.add(checkPosition);
        }

        return indices;
    }

    public void revealBlankTile(int position) {
        for(Integer index : findSurroundingIndices(position)) {
            if(gameBoardAdapter.getChangedTiles().contains(index)) {
                continue;
            }
            Tile tile = tilesActual.get(index);
            gameBoardAdapter.addToChangedTiles(index);
            if(!tile.getMine() && tile.getNumber() == 0) { // if tile at index is blank
                revealBlankTile(index);
            }
        }
    }

    public void changeTile(int position) {
        if(tilesView.get(position).getState().equals("unrevealed")) {
            gameBoardAdapter.addToChangedTiles(position);

            if(!tilesView.get(position).getMine() && tilesActual.get(position).getNumber() == 0) { // if tile is blank
                revealBlankTile(position);
            }

            gameBoardAdapter.notifyDataSetChanged();
        }
    }

    public void changeText(int position) {
        if(tilesActual.get(position).getMine()) {
            numMines--;
            minesLeft.setText(Integer.toString(numMines));
        }
    }

}
