package com.takemysaree.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.takemysaree.R;

/**
 * Created by think360user on 3/11/17.
 */

public class OrderActivity extends Activity {
    private ImageView ivHamburger;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cofirm);
        ivHamburger=(ImageView)findViewById(R.id.ivHamburger);
        ivHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrderActivity.this,DashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
