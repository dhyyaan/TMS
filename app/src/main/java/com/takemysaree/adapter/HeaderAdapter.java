package com.takemysaree.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.takemysaree.R;
import com.takemysaree.activity.Login;

import java.util.ArrayList;

public class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.HeaderHolder> {
    Context mContext;
    ArrayList<String> arrayList=new ArrayList<>();

    public HeaderAdapter(Context mContext, ArrayList<String> imagearray) {
        this.mContext = mContext;
        arrayList=imagearray;

    }


    @Override
    public HeaderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_child_menu, parent, false);
        HeaderHolder headerHolder = new HeaderHolder(view);
        return headerHolder;
    }



    @Override
    public void onBindViewHolder(HeaderHolder holder, int position) {
        try {
            Picasso.with(mContext).
                    load(arrayList.get(position))
                    .error(R.drawable.intro)
                    .placeholder(R.drawable.intro)
                    .into(holder.difficon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class HeaderHolder extends RecyclerView.ViewHolder {

        private TextView menuitems;
        private ImageView difficon;

        public HeaderHolder(View itemView) {
            super(itemView);


            difficon = (ImageView) itemView.findViewById(R.id.difficon);
        }
    }




}
