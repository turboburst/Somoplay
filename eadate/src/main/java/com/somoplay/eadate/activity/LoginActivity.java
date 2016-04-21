package com.somoplay.eadate.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.somoplay.eadate.R;
import com.somoplay.eadate.utils.LoginRequest2;


public class LoginActivity extends AppCompatActivity {

    protected EditText mEmail;
    protected EditText mPassword;
    protected Button mLoginButton;
    protected Button mAboutButton;
    public LoginRequest2 loginRequest2;
    protected ImageButton checkButton;
    protected LinearLayout loginBack;
    public int passedAdminId=0;
    private int adminId, userId;
    private boolean admin_or_user_fail;

    private SharedPreferences mSharedPreferences;

    private int colorChanged=0;

    protected TextView mSignUpTextView, mForgetPasswordTextView;

    private static final String TAG = LoginActivity.class.getSimpleName();

    public SharedPreferences pref;

    public int getPassedAdminId() {
        return passedAdminId;
    }

    public void setPassedAdminId(int passedAdminId) {
        this.passedAdminId = passedAdminId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // get db objects
        mSignUpTextView = (TextView) findViewById(R.id.signUpText);
        mForgetPasswordTextView = (TextView) findViewById(R.id.forgetPasswordText);
        loginBack=(LinearLayout)findViewById(R.id.loginBackground);
        loginBack.setBackgroundColor(Color.argb(255,57,86,146));
        loginRequest2 =new LoginRequest2();
        checkButton=(ImageButton)findViewById(R.id.checkImageButton);
        checkButton.setImageResource(R.drawable.uncheck);
        mEmail= (EditText) findViewById(R.id.emailLoginField);
        mEmail.setHintTextColor(Color.rgb(192, 192, 192));
        mPassword = (EditText) findViewById(R.id.passwordField);
        mPassword.setHintTextColor(Color.rgb(192, 192, 192));
        mLoginButton = (Button) findViewById(R.id.loginButton);
        mAboutButton = (Button) findViewById(R.id.aboutButton);

        mSignUpTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        mForgetPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // open link in app
//                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
//                startActivity(intent);

                // open link in android browser app
//                String url = "http://eadate.com:8080/screenshow/login/forgetUserPassword";
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                startActivity(i);
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage(R.string.login_error_message)
                            .setTitle(R.string.login_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else
                {
                    loginRequest2.sendStringRequest(LoginActivity.this, username, password);
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();

                }
            }
        });

        mAboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
//                Intent intent = new Intent(LoginActivity.this,AboutActivity.class);
//                startActivity(intent);
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (colorChanged==0){
                    checkButton.setImageResource(R.drawable.check);
                    colorChanged=1;
                    passedAdminId=1;

                }else if(colorChanged==1)
                {
                    checkButton.setImageResource(R.drawable.uncheck);
                    colorChanged=0;
                    passedAdminId=0;
                }
            }
        });
    }
    public  void showToast(final String toast){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showDialog(final boolean loginFail) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                String loginMessage;
                if (loginFail) {
                    loginMessage = "Admin Login Failed!";
                }
                else {
                    loginMessage = "User Login Failed!";
                }

                builder.setMessage("Wrong username or password")
                        .setTitle(loginMessage)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);}

    public void afterGetStatus(String status, Activity incomingActivity) {
        if(status.equals("dictionary")){
            Intent intent = new Intent(this, MainActivity.class);
            pref = this.getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);

//            SharedPreferences.Editor editor = pref.edit();
            adminId = pref.getInt("admin_id", 0);
            userId = pref.getInt("user_id", 0);

            intent.putExtra("EXTRA_MESSAGE",passedAdminId);
            finish();
        }
        else {
            if (passedAdminId == 1) {
                admin_or_user_fail = true;
                showDialog(admin_or_user_fail);
            }
            else {
                admin_or_user_fail = false;
                showDialog(admin_or_user_fail);
            }
        }
    }

}
