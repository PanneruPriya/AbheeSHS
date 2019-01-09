package com.abhee.abheetechnician2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceRequestsDataEdit extends Fragment {
    static int pos;
    static String desc,taskn,ids;
    EditText descrip, commen, tot,discnt, tax, amnt;
    Spinner spinner;
    String selectedItemText;
    int hold;
    ImageView iv_clinic;
    byte img_store[] = null;
    Button btnBrowse,submit,reset;
    Bitmap bitmap;
    String encodedImage=" ";
    ProgressDialog progressDialog;
    private static int PICK_IMAGE_REQUEST = 3;

    public Fragment newInstance(int position, String severity,
                                String servicetype, String updatedTime,
                                String subject, String description,
                                String priority, String additionalinfo,
                                String communicationaddress,
                                String assignby, String kstatus,
                                String uploadfile, String customer_id,
                                String taskno, String created_time,
                                String warranty, String id,
                                String category, String assignto,
                                String modelname, String taskdeadline) {
        ServiceRequestsDataEdit fragment = new ServiceRequestsDataEdit();
        pos = position;
        desc = description;
        taskn= taskno;
        ids=id;
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
        v = inflater.inflate(R.layout.service_requests_data_edit, container, false);
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("In Progress");
        descrip = (EditText) v.findViewById(R.id.desc_et);
        descrip.setText(desc);
        descrip.setEnabled(false);

        spinner = (Spinner) v.findViewById(R.id.spinner_status);
        String[] plants = new String[]{
                ".... Select Status ....",
                "Resolved",
                "Payment Pending"
        };
        final List<String> plantsList = new ArrayList<>(Arrays.asList(plants));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity().getApplicationContext(), R.layout.spinner_item, plantsList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItemText = (String) parent.getItemAtPosition(position);
                if (position > 0) {
                    if (spinner.getSelectedItemPosition()==1){
                        hold = 3;
                    }else if (spinner.getSelectedItemPosition()==2){
                        hold = 7;
                    }
                    //hold = spinner.getSelectedItemPosition();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //iv_clinic = (ImageView) v.findViewById(R.id.iv_clinic);
        reset=(Button)v.findViewById(R.id.resetSR);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descrip.setText("");
                commen.setText("");
                tot.setText("");
                discnt.setText("");
                tax.setText("");
                amnt.setText("");
                //spinner.setAdapter(null);
                //iv_clinic.setImageDrawable(null);
            }
        });
        submit=(Button)v.findViewById(R.id.submitSR);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("please wait...");
                progressDialog.show();

                // encodedImage = Base64.encodeToString(img_store, Base64.DEFAULT);
                //Log.e("img", "img" + encodedImage);
                //sendDetailsToServer();

                final String disc=descrip.getText().toString();
                if (!isValidDescrip(disc)){
                    descrip.setError("Add Description");
                }
                final String comm1=commen.getText().toString();
                if (!isValidComm(comm1)){
                    commen.setError("Add Comment");
                }
                /*final String tota=tot.getText().toString();
                if (!isValidTotal(tota)){
                    tot.setError("Add Total");
                }
                final String disco=discnt.getText().toString();
                if (!isValidDiscount(disco)){
                    discnt.setError("Add Discount");
                }
                final String taxe=tax.getText().toString();
                if (!isValidTax(taxe)){
                    tax.setError("Add Tax");
                }
                final String amoun=amnt.getText().toString();
                if (!isValidAmount(amoun)){
                    amnt.setError("Add Amount");
                }*/
                /*if (isValidTotal(tota)&& isValidAmount(amoun)&& isValidDescrip(disc)&&
                        isValidComm(comm1)&& isValidDiscount(disco)&& isValidTax(taxe)&&img_store!=null){
                    encodedImage = Base64.encodeToString(img_store, Base64.DEFAULT);
                    Log.e("img", "img" + encodedImage);
                    sendDetailsToServer();
                }*/
                if (isValidDescrip(disc)&&isValidComm(comm1)){
                    //encodedImage = Base64.encodeToString(img_store, Base64.DEFAULT);
                    Log.e("img", "img" + encodedImage);
                    sendDetailsToServer();
                }
            }
        });

        /*btnBrowse = (Button) v.findViewById(R.id.btnBrowse);
        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_store = null;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
*/
        commen = (EditText) v.findViewById(R.id.comm_et);
        //tot = (EditText) v.findViewById(R.id.total_et);
        //discnt = (EditText) v.findViewById(R.id.disc_et);
        //tax = (EditText) v.findViewById(R.id.tax_et);
        //amnt = (EditText) v.findViewById(R.id.amnt_et);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getActivity(), LoginPageActivity.class));
        return v;
    }

    private boolean isValidTax(String taxe) {
        if (taxe!=null){
            return true;
        }
        return false;
    }
    private boolean isValidDiscount(String disco) {
        if (disco!=null){
            return true;
        }
        return false;
    }
    private boolean isValidComm(String comm1) {
        if (comm1!=null){
            return true;
        }
        return false;
    }
    private boolean isValidDescrip(String disc) {
        if (disc!=null){
            return true;
        }
        return false;
    }
    private boolean isValidTotal(String name1) {
        if (name1!=null&& name1.length()>0){
            return true;
        }
        return false;
    }
    private boolean isValidAmount(String surname1) {
        if (surname1!=null&&surname1.length()>0){
            return true;
        }
        return false;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST) {
            try {
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 65, stream);
                    img_store = stream.toByteArray();
                    encodedImage = Base64.encodeToString(img_store, Base64.DEFAULT);
                    iv_clinic.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void sendDetailsToServer() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("taskno",taskn);
        postParam.put("id",ids);

        //postParam.put("description", descrip.getText().toString());
        //postParam.put("uploadfile",encodedImage);
        postParam.put("kstatus",String.valueOf(hold));
        postParam.put("addComment", commen.getText().toString());
        //postParam.put("total",tot.getText().toString());
        //postParam.put("discount",discnt.getText().toString());
        //postParam.put("tax",tax.getText().toString());
        //postParam.put("amount received",amnt.getText().toString());

        Log.i("PostData:---",""+postParam.toString());

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.URL+"saveServiceRequest",new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("llllllllll",response.toString());
                try {
                    JSONObject json = new JSONObject(response.toString());
                    progressDialog.dismiss();
                    // Toast.makeText(getContext(), json.getString("msg"), Toast.LENGTH_SHORT).show();
                    if (json.getString("status").equals("updated")) {
                        clearBox();
                        Intent intent=new Intent(getContext(),DashboardActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getActivity(),""+json.getString("status").toString(),Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){}

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error:-" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        queue.add(stringRequest);
    }

    private void clearBox() {
        descrip.setText("");
//        discnt.setText("");
        //iv_clinic.setImageDrawable(null);
    }
}
