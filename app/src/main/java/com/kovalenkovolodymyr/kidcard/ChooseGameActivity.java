package com.kovalenkovolodymyr.kidcard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ChooseGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game);
    }


    public void onClick(View view) {
        Intent intent  = new Intent();
        switch (view.getId()){
            case R.id.btnGame1:
                intent.setClass(this,GameFindSimilarActivity.class);
                startActivity(intent);
                break;
            case R.id.btnGame2:
                intent.setClass(this,GameFindLetter.class);
                startActivity(intent);
                break;
            case R.id.btnGame3:
                break;
        }
    }
}
