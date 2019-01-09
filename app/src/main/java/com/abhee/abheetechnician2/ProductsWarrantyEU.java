package com.abhee.abheetechnician2;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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


public class ProductsWarrantyEU extends Fragment {
    TextView textpw1,textpw2;
    Spinner spin1,spin2;
    EditText edt1,edt2;
    Button btn1,btn2,btn3;
    DatePickerDialog datePickerDialog;
    Model1 item1;
    ArrayList<HashMap<String, String>> commName,mCategoryList1;
    private ArrayList<Model1> productList;
    HashMap<String, String> hm;
    static int pos;
    static String expire,purchase,promodel,custid,promodid,orderid;
    public static ProductsWarrantyEU newInstance(int position, String expireddate,
                                                 String purchaseddate, String productmodelname,
                                                 String customerid,String orderId, String productmodelid) {
        ProductsWarrantyEU fragment = new ProductsWarrantyEU();
        pos=position;
        expire=expireddate;
        purchase=purchaseddate;
        promodel=productmodelname;
        custid=customerid;
        promodid=productmodelid;
        orderid=orderId;
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
        View v=inflater.inflate(R.layout.products_warranty_eu, container, false);
        spin1=(Spinner)v.findViewById(R.id.spinPW1);
        spin2=(Spinner)v.findViewById(R.id.spinPW2);
        textpw1=(TextView)v.findViewById(R.id.textPW1);
        textpw2=(TextView)v.findViewById(R.id.textPW2);

        textpw1.setText(promodel);
        textpw2.setText(custid);
        spin1.setSelection(Integer.valueOf(promodid));
        getDetailsFromServer();
        edt1=(EditText)v.findViewById(R.id.purchaseDt);
        edt1.setText(purchase);
        //edt1.setBackgroundColor(Color.parseColor("#000000"));
        //edt1.setBackgroundColor(R.drawable.editborder);
        edt1.setEnabled(false);
        edt2=(EditText)v.findViewById(R.id.expireDt);
        edt2.setText(expire);
        productList = new ArrayList<Model1>();
        edt2.setEnabled(false);
        btn1=(Button)v.findViewById(R.id.resetPW1);
        btn2=(Button) v.findViewById(R.id.submitPW1);
        btn3=(Button)v.findViewById(R.id.editPW1);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //textpw1.setVisibility(View.GONE);
                //textpw2.setVisibility(View.GONE);
                btn3.setVisibility(View.GONE);
                btn1.setVisibility(View.VISIBLE);
                btn2.setVisibility(View.VISIBLE);
                //spin1.setVisibility(View.VISIBLE);
                //spin2.setVisibility(View.VISIBLE);
                edt1.setEnabled(true);
                edt2.setEnabled(true);

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((!edt1.getText().toString().equals(null)&&edt1.getText().length()!=0)&&(!edt2.getText().toString().equals(null)&&edt2.getText().length()!=0)){
                    sendDetailstoserver();
                }
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spin1.setAdapter(null);
                edt1.setText("");
                edt2.setText("");
            }
        });

        edt1.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inType = edt1.getInputType();
                edt1.setInputType(InputType.TYPE_NULL);
                edt1.onTouchEvent(event);
                edt1.setInputType(inType);
                edt1.setFocusable(false);
                edt1.requestFocus();
                return true;
            }
        });
        edt1.setOnLongClickListener(new View.OnLongClickListener()
        {
            public boolean onLongClick(View v) {
                int inType = edt1.getInputType();
                edt1.setInputType(InputType.TYPE_NULL);
                edt1.setInputType(inType);
                edt1.setFocusable(false);
                edt1.requestFocus();
                return true;
            }
        });
        edt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                final Calendar calander2 = Calendar.getInstance();
                calander2.setTimeInMillis(0);
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                edt1.setInputType(InputType.TYPE_NULL);
                edt1.setFocusable(false);
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
                                String date_pick_res = year + "/" + month + "/" + dayOfMonth;
                                calander2.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                                Date SelectedDate = calander2.getTime();
                                DateFormat dateformat_UK = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
                                String StringDateformat_UK = dateformat_UK.format(SelectedDate);
                                edt1.setText(  StringDateformat_UK );
                                //edt1.setText(date_pick_res);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("Please select date");
                datePickerDialog.show();
            }
        });

        edt2.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inType = edt2.getInputType(); // backup the input type
                edt2.setInputType(InputType.TYPE_NULL); // disable soft input
                edt2.onTouchEvent(event); // call native handler
                edt2.setInputType(inType);
                edt2.setFocusable(false);// restore input type
                edt2.requestFocus();
                return true;
            }
        });
        edt2.setOnLongClickListener(new View.OnLongClickListener()
        {
            public boolean onLongClick(View v) {
                int inType = edt2.getInputType();
                edt2.setInputType(InputType.TYPE_NULL);
                edt2.setInputType(inType);
                edt2.setFocusable(false);
                edt2.requestFocus();
                return true;
            }
        });

        edt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                final Calendar calander2 = Calendar.getInstance();
                calander2.setTimeInMillis(0);
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                edt2.setInputType(InputType.TYPE_NULL);
                edt2.setFocusable(false);
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
                                edt2.setText(  StringDateformat_UK );
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

    private void sendDetailstoserver() {
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        Map<String,String> postparam = new HashMap<>();
        postparam.put("orderId",orderid);
        postparam.put("purchaseddate",edt1.getText().toString());
        postparam.put("expireddate",edt2.getText().toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.URL+"editProductWarranty", new JSONObject(postparam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("status",response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            Toast.makeText(getActivity(),jsonObject.getString("status"),Toast.LENGTH_SHORT).show();
                            if(jsonObject.getString("status").equals("updated")){
                                Intent intent= new Intent(getActivity(),DashboardActivity.class);
                                startActivity(intent);
                            }
                            else{
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

    private void getDetailsFromServer() {
        try {
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            final JSONObject jsonBody = new JSONObject();
            final String mRequestBody = jsonBody.toString();

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                    Constants.URL +"getproductdetails", new JSONObject(mRequestBody), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("Spinner:"," response:---"+response);
                    JSONObject json= null;
                    try {
                        json = new JSONObject(response.toString());
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

                            marketName.addAll(uniqueList);

                            item1 = new Model1(obj.getString("category"), obj.getString("companyname"),obj.getString("productmodelname"), obj.getString("id"));
                            productList.add(item1);

                            final CustomSpinnerAdapter1 customSpinnerAdapter = new CustomSpinnerAdapter1(getContext(), marketName);
                            spin1.setAdapter(customSpinnerAdapter);
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
            txt.setTextSize(14);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr.get(position));
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }
        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(getActivity());
            txt.setGravity(Gravity.CENTER);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(12);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }
    }
}
