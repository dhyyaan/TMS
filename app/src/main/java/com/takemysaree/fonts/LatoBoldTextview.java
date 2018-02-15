package com.takemysaree.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by think360user on 11/10/17.
 */

public class LatoBoldTextview extends TextView {
    public LatoBoldTextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LatoBoldTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LatoBoldTextview(Context context) {
        super(context);
        init();
    }

    private void init() {

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Lato-Bold.ttf");
        setTypeface(tf);

    }
}
