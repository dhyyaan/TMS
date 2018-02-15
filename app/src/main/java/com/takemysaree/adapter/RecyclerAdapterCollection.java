package com.takemysaree.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.takemysaree.R;
import com.takemysaree.models.RoundedTransformation;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterCollection extends RecyclerView.Adapter<RecyclerAdapterCollection.ViewHolder> {
    private Context mContext;
    List<String> images = new ArrayList<>();
    private int[] mImageResources = {
            R.drawable.n1,
            R.drawable.n2,
            R.drawable.n3,
            R.drawable.n4,
            R.drawable.n1,
            R.drawable.n2,
            R.drawable.n3,
            R.drawable.n4,
            R.drawable.n1,
            R.drawable.n2,
            R.drawable.n3,
    };


    public RecyclerAdapterCollection(Context mContext , List<String> images) {
        this.mContext = mContext;
        this.images=images;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_listdetails, parent, false);
        ViewHolder vh = new ViewHolder(view);


        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final int pos = position;
        Picasso.with(mContext)
                .load(images.get(pos))
                .placeholder(R.drawable.s1)
                .error(R.drawable.s1)

                .into(viewHolder.imageview1);






    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageview1;



        public ViewHolder(View itemView) {

            super(itemView);
            imageview1 = (ImageView) itemView.findViewById(R.id.imageview1);

        }
    }



}