package com.abhee.abheetechnician2;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Admin on 10-10-2018.
 */

public class ProductsWarrantyData extends Fragment {
    Spinner spin1,spin2;
    EditText edt1,edt2;
    Button btn1,btn2;
    Model1 item1;
    ArrayList<HashMap<String, String>> commName,mCategoryList1;
    private ArrayList<Model1> productList;
    HashMap<String, String> hm;
    static int pos;
    static String expire,purchase,promodel,custid,promodid;
    public ProductsWarrantyData newInstance(int position, String expireddate,
                                            String purchaseddate, String productmodelname,
                                            String customerid, String productmodelid) {
        ProductsWarrantyData fragment = new ProductsWarrantyData();
        pos=position;
        expire=expireddate;
        purchase=purchaseddate;
        promodel=productmodelname;
        custid=customerid;
        promodid=productmodelid;

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
        v = inflater.inflate(R.layout.productwarranty_data, container, false);
        spin1=(Spinner)v.findViewById(R.id.spinPW1);
        spin2=(Spinner)v.findViewById(R.id.spinPW2);
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

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spin1.setAdapter(null);
                edt1.setText("");
                edt2.setText("");
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
                    Constants.URL +"getproducts", new JSONObject(mRequestBody), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("Spinner:"," response:---"+response);
                    JSONObject json= null;
                    try {
                        json = new JSONObject(response.toString());
                        JSONArray array=json.getJSONArray("productDetails");
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

                            final CustomSpinnerAdapter1 customSpinnerAdapter = new CustomSpinnerAdapter1
                                    (getContext(), marketName);
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
