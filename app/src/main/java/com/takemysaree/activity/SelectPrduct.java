package com.takemysaree.activity;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.takemysaree.R;
import com.takemysaree.adapter.RecyclerAdapterOffer;
import com.vistrav.ask.Ask;


public class SelectPrduct extends AppCompatActivity {
    private RelativeLayout relsingle,relmulti;
    static String single;
    private ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddroducts);
        relsingle=(RelativeLayout)findViewById(R.id.relsingle);
        relmulti=(RelativeLayout)findViewById(R.id.relmulti);
        imageview=(ImageView)findViewById(R.id.imageview);
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        relsingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ask.on(SelectPrduct.this)
                        .forPermissions(Manifest.permission.CAMERA
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withRationales("Camera permission need for camera to work properly",
                                "In order to save file you will need to grant storage permission") //optional
                        .go();
                single="1";
                Intent intent=new Intent(SelectPrduct.this,SelectPrductCity.class);
                startActivity(intent);
                finish();
            }
        });
        relmulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ask.on(SelectPrduct.this)
                        .forPermissions(Manifest.permission.CAMERA
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withRationales("Camera permission need for camera to work properly",
                                "In order to save file you will need to grant storage permission") //optional
                        .go();
                single="2";
                Intent intent=new Intent(SelectPrduct.this,SelectPrductCity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(SelectPrduct.this,DashBoardActivity.class);
        startActivity(intent);
        finish();
    }
}
