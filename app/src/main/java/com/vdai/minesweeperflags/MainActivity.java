package com.vdai.minesweeperflags;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void playWithAI(View view) {

    }

    public void playWithFriend(View view) {

    }

    public void settings(View view) {

    }

    public void howToPlay(View view) {
        Intent intent = new Intent(this, HowToPlayActivity.class);
        startActivity(intent);
    }
}
