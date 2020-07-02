package com.lifeSavers.lifeSavers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import org.json.simple.parser.JSONParser;

import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_LONG;

/**
 * Created by hassan on 1/29/2019.
 */

public class BecomeDonarFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_become_donar,container,false);
        final Button becomeDonar = v.findViewById(R.id.becomeDonar);
        final Constants constants = new Constants();

        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait...");


        try {
            JSONObject userData = new JSONObject(SignupActivity.userInfo);
            final String username = userData.getString("username");
            if(userData.getBoolean("doner"))
            {
                becomeDonar.setEnabled(false);
                becomeDonar.setText("You're already a Donor!");
            }
            else
            {
                becomeDonar.setEnabled(true);
            }

            becomeDonar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.show();

                    String URL = "http://"+constants.getIP()+":3000/api/becomeDonar";
                    RequestQueue MyRequestQueue = Volley.newRequestQueue(getContext());
                    StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,
                            URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        final JSONObject json = new JSONObject(response);
                                        if(json.getBoolean("error"))
                                        {

                                        }else
                                        {
                                            new android.os.Handler().postDelayed(
                                                    new Runnable() {
                                                        public void run() {
                                                            progressDialog.dismiss();
                                                            becomeDonar.setEnabled(false);

                                                        }
                                                    }, 1000);

                                        }






                                        //Toast.makeText(LoginActivity.this,json.toString(),LENGTH_LONG).show();
                                    }  catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error)
                        {

                        }
                    }) {

                        @Override
                        public String getBodyContentType() {
                            return "application/x-www-form-urlencoded; charset=UTF-8";
                        }

                        @Override
                        protected Map<String, String> getParams()  {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("username", username);
                            return params;
                        }

                    };



                    MyRequestQueue.add(jsonObjRequest);



                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }







        return v;
    }
}
