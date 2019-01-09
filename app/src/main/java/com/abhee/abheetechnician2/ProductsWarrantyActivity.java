package com.abhee.abheetechnician2;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ProductsWarrantyActivity extends Fragment {
    ImageView image1,image2;
    LinearLayout linear1,linear2;
    LinearLayout layout1,layout2;
    Spinner categ,categ1;
    EditText purchDate,expirDate;
    FragmentTransaction transaction;
    ListView list;
    Model1 item1;
    Model2 item2;
    String compid,customerid;
    Button resetS,submitS;
    ArrayList<HashMap<String, String>> commName,commName1,mCategoryList1;
    private ArrayList<Model1> productList;
    private ArrayList<List<String>> productList2;
    HashMap<String, String> hm;
    ProgressDialog progressDialog;
    public static String custid,userid,mobile;
    SharedPreferences sharedPreferences;
    DatePickerDialog datePickerDialog;

    public static ProductsWarrantyActivity newInstance() {
        ProductsWarrantyActivity fragment = new ProductsWarrantyActivity();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.activity_productswarranty, container, false);
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("In Progress");
        progressDialog.setMessage("please wait...");
        progressDialog.show();
        linear1=(LinearLayout)v.findViewById(R.id.linear1);
        linear2=(LinearLayout)v.findViewById(R.id.linear2);
        layout1=(LinearLayout)v.findViewById(R.id.layout1);
        layout2=(LinearLayout)v.findViewById(R.id.layout2);
        image1=(ImageView)v.findViewById(R.id.img1);
        image2=(ImageView)v.findViewById(R.id.img2);
        categ=(Spinner)v.findViewById(R.id.spiS);
        categ1=(Spinner)v.findViewById(R.id.categS);
        purchDate=(EditText)v.findViewById(R.id.purchase1);
        expirDate=(EditText)v.findViewById(R.id.expire1);
        resetS=(Button)v.findViewById(R.id.resetS);
        submitS=(Button)v.findViewById(R.id.submitS);
        submitS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("please wait...");
                progressDialog.show();

                //compid=commName.get(categ.getSelectedItemPosition()).get("id");
                //customerid=commName1.get(categ1.getSelectedItemPosition()).get("customerid");
                //Toast.makeText(getActivity(),"updated soon",Toast.LENGTH_SHORT).show();
                sendDetailstoserver();
            }
        });
        /*categ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(categ.getSelectedItem().equals(commName.get(position+1).get("id"))) {
                    compid= commName.get(position+1).get("id");
                    Toast.makeText(getActivity(), compid, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        categ1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(categ1.getSelectedItem().equals(commName1.get(position).get("customerId"))) {
                    customerid = commName1.get(position).get("customerId").toString();
                    Toast.makeText(getActivity(), customerid, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/
        resetS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categ.setSelection(0);
                categ1.setSelection(0);
                purchDate.setText("");
                expirDate.setText("");
            }
        });

        productList = new ArrayList<Model1>();
        productList2 = new ArrayList<>();
        sharedPreferences = getActivity().getSharedPreferences("Abhee", Context.MODE_PRIVATE);
        userid=sharedPreferences.getString("userid", "");
        custid=sharedPreferences.getString("custid", "");
        mobile=sharedPreferences.getString("mobilenumber","");
        linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getDetailsToServer();
                linear1.setBackgroundColor(getResources().getColor(R.color.white));
                linear2.setBackgroundColor(getResources().getColor(R.color.gray));
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.GONE);
            }
        });
        linear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear1.setBackgroundColor(getResources().getColor(R.color.gray));
                linear2.setBackgroundColor(getResources().getColor(R.color.white));
                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.VISIBLE);
                getDetailsFromServer();
                getDetailsFromServer1();
            }
        });

        list = (ListView)v.findViewById(R.id.listTS);
        getDetailsToServer();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hm=mCategoryList1.get(position);
                custid=hm.get("customerId");
                transaction = getActivity().getSupportFragmentManager().beginTransaction();
               transaction.replace(R.id.frame, new ProductsWarrantyEU().newInstance(position,
                        hm.get("expireddate"),hm.get("purchaseddate"),hm.get("productmodelname"),
                        hm.get("customerid"),hm.get("orderId"),hm.get("productmodelid")));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        purchDate.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inType = purchDate.getInputType();
                purchDate.setInputType(InputType.TYPE_NULL);
                purchDate.onTouchEvent(event);
                purchDate.setInputType(inType);
                purchDate.setFocusable(false);
                purchDate.requestFocus();
                return true;
            }
        });
        purchDate.setOnLongClickListener(new View.OnLongClickListener()
        {
            public boolean onLongClick(View v) {
                int inType = purchDate.getInputType();
                purchDate.setInputType(InputType.TYPE_NULL);
                purchDate.setInputType(inType);
                purchDate.setFocusable(false);
                purchDate.requestFocus();
                return true;
            }
        });
        purchDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                final Calendar calander2 = Calendar.getInstance();
                calander2.setTimeInMillis(0);
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                purchDate.setInputType(InputType.TYPE_NULL);
                purchDate.setFocusable(false);
                datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String month="";
                                monthOfYear=monthOfYear+1;
                                if(monthOfYear<10)
                                    month="0"+monthOfYear;
                                else
                                    month=""+monthOfYear;
                                calander2.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                                Date SelectedDate = calander2.getTime();
                                DateFormat dateformat_UK = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
                                String StringDateformat_UK = dateformat_UK.format(SelectedDate);
                                purchDate.setText(  StringDateformat_UK );

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("Please select date");
                datePickerDialog.show();
            }
        });

        expirDate.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inType = expirDate.getInputType(); // backup the input type
                expirDate.setInputType(InputType.TYPE_NULL); // disable soft input
                expirDate.onTouchEvent(event); // call native handler
                expirDate.setInputType(inType);
                expirDate.setFocusable(false);// restore input type
                expirDate.requestFocus();
                return true;
            }
        });
        expirDate.setOnLongClickListener(new View.OnLongClickListener()
        {
            public boolean onLongClick(View v) {
                int inType = expirDate.getInputType();
                expirDate.setInputType(InputType.TYPE_NULL);
                expirDate.setInputType(inType);
                expirDate.setFocusable(false);
                expirDate.requestFocus();
                return true;
            }
        });

        expirDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                final Calendar calander2 = Calendar.getInstance();
                calander2.setTimeInMillis(0);
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                expirDate.setInputType(InputType.TYPE_NULL);
                expirDate.setFocusable(false);
                datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String month="";
                                monthOfYear=monthOfYear+1;
                                if(monthOfYear<10)
                                    month="0"+monthOfYear;
                                else
                                    month=""+monthOfYear;
                                calander2.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                                Date SelectedDate = calander2.getTime();
                                DateFormat dateformat_UK = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
                                String StringDateformat_UK = dateformat_UK.format(SelectedDate);
                                expirDate.setText(  StringDateformat_UK );

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("Please select date");
                //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getActivity(), LoginPageActivity.class));
        return v;
    }

    private void getDetailsFromServer() {
        try {
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            final JSONObject jsonBody = new JSONObject();
            final String mRequestBody = jsonBody.toString();

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                    Constants.URL +"getproductdetails", new JSONObject(mRequestBody), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("Spinner:"," response:---"+response);
                    JSONObject json= null;
                    try {
                        json = new JSONObject(response.toString());
                        progressDialog.dismiss();
                        JSONArray array=json.getJSONArray("productslist");
                        List<String> marketName = new ArrayList<String>();
                        commName=new ArrayList<HashMap<String, String>>();
                        for(int i=0; i<array.length(); i++) {

                            final JSONObject obj = array.getJSONObject(i);
                            marketName.add(obj.getString("productmodelname"));

                            hm = new HashMap<String, String>();
                            hm.put("products", obj.getString("category"));
                            hm.put("company",obj.getString("companyname"));
                            hm.put("model",obj.getString("productmodelname"));
                            hm.put("id",obj.getString("id"));
                            hm.put("categoryid",obj.getString("categoryid"));
                            commName.add(hm);

                            Set<String> uniqueList;
                            uniqueList = new HashSet<String>(marketName);
                            marketName.clear();
                            marketName.add("Select Model");
                            marketName.addAll(uniqueList);

                            item1 = new Model1(obj.getString("category"), obj.getString("companyname"),obj.getString("productmodelname"), obj.getString("id"));
                            productList.add(item1);

                            final CustomSpinnerAdapter1 customSpinnerAdapter = new CustomSpinnerAdapter1
                                    (getContext(), marketName);
                            categ.setAdapter(customSpinnerAdapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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

    private class CustomSpinnerAdapter1 extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private List<String> asr;

        public CustomSpinnerAdapter1(Context context, List<String> asr) {
            this.asr = asr;
            activity = context;
        }
        public int getCount() {
            return asr.size();
        }
        public int getPosition(String str){return asr.indexOf(str);}
        public Object getItem(int i) {
            return asr.get(i);
        }
        public long getItemId(int i) {
            return (long) i;
        }
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(getActivity());
            txt.setPadding(18, 18, 18, 18);
            txt.setTextSize(12);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr.get(position));
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }
        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(getActivity());
            txt.setGravity(Gravity.CENTER);
            txt.setPadding(18, 18, 18, 18);
            txt.setTextSize(12);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }
    }


    private void sendDetailstoserver() {
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        Map<String,String> postparam = new HashMap<>();
        postparam.put("productmodelid",categ.getSelectedItem().toString());
        postparam.put("customerid",categ1.getSelectedItem().toString());
        postparam.put("purchaseddate",purchDate.getText().toString());
        postparam.put("expireddate",expirDate.getText().toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.URL+"saveProductWarranty", new JSONObject(postparam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("status",response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),jsonObject.getString("status"),Toast.LENGTH_SHORT).show();
                            if(jsonObject.getString("status").equals("success")){
                                Intent intent= new Intent(getActivity(),DashboardActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(getActivity(),jsonObject.getString("status").toString(),Toast.LENGTH_SHORT).show();
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

    private void getDetailsFromServer1() {
        try {
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            final JSONObject jsonBody = new JSONObject();
            final String mRequestBody = jsonBody.toString();

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                    Constants.URL +"getCustomerIds", new JSONObject(mRequestBody), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("Spin"," response:---"+response);
                    JSONObject json= null;
                    try {
                        json = new JSONObject(response.toString());
                        JSONArray array=json.getJSONArray("CustomerIds");
                        List<String> marketName = new ArrayList<String>();
                        commName1=new ArrayList<HashMap<String, String>>();
                        for(int i=0; i<array.length(); i++) {
                            final JSONObject obj = array.getJSONObject(i);
                            marketName.add(obj.getString("customerId"));
                            hm = new HashMap<String, String>();
                            hm.put("customerId", obj.getString("customerId"));

                            commName1.add(hm);


                        }
                        Set<String> uniqueList;
                        uniqueList = new HashSet<String>(marketName);
                        marketName.clear();
                        marketName.add("Select Customer");
                        marketName.addAll(uniqueList);

                        //item2 = new Model2(marketName);
                        //productList2.add(marketName);

                        final CustomSpinnerAdapter2 customSpinnerAdapter = new CustomSpinnerAdapter2
                                (getContext(), marketName);
                        categ1.setAdapter(customSpinnerAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
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


    private class CustomSpinnerAdapter2 extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private List<String> asr;

        public CustomSpinnerAdapter2(Context context, List<String> asr) {
            this.asr = asr;
            activity = context;
        }
        public int getCount() {
            return asr.size();
        }
        public int getPosition(String str){return asr.indexOf(str);}
        public Object getItem(int i) {
            return asr.get(i);
        }
        public long getItemId(int i) {
            return (long) i;
        }
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(getActivity());
            txt.setPadding(18, 18, 18, 18);
            txt.setTextSize(12);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr.get(position));
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }
        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(getActivity());
            txt.setGravity(Gravity.CENTER);
            txt.setPadding(18, 18, 18, 18);
            txt.setTextSize(12);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }
    }






    private void getDetailsToServer() {
        RequestQueue queue = Volley.newRequestQueue(getContext().getApplicationContext());
        Map<String, Boolean> postParam= new HashMap<String, Boolean>();
        postParam.put("purchaseCustomer",true);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.URL+"warrantylist",new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Logresponse"," "+response.toString());
                mCategoryList1=new ArrayList<HashMap<String, String>>();
                try{
                    JSONObject json=new JSONObject(response.toString());
                    progressDialog.dismiss();
                    JSONArray jsonarray = json.getJSONArray("warrantylist");
                    for(int i=0; i<jsonarray.length(); i++) {
                        JSONObject obj = jsonarray.getJSONObject(i);
                        hm = new HashMap<String, String>();
                        hm.put("customerid",obj.getString("customerid"));
                        hm.put("expireddate",obj.getString("expireddate"));
                        hm.put("orderId",obj.getString("orderId"));
                        hm.put("purchaseddate",obj.getString("purchaseddate"));
                        hm.put("productmodelname",obj.getString("productmodelname"));
                        hm.put("productmodelid",obj.getString("productmodelid"));
                        //hm.put("status",obj.getString("status"));
                        mCategoryList1.add(hm);
                    }
                    CustomAdapter1 adapter = new CustomAdapter1(mCategoryList1);
                    list.setAdapter(adapter);
                }catch(JSONException e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext().getApplicationContext(), "Error" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public class CustomAdapter1 extends BaseAdapter {
        private LayoutInflater inflater=null;
        ArrayList<HashMap<String, String>> alData1;
        public CustomAdapter1( ArrayList<HashMap<String, String>> al) {
            alData1=al;
            inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return alData1.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if(convertView==null){
                convertView = inflater.inflate(R.layout.prdpay6, null);
            }
            HashMap<String,String> hm1=alData1.get(position);
            TextView tv=(TextView) convertView.findViewById(R.id.tv1);
            tv.setText(" "+hm1.get("productmodelname"));
            return convertView;
        }
    }
}
