package com.kovalenkovolodymyr.kidcard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class WinActivity extends AppCompatActivity {
    public final static String GAME_RESTART_EXTRA = "game_restart_extra";
    public final static String GAME_ACCURACY = "game_accuracy";
    public final static int GAME1_RESTART = 1;
    public final static int GAME2_RESTART = 2;
    public final static int GAME3_RESTART = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);
        double lastGameAccuracy = getIntent().getExtras().getDouble(GAME_ACCURACY);
        SharedPreferences sharedPreferences =
                getApplication().getSharedPreferences(MainActivity.PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        Toast.makeText(this,
                "Accuracy " + lastGameAccuracy,
                Toast.LENGTH_SHORT).show();
        int accuracy;
        int count;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch ((getIntent().getExtras().getInt(GAME_RESTART_EXTRA))){

            case GAME1_RESTART:
                accuracy = sharedPreferences.getInt(getResources().getString(R.string.game1_accuracy_key),0);
                count = sharedPreferences.getInt(getResources().getString(R.string.game1_count_key),0);
                count++;

                editor.putInt(getResources().getString(R.string.game1_accuracy_key),
                        Math.round((float) (accuracy + lastGameAccuracy*100)/count));
                editor.putInt(getResources().getString(R.string.game1_count_key),count);
                editor.apply();
                break;
            case GAME2_RESTART:
                accuracy = sharedPreferences.getInt(getResources().getString(R.string.game2_accuracy_key),0);
                count = sharedPreferences.getInt(getResources().getString(R.string.game2_count_key),0);
                count++;
                editor = sharedPreferences.edit();
                editor.putInt(getResources().getString(R.string.game2_accuracy_key),
                        Math.round((float) (accuracy + lastGameAccuracy*100)/count));
                editor.putInt(getResources().getString(R.string.game2_count_key),count);
                editor.apply();
                break;
        }
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
                        intent.setClass(this, GameFindLetterActivity.class);
                        finish();
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
