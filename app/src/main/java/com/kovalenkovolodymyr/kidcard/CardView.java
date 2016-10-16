package com.kovalenkovolodymyr.kidcard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Volodymyr on 10/15/2016.
 */

public class CardView extends View {
    private int faceColor;
    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setFaceColor(int color){
        faceColor = color;
    }

    public int getFaceColor() {
        return faceColor;
    }
}
