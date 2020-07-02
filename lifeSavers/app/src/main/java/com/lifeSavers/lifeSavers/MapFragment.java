package com.lifeSavers.lifeSavers;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MapFragment extends Fragment implements View.OnClickListener {

    MapView mMapView;
    public GoogleMap googleMap;
    private LatLng coordinates;
    private String state;

    public MapFragment() {
        this.coordinates = new LatLng(0,0);
        this.state = "";
    }

    public String getState()
    {
        return state;
    }
    public void setState(String stateName)
    {
        this.state = stateName;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }


    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_fragment, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);


        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button


                // For dropping a marker at a point on the Map

                LatLng lebanon = new LatLng(33.8938,35.5018);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(lebanon));
                mMap.addMarker(new MarkerOptions().position(lebanon));
                mMap.animateCamera( CameraUpdateFactory.zoomTo( 10.0f ) );
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(lebanon).zoom(12).build();
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(final LatLng point) {

                        Log.d("DEBUG","Map clicked [" + point.latitude + " / " + point.longitude + "]");
                        mMap.clear();

                        LatLng newLocation = new LatLng(point.latitude, point.longitude);
                        setCoordinates(newLocation);
                        mMap.addMarker(new MarkerOptions().position(newLocation).title("Marker in Sydney"));



                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(getContext(), Locale.getDefault());

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
                        setState(state);

                        EditText edit = (EditText)getActivity().findViewById(R.id.input_address);
                        edit.setText(getState());
                //Toast.makeText(getContext(),city+" "+state+" "+knownName,Toast.LENGTH_SHORT).show();



                      //  Toast.makeText(getContext(),newLocation.toString(),Toast.LENGTH_SHORT).show();
                        //ViewDialog alert = new ViewDialog(MapsActivity.this);



                            }
                        });
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onClick(View v) {

    }
}