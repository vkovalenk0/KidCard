package com.kovalenkovolodymyr.kidcard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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
    private int allTriesCount = 0;
    private int count_timer = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_find_similar);
        tlGame = (TableLayout) findViewById(R.id.tlGame);
        tvTimer = (TextView) findViewById(R.id.tvTimer);
        pair.setmListener(new PairCards.OnCardTurnedListener() {
            @Override
            public void onCardTurned(boolean result) {
                allTriesCount++;
                if (result) {
                    scoreCount++;
                    tvTimer.setText(String.valueOf(scoreCount));
                    if (scoreCount >= CARDS_COUNT / 2) {
                        Intent intent =
                                new Intent(GameFindSimilarActivity.this,WinActivity.class);
                        intent.putExtra(WinActivity.GAME_RESTART_EXTRA,WinActivity.GAME1_RESTART);
                        intent.putExtra(WinActivity.GAME_ACCURACY, (double)scoreCount/allTriesCount);
                        startActivity(intent);
                        finish();
                        //tvTimer.setText("Win!");
                    }
                }

            }
        });

        ocl = new View.OnClickListener() { //лісенер для кнопок
            @Override
            public void onClick(View v) {
                pair.turnCard((android.support.v7.widget.CardView) v);
            }
        };

        if (savedInstanceState == null) {
            getImages();
            shuffleArray(imagesArr);
            initGame();
        }
    }

    private void getImages() {

        imagesArr = new int[CARDS_COUNT];
        for (int i = 1; i <= CARDS_COUNT / 2; i++) {
            imagesArr[i - 1] = getResources().getIdentifier("pic" + i, "drawable", getPackageName());
        }

        System.arraycopy(imagesArr, 0, imagesArr, 8, CARDS_COUNT / 2);
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


    private void initGame() {
        int i = 0;
        TableRow row;
        android.support.v7.widget.CardView cv;
        CardView view;
        while (i < CARDS_COUNT) {
            row = (TableRow) tlGame.getChildAt(i / 4);
            cv = (android.support.v7.widget.CardView) row.getChildAt(i % 4);
            view = (CardView) cv.getChildAt(0);
            view.setImageDrawable(getResources().getDrawable(imagesArr[i]));
            view.setBackgroundColor(getResources().getColor(R.color.cardFaceColor));
            //view.setBackground(getResources().getDrawable(R.drawable.card_borders));
            view.setFaceImage(imagesArr[i]);
            i++;
        }


        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() { //Лютий піздец!! KILL IT WITH FIRE!!

            TableRow rrow;
            android.support.v7.widget.CardView rcv;
            CardView rview;

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
                        rcv = (android.support.v7.widget.CardView) rrow.getChildAt(i % 4);
                        rview = (CardView) rcv.getChildAt(0);
                        rview.setBackground(getResources().getDrawable(R.drawable.card_back));
                        rview.setImageDrawable(null);
                        rcv.setOnClickListener(ocl);
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
        TableRow row;
        android.support.v7.widget.CardView cv;
        boolean[] wasClickable = new boolean[CARDS_COUNT];
        for (int i = 0; i < CARDS_COUNT; i++) {
            row = (TableRow) tlGame.getChildAt(i / 4);
            cv = (android.support.v7.widget.CardView) row.getChildAt(i % 4);
            wasClickable[i] = cv.isClickable();
            if (pair.getCard1() == cv)
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
        TableRow row;
        android.support.v7.widget.CardView cv;
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
            cv = (android.support.v7.widget.CardView) row.getChildAt(i % 4);
            view = (CardView) cv.getChildAt(0);

            assert wasClickable != null;
            cv.setClickable(wasClickable[i]);
            view.setFaceImage(imagesArr[i]);
            if (!cv.isClickable()) {
                if (oneCardTurned == i) {
                    cv.setOnClickListener(ocl);
                    pair.setCard1(cv);
                    oneCardTurned = -1;
                }
                view.setImageDrawable(getResources().getDrawable(imagesArr[i]));
                view.setBackgroundColor(getResources().getColor(R.color.cardFaceColor));

            } else {
                view.setBackground(getResources().getDrawable(R.drawable.card_back));
                view.setImageDrawable(null);
                cv.setOnClickListener(ocl);
            }
        }

        if (!wasGameInitialized) {
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() { //Лютий піздец!! KILL IT WITH FIRE!!

                TableRow rrow;
                android.support.v7.widget.CardView rcv;
                CardView rview;

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
                            rcv = (android.support.v7.widget.CardView) rrow.getChildAt(i % 4);
                            rview = (CardView) rcv.getChildAt(0);
                            rview.setBackground(getResources().getDrawable(R.drawable.card_back));
                            rview.setImageDrawable(null);
                            rcv.setOnClickListener(ocl);
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
