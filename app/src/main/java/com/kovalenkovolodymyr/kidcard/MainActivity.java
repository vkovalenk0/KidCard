package com.kovalenkovolodymyr.kidcard;

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
