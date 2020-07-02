package com.lifeSavers.lifeSavers;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class UserInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);



        TextView fullName = (TextView)findViewById(R.id.fullName);
        TextView donarEmail = (TextView) findViewById(R.id.donarEmail);
        TextView donarPhone = (TextView) findViewById(R.id.donarPhone);
        TextView shareButton = (TextView) findViewById(R.id.shareButton);
        ImageView userBloodType = (ImageView) findViewById(R.id.userPhoto);

        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserInfo.this,"Back button pressed!!",Toast.LENGTH_LONG).show();
            }
        });



        try {

            final JSONObject data = new JSONObject(getIntent().getStringExtra("userInfo"));

            UserInfoMap map = new UserInfoMap(data.getDouble("longitude"),data.getDouble("latitude"));
            FragmentManager fm = getFragmentManager();
            FragmentTransaction FT = fm.beginTransaction();
            FT.add(R.id.userMapLayout,map);
            FT.commit();

            String bloodType = data.getString("bloodType");
            switch(bloodType) {
                case "A+":
                    userBloodType.setImageResource(R.drawable.apositive);
                    break;
                case "B+":
                    userBloodType.setImageResource(R.drawable.bpositive);
                    break;
                case "A-":
                    userBloodType.setImageResource(R.drawable.anegative);
                    break;
                case "B-":
                    userBloodType.setImageResource(R.drawable.bnegative);
                    break;
                case "AB+":
                    userBloodType.setImageResource(R.drawable.abpositive);
                    break;
                case "AB-":
                    userBloodType.setImageResource(R.drawable.abnegative);
                    break;
                case "O+":
                    userBloodType.setImageResource(R.drawable.opositive);
                    break;
                case "O-":
                    userBloodType.setImageResource(R.drawable.onegative);
                    break;
            }


            fullName.setText(data.getString("fullName"));
            donarPhone.setText(data.getString("mobileNumber"));
            donarEmail.setText(data.getString("email"));

            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                    whatsappIntent.setType("text/plain");
                    whatsappIntent.setPackage("com.whatsapp");
                    try {
                        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Donar's full Name: "+data.getString("fullName")+
                                                                        "\nDonar's phone number: "+data.getString("mobileNumber")+
                                                                        "\nDonar's email: "+data.getString("email")+
                                                                        "\nDonar's lacation is: "+data.getString("location"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        startActivity(whatsappIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(UserInfo.this,"Whatsapp is not installed",Toast.LENGTH_LONG).show();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            {
                Intent i = new Intent(UserInfo.this,Dashboard.class);
                i.putExtra("userData",getIntent().getStringExtra("userData"));
                Toast.makeText(UserInfo.this, "Back", Toast.LENGTH_LONG).show();
                startActivity(i);
            }

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
