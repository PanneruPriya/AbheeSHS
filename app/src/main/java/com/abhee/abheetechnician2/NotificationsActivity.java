package com.abhee.abheetechnician2;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
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

import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 07-07-2018.
 */

public class NotificationsActivity extends Fragment {


    SharedPreferences sharedPreferences;
    String userid="";
    ArrayList<HashMap<String,String>> list;
    ListView listView;

     public static NotificationsActivity newInstance() {
        NotificationsActivity fragment = new NotificationsActivity();
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

// Inflate the layout for this fragment
        View v;
        v = inflater.inflate(R.layout.activity_notifications, container, false);
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("Abhee", Context.MODE_PRIVATE);
        userid=sharedPreferences.getString("user_id",null);
        list= new ArrayList<>();
        listView = (ListView)v.findViewById(R.id.list_item);
        getDetailsfromserver();
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getActivity(), LoginPageActivity.class));

        return v;
    }
/*"servicetype": "Installation",
        "request_type": "Service Request",
        "kstatus": "CUSTOMER NOT AVAILABLE",
        "user_id": "9PHIL",
        "taskno": "JK25Z",*/
    private void getDetailsfromserver() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("assignto", userid);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.URL+"getTechnicianNotificationByUserId",
                new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                Log.i("ResponsePin:-",response.toString());
                try {
                    JSONObject json = new JSONObject(response.toString());
                    JSONArray jsonArray = json.getJSONArray("TechnicianNotificationList");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HashMap<String,String> hm = new HashMap<>();
                        hm.put("servicetype",jsonObject.getString("servicetype"));
                        hm.put("request_type",jsonObject.getString("request_type"));
                        hm.put("kstatus",jsonObject.getString("kstatus"));
                        hm.put("user_id",jsonObject.getString("user_id"));
                        hm.put("taskno",jsonObject.getString("taskno"));
                        hm.put("updated_time",jsonObject.getString("updated_time"));
                        list.add(hm);

                    }
                    ListviewAdapter1 adapter1 = new ListviewAdapter1(getActivity(),list);
                    listView.setAdapter(adapter1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
    public class ListviewAdapter1 extends BaseAdapter {
        public ArrayList<HashMap<String,String>> productList;
        Activity activity;

        public ListviewAdapter1(Activity activity, ArrayList<HashMap<String,String>> productList) {
            super();
            this.activity = activity;
            this.productList = productList;
        }

        @Override
        public int getCount() {
            return productList.size();
        }

        @Override
        public Object getItem(int position) {
            return productList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            AppCompatTextView servicetype,request_type,kstatus,times,taskno;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            LayoutInflater inflater = activity.getLayoutInflater();

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.listview_row1, null);
                holder = new ViewHolder();
                holder.servicetype = (AppCompatTextView) convertView.findViewById(R.id.servicetype);
                //holder.request_type = (AppCompatTextView) convertView.findViewById(R.id.request_type);
                holder.kstatus = (AppCompatTextView) convertView.findViewById(R.id.kstatus);
                holder.times = (AppCompatTextView) convertView.findViewById(R.id.createdtime);
                holder.taskno = (AppCompatTextView) convertView.findViewById(R.id.taskno);
               /* holder.maxprice = (AppCompatTextView) convertView.findViewById(R.id.maxprice);
                holder.minprice = (AppCompatTextView) convertView.findViewById(R.id.minprice);*/

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            HashMap<String,String> item = productList.get(position);
            holder.taskno.setText("Task Number: "+item.get("taskno"));
            holder.servicetype.setText("Service Type: "+item.get("servicetype"));
            //holder.request_type.setText("Requset Type: "+item.get("request_type"));
            holder.kstatus.setText("Serivce Status: "+item.get("kstatus"));
            holder.times.setText(item.get("updated_time"));
            /*holder.maxprice.setText(item.getMaxprice().toString());
            holder.minprice.setText(item.getMinprice().toString());*/
            return convertView;
        }
    }

}
