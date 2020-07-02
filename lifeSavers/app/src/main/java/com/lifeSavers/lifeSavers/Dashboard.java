package com.lifeSavers.lifeSavers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        Constants constants = new Constants();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.flContent, new BloodDoners());
        tx.commit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        TextView username = (TextView)headerView.findViewById(R.id.navUsername);
        TextView email    = (TextView)headerView.findViewById(R.id.navEmail);
        ImageView userImage = (ImageView)headerView.findViewById(R.id.userImage);





                userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.flContent,new BecomeDonarFragment()).commit();
                drawer.closeDrawers();
            }
        });


        navigationView.setNavigationItemSelectedListener(this);
        if(SignupActivity.userInfo.equals(""))
            SignupActivity.userInfo = getIntent().getStringExtra("userData");
        try {
            JSONObject userData = new JSONObject(SignupActivity.userInfo);
            username.setText(userData.getString("username"));
            email.setText(userData.getString("email"));
            String bloodType = userData.getString("bloodType");
            switch(bloodType) {
                case "A+":
                    userImage.setImageResource(R.drawable.apositive);
                    break;
                case "B+":
                    userImage.setImageResource(R.drawable.bpositive);
                    break;
                case "A-":
                    userImage.setImageResource(R.drawable.anegative);
                    break;
                case "B-":
                    userImage.setImageResource(R.drawable.bnegative);
                    break;
                case "AB+":
                    userImage.setImageResource(R.drawable.abpositive);
                    break;
                case "AB-":
                    userImage.setImageResource(R.drawable.abnegative);
                    break;
                case "O+":
                    userImage.setImageResource(R.drawable.opositive);
                    break;
                case "O-":
                    userImage.setImageResource(R.drawable.onegative);
                    break;
            }



            Toast.makeText(Dashboard.this, userData.toString(), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Class fragmentClass;


        if (id == R.id.bloodDonars) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flContent,new BloodDoners()).commit();

        } else if (id == R.id.logout) {
            String URL = "http://"+constants.getIP()+":3000/api/logout";
            RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);
            StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(Dashboard.this,"You have successfully logged out!",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Dashboard.this,LoginActivity.class);
                    startActivity(i);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            requestQueue.add(request);
            }


         else if (id == R.id.home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flContent,new BloodDoners()).commit();
        } else if (id == R.id.become_donar) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flContent,new BecomeDonarFragment()).commit();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
