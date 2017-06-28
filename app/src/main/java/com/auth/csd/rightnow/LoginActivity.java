package com.auth.csd.rightnow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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
    }

    public void onLoginClick(View view) {
        String username = viewUsernameField.getText().toString();
        String password = viewPasswordField.getText().toString();

        Log.i("_tag", "username:" + username + " password:" + password);

        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }
}
