package com.abhee.abheetechnician2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class ServiceRequestsDataEdit1 extends Fragment {
    static int pos;
    static String desc,taskn,ids,customerid,mname,warrantynum;
    static String subjects,communicationaddresss,categorys,prioritys,taskdeadlines,servicetypeS;
    AppCompatTextView taskno,subject,communicationaddress,category,priorityy,taskdeadline;
    EditText descrip, commen, tot,discnt, tax, amnt;
    Spinner spinner,spinner2;
    String selectedItemText;
    int hold;
    ImageView iv_clinic;
    byte img_store[] = null;
    Button btnBrowse,submit,reset;
    Bitmap bitmap;
    String encodedImage=" ";
    ProgressDialog progressDialog;
    int width,heights;
    Dialog dialog;
    RadioGroup rg1;
    RadioButton rb1,rb2;
    String currentdate1;
    int dialognum=0;
    LinearLayout imagesss,warrintylayout;
    ImageView mic;

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private static int PICK_IMAGE_REQUEST = 3;

    public static ServiceRequestsDataEdit1 newInstance(int position, String severity,
                                                       String servicetype, String updatedTime,
                                                       String subject, String description,
                                                       String priority, String additionalinfo,
                                                       String communicationaddress,
                                                       String assignby, String kstatus,
                                                       String uploadfile, String customer_id,
                                                       String taskno, String created_time,
                                                       String warranty, String id,
                                                       String category, String assignto,
                                                       String modelname, String taskdeadline,String imgfile) {
        ServiceRequestsDataEdit1 fragment = new ServiceRequestsDataEdit1();
        pos = position;
        desc = description;
        taskn= taskno;
        ids=id;
        customerid= customer_id;
        mname = modelname;
        warrantynum=warranty;
        subjects=subject;
        communicationaddresss=communicationaddress;
        categorys=category;
        prioritys=priority;
        taskdeadlines=taskdeadline;
        servicetypeS =servicetype;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v;
        v =inflater.inflate(R.layout.service_requests_data_edit1, container, false);
        component(v);
        Log.i("model_name",mname);
        Log.i("war_num",warrantynum);
        if(servicetypeS.equals("Repair")){
            warrintylayout.setVisibility(View.GONE);
        }

        if(warrantynum.equals("0")){
            warrintylayout.setVisibility(View.GONE);
        }
        /*Date date =new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-mm-yyyy");
        String currentdate=format.format(date);*/
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("In Progress");

        return v;
    }
    public void dialog(int i){
        if (i==0) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            //String currentdate= df.format(c.getTime());
            Date SelectedDate = c.getTime();
            DateFormat dateformat_UK = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
            String currentdate = dateformat_UK.format(SelectedDate);
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            width = display.getWidth();
            heights = display.getHeight();
            LayoutInflater inflater1 = getActivity().getLayoutInflater();
            View alertLayout = inflater1.inflate(R.layout.warrantydialog, null);
            final EditText purchaseDt = (EditText) alertLayout.findViewById(R.id.purchaseDt);
            final EditText expireDt = (EditText) alertLayout.findViewById(R.id.expireDt);
            spinner = (Spinner) alertLayout.findViewById(R.id.expireyeaer);
            String[] plants = new String[]{
                    ".... Years ....",
                    "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
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

                    if (position != 0) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.YEAR, position);
                        SimpleDateFormat dd = new SimpleDateFormat("dd-MM-yyyy");
                        //String currentdate= df.format(c.getTime());
                        Date SelectedDate1 = calendar.getTime();
                        DateFormat dateformat_UK1 = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
                        currentdate1 = dateformat_UK1.format(SelectedDate1);
                        //c.add(Calendar.YEAR,1);
                        expireDt.setText(currentdate1);
                        expireDt.setEnabled(false);
                    } else {
                        Toast.makeText(getActivity(), "plaese select the Year", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            purchaseDt.setText(currentdate);
            purchaseDt.setEnabled(false);

            Button resetPW1 = (Button) alertLayout.findViewById(R.id.resetPW1);
            Button submitPW1 = (Button) alertLayout.findViewById(R.id.submitPW1);
            Button closePW1 = (Button) alertLayout.findViewById(R.id.closePW1);
            closePW1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    rb2.setChecked(true);
                }
            });
            resetPW1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //purchaseDt.setText("");
                    expireDt.setText("");
                    spinner.setSelection(0);
                }
            });
            submitPW1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.setTitle("Warranty..");
                    progressDialog.setMessage("please wait...");
                    progressDialog.show();
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    Map<String, String> params = new HashMap<>();
                    params.put("productmodelid", mname);
                    params.put("customerid", customerid);
                    params.put("purchaseddate", purchaseDt.getText().toString());
                    params.put("expireddate", expireDt.getText().toString());
                    Log.i("Datafields:-", "" + params);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.URL + "saveProductWarranty", new JSONObject(params),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i("status", response.toString());
                                    try {
                                        JSONObject jsonObject = new JSONObject(response.toString());
                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity(), jsonObject.getString("status"), Toast.LENGTH_SHORT).show();
                                        if (jsonObject.getString("status").equals("success")) {
                                            warrantynum = "0";
                                            dialognum=1;
                                            dialog.dismiss();
                                            rb1.setChecked(true);
                                        } else {
                                            Toast.makeText(getActivity(), jsonObject.getString("status").toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }


                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    requestQueue.add(jsonObjectRequest);

                }
            });

            //---------------------------------------------------------
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setView(alertLayout);
            alert.setCancelable(false);
            dialog = alert.create();
            dialog.getWindow().setLayout(width / 2, heights / 2);
        }else{

        }

    }

    private void component(View v) {
        descrip = (EditText) v.findViewById(R.id.desc_et);
        descrip.setText(desc);
        descrip.setEnabled(false);
        taskno=(AppCompatTextView)v.findViewById(R.id.sno);
        taskno.setText(taskn);
        subject=(AppCompatTextView)v.findViewById(R.id.subject);
        subject.setText(subjects);
        communicationaddress=(AppCompatTextView)v.findViewById(R.id.caddress);
        communicationaddress.setText(communicationaddresss);
        category=(AppCompatTextView)v.findViewById(R.id.category);
        category.setText(categorys);
        priorityy=(AppCompatTextView)v.findViewById(R.id.priority);
        priorityy.setText(prioritys);
        taskdeadline=(AppCompatTextView)v.findViewById(R.id.taskdeadline);
        taskdeadline.setText(taskdeadlines);
        imagesss=(LinearLayout)v.findViewById(R.id.imagesss);
        warrintylayout=(LinearLayout)v.findViewById(R.id.warrintylayout);
        iv_clinic = (ImageView) v.findViewById(R.id.iv_clinic);
        commen=(EditText)v.findViewById(R.id.comm_et);
        rg1=(RadioGroup)v.findViewById(R.id.rg1);
        rb1=(RadioButton)v.findViewById(R.id.radiob1);
        rb2=(RadioButton)v.findViewById(R.id.radiob2);
        mic=(ImageView)v.findViewById(R.id.mic);
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
        spinner2=(Spinner)v.findViewById(R.id.spinner_status);
        String[] plants = new String[]{
                ".... Select Status ....","Resolved","Payment Pending","Customer not Available","Payment Received"

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
        spinner2.setAdapter(spinnerArrayAdapter);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItemText = (String) parent.getItemAtPosition(position);
                if (position > 0) {
                    if (spinner2.getSelectedItemPosition()==1){
                        hold = 3;
                        imagesss.setVisibility(View.GONE);
                    }else if (spinner2.getSelectedItemPosition()==2){
                        hold = 7;
                        imagesss.setVisibility(View.GONE);
                    }else if (spinner2.getSelectedItemPosition()==3){
                        hold = 8;
                        imagesss.setVisibility(View.VISIBLE);
                    }else if(spinner2.getSelectedItemPosition()==3){
                        hold = 10;
                        imagesss.setVisibility(View.GONE);
                    }
                    //hold = spinner.getSelectedItemPosition();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rb1.getId()==checkedId){
                    if (dialognum==0) {
                        dialog(dialognum);
                        dialog.show();
                    }else{

                    }

                }
                if(rb2.getId()==checkedId){
                    warrantynum="1";
                }
            }
        });
        reset=(Button)v.findViewById(R.id.resetSR);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commen.setText("");
            }
        });
        submit=(Button)v.findViewById(R.id.submitSR);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("please wait...");
                progressDialog.show();
                final String disc=descrip.getText().toString();
                if (!isValidDescrip(disc)){
                    descrip.setError("Add Description");
                }
                final String comm1=commen.getText().toString();
                if (!isValidComm(comm1)){
                    commen.setError("Add Comment");
                }
                if (!descrip.getText().toString().equals(null)&&!commen.getText().toString().equals(null)&&spinner2.getSelectedItemPosition()!=0){
                    //encodedImage = Base64.encodeToString(img_store, Base64.DEFAULT);
                    Log.e("img", "img" + encodedImage);
                    sendDetailsToServer();
                }
            }
        });
        btnBrowse = (Button) v.findViewById(R.id.btnBrowse);
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
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    commen.setText(result.get(0));
                }
                break;
            }

        }
    }
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }
    private void sendDetailsToServer() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("taskno",taskn);
        postParam.put("id",ids);
        postParam.put("kstatus",String.valueOf(hold));
        postParam.put("warranty",warrantynum);
        postParam.put("imgfile",encodedImage);
        postParam.put("addComment", commen.getText().toString());
        Log.i("PostData:---",""+postParam.toString());

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.URL+"saveServiceRequest",new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("data",response.toString());
                try {
                    JSONObject json = new JSONObject(response.toString());
                    progressDialog.dismiss();
                    // Toast.makeText(getContext(), json.getString("msg"), Toast.LENGTH_SHORT).show();
                    if (json.getString("status").equals("updated")) {
                        //clearBox();
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
    private void clearBox() {
        descrip.setText("");
        commen.setText("");
        //discnt.setText("");
        //iv_clinic.setImageDrawable(null);
    }
}
