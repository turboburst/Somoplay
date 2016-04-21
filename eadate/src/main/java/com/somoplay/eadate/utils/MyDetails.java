package com.somoplay.eadate.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.somoplay.eadate.App;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Frank on 16-01-07.
 */
public class MyDetails {
    private Context context=null;

    private static final String userUrl = "http://ec2-52-4-197-144.compute-1.amazonaws.com:8080/api/mydetail";
   private String password;
    private String jsonString;
    private String email;
    private  String userName;
    private  String access_token;
    private  String token_type;
    private int adminId, userId;
    private String loginStatus;

    public SharedPreferences pref;

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


    public void sendStringRequest(final String access_token,final String token_type) {
        this.access_token=access_token;
        this.token_type=token_type;

        String searchUrl;

            searchUrl = userUrl;

        StringRequest postReq = new StringRequest(Request.Method.GET, searchUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        jsonString=response;
                        new ParseJson().execute();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("+++++++++++++++++++++++++++++++Error ["+error.getMessage()+"]");
                    }
                }
        )
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Accept","application/json");
                params.put("Authorization",token_type + " " + access_token);
//                params.put("Authorization","Bearer d218acc9-6109-49a5-93fa-1dc013768dfb");
                return params;
            }

        };
        App.getInstance().addToRequestQueue(postReq);
    }

    //get Strings from service:
    public void generateObjectFromJson(String jsonString) {

        try{
            JSONObject jsonObject = new JSONObject(jsonString);
            String accessToken=jsonObject.getString("access_token");
            String tokenType=jsonObject.getString("token_type");
            String refreshToken=jsonObject.getString("refresh_token");
            String expiresIn=jsonObject.getString("expires_in");
            String scope=jsonObject.getString("scope");
//            if(type.equals("dictionary")){
//                JSONObject data = jsonObject.getJSONObject("data");
//                loginStatus= jsonObject.getString("status");
//                    userId=data.getInt("user_id");
//                    adminId = 0;

//                pref = loginActivity.getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = pref.edit();

                editor.putInt("admin_id", adminId);
                editor.putInt("user_id", userId);
                editor.commit();

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
        protected Void doInBackground(Void... params) {
            generateObjectFromJson(jsonString);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        }
    }
}
