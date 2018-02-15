package com.takemysaree.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;
import com.mukesh.countrypicker.models.Country;
import com.takemysaree.R;
import com.takemysaree.colorpalette.SpectrumPalette;
import com.takemysaree.fonts.LatoRegularTextview;


/**
 * Created by think360 on 03/11/17.
 */

public class ItemFragment extends Fragment implements View.OnClickListener{
    private LatoRegularTextview btnRent,btnSell,btnBoth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_fragment, container, false);
        btnSell=(LatoRegularTextview)rootView.findViewById(R.id.btnSell);
        btnRent=(LatoRegularTextview)rootView.findViewById(R.id.btnRent);
        btnBoth=(LatoRegularTextview)rootView.findViewById(R.id.btnBoth);
        initListeners();
        SpectrumPalette ss = (SpectrumPalette) rootView.findViewById(R.id.palette);
         ss.setOnColorSelectedListener(new SpectrumPalette.OnColorSelectedListener() {
             @Override
             public void onColorSelected(int color) {
                 String hexColor = "#"+ String.format("%06X", (color)).substring(2);
                 Toast.makeText(getActivity(),""+hexColor, Toast.LENGTH_SHORT).show();
             }
         });
        Country country = Country.getCountryFromSIM(getActivity());

        final Button etChooseCountry1 = (Button) rootView.findViewById(R.id.btnChooseCountry1);
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
                      //  etChooseCountry1.setCompoundDrawablesWithIntrinsicBounds( flagDrawableResID,0, 0, 0);
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
                       // etChooseCountry2.setCompoundDrawablesWithIntrinsicBounds( flagDrawableResID,0, 0, 0);
                        etChooseCountry2.setCompoundDrawablesWithIntrinsicBounds( new BitmapDrawable(getResources(),
                                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),flagDrawableResID),
                                        65, 65, true)),null, null, null);
                        etChooseCountry2.setText(name);
                        picker.dismiss();
                    }
                });
                picker.show(getChildFragmentManager(), "COUNTRY_PICKER");
            }
        });
        return rootView;
    }

    private void initListeners() {
        btnSell.setOnClickListener(this);
        btnBoth.setOnClickListener(this);
        btnRent.setOnClickListener(this);
    }


    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRent:
                btnRent.setTextColor(Color.parseColor("#ffffff"));
                btnRent.setBackground(getResources().getDrawable(R.drawable.round_rectangle));
                btnSell.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                btnSell.setTextColor(Color.parseColor("#919191"));
                btnBoth.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                btnBoth.setTextColor(Color.parseColor("#919191"));
                btnRent.setPadding(42,0,42,0);
                btnSell.setPadding(42,0,42,0);
                btnBoth.setPadding(42,0,42,0);
                break;
            case R.id.btnSell:
                btnSell.setTextColor(Color.parseColor("#ffffff"));
                btnSell.setBackground(getResources().getDrawable(R.drawable.round_rectangle));
                btnRent.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                btnRent.setTextColor(Color.parseColor("#919191"));
                btnBoth.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                btnBoth.setTextColor(Color.parseColor("#919191"));
                btnRent.setPadding(42,0,42,0);
                btnSell.setPadding(42,0,42,0);
                btnBoth.setPadding(42,0,42,0);
                break;
            case R.id.btnBoth:
                btnBoth.setTextColor(Color.parseColor("#ffffff"));
                btnBoth.setBackground(getResources().getDrawable(R.drawable.round_rectangle));
                btnSell.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                btnSell.setTextColor(Color.parseColor("#919191"));
                btnRent.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                btnRent.setTextColor(Color.parseColor("#919191"));
                btnRent.setPadding(42,0,42,0);
                btnSell.setPadding(42,0,42,0);
                btnBoth.setPadding(42,0,42,0);
                break;
        }

    }
}