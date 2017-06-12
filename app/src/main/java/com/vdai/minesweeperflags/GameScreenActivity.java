package com.vdai.minesweeperflags;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vdai.minesweeperflags.ai.AIController;
import com.vdai.minesweeperflags.ai.EasyAIController;
import com.vdai.minesweeperflags.ai.RegularAIController;

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
    private AIController aiController;

    public int gridSize;
    public int totalSize;
    public int numMines;
    public int redMinesFound;
    public int blueMinesFound;
    public List<Tile> tilesView = new ArrayList<>();
    public List<Tile> tilesActual = new ArrayList<>();
    public List<Integer> mines = new ArrayList<>();
    public String thisTurn;
    public String prevTurn;

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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String difficulty = extras.getString("difficulty");
            if (difficulty.equals("easy")) {
                aiController = new EasyAIController();
            } else if (difficulty.equals("regular")) {
                aiController = new RegularAIController();
            }

            message.setText(R.string.you_start);
        }
    }


    public void createGrid() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        gridSize = sharedPreferences.getInt(Integer.toString(R.string.grid_size_key), DEFAULT_GRID_SIZE);
        numMines = sharedPreferences.getInt(Integer.toString(R.string.num_mines_key), DEFAULT_NUM_MINES);
        totalSize = gridSize * gridSize;
        thisTurn = "red";
        prevTurn = "blue";

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
                handleUserClick(position);
            }
        });
    }

    public void handleUserClick(int position) {
        if(numMines != 0 && tilesView.get(position).getState().equals("unrevealed")) {
            gameBoardAdapter.setPlayerColor(thisTurn);
            changeTile(position);
            updateNumMines(position);
            updateScore(position);

            if (aiController != null) {
                if (tilesActual.get(position).getMine()) {
                    printAIMessage(false, 0);
                    return;
                }
                doAITurn();
            } else {
                updateTurns(position);
                printMessage();
            }
        }
        //Toast.makeText(GameScreenActivity.this, "Position of click is: " + position, Toast.LENGTH_SHORT).show();
    }

    public void doAITurn() {
        gameBoardAdapter.setPlayerColor("blue");
        prevTurn = "red";
        thisTurn = "blue";
        int position = aiController.getNextClick(tilesView, tilesActual);
        changeTile(position);
        updateNumMines(position);
        updateScore(position);

        int minesFound = 0;
        while(tilesActual.get(position).getMine()) {
            minesFound++;
            position = aiController.getNextClick(tilesView, tilesActual);
            changeTile(position);
            updateNumMines(position);
            updateScore(position);
        }

        printAIMessage(true, minesFound);

        prevTurn = "blue";
        thisTurn = "red";
    }

    private void printAIMessage(boolean AILastTurn, int AIMinesFound) {
        if (numMines == 0) {
            message.setGravity(Gravity.CENTER);
            if (redMinesFound > blueMinesFound) {
                message.setText(R.string.red_win);
            } else if (redMinesFound < blueMinesFound) {
                message.setText(R.string.blue_win);
            } else {
                message.setText(R.string.draw);
            }

            ((LinearLayout) findViewById(R.id.mines_info)).setVisibility(View.GONE);
            ((Button) findViewById(R.id.play_again_button)).setVisibility(View.VISIBLE);
            return;
        }

        if (!AILastTurn) {
            message.setText(R.string.you_again);
            return;
        }

        if (AIMinesFound != 0) {
            message.setText(String.format(getString(R.string.your_turn_after_ai), AIMinesFound));
        } else {
            message.setText(R.string.your_turn_after_ai2);
        }
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
            tile.setRevealed(thisTurn);
            tilesView.get(index).setRevealed(thisTurn);
            if(!tile.getMine() && tile.getNumber() == 0) { // if tile at index is blank
                revealBlankTile(index);
            }
        }
    }

    public void changeTile(int position) {
        if(tilesView.get(position).getState().equals("unrevealed")) {
            gameBoardAdapter.addToChangedTiles(position);
            tilesActual.get(position).setRevealed(thisTurn);
            tilesView.get(position).setRevealed(thisTurn);

            if(!tilesView.get(position).getMine() && tilesActual.get(position).getNumber() == 0) { // if tile is blank
                revealBlankTile(position);
            }

            gameBoardAdapter.notifyDataSetChanged();
        }
    }

    public void updateNumMines(int position) {
        if(tilesActual.get(position).getMine()) {
            numMines--;
            minesLeft.setText(Integer.toString(numMines));
        }
    }

    public void printMessage() {
        if (numMines == 0) {
            message.setGravity(Gravity.CENTER);
            if (redMinesFound > blueMinesFound) {
                message.setText(R.string.red_win);
            } else if (redMinesFound < blueMinesFound) {
                message.setText(R.string.blue_win);
            } else {
                message.setText(R.string.draw);
            }

            ((LinearLayout) findViewById(R.id.mines_info)).setVisibility(View.GONE);
            ((Button) findViewById(R.id.play_again_button)).setVisibility(View.VISIBLE);
            return;
        }

        if (thisTurn.equals("red")) {
            if (prevTurn.equals("red")) {
                message.setText(R.string.red_again);
            } else {
                message.setText(R.string.reds_turn);
            }
        } else if (thisTurn.equals("blue")) {
            if (prevTurn.equals("blue")) {
                message.setText(R.string.blue_again);
            } else {
                message.setText(R.string.blues_turn);
            }
        }
    }

    public void updateTurns(int position) {
        if(tilesActual.get(position).getMine()) {
            prevTurn = thisTurn;
        } else {
            if(thisTurn.equals("red")) {
                prevTurn = "red";
                thisTurn = "blue";
            } else {
                prevTurn = "blue";
                thisTurn = "red";
            }
        }
    }

    public void updateScore(int position) {
        if(tilesActual.get(position).getMine()) {
            if(thisTurn.equals("red")) {
                redMinesFound++;
                redScore.setText(Integer.toString(redMinesFound));
            } else {
                blueMinesFound++;
                blueScore.setText(Integer.toString(blueMinesFound));
            }
        }
    }

    public void playAgain(View view) {
        finish();
        startActivity(getIntent());
    }


}
