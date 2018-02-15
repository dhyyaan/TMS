package com.takemysaree.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.takemysaree.R;
import com.vistrav.ask.Ask;

import java.util.ArrayList;


public class SelectPrductCity extends AppCompatActivity {
    private RelativeLayout relus,relcanada,relboth;
    private static final int PERMISSION_REQUEST_CAMERA = 0;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    ArrayList<Uri> image_uris = new ArrayList<Uri>();
    private ImageView imagevs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcity);
        relus=(RelativeLayout)findViewById(R.id.relus);
        relcanada=(RelativeLayout)findViewById(R.id.relcanada);
        relboth=(RelativeLayout)findViewById(R.id.relboth);
        imagevs=(ImageView)findViewById(R.id.imagevs);
        imagevs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        relus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if( SelectPrduct.single.equals("1")) {
                  getCode();
              }else{
                  Intent intent=new Intent(SelectPrductCity.this,MultiDetailsActivity.class);
                  startActivity(intent);
                  finish();
              }



            }
        });
        relcanada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( SelectPrduct.single.equals("1")) {
                    getCode();
                }else{
                    Intent intent=new Intent(SelectPrductCity.this,MultiDetailsActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        relboth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( SelectPrduct.single.equals("1")) {
                    getCode();
                }else{
                    Intent intent=new Intent(SelectPrductCity.this,MultiDetailsActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private void getCode() {
        Config config = new Config();
        config.setToolbarTitleRes(R.string.app_name);
        config.setSelectionMin(1);
        config.setSelectionLimit(5);

        config.setFlashOn(true);


        getImages(config);

      /*  if (ContextCompat.checkSelfPermission(SelectPrductCity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SelectPrductCity.this,
                    new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CAMERA);

        }else{
            Config config = new Config();
            config.setToolbarTitleRes(R.string.app_name);
            config.setSelectionMin(1);
            config.setSelectionLimit(5);

            config.setFlashOn(true);


            getImages(config);
        }*/
    }

/*
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Config config = new Config();
                    config.setToolbarTitleRes(R.string.app_name);
                    config.setSelectionMin(1);
                    config.setSelectionLimit(5);

                    config.setFlashOn(true);


                    getImages(config);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }*/

    private void getImages(Config config) {


        ImagePickerActivity.setConfig(config);

        Intent intent = new Intent(this, ImagePickerActivity.class);

        intent.putExtra("checkvalue", "1");
        startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);


        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == INTENT_REQUEST_GET_IMAGES) {

                image_uris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);

                if (image_uris != null) {
                    if(SelectPrduct.single.equals("1")){
                        Intent intent1 = new Intent(SelectPrductCity.this, AddProductActivity1.class);
                        intent1.putParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS, image_uris);
                        startActivity(intent1);
                        finish();
                    }else{
                        Intent intent1 = new Intent(SelectPrductCity.this, MultiDetailsActivity.class);
                        intent1.putParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS, image_uris);
                        startActivity(intent1);
                        finish();
                    }

                }


            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent1 = new Intent(SelectPrductCity.this, SelectPrduct.class);
        startActivity(intent1);
        finish();
    }
}
