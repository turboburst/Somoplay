package com.somoplay.eadate.webrequest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.somoplay.eadate.App;
import com.somoplay.eadate.activity.LoginActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Frank on 16-01-07.
 */
public class LoginRequest {
    private Context context=null;
    private LoginActivity loginActivity = null;

    private static final String userUrl = "http://eadate.com:8080/screenshow/restful/user/search";
//    private static final String userUrl = "ec2-52-4-197-144.compute-1.amazonaws.com:8080/rest";

    private static final String adminUrl = "http://eadate.com:8080/screenshow/restful/admin/search";
    private String password;
    private String jsonString;
    private String email;
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

    public LoginRequest(LoginActivity loginActivity) {
        super();
        this.loginActivity = loginActivity;
    }

    public void sendStringRequest(final String email,final String password) {
        this.email=email;
        this.password=password;

        String searchUrl;

        if (loginActivity.getPassedAdminId() == 1) {
            searchUrl = adminUrl;
        }
        else {
            searchUrl = userUrl;
        }

        StringRequest postReq = new StringRequest(Request.Method.POST, searchUrl,
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
                        System.out.println("+++++++++++++++++++++++++++++++Error ["+error+"]");
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("searchtype", "login");
                params.put("email", email);
                params.put("password",password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        App.getInstance().addToRequestQueue(postReq);
    }

    //get Strings from service:
    public void generateObjectFromJson(String jsonString) {

        try{
            JSONObject jsonObject = new JSONObject(jsonString);
            String type=jsonObject.getString("type");
            if(type.equals("dictionary")){
                JSONObject data = jsonObject.getJSONObject("data");
                loginStatus= jsonObject.getString("status");
                if (loginActivity.getPassedAdminId()==1) {
                    adminId=data.getInt("admin_id");
                    userId = 0;
                }
                else {
                    userId=data.getInt("user_id");
                    adminId = 0;
                }

                pref = loginActivity.getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = pref.edit();

                editor.putInt("admin_id", adminId);
                editor.putInt("user_id", userId);
                editor.commit();

                this.loginActivity.afterGetStatus(type, loginActivity);
            }
            else if(type.equals("message")){
                loginActivity.afterGetStatus(type, loginActivity);

            }

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
