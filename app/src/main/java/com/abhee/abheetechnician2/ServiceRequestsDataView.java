package com.abhee.abheetechnician2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

public class ServiceRequestsDataView extends Fragment {
    static int pos;
    static String sever,servtype,updtime,sub,desc,
            prior,addinfo,comadd,assgnby,kstatu,
            uplodfile,custId,task,crtdtime,warr,
            iD,categ,assto,moden,deadline,imgfiles,firstnames, lastnames, mobilenumbers;
    AppCompatTextView text1,text2,text3,text4,text5,
            text6,text7,text8,text9,text10,text11,text12,text13,text14,text15;
    AppCompatImageView customerupload,adminupload;
    Button editView;
    int width,heigth;
    public ServiceRequestsDataView newInstance(int position, String severity, String serviceType,
                                               String updatedTime, String subject, String description,
                                               String priority, String additionalinfo,
                                               String communicationaddress, String assignby,
                                               String kstatus, String uploadfile,
                                               String customerId, String taskno,
                                               String createdTime, String warranty,
                                               String id, String category,
                                               String assignto, String modelname, String taskdeadline,String imgfile,
                                               String firstname,String lastname,String mobilenumber) {
        ServiceRequestsDataView fragment = new ServiceRequestsDataView();
        pos=position;
        sever=severity;
        servtype=serviceType;
        updtime=updatedTime;
        sub=subject;
        desc=description;
        prior=priority;
        addinfo=additionalinfo;
        comadd=communicationaddress;
        assgnby=assignby;
        kstatu=kstatus;
        moden=modelname;
        uplodfile=uploadfile;
        custId=customerId;
        task=taskno;
        crtdtime=createdTime;
        warr=warranty;
        iD=id;
        categ=category;
        assto=assignto;
        deadline=taskdeadline;
        imgfiles=imgfile;
        firstnames =firstname;
        lastnames =lastname;
        mobilenumbers =mobilenumber;
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
        View v;
        v = inflater.inflate(R.layout.service_requests_data_view, container, false);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        width =display.getWidth();
        heigth=display.getHeight();
        text1=(AppCompatTextView)v.findViewById(R.id.textView1);
        text2=(AppCompatTextView)v.findViewById(R.id.textView2);
        text3=(AppCompatTextView)v.findViewById(R.id.textView3);
        text4=(AppCompatTextView)v.findViewById(R.id.textView4);
        text5=(AppCompatTextView)v.findViewById(R.id.textView5);
        text6=(AppCompatTextView)v.findViewById(R.id.textView6);
        text7=(AppCompatTextView)v.findViewById(R.id.textView7);
        text8=(AppCompatTextView)v.findViewById(R.id.textView8);
        text9=(AppCompatTextView)v.findViewById(R.id.textView9);
        text10=(AppCompatTextView)v.findViewById(R.id.textView10);
        text11=(AppCompatTextView)v.findViewById(R.id.textView11);
        text12=(AppCompatTextView)v.findViewById(R.id.textView12);
        text13=(AppCompatTextView)v.findViewById(R.id.textView13);
        text14=(AppCompatTextView)v.findViewById(R.id.cutname);
        text15=(AppCompatTextView)v.findViewById(R.id.mobilenumber);
        customerupload=(AppCompatImageView)v.findViewById(R.id.uploadfile);
        adminupload=(AppCompatImageView)v.findViewById(R.id.imgfiles);

        editView=(Button)v.findViewById(R.id.edit_view);

        text1.setText(task);
        text2.setText(categ);
        text3.setText(moden);
        text4.setText(custId);
        text5.setText(servtype);
        text6.setText(sever);
        text7.setText(prior);
        text8.setText(assto);
        text9.setText(sub);
        text10.setText(deadline);
        text11.setText(kstatu);
        text12.setText(crtdtime);
        text13.setText(comadd);
        text14.setText(firstnames+" "+lastnames);
        text15.setText(mobilenumbers);
        Picasso.with(getActivity()).load(Constants.IMG_URL+uplodfile).into(customerupload);
        Picasso.with(getActivity()).load(Constants.IMG_URL+imgfiles).into(adminupload);
        customerupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.imagedialog, null);
                /*final EditText userInput = (EditText) alertLayout.findViewById(R.id.et_input);
                // userInput.setText(code);
                final Button btnSave = (Button) alertLayout.findViewById(R.id.btnVerify);*/
                final ImageView btnCancel = (ImageView) alertLayout.findViewById(R.id.btnCancel);
                final ImageView viewimage = (ImageView) alertLayout.findViewById(R.id.imageView2);
                Picasso.with(getActivity()).load(Constants.IMG_URL+uplodfile).into(viewimage);
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                /*alert.setTitle("Image");
                alert.setIcon(R.drawable.icon);*/
                alert.setView(alertLayout);
                alert.setCancelable(false);
                final AlertDialog dialog = alert.create();
                dialog.show();
                dialog.getWindow().setLayout(width/2+200,width/2);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  Toast.makeText(mContext, "1", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
        adminupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.imagedialog, null);
                /*final EditText userInput = (EditText) alertLayout.findViewById(R.id.et_input);
                // userInput.setText(code);
                final Button btnSave = (Button) alertLayout.findViewById(R.id.btnVerify);*/
                final ImageView btnCancel = (ImageView) alertLayout.findViewById(R.id.btnCancel);
                final ImageView viewimage = (ImageView) alertLayout.findViewById(R.id.imageView2);
                Picasso.with(getActivity()).load(Constants.IMG_URL+imgfiles).into(viewimage);
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                /*alert.setTitle("Image");
                alert.setIcon(R.drawable.icon);*/
                alert.setView(alertLayout);
                alert.setCancelable(false);
                final AlertDialog dialog = alert.create();
                dialog.show();
                dialog.getWindow().setLayout(width/2+200,width/2);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  Toast.makeText(mContext, "1", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
        editView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "edit", Toast.LENGTH_SHORT).show();
            }
        });
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getActivity(), LoginPageActivity.class));
        return v;
    }
}
