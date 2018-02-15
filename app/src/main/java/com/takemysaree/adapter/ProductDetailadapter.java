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

import java.util.ArrayList;

public class ProductDetailadapter extends RecyclerView.Adapter<ProductDetailadapter.ViewHolder> {
    private Context mContext;
private ArrayList<String> images=new ArrayList<>();
private ArrayList<String> cmnts=new ArrayList<>();
private ArrayList<String> user_name=new ArrayList<>();
private ArrayList<String> dates=new ArrayList<>();


    public ProductDetailadapter(Context mContext, ArrayList<String> cmnts,
                                ArrayList<String> images,ArrayList<String> user_name,
                                ArrayList<String> dates) {
        this.mContext = mContext;
        this.images=images;
        this.cmnts=cmnts;
        this.user_name=user_name;
        this.dates=dates;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_rv_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final int pos = position;

viewHolder.tvName.setText(user_name.get(pos));
viewHolder.tvTime.setText(dates.get(pos));
viewHolder.tvMsg.setText(cmnts.get(pos));
        Picasso.with(mContext).
                load(images.get(position))
                .error(R.drawable.emptyimage1)
                .placeholder(R.drawable.emptyimage1)
                .into(viewHolder.ivProfile);




    }

    @Override
    public int getItemCount() {
        return cmnts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName,tvMsg,tvTime;
        public ImageView ivProfile;


        public ViewHolder(View itemView) {
            super(itemView);
            tvName=(TextView)itemView.findViewById(R.id.tvName);
            tvTime=(TextView)itemView.findViewById(R.id.tvTime);
            tvMsg=(TextView)itemView.findViewById(R.id.tvMsg);
            ivProfile = (ImageView) itemView.findViewById(R.id.ivProfile);

        }
    }



}