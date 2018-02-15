package com.takemysaree.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by think360user on 17/11/17.
 */

public class MyObjects implements Parcelable {

    private int age;
    private String name;

    private ArrayList<String> address;

    public MyObjects(String name, int age, ArrayList<String> address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }
public MyObjects (Parcel source){
        age=source.readInt();
        name=source.readString();
        address=source.createStringArrayList();
}

    public static final Creator<MyObjects> CREATOR = new Creator<MyObjects>() {
        @Override
        public MyObjects createFromParcel(Parcel in) {
            return new MyObjects(in);
        }

        @Override
        public MyObjects[] newArray(int size) {
            return new MyObjects[size];
        }
    };

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getAddress() {
        return address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(age);
        dest.writeString(name);
        dest.writeStringList(address);

    }
}
