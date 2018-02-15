package com.takemysaree.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import com.takemysaree.R;
import com.takemysaree.fonts.LatoBoldTextview;
import com.takemysaree.listeners.OnCustomerListChangedListener;
import com.takemysaree.listeners.OnStartDragListener;
import com.takemysaree.models.Customer;
import com.takemysaree.models.F1models;
import com.takemysaree.utils.ItemTouchHelperAdapter;
import com.takemysaree.utils.ItemTouchHelperViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Valentine on 10/18/2015.
 */
public class CustomerListAdapter extends
        RecyclerView.Adapter<CustomerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    private ArrayList<F1models> mCustomers;
    private Context mContext;
    private OnStartDragListener mDragStartListener;
    private OnCustomerListChangedListener mListChangedListener;

    public CustomerListAdapter(ArrayList<F1models> arrayList, Context context,
                               OnStartDragListener dragLlistener,
                               OnCustomerListChangedListener listChangedListener){
        mCustomers = arrayList;
        mContext = context;
        mDragStartListener = dragLlistener;
        mListChangedListener = listChangedListener;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_customer_list, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {

        //final Customer selectedCustomer = mCustomers.get(position);

        holder.customerName.setText(mCustomers.get(position).getProductname());
        holder.customerEmail.setText("$"+mCustomers.get(position).getRentprice());
        Picasso.with(mContext)
                .load(mCustomers.get(position).getSize())
                .placeholder(R.drawable.profilepic)
                .into(holder.profileImage);



        holder.handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return mCustomers.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mCustomers, fromPosition, toPosition);
        mListChangedListener.onNoteListChanged(mCustomers);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {

    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {
        public final LatoBoldTextview customerName, customerEmail;
        public final ImageView handleView, profileImage;


        public ItemViewHolder(View itemView) {
            super(itemView);
            customerName = (LatoBoldTextview)itemView.findViewById(R.id.text_view_customer_name);
            customerEmail = (LatoBoldTextview)itemView.findViewById(R.id.text_view_customer_email);
            handleView = (ImageView)itemView.findViewById(R.id.handle);
            profileImage = (ImageView)itemView.findViewById(R.id.image_view_customer_head_shot);


        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
