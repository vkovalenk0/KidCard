package com.kovalenkovolodymyr.kidcard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class WinActivity extends AppCompatActivity {
    public final static String GAME_RESTART_EXTRA = "game_restart_extra";
    public final static int GAME1_RESTART = 1;
    public final static int GAME2_RESTART = 2;
    public final static int GAME3_RESTART = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

    }

    public void onClick(View view) {
        AppCompatActivity activity;

        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.btnGameRestart:
                switch (getIntent().getExtras().getInt(GAME_RESTART_EXTRA)){
                    case GAME1_RESTART:
                        intent.setClass(this, GameFindSimilarActivity.class);
                        finish();
                        break;
                    case GAME2_RESTART:
                        break; //TODO Game2 and Game3
                    case GAME3_RESTART:
                        break;
                }


                startActivity(intent);
                finish();
                break;
            case R.id.btnToChooseGame:
                finish();
                break;
        }
    }

}
