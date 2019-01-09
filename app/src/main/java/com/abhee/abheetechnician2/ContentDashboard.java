package com.abhee.abheetechnician2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static android.content.Context.NOTIFICATION_SERVICE;

public class ContentDashboard extends Fragment {
    TextView user;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edt;
    FragmentTransaction transaction;
    FragmentManager manager;
    ImageView lin1, lin2;
    Display display;
    int width;
    int height;

    public static ContentDashboard newInstance() {
        ContentDashboard fragment = new ContentDashboard();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.content_dashboard, container, false);



        display=getActivity().getWindowManager().getDefaultDisplay();
        sharedPreferences = getActivity().getSharedPreferences("Abhee", Context.MODE_PRIVATE);
        String fname = sharedPreferences.getString("fname", "");
        String lname = sharedPreferences.getString("lname", "");

       /* Intent intent = new Intent(getActivity(), NotificationReceiverActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(getActivity(), (int) System.currentTimeMillis(), intent, 0);

               Notification noti = new Notification.Builder(getActivity())
                .setContentTitle("New mail from " + "test@gmail.com")
                .setContentText("Subject").setSmallIcon(R.drawable.icon)
                .setContentIntent(pIntent).build();
              *//*  .addAction(R.drawable.icon, "Call", pIntent)
                .addAction(R.drawable.icon, "More", pIntent)
                .addAction(R.drawable.icon, "And more", pIntent).build();
     *//*   NotificationManager notificationManager = (NotificationManager)getActivity().getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);

*/
        lin1 = (ImageView) v.findViewById(R.id.image7);
        int width=display.getWidth();
        int height=display.getHeight();
        ViewGroup.LayoutParams params=lin1.getLayoutParams();
        params.width=width/2;
        params.height=height/10;
        lin1.setLayoutParams(params);
        lin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, new ServiceRequestsActivity().newInstance());
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.addToBackStack("tag");
                transaction.commit();
            }
        });
        lin2 = (ImageView) v.findViewById(R.id.image8);
        lin2.setVisibility(View.GONE);
         width=display.getWidth();
         height=display.getHeight();
        Log.i("kir",""+width);
        ViewGroup.LayoutParams params1=lin2.getLayoutParams();
        params1.width=width/2;
        params1.height=height/10;
        lin2.setLayoutParams(params1);

         lin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, new ProductsWarrantyActivity().newInstance());
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.addToBackStack("tag");
                transaction.commit();
            }
            });

        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getActivity(), LoginPageActivity.class));

        return v;

    }

}