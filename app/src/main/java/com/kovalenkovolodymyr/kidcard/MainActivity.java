package com.kovalenkovolodymyr.kidcard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.LocaleList;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final String PREFERENCE_FILE_KEY =
            MainActivity.class.getCanonicalName() + "PREFERENCE_FILE_KEY";
    public static final String STRING_LANG_KEY =
            MainActivity.class.getCanonicalName() + "STRING_LANG_KEY";
    SharedPreferences sharedPreferences;

    private Button btnOptions;
    private Button btnPlay;
    private Button btnExit;
//    public static final String PREFERENCE_GAME1 =
//            GameFindSimilarActivity.class.getCanonicalName() + "_key";
//    public static final String PREFERENCE_GAME2 =
//            GameFindLetterActivity.class.getCanonicalName() + "_key";


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("mylogs","MainActivity.onResume()");
        SettingsActivity.setDefaultLocale(MainActivity.this,
                sharedPreferences.getString(STRING_LANG_KEY,Locale.getDefault().getDisplayLanguage()));
        btnPlay.setText(R.string.play);
        btnOptions.setText(R.string.options);
        btnExit.setText(R.string.exit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnOptions = (Button) findViewById(R.id.btnOptions);
        btnPlay  = (Button) findViewById(R.id.btnPlay);
        btnExit = (Button) findViewById(R.id.btnExit);
        sharedPreferences = getApplication().getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);

    }

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.btnPlay:
                intent = new Intent(this,ChooseGameActivity.class);
                startActivity(intent);
                break;
            case R.id.btnOptions:
                intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.btnExit:
                this.finishAffinity();
                break;
        }
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
