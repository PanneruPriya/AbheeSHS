package com.abhee.abheetechnician2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Admin on 25-08-2018.
 */

public class ProfilePage extends Fragment {
    EditText first, last, mobile, email,uid;
    Button editP, saveP;
    TextView chngpswd;
    SharedPreferences sharedPreferences;
    String mob,pass,id;
    FragmentTransaction transaction;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static AppCompatTextView farname;
    ProgressDialog progressDialog;
    public static ProfilePage newInstance() {
        ProfilePage fragment = new ProfilePage();

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
        v = inflater.inflate(R.layout.profilepage, container, false);
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("In Progress");
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        first = (EditText) v.findViewById(R.id.text1);
        last = (EditText) v.findViewById(R.id.text2);
        mobile = (EditText) v.findViewById(R.id.text3);
        email = (EditText) v.findViewById(R.id.text4);
        uid=(EditText)v.findViewById(R.id.textid);
        uid.setEnabled(false);

        sharedPreferences = getActivity().getSharedPreferences("Abhee", Context.MODE_PRIVATE);
        mob =sharedPreferences.getString("mobilenumber","");
        pass=sharedPreferences.getString("password","");
        id=sharedPreferences.getString("id","");
        getDetailsFromServer();
        editP = (Button) v.findViewById(R.id.edit_per);
        editP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editP.setVisibility(View.GONE);
                saveP.setVisibility(View.VISIBLE);
                first.setEnabled(true);
                last.setEnabled(true);
                mobile.setEnabled(false);
                email.setEnabled(false);
            }
        });
        saveP = (Button) v.findViewById(R.id.save_per);
        saveP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sendDetailstoServer();
                editP.setVisibility(View.VISIBLE);
                saveP.setVisibility(View.GONE);
                first.setEnabled(false);
                last.setEnabled(false);
                mobile.setEnabled(false);
                email.setEnabled(false);
                final String name=first.getText().toString();
                if (!isValidName(name)){
                    first.setError("Invalid name");
                }
                final String surname=last.getText().toString();
                if (!isValidSurname(surname)){
                    last.setError("Invalid surname");
                }
                final String num=mobile.getText().toString();
                if (!isValidNumber(num)){
                    mobile.setError("Invalid Number");
                }
                final String email1=email.getText().toString();
                if (!isValidEmail(email1)){
                    email.setError("Invalid Email");
                }
                if ((!first.getText().equals(null)&&first.getText().length()>0)
                        &&(!last.getText().equals(null)&&last.getText().length()>0)){
                    sendDetailstoServer();
                }else {
                    Toast.makeText(getActivity(),"please check your profile",Toast.LENGTH_SHORT).show();
                }
                getDetailsFromServer();
            }
        });

        chngpswd = (TextView) v.findViewById(R.id.chngpswd);
        chngpswd.setPaintFlags(chngpswd.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        chngpswd.setText("Change Password!");
        chngpswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, new ChangePassword().newInstance());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getActivity(), LoginPageActivity.class));

        return v;
    }
    private boolean isValidAddress(String addres) {
        if (!addres.equals(null)&& addres.length()>0){
            return true;
        }else
            return false;
    }
    private boolean isValidEmail(String email) {
        if (!email.equals(null) && email.matches(emailPattern)){
            return true;
        }else
            return false;
    }
    private boolean isValidName(String name1) {
        if (name1!=null&& name1.length()>0){
            return true;
        }else
            return false;
    }
    private boolean isValidSurname(String surname1) {
        if (surname1!=null&&surname1.length()>0){
            return true;
        }else
            return false;
    }
    private boolean isValidNumber(String num) {
        if (num!=null&&num.length()==10){
            return true;
        }else
            return false;
    }
    private void getDetailsFromServer() {
        try {
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", mob);
            jsonBody.put("password", pass);
            final String mRequestBody = jsonBody.toString();

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                    Constants.URL+"userloggging ", new JSONObject(mRequestBody), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("Log1", " response" + response.toString());
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("status");
                        progressDialog.dismiss();
                            for (int i=0;i<jsonArray.length();i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                first.setText(obj.getString("firstname"));
                                first.setEnabled(false);

                                last.setText(obj.getString("lastname"));
                                last.setEnabled(false);

                                mobile.setText(obj.getString("mobilenumber"));
                                mobile.setEnabled(false);

                                email.setText(obj.getString("email"));
                                email.setEnabled(false);

                                uid.setText(obj.getString("user_id"));
                                uid.setEnabled(false);
                            }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    chik();
                   // Toast.makeText(getActivity().getApplicationContext(), "Could not get Data from Online Server", Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(stringRequest);
        } catch (JSONException e) {
        }
    }
    private void sendDetailstoServer() {
        try {
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("id", id);
            jsonBody.put("firstname", first.getText().toString());
            jsonBody.put("lastname", last.getText().toString());
            jsonBody.put("mobilenumber", mobile.getText().toString());
            jsonBody.put("email", email.getText().toString());

            final String mRequestBody = jsonBody.toString();
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                    Constants.URL+"adminEditProfile", new JSONObject(mRequestBody), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("Log1", " response" + response.toString());
                    try {
                        JSONObject obj = new JSONObject(response.toString());
                        if (obj.getString("profileinfo").contains("Not Updated")) {
                            Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                        } else if (obj.getString("profileinfo").contains("Updated")){
                            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                            getDetailsFromServer();
                            Intent i=new Intent(getContext(),DashboardActivity.class);
                            startActivity(i);
                        }
                        else {
                            Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity().getApplicationContext(), "Could not get Data from Online Server", Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(stringRequest);
        } catch (JSONException e) {
        }
    }
    public void chik() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.alertlayout1, null);
        final Button btnSave = (Button) alertLayout.findViewById(R.id.rbutton);
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Reload");
        // alert.setIcon(R.drawable.ic_launcher);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();
        dialog.show();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDetailsFromServer();
                dialog.hide();
            }
        });
    }
}