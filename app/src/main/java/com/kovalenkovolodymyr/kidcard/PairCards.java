package com.kovalenkovolodymyr.kidcard;

import android.content.Context;
import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by Volodymyr on 10/15/2016.
 */

public class PairCards {
    private final int ANIM_DURATION = 125;

    private CardView card1, card2;
    private OnCardTurnedListener mListener;
    private boolean clickBan = false; // Поки анімація триває не обробляєм кліки
    private boolean checking = false; // Встановлюєм в true під час перевірки
    private Runnable optionRunnable = null; //що виконується після зворотньої анімації
    private Context context;


    public PairCards(Context context) {
        this.context = context;
        card1 = null;
        card2 = null;
    }


    public void turnCard(final CardView v) { //true if cardColors equal
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


    private boolean checkCards() {

        checking = true;
        if (card1.getFaceImage() == card2.getFaceImage()) {
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

    private void turnAnimation(final CardView v) {

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

    private void turnBackAnimation(CardView v) {
        if (!checking) { //якщо переревертається на пузо

            v.setImageDrawable(context.getResources().getDrawable(v.getFaceImage()));
            v.setBackground(context.getResources().getDrawable(R.drawable.card_borders));
        } else {
            v.setBackground(context.getResources().getDrawable(R.drawable.card_back));
            v.setImageDrawable(null);
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

    public void setCard1(CardView view) {
        card1 = view;
    }

    public CardView getCard1() {
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
