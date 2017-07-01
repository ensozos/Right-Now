package com.auth.csd.rightnow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.auth.csd.rightnow.controller.GsonRequest;
import com.auth.csd.rightnow.utils.ConnectionProperties;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class LoginActivity extends AppCompatActivity {
    public static final String PREFS_FILE = "app_prefs";
    public static final String REMEMBER_USERNAME_KEY = "remember_username";

    private EditText viewUsernameField;
    private EditText viewPasswordField;
    private CheckBox viewRememberUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        viewUsernameField = (EditText) findViewById(R.id.username_field);
        viewPasswordField = (EditText) findViewById(R.id.password_field);
        viewRememberUsername = (CheckBox) findViewById(R.id.remember_username);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);

        if (sharedPref.getString(REMEMBER_USERNAME_KEY, null) != null) {
            viewUsernameField.setText(sharedPref.getString(REMEMBER_USERNAME_KEY, ""));
            viewPasswordField.requestFocus();
            viewRememberUsername.setChecked(true);
        }
    }

    public void onLoginClick(View view) {
        final String username = viewUsernameField.getText().toString();
        final String password = viewPasswordField.getText().toString();
        final ProgressDialog dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);
        final Intent mainIntent = new Intent(this, MainActivity.class);
        final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);

        StringRequest request = new StringRequest(Request.Method.POST,
                ConnectionProperties.getLoginUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("VOLLEY", response);
                        dialog.cancel();

                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            if (jsonResponse.getBoolean("error")) {
                                LoginActivity.this.alert("Error", jsonResponse.getString("response_msg"));
                                return;
                            }

                            if (LoginActivity.this.viewRememberUsername.isChecked()) {
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString(REMEMBER_USERNAME_KEY, username);
                                editor.commit();
                            }

//                            String sid = jsonResponse.getString("sid");
                            String sid = "some_random_string";
                            startActivity(mainIntent);
                        } catch (JSONException e) {
                            LoginActivity.this.alert("Error", "Invalid response from server");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("VOLLEY", "ERROR " + error.getMessage());
                        dialog.cancel();

                        LoginActivity.this.alert("Error", "Server responsed with error");
                    }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> postData = new HashMap<String, String>();
                postData.put("username", username);
                postData.put("pass", password);
                return postData;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }
        };

        MyApplication.getInstance().addToRequestQueue(request);
    }

    public void alert(String title, String text) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        alertDialog.setTitle(title);
        alertDialog.setMessage(text);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
    }

}
