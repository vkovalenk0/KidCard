package com.kovalenkovolodymyr.kidcard;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

/**
 * Created by Volodymyr on 10/15/2016.
 */

public class PairCards {
    private final int ANIM_DURATION = 125;

    private android.support.v7.widget.CardView card1, card2;
    private android.support.v7.widget.CardView curLetterCard,curImageCard; //used in game2

    private OnCardTurnedListener mListener;
    private boolean clickBan = false; // Поки анімація триває не обробляєм кліки
    private boolean checking = false; // Встановлюєм в true під час перевірки
    private Runnable optionRunnable = null; //що виконується після зворотньої анімації
    private Context context;


    public PairCards(Context context) { //конструктор для game1
        this.context = context;
        card1 = null;
        card2 = null;
        curLetterCard = null;
        curImageCard = null;
    }

    //Конструктор game2
    public PairCards(Context context,
                     android.support.v7.widget.CardView curLetter,
                     android.support.v7.widget.CardView curImage){
        this.context = context;
        card1 = null;
        card2 = null;
        curLetterCard = curLetter;
        curImageCard = curImage;
    }

    public void turnCard(final android.support.v7.widget.CardView v) { //true if cardColors equal
        if (!clickBan) {
            // Handler handler = new Handler();
            clickBan = true; // Пофікшено баги з одночасним нажаттям на картки
            if (card1 == null) { //якщо нажали на першу карту
                card1 = v;
                card1.setClickable(false); //Пофікшено нажимання на 1 картку 2 рази
                turnAnimation(card1);
            } else {
                card2 = v;
                card2.setClickable(false);
                turnAnimation(card2);
            }
        }
    }

    public void chooseCard(android.support.v7.widget.CardView v){
        chooseAnimation(v);
    }

    private void chooseAnimation(final android.support.v7.widget.CardView v) {
        final View innerView = v.getChildAt(0);
        Runnable postAnim = null;
        if (innerView instanceof TextView){
            postAnim = new Runnable() {
                @Override
                public void run() {
                    ((TextView) curLetterCard.getChildAt(0))
                            .setText(((TextView) innerView).getText());
                    showCurCardAnimation(curLetterCard);
                }
            };
        }
        if (innerView instanceof CardView) {
            postAnim = new Runnable() {
                @Override
                public void run() {
                    CardView inImageCard = ((CardView) curImageCard.getChildAt(0));

                    inImageCard.setFaceImage(((CardView) innerView).getFaceImage());
                    inImageCard.setLetter(((CardView) innerView).getLetter());

                    inImageCard.setImageResource(inImageCard.getFaceImage());

                    showCurCardAnimation(curImageCard);
                }
            };
        }

        v.animate()
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .scaleX(0)
                .scaleY(0)
                .setDuration(ANIM_DURATION)
                .withEndAction(postAnim)
                .start();

    }

    private void showCurCardAnimation(final android.support.v7.widget.CardView curCard) {
        Runnable postAnim = new Runnable() {
            @Override
            public void run() {
                curCard.setClickable(true);
            }
        };

        curCard.animate()
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .scaleY(1)
                .scaleX(1)
                .setDuration(ANIM_DURATION)
                .withEndAction(postAnim)
                .start();
    }

    public void unchooseCard(android.support.v7.widget.CardView v) {

    }

    private boolean checkCards() {
        CardView inCard1 = (CardView) card1.getChildAt(0);
        CardView inCard2 = (CardView) card2.getChildAt(0);

        checking = true;
        if (inCard1.getFaceImage() == inCard2.getFaceImage()) {
            resetCards(ANIM_DURATION); // мб +1
            return true;
        }

        card1.setClickable(true);
        card2.setClickable(true);
        turnAnimation(card1);
        turnAnimation(card2);

        resetCards(2 * ANIM_DURATION);
        return false;
    }

    private void turnAnimation(final android.support.v7.widget.CardView v) {

        //якщо нажато на 1 карту
        if (card2 == null) {
            optionRunnable = new Runnable() { //Вова, це треба, не видаляй!!!! (дозволяє нажимати)
                @Override
                public void run() {
                    clickBan = false;
                }
            };
        }
        //якщо нажато на 2 карту
        else {
            optionRunnable = new Runnable() {
                @Override
                public void run() {
                    mListener.onCardTurned(checkCards());
                }
            };
        }

        //якщо карти перевертаються назад
        if (checking) {
            if (v.isClickable()) { //якщо карти не вгадано
                optionRunnable = null;
            } else { //якщо вгадано анімації немає
                return;
            }
        }

        v.animate()
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .scaleX(0)
                .setDuration(ANIM_DURATION)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        turnBackAnimation(v);
                    }
                })
                .start();
    }

    private void turnBackAnimation(android.support.v7.widget.CardView v) {
        CardView inCard = (CardView) v.getChildAt(0);
        if (!checking) { //якщо переревертається на пузо

            inCard.setImageDrawable(context.getResources().getDrawable(inCard.getFaceImage()));
            inCard.setBackgroundColor(context.getResources().getColor(R.color.cardFaceColor));

            //v.setBackground(context.getResources().getDrawable(R.drawable.card_borders));
        } else {
            inCard.setBackground(context.getResources().getDrawable(R.drawable.card_back));
            inCard.setImageDrawable(null);
        }
        v.animate()
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .scaleX(1)
                .setDuration(ANIM_DURATION)
                .withEndAction(optionRunnable)
                .start();

    }

    public OnCardTurnedListener getmListener() {
        return mListener;
    }

    public void setmListener(OnCardTurnedListener mListener) {
        this.mListener = mListener;
    }



    public interface OnCardTurnedListener {
        void onCardTurned(boolean result);
    }

    public void setCard1(android.support.v7.widget.CardView view) {
        card1 = view;
    }

    public android.support.v7.widget.CardView getCard1() {
        return card1;
    }

    private void resetCards(int delay) { //(int kostyl)
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                card1 = null;
                card2 = null;
                checking = false;
                clickBan = false;
            }
        }, delay);
    }
}
