package com.kovalenkovolodymyr.kidcard;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Random;

public class GameFindSimilarActivity extends AppCompatActivity {

    private final int CARDS_COUNT = 16;

    private final String WASCLIKABLE_KEY = "wasClickable";
    private final String SCORE_KEY = "scoreCount";
    private final String TIMER_COUNT_KEY = "count_timer";
    private final String WASGAMEINITIALIZED_KEY = "wasGameInitialized";
    private final String ONECARDTURNED_KEY = "oneCardTurned";
    private final String IMAGES_KEY = "imagesArr";


    private boolean wasGameInitialized = false;
    private int oneCardTurned = -1;


    private TableLayout tlGame;
    private TextView tvTimer;

    private int[] imagesArr;

    private View.OnClickListener ocl;
    private PairCards pair = new PairCards(this);

    private int scoreCount = 0;
    private int count_timer = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_find_similar);
        tlGame = (TableLayout) findViewById(R.id.tlGame);
        tvTimer = (TextView) findViewById(R.id.tvTimer);
        Log.d("mylogs", "onCreate()");
        pair.setmListener(new PairCards.OnCardTurnedListener() {
            @Override
            public void onCardTurned(boolean result) {
                if (result) {
                    scoreCount++;
                    tvTimer.setText(String.valueOf(scoreCount));
                    if (scoreCount >= CARDS_COUNT / 2) {
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

        if (savedInstanceState == null) {
            getImages();
            shuffleArray(imagesArr);
            initGame(imagesArr);
        }
    }

    private void getImages() {

        imagesArr = new int[CARDS_COUNT];
        for (int i = 1; i <= CARDS_COUNT / 2; i++) {
            imagesArr[i - 1] = getResources().getIdentifier("pic" + i, "drawable", getPackageName());
        }
        for (int i = 0; i < CARDS_COUNT / 2; i++) {
            imagesArr[i + 8] = imagesArr[i];
        }
    }

    private void shuffleArray(int[] ar) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }


    private void initGame(int[] images) {
        int i = 0;
        TableRow row;
        CardView view;
        while (i < CARDS_COUNT) {
            row = (TableRow) tlGame.getChildAt(i / 4);
            view = (CardView) row.getChildAt(i % 4);
            view.setBackground(getResources().getDrawable(imagesArr[i]));
            view.setFaceImage(imagesArr[i]);
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
                if (count_timer >= 0)
                    handler.postDelayed(this, 1000);
                else {
                    tvTimer.setText(R.string.go);
                    int i = 0;
                    while (i < CARDS_COUNT) {
                        rrow = (TableRow) tlGame.getChildAt(i / 4);
                        rview = rrow.getChildAt(i % 4);
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
        Log.d("mylogs", "onSaveInstanceState()");
        TableRow row;
        CardView view;
        boolean[] wasClickable = new boolean[CARDS_COUNT];
        for (int i = 0; i < CARDS_COUNT; i++) {
            row = (TableRow) tlGame.getChildAt(i / 4);
            view = (CardView) row.getChildAt(i % 4);
            wasClickable[i] = view.isClickable();
            if (pair.getCard1() == view)
                oneCardTurned = i;
        }


        outState.putBoolean(WASGAMEINITIALIZED_KEY, wasGameInitialized);
        outState.putInt(SCORE_KEY, scoreCount);
        outState.putInt(TIMER_COUNT_KEY, count_timer);
        outState.putBooleanArray(WASCLIKABLE_KEY, wasClickable);
        outState.putIntArray(IMAGES_KEY, imagesArr);
        outState.putInt(ONECARDTURNED_KEY, oneCardTurned);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d("mylogs", "onRestoreInstanceState()");
        TableRow row;
        CardView view;
        boolean[] wasClickable = savedInstanceState.getBooleanArray(WASCLIKABLE_KEY);
        imagesArr = savedInstanceState.getIntArray(IMAGES_KEY);
        scoreCount = savedInstanceState.getInt(SCORE_KEY);
        count_timer = savedInstanceState.getInt(TIMER_COUNT_KEY);
        wasGameInitialized = savedInstanceState.getBoolean(WASGAMEINITIALIZED_KEY);
        oneCardTurned = savedInstanceState.getInt(ONECARDTURNED_KEY, oneCardTurned);


        tvTimer.setText(String.valueOf(scoreCount));
        for (int i = 0; i < CARDS_COUNT; i++) {
            row = (TableRow) tlGame.getChildAt(i / 4);
            view = (CardView) row.getChildAt(i % 4);
            view.setClickable(wasClickable[i]);
            view.setFaceImage(imagesArr[i]);
            if (!view.isClickable()) {
                if (oneCardTurned == i) {
                    view.setOnClickListener(ocl);
                    pair.setCard1(view);
                    oneCardTurned = -1;
                }
                view.setBackground(getResources().getDrawable(imagesArr[i]));
            } else {
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
                    if (count_timer >= 0)
                        handler.postDelayed(this, 1000);
                    else {
                        tvTimer.setText(R.string.go);
                        int i = 0;
                        while (i < CARDS_COUNT) {
                            rrow = (TableRow) tlGame.getChildAt(i / 4);
                            rview = rrow.getChildAt(i % 4);
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
