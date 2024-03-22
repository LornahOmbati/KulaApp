package com.example.kulaapp.Services;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.kulaapp.Activities.LoginActivity;
import com.example.kulaapp.Utils.AESHelper;
import com.example.kulaapp.Utils.DbHelper;
import com.example.kulaapp.Utils.GlobalVariables;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MyServiceLogin extends Service {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private static final String surl = GlobalVariables.surl;
    private static int startidserv;
    AESHelper aesHelper;
    JSONObject jsonjob = new JSONObject();
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static String serverAuth;
    String siqili,move;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {


        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler                                    //1
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();

        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        //Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.

            startidserv = msg.arg1;
            aesHelper = new AESHelper(getApplicationContext());

            String url = surl+"SystemAccounts/Authentication/Login/Submit";
            try {
                postData(url,Login().toString(),"JobLogin");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public JSONObject Login(){

        String url = surl+"SystemAccounts/Authentication/Login/Submit";
        JSONObject json = new JSONObject();
        JSONObject json1 = new JSONObject();
        JSONObject jsonurl = new JSONObject();

        try{

            json.put("PassWord", "admin123");
            json.put("UserName", "admin");

            //to return if login page exists
//            json.put("PassWord", LoginActivity.password1);
//            json.put("UserName", LoginActivity.user1);

            json.put("Language", "English");
            jsonurl.put("url", url);
            jsonjob.put("httpjob","JobLogin");
            json1.put("CurrentUser", json);
            json1.put("Language", "English");
            json1.put("IsRenewalPasswordRequest", false);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject[] job = {json1,jsonurl, jsonjob};
        Log.v("KKKKKK","LLLLLLLLLLLLLL "+job);

        return json1;

    }

    public void postData(String url, String json,String http_job) throws IOException {
        Log.v("jjjjjjj","jjjjjj "+json);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.writeTimeout(60, TimeUnit.SECONDS);
        client = builder.build();

        RequestBody body = RequestBody.create(json, JSON);
        Request request = null;
        request = new Request.Builder()
                .url(url)
                .header("User-Agent", "OkHttp Headers.java")
                .addHeader("Content-Type", "application/json;")
                .post(body)
                .build();


        if (!http_job.matches("JobLogin")) {
            request = new Request.Builder()
                    .url(url)
                    .header("User-Agent", "OkHttp Headers.java")
                    .addHeader("Content-Type", "application/json;")
                    .addHeader("Authorization", serverAuth)
                    .post(body)
                    .build();
        }
        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Log.v("kkkkkkk", "kkkk "+e.getLocalizedMessage());
                jobDone("error", "error");
                e.printStackTrace();
            }
            @Override public void onResponse(Call call, Response response) throws IOException {
                //try (ResponseBody responseBody = response.body()) {
                try {
                    if (!response.isSuccessful()) {
                        ResponseBody error_responce = response.peekBody(Long.MAX_VALUE);
                        //Log.v("RESPONSE K==", error_responce.string());
                        jobDone(error_responce.string(), "failed");
                        throw new IOException("Unexpected code " + response);
                    }

                    System.out.println("Server: " + response.header("Authorization"));
                    System.out.println("Date: " + response.header("Date"));

                    serverAuth = response.header("Authorization");

                    ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);

                    jobDone(responseBodyCopy.string(), http_job);
                } catch (Exception e) {
                    Log.v("HERE FAIL", e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        });
    }


    public void jobDone(String httpjobresponse, String httpjob){

        String SW = httpjob;

        switch (SW)
        {
            case "JobLogin":
                // TODO Auto-generated method stub

                try {
                    JSONObject jObj = new JSONObject(httpjobresponse);
                    JSONObject jMembers = jObj.getJSONObject("Result");
                    final JSONObject jMemberDetails = jMembers.getJSONObject("Result");

                    Log.v("jobLogin", jObj.toString());

                    int user_id = jMemberDetails.getInt("id");

                    ContentValues memberMap = new ContentValues();
                    ContentValues logMap = new ContentValues();

                    logMap.put("user_id", jMemberDetails.getInt("id"));
                    memberMap.put("rid", String.valueOf(jMemberDetails.getInt("id")));
                    memberMap.put("username", jMemberDetails.getString("username"));
                    memberMap.put("password", jMemberDetails.getString("password"));
                    memberMap.put("status_id", jMemberDetails.getBoolean("status_id"));
                    memberMap.put("account_id", jMemberDetails.getInt("account_id"));

                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("com.example.safety_cap", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();

                    editor.putString("user_model", jMemberDetails.getString("username"));

                    if(GlobalVariables.is_encrypted){
                        String accounts = jMemberDetails.getJSONObject("sub_accounts").getJSONObject("account").getString("contact_person");

                        editor.putString("move", move);
                        editor.putString("siqili", siqili);
                    }
                    else{
                        editor.putString("move", move);
                        editor.putString("siqili", siqili);
                    }
                    editor.putString("pass", LoginActivity.password1);
                    editor.apply();


                    int updatingID = jMemberDetails.getInt("id");
                    Log.i("???>>>>>>  ", user_id+"+++++++++++"+updatingID);

                    //Check If User Exists
                    new DbHelper(getApplicationContext()).updateUser(memberMap, updatingID, logMap);

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                LoginActivity.stopPD();
                                Toast.makeText(getApplicationContext(), "Welcome "+jMemberDetails.getString("username")+" ", Toast.LENGTH_LONG).show();
                                new DbHelper(getApplicationContext()).log_event(getApplicationContext(),"User Online Authenticated");
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });


                    this.stopSelf();

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.i("???>>>>>>  ", "+++++++++++"+e.getMessage());

                }

                break;

            case "failed":
                try{
                    Log.v("JJJJ","HERE AS THE RESPONSE = "+httpjobresponse.toString());
                    JSONObject jObj = new JSONObject(httpjobresponse);
                    final JSONObject jMemberss = jObj.getJSONObject("Result");

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                LoginActivity.stopspinning1();
                                Toast.makeText(MyServiceLogin.this, "Invalid username and password", Toast.LENGTH_SHORT).show();
                                new DbHelper(getApplicationContext()).log_event(getApplicationContext(),"User Online Authenticated");
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });

                    this.stopSelf();
                }catch(Exception e){
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            LoginActivity.stopspinning1();
                            Toast.makeText(getApplicationContext(), "CONNECTION PROBLEM OR INVALID URL", Toast.LENGTH_LONG).show();
                        }
                    });
                    e.printStackTrace();
                    this.stopSelf();
                }

                break;

            case "error":
                try{
                    Log.v("JJJJ","HERE AS THE RESPONSE = "+httpjobresponse.toString());

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                LoginActivity.stopspinning1();
                                Toast.makeText(getApplicationContext(), "Time out Connection", Toast.LENGTH_LONG).show();
                                new DbHelper(getApplicationContext()).log_event(getApplicationContext(),"User Online Authenticated");
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });

                    this.stopSelf();
                }catch(Exception e){
                    e.printStackTrace();
                    this.stopSelf();
                }

                break;


            default:

                break;
        }

    }

}
