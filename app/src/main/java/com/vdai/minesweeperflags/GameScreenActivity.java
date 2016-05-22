package com.vdai.minesweeperflags;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

/**
 * Created by Vivian on 2016-05-22.
 */
public class GameScreenActivity extends AppCompatActivity {

    public GameBoardGridView gameBoard;
    public GameBoardAdapter gameBoardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        createGrid();
    }


    public void createGrid() {
        gameBoard = (GameBoardGridView) findViewById(R.id.game_board);
        gameBoardAdapter = new GameBoardAdapter(this);
        gameBoard.setAdapter(gameBoardAdapter);

        gameBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(GameScreenActivity.this, "Position of click is: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
