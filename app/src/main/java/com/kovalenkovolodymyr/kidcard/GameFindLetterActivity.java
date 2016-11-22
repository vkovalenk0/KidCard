package com.kovalenkovolodymyr.kidcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Random;

public class GameFindLetterActivity extends AppCompatActivity {
    private final int CARDS_COUNT = 16;
    private int countLetters;
    private int score = 0;

    private int[] imagesArr;
    private String alphabet;

    private TableLayout tlGame;
    private View.OnClickListener ocl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_find_letter);
        alphabet = getResources().getString(R.string.alphabet);
        countLetters = alphabet.length();
        tlGame = (TableLayout) findViewById(R.id.tlGame2);

        android.support.v7.widget.CardView cvCurrentLetter =
                (android.support.v7.widget.CardView) findViewById(R.id.cvCurrentLetter);
        android.support.v7.widget.CardView cvCurrentImage =
                (android.support.v7.widget.CardView) findViewById(R.id.cvCurrentImage);

        final PairCards pair = new PairCards(this,cvCurrentLetter,cvCurrentImage);
        pair.setmListener(new PairCards.OnCardTurnedListener() {
            @Override
            public void onCardTurned(boolean result) {
                if(result){
                    score++;
                    Log.d("mylogs", "score " + score);
                    if(score == CARDS_COUNT/2){
                        Intent intent =
                                new Intent(GameFindLetterActivity.this,WinActivity.class);
                        intent.putExtra(WinActivity.GAME_RESTART_EXTRA,WinActivity.GAME2_RESTART);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        ocl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.cvCurrentLetter:
                    case R.id.cvCurrentImage:
                        pair.unchooseCard((android.support.v7.widget.CardView) v);
                        break;
                    default:
                        pair.chooseCard(((android.support.v7.widget.CardView) v));

                }

            }
        };

        CardView[] cardViews;
        TextView[] textViews;
        View[] allViews;

        imagesArr = getImages();
        shuffleAbcAndImages(); //синхронно перемішує картинки і букви
        cardViews = formCardViews(alphabet, imagesArr);
        textViews = formTextViews(alphabet);
        allViews = shuffleViews(cardViews,textViews);
        initGame(allViews);
    }

    private View[] shuffleViews(CardView[] cardViews, TextView[] textViews) {
        View[] views = new View[CARDS_COUNT];
        System.arraycopy(cardViews, 0, views, 0, CARDS_COUNT / 2);
        System.arraycopy(textViews, 0, views, CARDS_COUNT / 2, CARDS_COUNT - 8);

        Random rnd = new Random();
        for (int i = views.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            View a = views[index];
            views[index] = views[i];
            views[i] = a;
        }
        return views;
    }

    private void initGame(View[] views) {
        TableRow tr;
        android.support.v7.widget.CardView cv;
        android.support.v7.widget.CardView cvCurrentLetter =
                (android.support.v7.widget.CardView) findViewById(R.id.cvCurrentLetter);
        android.support.v7.widget.CardView cvCurrentImage =
                (android.support.v7.widget.CardView) findViewById(R.id.cvCurrentImage);

        cvCurrentLetter.setScaleX(0);
        cvCurrentLetter.setScaleY(0);
        cvCurrentLetter.setOnClickListener(ocl);
        //cvCurrentLetter.setClickable(false);

        cvCurrentImage.setScaleX(0);
        cvCurrentImage.setScaleY(0);
        cvCurrentImage.setOnClickListener(ocl);
        //cvCurrentImage.setClickable(false);


        for(int i = 0;i<CARDS_COUNT;i++){
            tr = ((TableRow) tlGame.getChildAt(i / 4));
            cv = (android.support.v7.widget.CardView) tr.getChildAt(i%4);
            cv.addView(views[i]);
            cv.setOnClickListener(ocl);
        }
    }



    private void shuffleAbcAndImages() {

        Random rnd = new Random();
        char[] abcArr = alphabet.toCharArray();

        for (int i = abcArr.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            char a = abcArr[index];
            abcArr[index] = abcArr[i];
            abcArr[i] = a;

            int b = imagesArr[index];
            imagesArr[index] = imagesArr[i];
            imagesArr[i] = b;
        }

        alphabet = String.valueOf(abcArr);
    }

    private TextView[] formTextViews(String s) {
        TextView[] texts = new TextView[countLetters];

        for(int i = 0;i<CARDS_COUNT/2;i++){
            texts[i] = new TextView(this);
            texts[i].setText(String.valueOf(alphabet.charAt(i)));
            texts[i].setGravity(Gravity.CENTER);

            texts[i].setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimensionPixelSize(R.dimen.card_text_size));

            texts[i].setLayoutParams(new android.support.v7.widget.CardView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
        }

        return texts;
    }

    private CardView[] formCardViews(String abc, int[] images) {
        CardView[] cards = new CardView[countLetters];
        int padding = getResources().getDimensionPixelSize(R.dimen.card_margin);

        for (int i = 0; i < CARDS_COUNT/2; i++) {
            cards[i] = new CardView(this, null);
            cards[i].setFaceImage(images[i]);
            cards[i].setImageDrawable(getResources().getDrawable(images[i]));
            cards[i].setLetter(abc.charAt(i));
            cards[i].setLayoutParams(new android.support.v7.widget.CardView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            cards[i].setPadding(padding,padding,padding,padding);
        }

        return cards;
    }

    private int[] getImages() {
        int[] images = new int[countLetters];

        for (int i = 0; i < countLetters; i++) {
            images[i] = getResources().getIdentifier("abc" + i, "drawable", getPackageName());
        }
        return images;
    }


}
