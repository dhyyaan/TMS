package com.takemysaree.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.takemysaree.R;

public class RecyclerAdapterOffer extends RecyclerView.Adapter<RecyclerAdapterOffer.ViewHolder> {
    private Context mContext;



    public RecyclerAdapterOffer(Context mContext ) {
        this.mContext = mContext;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_offers_rv_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final int pos = position;






    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public CheckBox chkSelected;


        public ViewHolder(View itemView) {
            super(itemView);

        }
    }



}