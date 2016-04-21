package com.somoplay.eadate.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.somoplay.eadate.App;
import com.somoplay.eadate.activity.MainActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Frank on 16-01-07.
 */
public class LoginRequest2 {
    private Context context;

    //private static final String userUrl = "http://ec2-52-4-197-144.compute-1.amazonaws.com:8080/oauth/token";
//    private static final String userUrl = "ec2-52-4-197-144.compute-1.amazonaws.com:8080/rest";
    private static final String userUrl = "http://eadate.com/oauth/token";

    private static final String adminUrl = "http://eadate.com:8080/screenshow/restful/admin/search";
    private String password;
    private String jsonString;
    private String email;
    private String userName;
    private int adminId, userId;
    private String loginStatus;
    private RequestFuture<String> future;

    public SharedPreferences mSharedPreferences;

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public void sendStringRequest(final Context context,final String userName,final String password) {
        this.userName=userName;
        this.password=password;
        this.context=context;

        String searchUrl;

        searchUrl = userUrl;

        future = RequestFuture.newFuture();

        StringRequest postReq = new StringRequest(Request.Method.POST,searchUrl,future,future)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", userName);
                params.put("password", password);
                params.put("grant_type", "password");
                params.put("scope", "read write");
                params.put("client_secret", "mySecretOAuthSecret");
                params.put("client_id", "eadateapp");
                return params;
            }

        };
        App.getInstance().addToRequestQueue(postReq);
        new ParseJson().execute();
    }

    //get Strings from service:
    public void generateObjectFromJson(String jsonString) {

        try{

            if (!jsonString.equals("error") && jsonString != null) {
                JSONObject jsonObject = new JSONObject(jsonString);
                Log.d("JsonString object", jsonObject.toString());
                String accessToken = jsonObject.getString("access_token");
                Log.d("LoginRequest2",accessToken);
                String tokenType = jsonObject.getString("token_type");
                String refreshToken = jsonObject.getString("refresh_token");
                String expiresIn = jsonObject.getString("expires_in");
                String scope = jsonObject.getString("scope");

//              pref = loginActivity.getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
                mSharedPreferences = context.getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
                mSharedPreferences.edit().putString("access_token", accessToken);
                mSharedPreferences.edit().putString("token_type", tokenType);
                mSharedPreferences.edit().putString("refresh_token", refreshToken);
                mSharedPreferences.edit().putString("expires_in", expiresIn);
                mSharedPreferences.edit().putString("scope", scope);
                if(mSharedPreferences.edit().commit()) {
                    SharedPreferences sp = context.getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
                    String token = sp.getString("access_token","");

                    Log.d("LoginRequest2", "SharedPreferences commit success, token is "+token);
                }
            }
            else {
                mSharedPreferences = App.getInstance().getSharedPreferences("LOGIN", 0);
                mSharedPreferences.edit().putString("error","error");
                mSharedPreferences.edit().commit();
            }
//                this.loginActivity.afterGetStatus(type, loginActivity);
//            }
//            else if(type.equals("message")){
////                loginActivity.afterGetStatus(type, loginActivity);
//
//            }

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    class ParseJson extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                jsonString = future.get(3,TimeUnit.SECONDS);
                System.out.println(jsonString.toString());
                Log.d("LoginRequest",jsonString);
                //future.get(timeout, unit)
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            generateObjectFromJson(jsonString);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(jsonString == null)
            {
                Toast.makeText(context, "Wrong username or passoword", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Log.d("JsonString", jsonString);
                Log.d("JsonString username", userName);
                Log.d("JsonString password", password);
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }


        }
    }
}
