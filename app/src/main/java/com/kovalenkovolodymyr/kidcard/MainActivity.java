package com.kovalenkovolodymyr.kidcard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String PREFERENCE_FILE_KEY =
            MainActivity.class.getCanonicalName() + "PREFERENCE_FILE_KEY";
//    public static final String PREFERENCE_GAME1 =
//            GameFindSimilarActivity.class.getCanonicalName() + "_key";
//    public static final String PREFERENCE_GAME2 =
//            GameFindLetterActivity.class.getCanonicalName() + "_key";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences =
                getApplication().getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnPlay:
                Intent chooseGameIntent = new Intent(this,ChooseGameActivity.class);
                startActivity(chooseGameIntent);
                break;
            case R.id.btnOptions:
                break;
            case R.id.btnExit:
                this.finishAffinity();
                break;
        }
    }
}
