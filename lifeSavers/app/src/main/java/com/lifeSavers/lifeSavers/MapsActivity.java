package com.lifeSavers.lifeSavers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        LatLng lebanon = new LatLng(33.8938,35.5018);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(lebanon));
        mMap.addMarker(new MarkerOptions().position(lebanon));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 10.0f ) );

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng point) {
                Log.d("DEBUG","Map clicked [" + point.latitude + " / " + point.longitude + "]");
                mMap.clear();

                LatLng newLocation = new LatLng(point.latitude, point.longitude);
                mMap.addMarker(new MarkerOptions().position(newLocation));






                Toast.makeText(MapsActivity.this,newLocation.toString(),Toast.LENGTH_SHORT).show();
                //ViewDialog alert = new ViewDialog(MapsActivity.this);
                final BottomSheetDialog dialog = new BottomSheetDialog(MapsActivity.this);
                dialog.setContentView(R.layout.dialog);

                Button yes = (Button) dialog.findViewById(R.id.yes);
                Button no = (Button) dialog.findViewById(R.id.no);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(MapsActivity.this,"sqsqsqs",Toast.LENGTH_SHORT).show();
                        final ProgressDialog progressDialog = new ProgressDialog(MapsActivity.this,
                                R.style.AppTheme_Dark_Dialog);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Please Wait...");
                        progressDialog.show();

                        Geocoder geocoder;
                        List<Address> addresses = null;
                        geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());

                        try {
                            addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        //String country = addresses.get(0).getCountryName();
                        //String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                        Intent i = new Intent(MapsActivity.this,SignupActivity.class);
                       // i.putExtra("city",city);
                        i.putExtra("state",state);
                        //i.putExtra("knownName",knownName);
                        //Toast.makeText(MapsActivity.this,city+" "+state+" "+knownName,Toast.LENGTH_SHORT).show();
                        startActivity(i);



                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMap.clear();
                        dialog.dismiss();
                    }
                });
                dialog.show();


            }
        });

        // Add a marker in Sydney and move the camera



    }
}
