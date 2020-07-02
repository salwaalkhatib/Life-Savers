package com.lifeSavers.lifeSavers;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.sql.Date;
import java.util.ArrayList;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.widget.Toast.LENGTH_LONG;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_name)
    EditText _nameText;
    @BindView(R.id.input_address)
    EditText _addressText;
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_mobile)
    EditText _mobileText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.input_username)
    EditText _username;
    @BindView(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup)
    Button _signupButton;
    @BindView(R.id.link_login)
    TextView _loginLink;
    @BindView(R.id.bloodType)
    Spinner bloodTypeSpinner;
    @BindView(R.id.donerBool)
    CheckBox donerBoolean;
    @BindView(R.id.donationDate)
    EditText donationDate;
    String bloodTypeStr;
    Constants constants = new Constants();
    final MapFragment map = new MapFragment();
    DatePickerDialog.OnDateSetListener mDatePicker;
    public static String userInfo = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Toast.makeText(SignupActivity.this,"jsakjskajskajskajskjakjskaj",Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        //Initializing map fragment and loadiing it
        Intent i = getIntent();
        String location = "";
        FragmentManager fm = getFragmentManager();
        FragmentTransaction FT = fm.beginTransaction();
        FT.add(R.id.mapLayout,map);
        FT.commit();
        //Disabling address editable text
        _addressText.setFocusable(false);
        donationDate.setFocusable(false);
        //Initializing blood type spinner
        final List<String> bloodTypes = new ArrayList<String>();
        bloodTypes.add("Pick your Blood Type");
        bloodTypes.add("A+");
        bloodTypes.add("A-");
        bloodTypes.add("B+");
        bloodTypes.add("B-");
        bloodTypes.add("AB+");
        bloodTypes.add("AB-");
        bloodTypes.add("opositive");
        bloodTypes.add("O-");

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,bloodTypes);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodTypeSpinner.setAdapter(adp);
        bloodTypeSpinner.setSelection(0,false);

        bloodTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                bloodTypeStr = bloodTypes.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        donationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal =  Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(SignupActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDatePicker,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                donationDate.setText(date);
            }
        };


        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }



    public void signup() {

        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();
        LatLng coordinates = map.getCoordinates();
        final String donerBool;
        if(donerBoolean.isChecked())
            donerBool = "true";
        else
            donerBool = "false";
        /*Date date1= (Date) new Date
                (donationDate.getYear(), donationDate.getMonth(), donationDate.getDayOfMonth());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        final String dateString = sdf.format(date1);*/



        final String name = _nameText.getText().toString();
        final String username = _username.getText().toString();
        final double latitude = coordinates.latitude;
        final double longitude = coordinates.longitude;
        final String email = _emailText.getText().toString();
        final String mobile = _mobileText.getText().toString();
        final String password = _passwordText.getText().toString();
        final String bloodType = bloodTypeStr;
        String reEnterPassword = _reEnterPasswordText.getText().toString();
        final String location = _addressText.getText().toString();
        final String date = donationDate.getText().toString();

        // TODO: Implement your own signup logic here.

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        String URL = "http://"+constants.getIP()+":3000/api/newUser";
        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONParser parser = new JSONParser();
                        try {
                            final JSONObject json = new JSONObject(response);
                            if(!json.getBoolean("success"))
                            {
                                progressDialog.dismiss();

                                Toast.makeText(SignupActivity.this,json.getString("msg"),LENGTH_LONG).show();
                                onSignupFailed();
                            }else
                            {
                                new Handler().postDelayed(
                                        new Runnable() {
                                            public void run() {
                                                Intent i;
                                                i = new Intent(SignupActivity.this,Dashboard.class);
                                                try {
                                                    i.putExtra("userData",json.getJSONObject("msg").toString());
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                // On complete call either onLoginSuccess or onLoginFailed
                                                onSignupSuccess();
                                                // onLoginFailed();
                                                progressDialog.dismiss();
                                                startActivity(i);

                                            }
                                        }, 3000);
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
                params.put("fullName", name);
                params.put("username", username);
                params.put("password", password);
                params.put("email", email);
                params.put("mobileNumber", mobile);
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude", String.valueOf(longitude));
                params.put("bloodType",bloodType);
                params.put("doner",donerBool);
                params.put("location",location);
                params.put("lastDateOfDonation",date);

                return params;
            }

        };



        MyRequestQueue.add(jsonObjRequest);



    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(SignupActivity.this,map.getState(),Toast.LENGTH_SHORT).show();
        //Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String username = _username.getText().toString();
        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }
        if (username.isEmpty() || name.length() < 3) {
            _username.setError("at least 3 characters");
            valid = false;
        } else {
            _username.setError(null);
        }
        if (address.isEmpty()) {
            _addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            _addressText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() != 10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("address", _addressText.getText().toString());
        outState.putString("name", _nameText.getText().toString());

        Toast.makeText(SignupActivity.this,_nameText.getText().toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Toast.makeText(SignupActivity.this,"FROM RESTORE!",Toast.LENGTH_SHORT).show();
    }


}
