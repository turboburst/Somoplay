package com.somoplay.eadate.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.somoplay.eadate.R;
import com.somoplay.eadate.utils.SignupRequest;


public class SignUpActivity extends ActionBarActivity {

    protected EditText mUsername;
    protected EditText mFirstname;
    protected EditText mLastname;
    protected EditText mEmail;
    protected EditText mPassword;
    protected EditText mNewPassword;
    protected EditText mAdmin;
    protected Button mSignUpButton;
    protected RelativeLayout signUpBack;
    SignupRequest signupRequest=new SignupRequest();

    private SharedPreferences mSharedPreferences;

//    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // get db objects
//        db = new Database(this);

        mUsername = (EditText) findViewById(R.id.usernameField);
        mUsername.setHintTextColor(Color.rgb(192,192,192));
        mFirstname=(EditText)findViewById(R.id.firstNameField);
        mFirstname.setHintTextColor(Color.rgb(192,192,192));
        mLastname=(EditText)findViewById(R.id.lastNameField);
        mLastname.setHintTextColor(Color.rgb(192,192,192));
        mEmail = (EditText) findViewById(R.id.emailField);
        mEmail.setHintTextColor(Color.rgb(192,192,192));
        mPassword = (EditText) findViewById(R.id.passwordField);
        mPassword.setHintTextColor(Color.rgb(192,192,192));
        mNewPassword=(EditText)findViewById(R.id.newPasswordField);
        mNewPassword.setHintTextColor(Color.rgb(192,192,192));
        mSignUpButton = (Button) findViewById(R.id.signupButton);
        mSignUpButton.setBackgroundColor(Color.argb(127, 58, 86, 213));
        signUpBack=(RelativeLayout)findViewById(R.id.signUpBack);
        signUpBack.setBackgroundColor(Color.argb(255,57,86,146));
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUsername.getText().toString();
//                String firstname = mFirstname.getText().toString();
//                String lastname = mLastname.getText().toString();
                String password = mPassword.getText().toString();
                String email = mEmail.getText().toString();
                String newpassword = mNewPassword.getText().toString();

//                String username = "FrankLyn";
                String firstname = "Frank";
                String lastname = "Lin";
//                String password = "123456";
//                String email = "707648286@qq.com";
//                String newpassword = "123456";

                if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setMessage("Please make sure you enter a username, password, and email address.")
                            .setTitle("Oops!")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else if(!password.equals(newpassword)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setMessage("Please make sure you enter same password.")
                            .setTitle("Oops!")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else if(password.length() < 5){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setMessage("Please make sure the password has to be more than 3 digits.")
                            .setTitle("Oops!")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    signupRequest.sendStringRequest(username, firstname, lastname, password, email, newpassword,"0");

                    mSharedPreferences=getApplicationContext().getSharedPreferences("PRO", 0);
                    String stauts = mSharedPreferences.getString("status", "");
                    String title = mSharedPreferences.getString("title", "");
                    String content = mSharedPreferences.getString("content", "");
                    if (stauts.equals("error") && title.equals("Error")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                        builder.setMessage(content + "use another email and try again")
                                .setTitle("Oops!")
                                .setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    else {
//                        Toast.makeText(getApplicationContext(),
//                                "Congratulations!Sign up successfully!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    // this is where we will create the new user
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
}
