package com.takemysaree.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class LatoNormalEditText extends EditText {

    public LatoNormalEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LatoNormalEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LatoNormalEditText(Context context) {
        super(context);
        init();
    }

    private void init() {

            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Lato-Regular.ttf");
            setTypeface(tf);

    }
}