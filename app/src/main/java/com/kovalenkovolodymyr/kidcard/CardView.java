package com.kovalenkovolodymyr.kidcard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Volodymyr on 10/15/2016.
 */

public class CardView extends ImageView {
    private int faceColor;
    private int faceImage;
    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setFaceColor(int color){
        faceColor = color;
    }

    public int getFaceColor() {
        return faceColor;
    }

    public int getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(int faceImage) {
        this.faceImage = faceImage;
    }
}
