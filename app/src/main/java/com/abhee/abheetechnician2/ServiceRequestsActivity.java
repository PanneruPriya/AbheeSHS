package com.abhee.abheetechnician2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServiceRequestsActivity extends Fragment {
    TextView srno,categ,model,asignd,subj,custid;
    ImageView imageView;
    Button view,edit;
    FragmentTransaction transaction;
    ArrayList<HashMap<String, String>> mCategoryList1;
    HashMap<String, String> hm;
    HashMap<String, String> hm1;
    ListView list;
    String custid1;
    String empid=null;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    public static ServiceRequestsActivity newInstance() {
        ServiceRequestsActivity fragment = new ServiceRequestsActivity();
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
        v = inflater.inflate(R.layout.activity_servicerequests, container, false);
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("In Progress");
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        sharedPreferences = getActivity().getSharedPreferences("Abhee", Context.MODE_PRIVATE);
        String mob=sharedPreferences.getString("username",null);
        String pass=sharedPreferences.getString("password",null);
        empid = sharedPreferences.getString("user_id",null);
        Log.i("empid",empid);
        list = v.findViewById(R.id.mobile_list7);
        getDetailsToServer();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getActivity(), LoginPageActivity.class));
        return v;
    }
    private void getDetailsToServer() {
        RequestQueue queue = Volley.newRequestQueue(getContext().getApplicationContext());
        Map<String, String> postParam= new HashMap<String, String>();

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.URL+"taskslist",new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Logresponse"," "+response.toString());
                mCategoryList1=new ArrayList<HashMap<String, String>>();
                try{
                    JSONObject json=new JSONObject(response.toString());
                    progressDialog.dismiss();
                    JSONArray jsonarray = json.getJSONArray("taskslist");
                    for(int i=0; i<jsonarray.length(); i++) {
                        JSONObject obj = jsonarray.getJSONObject(i);
                        if(empid.equals(obj.getString("empid"))) {
                            hm = new HashMap<String, String>();
                            hm.put("severity", obj.getString("severity"));
                            hm.put("servicetype", obj.getString("servicetype"));
                            hm.put("subject", obj.getString("subject"));
                            hm.put("description", obj.getString("description"));
                            hm.put("kstatus", obj.getString("kstatus"));
                            hm.put("customer_id", obj.getString("customer_id"));
                            hm.put("taskno", obj.getString("taskno"));
                            hm.put("category", obj.getString("category"));
                            hm.put("assignto", obj.getString("assignto"));
                            hm.put("modelname", obj.getString("modelname"));
                            hm.put("created_time", obj.getString("created_time"));
                            hm.put("communicationaddress", obj.getString("communicationaddress"));
                            hm.put("priority", obj.getString("priority"));
                            hm.put("warranty",obj.getString("warranty"));
                            hm.put("imgfile",obj.getString("imgfile"));
                            hm.put("uploadfile",obj.getString("uploadfile"));
                            hm.put("firstname", obj.getString("firstname"));
                            hm.put("lastname", obj.getString("lastname"));
                            hm.put("mobilenumber", obj.getString("mobilenumber"));
                            hm.put("taskdeadline", obj.getString("taskdeadline"));
                            hm.put("invimg",obj.getString("invimg"));

                            mCategoryList1.add(hm);
                        }
                    }
                    CustomAdapter1 adapter = new CustomAdapter1(mCategoryList1);
                    list.setAdapter(adapter);
                }catch(JSONException e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //chik();
                //Toast.makeText(getContext().getApplicationContext(), "Error" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public class CustomAdapter1 extends BaseAdapter {
        private LayoutInflater inflater=null;
        ArrayList<HashMap<String, String>> alData1;
        public CustomAdapter1( ArrayList<HashMap<String, String>> al) {
            alData1=al;
            inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                convertView = inflater.inflate(R.layout.prdpay7, null);
            }
            hm1=alData1.get(position);

            srno= convertView.findViewById(R.id.srnum_tt);
            categ= convertView.findViewById(R.id.categ_tt);
            model= convertView.findViewById(R.id.model_tt);
            asignd= convertView.findViewById(R.id.asgnd_tt);
            subj= convertView.findViewById(R.id.sub_tt);
            custid= convertView.findViewById(R.id.custid_tt);
            view= convertView.findViewById(R.id.view_bt);
            edit= convertView.findViewById(R.id.edit_bt);
            imageView=convertView.findViewById(R.id.priority);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hm1=mCategoryList1.get(position);
                    custid1=hm1.get("id");
                    transaction =getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame,
                            new ServiceRequestsDataView().newInstance(position,hm1.get("severity"),
                                    hm1.get("servicetype"),hm1.get("updatedTime"),hm1.get("subject"),
                                    hm1.get("description"),hm1.get("priority"),hm1.get("additionalinfo"),
                                    hm1.get("communicationaddress"),hm1.get("assignby"),hm1.get("kstatus"),
                                    hm1.get("uploadfile"),hm1.get("customer_id"),hm1.get("taskno"),
                                    hm1.get("created_time"),hm1.get("warranty"),hm1.get("id"),
                                    hm1.get("category"),hm1.get("assignto"),hm1.get("modelname"),
                                    hm1.get("taskdeadline"),hm1.get("imgfile"),hm.get("firstname"),hm.get("lastname"),hm.get("mobilenumber"),hm.get("invimg")));
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String,String >hm2=mCategoryList1.get(position);
                    custid1=hm2.get("id");
                    transaction =getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame,
                            new ServiceRequestsDataEdit1().newInstance(position,hm2.get("severity"),
                                    hm2.get("servicetype"),hm2.get("updatedTime"),hm2.get("subject"),
                                    hm2.get("description"),hm2.get("priority"),hm2.get("additionalinfo"),
                                    hm2.get("communicationaddress"),hm2.get("assignby"),hm2.get("kstatus"),
                                    hm2.get("uploadfile"),hm2.get("customer_id"),hm2.get("taskno"),
                                    hm2.get("created_time"),hm2.get("warranty"),hm2.get("id"),
                                    hm2.get("category"), hm2.get("assignto"),hm2.get("modelname"),
                                    hm2.get("taskdeadline"),hm2.get("imgfile")));
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
             /*  String text = "<font color=#cc0029>Srinivasa</font> <font color=#ffcc00>Rao</font>";*/
            String text1 = "<font COLOR=\'#696969\'>" + "SR No: " + "</font>"
                + "<font COLOR=\'#FA8072\'>" + " "+hm1.get("taskno") + "</font>"
                + " ";
            srno.setText(Html.fromHtml(text1));

            categ.setText("Category: "+hm1.get("category"));
            model.setText("Model: "+hm1.get("modelname"));

            String text2 = "<font COLOR=\'#696969\'>" + "Service Type: " + "</font>"
                    + "<font COLOR=\'#FA8072\'>" + " "+hm1.get("servicetype") + "</font>"
                    + " ";
            asignd.setText(Html.fromHtml(text2));

            String text3 = "<font COLOR=\'#696969\'>" + "Subject: " + "</font>"
                    + "<font COLOR=\'#FA8072\'>" + " "+hm1.get("kstatus") + "</font>"
                    + " ";
            subj.setText(Html.fromHtml(text3));

            String text4 = "<font COLOR=\'#696969\'>" + "Customer ID: " + "</font>"
                    + "<font COLOR=\'#FA8072\'>" + " "+hm1.get("customer_id") + "</font>"
                    + " ";
            custid.setText(Html.fromHtml(text4));
                    Picasso.with(getActivity()).load(Constants.IMG_URL+mCategoryList1.get(position).get("priority")+".png").into(imageView);
                    Log.i("url",Constants.IMG_URL+mCategoryList1.get(position).get("priority")+".png");
            return convertView;
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
                getDetailsToServer();
                dialog.hide();
            }
        });
    }
}
