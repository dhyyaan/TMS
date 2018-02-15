package com.takemysaree.listeners;



import com.takemysaree.models.Customer;
import com.takemysaree.models.F1models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valentine on 9/5/2015.
 */
public interface OnCustomerListChangedListener {
    void onNoteListChanged(ArrayList<F1models> models);
}
