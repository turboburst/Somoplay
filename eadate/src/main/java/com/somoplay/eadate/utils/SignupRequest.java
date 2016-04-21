package com.somoplay.eadate.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;
import com.somoplay.eadate.App;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by JudyShuai on 15-07-08.
 */
public class SignupRequest {
    public static final String TAG = "SignupRequest";
    private Context context = null;

    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String newPassword;
    private String admin;
    private int timeOfDownloadImages;

    private String jsonString;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    //        String searchUrl = "http://www.baidu.com/";
    String searchUrl = "http://ec2-52-4-197-144.compute-1.amazonaws.com:8080/api/register";
    JsonObject jsonObject = new JsonObject();

    String json;




    public void sendStringRequest(final String userName, final String firstName, final String lastName, final String password,
                                  final String email, final String newPassword, final String admin) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.newPassword = newPassword;
        this.admin = admin;

        jsonObject.addProperty("email",email);
        jsonObject.addProperty("login",userName);
        jsonObject.addProperty("password", password);

        json =  jsonObject.toString();
        Log.d(TAG, json);

        new ParseJson().execute();
    }
    public static String makeRequest(String uri, String json){
        HttpURLConnection urlConnection;
        String url;
        String data = json;
        String result = null;
        try {
            //Connect
            urlConnection = (HttpURLConnection) ((new URL(uri).openConnection()));
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept","text/plain");
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();

            //Write
            OutputStream outputStream = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(data);
            writer.close();
            outputStream.close();

            //Read
            int respCode=urlConnection.getResponseCode();
            if (respCode >= 400) {
                if (respCode == 404 || respCode == 410) {
                    throw new FileNotFoundException(urlConnection.toString());
                } else {
                    throw new java.io.IOException(
                            "Server returned HTTP"
                                    + " response code: " + respCode
                                    + " for URL: " + urlConnection.toString());
                }
            }else {
                InputStream respone = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(respone, "UTF-8"));

                String line;
                StringBuilder sb = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                result = sb.toString();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
//    public void sendStringRequest(final String userName, final String firstName, final String lastName, final String password,
//                                  final String email, final String newPassword, final String admin) {
//        this.userName = userName;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//        this.password = password;
//        this.newPassword = newPassword;
//        this.admin = admin;
//     /*   String username = "FrankLyn";
//        String firstname = "Frank";
//        String lastname = "Lin";
//        String password = "123456";
//        String email = "707648286@qq.com";
//        String newpassword = "123456";*/
//
////        String searchUrl = "http://www.baidu.com/";
//        String searchUrl = "http://ec2-52-4-197-144.compute-1.amazonaws.com:8080/api/register";
////        final Map<String, String> params1 = new HashMap<String, String>();
////        params1.put("email", email);
////        params1.put("login", userName);
////        params1.put("password", password);
////        params1.put("Content-Type","application/json");
//
//
//        StringRequest postReq = new StringRequest(Request.Method.POST ,searchUrl,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        System.out.println(response);
//                        jsonString=response;
//                        new ParseJson().execute();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        System.out.println("+++++++++++++++++++++++++++++++Error [" + error + "]");
//                    }
//                }
//        )
//        {
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                /*params.put("searchtype", "add");
//                params.put("user_name", userName);
//                params.put("first_name", firstName);
//                params.put("last_name", lastName);
//                params.put("email", email);
//                params.put("password", password);
//                params.put("newpassword", newPassword);
//                params.put("admin",admin);*/
//                params.put("email", email);
////                params.put("firstName", firstName);
////                params.put("langKey", "en");
////                params.put("lastName", lastName);
//                params.put("password", password);
//                params.put("login", userName);
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String,String> params = new HashMap<String, String>();
//                params.put("Accept","text/plain");
//                params.put("Content-Type","application/json");
////                params.put("Authorization","Bearer d218acc9-6109-49a5-93fa-1dc013768dfb");
//                return params;
//            }
//
//        };
//        App.getInstance().addToRequestQueue(postReq);
//    }

    public void generateObjectFromJson(String jsonString) {

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String type = jsonObject.getString("type");
            if (type.equals("message")) {
                String stauts = jsonObject.getString("status");
                String title = jsonObject.getString("title");
                String content = jsonObject.getString("content");

                mSharedPreferences = App.getInstance().getSharedPreferences("PRO", 0);
                mEditor = mSharedPreferences.edit();
                mEditor.putString("status", stauts);
                mEditor.putString("title", title);
                mEditor.putString("content", content);
                mEditor.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class ParseJson extends AsyncTask<Void, Void, Void> {

        /**
         * Runs on the UI thread before {@link #doInBackground}.
         *
         * @see #onPostExecute
         * @see #doInBackground
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {
            System.out.print(makeRequest(searchUrl, json));
//            generateObjectFromJson(json);
            return null;
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param aVoid The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
