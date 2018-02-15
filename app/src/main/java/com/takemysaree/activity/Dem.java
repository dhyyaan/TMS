package com.takemysaree.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.takemysaree.R;

/**
 * Created by think360user on 13/10/17.
 */

public class Dem extends Activity {
    int index = 1;
    int listviewCount =0;
    Runnable runnable;
    int ANIMATION_TIME = 1000;
    Handler handler = new Handler();
    ListView listView;
    // Array of strings...
    String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dem);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, mobileArray);

        listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
      listviewCount = adapter.getCount();


        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                if(listviewCount >= index){
                    handler.removeCallbacks(runnable);
                } else {
                    listView.smoothScrollToPosition(index);
                    index = index + 1;
                    handler.postDelayed(runnable , ANIMATION_TIME);
                }
            }
        }, 0);
    }
}
