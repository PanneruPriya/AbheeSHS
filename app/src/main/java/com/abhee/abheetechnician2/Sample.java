package com.abhee.abheetechnician2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Sample extends AppCompatActivity {
    EditText user,pswd;
    Button login;
    TextView forgtPswd;
    AlertDialog dialog;
    ImageView image;
    AppCompatEditText mobileDiaP;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefsEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample);
        user=(EditText)findViewById(R.id.et_user);
        pswd=(EditText)findViewById(R.id.et_pswd);
        login=(Button)findViewById(R.id.bt_login);
      /*  forgtPswd=(TextView)findViewById(R.id.forgotPswd);*/
        sharedPreferences = getApplicationContext().getSharedPreferences("Abhee", Context.MODE_PRIVATE);
        String mob=sharedPreferences.getString("username",null);
        String pass=sharedPreferences.getString("password",null);
        if(mob !=null && pass !=null){
            // startActivity(new Intent(LoginActivity.this,PinEnterActivity.class));
        }else{
            //  Toast.makeText(getApplicationContext(),"User not Register ",Toast.LENGTH_SHORT).show();
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = user.getText().toString();
                if (!isUName(uname)) {
                    user.setError("Name should not be empty.");
                    return;
                }
                String pwd = pswd.getText().toString();
                if (!isPWD(pwd)) {
                    pswd.setError("Password should not be empty.");
                    return;
                }
                if (uname != null && pwd != null) {

                }
                sendDetailsToServer();
            }
        });
/*        forgtPswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    try {
                        LayoutInflater inflater1 = getLayoutInflater();
                        View alertLayout = inflater1.inflate(R.layout.custom_forgot, null);
                        image = (ImageView) alertLayout.findViewById(R.id.close_dialog1);
                        image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        mobileDiaP = (AppCompatEditText) alertLayout.findViewById(R.id.editT);
                        AppCompatButton btnSubmit = (AppCompatButton) alertLayout.findViewById(R.id.btn_submit);
                        AlertDialog.Builder alert = new AlertDialog.Builder(Sample.this);
                        alert.setView(alertLayout);
                        alert.setCancelable(false);
                        dialog = alert.create();
                        dialog.getWindow().setLayout(200, 300);
                        dialog.show();
                        btnSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String phone = mobileDiaP.getText().toString();
                                if (!isUName(phone)) {
                                    mobileDiaP.setError("Invalid Number");
                                    return;
                                }
                                sendDetailsToServerPin();
                            }
                        });
                    }catch (Exception e){
                        
                    }

            }
        });*/
    }
    private void sendDetailsToServerPin() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("mobilenumber", mobileDiaP.getText().toString().trim());
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.URL+"restForgotPassword",
                new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                Log.i("ResponsePin:-",response.toString());
                try {
                    JSONObject json = new JSONObject(response.toString());
                    Toast.makeText(Sample.this, "check the password", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Sample.this, "We are Unable to Provide Data", Toast.LENGTH_SHORT).show();
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        queue.add(stringRequest);
    }
    private boolean isUName(String uname) {
        if (uname != null && uname.length() > 0) {
            return true;
        }
        return false;
    }

    private boolean isPWD(String pwd) {
        if (pwd != null && pwd.length() > 0) {
            return true;
        }
        return false;
    }

    private void sendDetailsToServer() {
        try {
            RequestQueue queue = Volley.newRequestQueue(this);

            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", user.getText().toString());
            jsonBody.put("password", pswd.getText().toString());
            final String mRequestBody = jsonBody.toString();
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                    Constants.URL+"userloggging",new JSONObject(mRequestBody), new Response.Listener<JSONObject>() {

                public void onResponse(JSONObject response) {
                    Log.e("Log1"," response"+response);

                    try{
                        JSONObject json=new JSONObject(response.toString());
                        try {
                            JSONObject obj = new JSONObject(response.toString());
                            JSONArray jsonarray = obj.getJSONArray("status");
                            if (jsonarray.length() > 0) {
                                JSONObject obj1 = jsonarray.getJSONObject(0);
                                prefsEditor = sharedPreferences.edit();
                                prefsEditor.putString("mobilenumber", obj1.getString("mobilenumber"));
                                prefsEditor.putString("firstname", obj1.getString("firstname"));
                                prefsEditor.putString("lastname", obj1.getString("lastname"));
                                prefsEditor.putString("id", obj1.getString("id"));
                                prefsEditor.putString("password", obj1.getString("password"));
                                prefsEditor.putString("user_id", obj1.getString("user_id"));
                                prefsEditor.commit();
                                Intent intent = new Intent(Sample.this, DashboardActivity.class);
                                startActivity(intent);
                            }
                        }catch(JSONException e){

                        }
                    }catch(JSONException e){

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error:-" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
            };
            queue.add(stringRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
