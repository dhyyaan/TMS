package com.takemysaree.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;
import com.mukesh.countrypicker.models.Country;
import com.takemysaree.R;

/**
 * Created by think360 on 03/11/17.
 */

public class VendorFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.vendor_fragment, container, false);
        final Button etChooseCountry1 = (Button) rootView.findViewById(R.id.btnChooseCountry1);
        Country country = Country.getCountryFromSIM(getActivity());



        etChooseCountry1.setCompoundDrawablesWithIntrinsicBounds( new BitmapDrawable(getResources(),
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),country.getFlag()),
                        65, 65, true)),null, null, null);
        etChooseCountry1.setText(country.getName());


        etChooseCountry1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CountryPicker picker = CountryPicker.newInstance("Select Country");  // dialog title
                picker.setListener(new CountryPickerListener() {
                    @Override
                    public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                        // Implement your code here

                        etChooseCountry1.setCompoundDrawablesWithIntrinsicBounds( new BitmapDrawable(getResources(),
                                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),flagDrawableResID),
                                        65, 65, true)),null, null, null);
                        etChooseCountry1.setText(name);
                        picker.dismiss();

                    }
                });
                picker.show(getChildFragmentManager(), "COUNTRY_PICKER");
            }
        });
        final Button etChooseCountry2 = (Button) rootView.findViewById(R.id.btnChooseCountry2);
        etChooseCountry2.setCompoundDrawablesWithIntrinsicBounds( new BitmapDrawable(getResources(),
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),country.getFlag()),
                        65, 65, true)),null, null, null);
        etChooseCountry2.setText(country.getName());
        etChooseCountry2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CountryPicker picker = CountryPicker.newInstance("Select Country");  // dialog title
                picker.setListener(new CountryPickerListener() {
                    @Override
                    public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                        // Implement your code here
                        etChooseCountry2.setCompoundDrawablesWithIntrinsicBounds( new BitmapDrawable(getResources(),
                                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),flagDrawableResID),
                                        65, 65, true)),null, null, null);
                         // etChooseCountry2.setCompoundDrawablesWithIntrinsicBounds( flagDrawableResID,0, 0, 0);
                          etChooseCountry2.setText(name);
                          picker.dismiss();
                    }
                });
                picker.show(getChildFragmentManager(), "COUNTRY_PICKER");
            }
        });
        return rootView;
    }

}