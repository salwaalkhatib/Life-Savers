package com.lifeSavers.lifeSavers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by hassan on 1/29/2019.
 */

public class BloodDoners extends Fragment {

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_balance,container,false);
        //Button viewDoners = (Button) v.findViewById(R.id.doners);

        final ListView list = (ListView)v.findViewById(R.id.list);


        final ArrayList<String> donersFullName = new ArrayList<>();
        final ArrayList<String> donersLocation = new ArrayList<>();
        final ArrayList<Integer> images = new ArrayList<>();

       /* viewDoners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),donersBloodType.toString(),Toast.LENGTH_LONG).show();


            }
        });*/
        ((Dashboard) getActivity())
                .setActionBarTitle("Blood Doners");

        RequestQueue queue = Volley.newRequestQueue(getContext());  // this = context
        final Constants c = new Constants();
        final String url = "http://"+c.getIP()+":3000/api/doners";

        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray donersList = new JSONArray(response);
                            Toast.makeText(getContext(),donersList.toString(),Toast.LENGTH_LONG).show();
                            for(int i = 0; i< donersList.length();i++)
                            {
                                try {
                                    donersFullName.add(donersList.getJSONObject(i).getString("fullName"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    donersLocation.add(donersList.getJSONObject(i).getString("location"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try
                                {
                                    String bloodType = donersList.getJSONObject(i).getString("bloodType");
                                    switch(bloodType)
                                    {
                                        case "A+":
                                            images.add(R.drawable.apositive);
                                            break;
                                        case "B+":
                                            images.add(R.drawable.bpositive);
                                            break;
                                        case "A-":
                                            images.add(R.drawable.anegative);
                                            break;
                                        case "B-":
                                            images.add(R.drawable.bnegative);
                                            break;
                                        case "AB+":
                                            images.add(R.drawable.abpositive);
                                            break;
                                        case "AB-":
                                            images.add(R.drawable.abnegative);
                                            break;
                                        case "O+":
                                            images.add(R.drawable.opositive);
                                            break;
                                        case "O-":
                                            images.add(R.drawable.onegative);
                                            break;
                                    }
                                } catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
                            }




                            MyListView adapter=new MyListView(getActivity(), donersFullName,donersLocation,images);
                            list.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error

                    }
                }

        );


        queue.add(getRequest);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                Toast.makeText(getContext(),donersFullName.get(position),Toast.LENGTH_LONG).show();

                final String url = "http://"+c.getIP()+":3000/api/doners/"+donersFullName.get(position);
                RequestQueue queue = Volley.newRequestQueue(getContext());  // this = context
                StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();


                                    Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(getActivity(),UserInfo.class);
                                    i.putExtra("userInfo",response);
                                    startActivity(i);





                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error

                            }
                        }

                );


                queue.add(getRequest);
                /*else if(position == 1) {
                    //code specific to 2nd list item
                    Toast.makeText(getContext(),"Place Your Second Option Code",Toast.LENGTH_SHORT).show();
                }

                else if(position == 2) {

                    Toast.makeText(getContext(),"Place Your Third Option Code",Toast.LENGTH_SHORT).show();
                }
                else if(position == 3) {

                    Toast.makeText(getContext(),"Place Your Forth Option Code",Toast.LENGTH_SHORT).show();
                }
                else if(position == 4) {

                    Toast.makeText(getContext(),"Place Your Fifth Option Code",Toast.LENGTH_SHORT).show();
                }
*/
            }

        });




        return v;


    }
    public Bitmap getBitmapfromUrl(String imageUrl)
    {
        try
        {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }
}
