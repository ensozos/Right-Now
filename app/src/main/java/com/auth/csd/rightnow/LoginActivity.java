package com.auth.csd.rightnow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.auth.csd.rightnow.controller.GsonRequest;
import com.auth.csd.rightnow.utils.ConnectionProperties;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText viewUsernameField;
    private EditText viewPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        viewUsernameField = (EditText) findViewById(R.id.username_field);
        viewPasswordField = (EditText) findViewById(R.id.password_field);

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", ); //TODO 

        GsonRequest<String> request = new GsonRequest<String>(Request.Method.POST, ConnectionProperties.getLoginUrl(), String.class, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //TODO
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO

            }
        },headers ,true);
        MyApplication.getInstance().addToRequestQueue(request);

    }

    public void onLoginClick(View view) {
        String username = viewUsernameField.getText().toString();
        String password = viewPasswordField.getText().toString();

        Log.i("_tag", "username:" + username + " password:" + password);

        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }
}
