package com.vdai.minesweeperflags;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void playWithAI(View view) {
        // Intent intent = new Intent(this, PlayWithAIActivity.class);
        // startActivity(intent);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.ai_button);

        builder.setNegativeButton("Easy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAIGame("easy");
            }
        });
        builder.setPositiveButton("Regular", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAIGame("regular");
            }
        });

        builder.show();

    }

    public void playWithFriend(View view) {
        Intent intent = new Intent(this, PlayWithFriendActivity.class);
        startActivity(intent);
    }

    public void settings(View view) {

    }

    public void howToPlay(View view) {
        Intent intent = new Intent(this, HowToPlayActivity.class);
        startActivity(intent);
    }

    private void startAIGame(String difficulty) {
        Log.i("difficulty", difficulty);
        Intent intent = new Intent(getBaseContext(), PlayWithAIActivity.class);
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
    }
}
