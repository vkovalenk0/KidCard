package com.kovalenkovolodymyr.kidcard;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.*;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import java.util.Random;

public class GameFindSimilarActivity extends AppCompatActivity {
//
//    Intent intent = new Intent(this,MainActivity.class);
//
//
//    startActivity(intent);
//    finish();
    private final int COLOR_COUNT = 16;
    private final String COLORS_KEY = "colorsArr";
    private final String WASCLIKABLE_KEY = "wasClickable";
    private final String SCORE_KEY = "scoreCount";
    private final String TIMER_COUNT_KEY = "count_timer";
    private final String WASGAMEINITIALIZED_KEY = "wasGameInitialized";
    private final String ONECARDTURNED_KEY = "oneCardTurned";

    private boolean wasGameInitialized = false;
    private int oneCardTurned = -1;

    private LinearLayout llayoutMain;
    private TableLayout tlGame;
    private TextView tvTimer;

    private int[] colorsArr = new int[COLOR_COUNT];

    private View.OnClickListener ocl;
    private PairCards pair = new PairCards(this);

    private int scoreCount = 0;
    private int count_timer = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_find_similar);
        llayoutMain = (LinearLayout) findViewById(R.id.activity_game_find_similar);
        tlGame = (TableLayout) findViewById(R.id.tlGame);
        tvTimer = (TextView) findViewById(R.id.tvTimer);
        Log.d("mylogs","onCreate()");
        pair.setmListener(new PairCards.OnCardTurnedListener() {
            @Override
            public void onCardTurned(boolean result) {


                if(result){
                    scoreCount++;
                    tvTimer.setText(String.valueOf(scoreCount));
                    if(scoreCount >= COLOR_COUNT/2){
                        //startWinnerActivity();
                        tvTimer.setText("Win!");
                    }
                }
            }
        });

        ocl = new View.OnClickListener() { //лісенер для кнопок
            @Override
            public void onClick(View v) {
                pair.turnCard((CardView) v);
            }
        };

        if(savedInstanceState==null) {
            colorsArr = getResources().getIntArray(R.array.colorsForCards);
            shuffleArray(colorsArr);
            initGame(colorsArr);
        }
    }

    private void shuffleArray(int[] ar) {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }


    private void initGame(int[] colors){
        int i = 0;
        TableRow row;
        CardView view;
        while (i< COLOR_COUNT){
            row = (TableRow) tlGame.getChildAt(i/4);
            view = (CardView) row.getChildAt(i%4);
            view.setBackgroundColor(colors[i]);
            view.setFaceColor(colors[i]);
            //view.setClickable(true);
            i++;
        }



        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() { //Лютий піздец!! KILL IT WITH FIRE!!

            TableRow rrow;
            View rview;
            @Override
            public void run() {

                tvTimer.setText(String.valueOf(count_timer));
                count_timer--;
                if(count_timer>=0)
                    handler.postDelayed(this,1000);
                else {
                    tvTimer.setText(R.string.go);
                    int i=0;
                    while (i< COLOR_COUNT){
                        rrow = (TableRow) tlGame.getChildAt(i/4);
                        rview = rrow.getChildAt(i%4);
                        rview.setBackground(getResources().getDrawable(R.drawable.card_back));
                        rview.setOnClickListener(ocl);
                        i++;
                    }
                    wasGameInitialized = true;
                }
            }
        };

        handler.post(runnable);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("mylogs","onSaveInstanceState()");
        TableRow row;
        CardView view,test;
        boolean[] wasClickable = new boolean[COLOR_COUNT];
        for (int i = 0; i < COLOR_COUNT; i++) {
            row = (TableRow) tlGame.getChildAt(i/4);
            view = (CardView) row.getChildAt(i%4);
            wasClickable[i] = view.isClickable();
            test = pair.getCard1();
            if(pair.getCard1()==view)
                oneCardTurned = i;
        }



        outState.putBoolean(WASGAMEINITIALIZED_KEY,wasGameInitialized);
        outState.putInt(SCORE_KEY,scoreCount);
        outState.putInt(TIMER_COUNT_KEY,count_timer);
        outState.putBooleanArray(WASCLIKABLE_KEY,wasClickable);
        outState.putIntArray(COLORS_KEY,colorsArr);
        outState.putInt(ONECARDTURNED_KEY, oneCardTurned);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d("mylogs","onRestoreInstanceState()");
        TableRow row;
        CardView view;
        boolean[] wasClickable = savedInstanceState.getBooleanArray(WASCLIKABLE_KEY);
        colorsArr = savedInstanceState.getIntArray(COLORS_KEY);
        scoreCount = savedInstanceState.getInt(SCORE_KEY);
        count_timer = savedInstanceState.getInt(TIMER_COUNT_KEY);
        wasGameInitialized = savedInstanceState.getBoolean(WASGAMEINITIALIZED_KEY);
        oneCardTurned = savedInstanceState.getInt(ONECARDTURNED_KEY,oneCardTurned);


        tvTimer.setText(String.valueOf(scoreCount));
        for (int i = 0; i < COLOR_COUNT; i++) {
            row = (TableRow) tlGame.getChildAt(i/4);
            view = (CardView) row.getChildAt(i%4);
            view.setClickable(wasClickable[i]);
            view.setFaceColor(colorsArr[i]);
            if(!view.isClickable())
            {
                if(oneCardTurned==i){
                    view.setOnClickListener(ocl);
                    pair.setCard1(view);
                    oneCardTurned = -1;
                }
                view.setBackgroundColor(colorsArr[i]);
            }
            else {
                view.setBackground(getResources().getDrawable(R.drawable.card_back));
                view.setOnClickListener(ocl);
            }
        }

        if (!wasGameInitialized) {
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() { //Лютий піздец!! KILL IT WITH FIRE!!

                TableRow rrow;
                View rview;
                @Override
                public void run() {

                    tvTimer.setText(String.valueOf(count_timer));
                    count_timer--;
                    if(count_timer>=0)
                        handler.postDelayed(this,1000);
                    else {
                        tvTimer.setText(R.string.go);
                        int i=0;
                        while (i< COLOR_COUNT){
                            rrow = (TableRow) tlGame.getChildAt(i/4);
                            rview = rrow.getChildAt(i%4);
                            rview.setBackground(getResources().getDrawable(R.drawable.card_back));
                            rview.setOnClickListener(ocl);
                            i++;
                        }
                        wasGameInitialized = true;
                    }
                }
            };

            handler.post(runnable);
        }

        super.onRestoreInstanceState(savedInstanceState);
    }
}
