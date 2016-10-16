package com.kovalenkovolodymyr.kidcard;

import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;

/**
 * Created by Volodymyr on 10/15/2016.
 */

public class PairCards {
    private CardView card1, card2;
    private OnCardTurnedListener mListener;
    private boolean cardsTurned = false;

    public PairCards()
    {
        card1 = null;
        card2 = null;
    }


    public void turnCard(CardView v){ //true if cardColors equal
        if(!cardsTurned) {
            Handler handler = new Handler();
            if (card1 == null) {
                card1 = v;
                card1.setBackgroundColor(card1.getFaceColor());
                card1.setClickable(false); //Пофікшено нажимання на 1 картку 2 рази
                mListener.onCardTurned(false);
                return;
            } else {
                card2 = v;
                card2.setBackgroundColor(card2.getFaceColor());
                card2.setClickable(false);
                cardsTurned = true;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mListener.onCardTurned(checkCards());

                    }
                }, 350);
            }
        }
        return;

    }


    private boolean checkCards(){
        cardsTurned = false;

        if(card1.getFaceColor() == card2.getFaceColor()){
            card1 = null;
            card2 = null;
            return true;
        }
        card1.setBackgroundColor(Color.BLACK);
        card2.setBackgroundColor(Color.BLACK);
        card1.setClickable(true);
        card2.setClickable(true);
        card1 = null;
        card2 = null;
        return false;
    }

    public OnCardTurnedListener getmListener() {
        return mListener;
    }

    public void setmListener(OnCardTurnedListener mListener) {
        this.mListener = mListener;
    }

    public interface OnCardTurnedListener{
        void onCardTurned(boolean result);
    }

    public void setCard1(CardView view){
        card1 = view;
    }
    public CardView getCard1(){
        return card1;
    }
    public CardView getCard2(){
        return card2;
    }
}
