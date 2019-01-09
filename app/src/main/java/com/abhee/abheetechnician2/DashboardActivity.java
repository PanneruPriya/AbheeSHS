package com.abhee.abheetechnician2;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;
    FragmentTransaction transaction;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Boolean exit = false;
    TextView user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        sharedPreferences = this.getSharedPreferences("Abhee", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String fname = sharedPreferences.getString("firstname", "");
        String lname = sharedPreferences.getString("lastname", "");

        mToolbar = (Toolbar) findViewById(R.id.nav_tool);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
            }
        });
        setSupportActionBar(mToolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        user_name = (AppCompatTextView) header.findViewById(R.id.user);
        user_name.setText(fname+" "+lname);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, new ServiceRequestsActivity().newInstance());
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getApplicationContext(), LoginPageActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        int id = item.getItemId();
        if (id == R.id.action_notify) {
           /* transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, new NotificationsActivity().newInstance());
            transaction.addToBackStack("tag");
            transaction.addToBackStack(null);
            transaction.commit();*/
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void displaySelectedScreen(int itemId) {
        transaction = getSupportFragmentManager().beginTransaction();
        switch (itemId) {
            case R.id.nav_home:
                transaction.replace(R.id.frame, new ServiceRequestsActivity().newInstance());
                transaction.addToBackStack("tag");
                break;
            case R.id.nav_profile:
                transaction.replace(R.id.frame, new ProfilePage().newInstance());
                transaction.addToBackStack("tag");
                break;
            case R.id.nav_logout:
                Intent intent = new Intent(DashboardActivity.this, LoginPageActivity.class);
                startActivity(intent);
                break;
        }
        transaction.addToBackStack(null);
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }
}
