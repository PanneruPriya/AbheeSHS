package com.abhee.abheetechnician2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Admin on 07-07-2018.
 */

public class NotificationsActivity extends Fragment {


    public static NotificationsActivity newInstance() {
        NotificationsActivity fragment = new NotificationsActivity();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

// Inflate the layout for this fragment
        View v;
        v = inflater.inflate(R.layout.activity_notifications, container, false);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getActivity(), LoginPageActivity.class));
        return v;
    }


}
