package com.kovalenkovolodymyr.kidcard;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;

/**
 * Created by Volodymyr on 10/15/2016.
 */

public class PairCards {
    private CardView card1, card2;
    private OnCardTurnedListener mListener;
    private boolean cardsTurned = false;
    private Context context;

    public PairCards(Context context) {
        this.context = context;
        card1 = null;
        card2 = null;
    }


    public void turnCard(final CardView v) { //true if cardColors equal
        if (!cardsTurned) {
            // Handler handler = new Handler();
            if (card1 == null) {
                card1 = v;

                card1.setClickable(false); //Пофікшено нажимання на 1 картку 2 рази
                card1.animate()
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .setDuration(200)
                        .scaleX(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                card1.setBackgroundColor(card1.getFaceColor());

                                mListener.onCardTurned(false);

                                card1.animate()
                                        .setInterpolator(new AccelerateDecelerateInterpolator())
                                        .setDuration(200)
                                        .scaleX(1)
                                        .start();
                            }
                        })
                        .start();
                return;
            } else {
                card2 = v;
                card2.setClickable(false);
                card2.animate()
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .setDuration(200)
                        .scaleX(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                card2.setBackgroundColor(card2.getFaceColor());
                                cardsTurned = true;


                                card2.animate()
                                        .setInterpolator(new AccelerateDecelerateInterpolator())
                                        .setDuration(200)
                                        .scaleX(1)
                                        .withEndAction(new Runnable() {
                                            @Override
                                            public void run() {
                                                mListener.onCardTurned(checkCards());
                                            }
                                        })
                                        .start();

                            }
                        })
                        .start();
              /*  handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mListener.onCardTurned(checkCards());

                    }
                }, 350);*/
            }
        }
        return;

    }


    private boolean checkCards() {


        if (card1.getFaceColor() == card2.getFaceColor()) {
            card1 = null;
            card2 = null;
            cardsTurned = false;
            return true;
        }



        card1.setClickable(true);
        card2.setClickable(true);
        /*card1.animate()
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .scaleX(0)
                .setDuration(200)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        card1.setBackground(context.getResources().getDrawable(R.drawable.card_back));
                        card1.animate()
                                .setInterpolator(new AccelerateDecelerateInterpolator())
                                .scaleX(1)
                                .setDuration(200)
                                .start();
                        card1 = null;
                    }
                })
                .start();

        card2.animate()
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .scaleX(0)
                .setDuration(200)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        card2.setBackground(context.getResources().getDrawable(R.drawable.card_back));
                        card2.animate()
                                .setInterpolator(new AccelerateDecelerateInterpolator())
                                .scaleX(1)
                                .setDuration(200)
                                .start();
                        card2 = null;
                    }
                })
                .start();*/
            turnAnimation(card1);
            turnAnimation(card2);
            Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                card1 = null;
                card2 = null;
            }
        },401);

        return false;
    }

    private void turnAnimation(final View v){
        v.animate()
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .scaleX(0)
                .setDuration(200)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                       turnBackAnimation(v);
                    }
                })
                .start();
    }

    private void turnBackAnimation(View v){
        v.setBackground(context.getResources().getDrawable(R.drawable.card_back));
        v.animate()
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .scaleX(1)
                .setDuration(200)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        cardsTurned = false;
                    }
                })
                .start();
        v = null;
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

    public CardView getCard2() {
        return card2;
    }
}
