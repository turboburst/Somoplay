package com.somoplay.eadate.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.somoplay.eadate.R;
import com.somoplay.eadate.utils.MyDetails;
import com.somoplay.eadate.utils.UserList;

/**
 * Created by Frank on 16/2/10.
 */
public class MainActivity2 extends Activity {

    private Button myDetail, userlist;
    MyDetails myDetailsRequest;
    UserList userListRequest;
    private Intent intent;

    private String access_token;
    private String token_type;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        //Intent传递第参数，用于暂时获取token
//        intent = getIntent();
//        access_token = intent.getStringExtra("access_token");
//        token_type = intent.getStringExtra("token_type");

        //sharedpreferences
        mSharedPreferences = getApplicationContext().getSharedPreferences("LOGIN", 0);
        String isError = mSharedPreferences.getString("error", "");
        if (isError.equals("")) {
            access_token = mSharedPreferences.getString("access_token", "");
            token_type = mSharedPreferences.getString("token_type", "");
//            String refresh_token = mSharedPreferences.getString("refresh_token", "");
//            String expires_in = mSharedPreferences.getString("expires_in", "");
//            String scope = mSharedPreferences.getString("scope", "");
        }

        myDetail = (Button) findViewById(R.id.mydetail);
        userlist = (Button) findViewById(R.id.userlist);
        myDetailsRequest = new MyDetails();
        userListRequest = new UserList();

        myDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDetailsRequest.sendStringRequest(access_token, token_type);

            }
        });

        userlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userListRequest.sendStringRequest(access_token, token_type);
            }
        });
    }
}
