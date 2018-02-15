package com.takemysaree.activity;


import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;


import com.takemysaree.R;
import com.takemysaree.adapter.RecyclerAdapter;

import java.util.List;


public class MyOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order);
        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        rv.setAdapter(new RecyclerAdapter(MyOrderActivity.this));
    }


}
