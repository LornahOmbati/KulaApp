package com.example.kulaapp.Services;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.kulaapp.Activities.MainActivity;
import com.example.kulaapp.Fragments.ProfileFragment;
import com.example.kulaapp.Utils.AESHelper;
import com.example.kulaapp.Utils.DbHelper;
import com.example.kulaapp.Utils.GlobalVariables;
import com.example.kulaapp.Utils.dataretriever;

import net.sqlcipher.database.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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

public class MyServiceIntent extends Service {

    Looper mServiceLooper;
    ServiceHandler mServiceHandler;
    HandlerThread thread;
    int startidserv;
    AESHelper aesHelper;
    DbHelper doa;
    private static final String surl = GlobalVariables.surl;
    JSONObject jsonjob = new JSONObject();
    JSONObject json1 = new JSONObject();

    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    String serverAuth;

    public static boolean is_passwordhashing = true;
    String url;
    String HTTPjob;
    String Params;
    long dComparer;
    int take = 1000;
    int skip = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        //startForeground(1,new Notification());
        Log.v("BUILD VERSION ", Build.VERSION.SDK_INT + " SDK INT -- AND CODE " + Build.VERSION_CODES.O);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.v("JJJJJ", "JERE JERE");
            startMyOwnForeground();
        } else {
            Log.v("JJJJJ", "OTHER START JERE JERE");
            startForeground(1, new Notification());
        }
        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    private void startMyOwnForeground() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String NOTIFICATION_CHANNEL_ID = "com.example.kulaapp";
            String channelName = "My Background Service";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            Notification notification = notificationBuilder.setOngoing(true)
                    //.setSmallIcon(AppSpecific.SMALL_ICON)
                    .setContentTitle("App is running in background")
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            startForeground(2, notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

//        if (GlobalVariabless.is_new_system) {
//            WeightCAPTURE = "WeightCAPTURE/";
//        } else {
//            WeightCAPTURE = "";
//        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {

    }

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            super.handleMessage(msg);
            Log.e("KKKKKKK", "KKKKKKKKKKKKKK HERE ServiceHandler");
            startidserv = msg.arg1;
            aesHelper = new AESHelper(getApplicationContext());

            String DB_PATH = getExternalFilesDir(null).getAbsolutePath() + "/";

            String path_encrypted = DB_PATH + "kula.db";

            SQLiteDatabase.loadLibs(getApplicationContext());
            doa = new DbHelper(getApplicationContext(), (Activity) getApplicationContext());

            String url = surl + "SystemAccounts/Authentication/Login/Submit";
            try {
                postData(url, Login().toString(), "JobLogin");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public JSONObject Login() {

        String url = surl + "SystemAccounts/Authentication/Login/Submit";
        JSONObject json = new JSONObject();
        JSONObject json1 = new JSONObject();
        JSONObject jsonurl = new JSONObject();

        try {
            dataretriever data = new dataretriever();

//            data.password = getApplicationContext().getSharedPreferences("com.example.safety_cap", Context.MODE_PRIVATE).getString("pass", "");
//            data.username = getApplicationContext().getSharedPreferences("com.example.safety_cap", Context.MODE_PRIVATE).getString("user_model", "");

            data.password = "admin123";
            data.username = "admin";


            if (GlobalVariables.is_pps.equalsIgnoreCase("Yes")) {

                json.put("PassWord", data.password);
                json.put("UserName", data.username);
                json.put("Language", "English");
                jsonurl.put("url", url);
                jsonjob.put("httpjob", "JobLogin");
                json1.put("CurrentUser", json);
                json1.put("Language", "English");
                json1.put("IsRenewalPasswordRequest", false);

            } else {

                json.put("PassWord", data.password);
                json.put("UserName", data.username);
                json.put("AccountName", data.account_name);
                json.put("Branch", data.branch);
                json.put("Language", "English");
                jsonurl.put("url", url);
                jsonjob.put("httpjob", "JobLogin");
                json1.put("CurrentUser", json);
                json1.put("Language", "English");
                json1.put("IsRenewalPasswordRequest", false);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject[] job = {json1, jsonurl, jsonjob};

        return json1;

    }


    public void postData(String url, String json, String http_job) throws IOException {
        Log.v(http_job + "====", "MyIntentService POST OBJECT DATA " + json);

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
            @Override
            public void onFailure(Call call, IOException e) {
                try {
                    jobDone("error", "error");
                } catch (JSONException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //try (ResponseBody responseBody = response.body()) {
                try {
                    if (!response.isSuccessful()) {

                        ResponseBody error_responce = response.peekBody(Long.MAX_VALUE);

                        jobDone(error_responce.string(), http_job);
                        throw new IOException("Unexpected code " + response);
                    }


                    serverAuth = response.header("Authorization");

                    ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);

                    jobDone(responseBodyCopy.string(), http_job);
                } catch (Exception e) {
                    Log.e("FFFFFFFFF", "kkkkkkkkkkkkkkk" + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressLint({"NewApi", "LongLogTag"})
    public void jobDone(String httpjobresponse, String httpjob) throws JSONException, IOException{
        String SW = httpjob;

        try {
            Log.v(SW, new JSONObject(httpjobresponse).toString());
        } catch (Exception e) {
            endService("ERROR");
            Log.v(SW, httpjobresponse);
        }

        JSONObject jObj = new JSONObject(httpjobresponse);

        Log.v(SW, jObj.toString());

        switch (SW) {

            case "JobGetRecipes":

                int recipes = 0;

                Log.e("RECIPES OBJ==>>", ": "+ jObj.toString());

                try {
                    if (!jObj.getString("StatusCode").endsWith("603")){

                        if (!jObj.getJSONObject("Result").getString("Result").equals("null")) {

                            recipes = jObj.getJSONObject("Result").getJSONArray("Result").length();

                            doa.InsertRecipes(jObj.getJSONObject("Result").getJSONArray("Result"), getApplicationContext());

                            Log.e("+++++++", "recipes: " + recipes);

                        }
                    }
                }catch (Exception e){
                    Log.e("Catch:: ", "ERR--->>>" + e.getMessage());
                    e.printStackTrace();
                }

            break;

            case "jobSendRecipes":
                try {
                    Log.e("SEND RECIPES RESPONSE ", "" + jObj.toString());

//                    if (jObj.getString("StatusCode").equals("200") || jObj.getString("StatusCode").equalsIgnoreCase("201")) {

                        ContentValues cv = new ContentValues();
                        cv.put("sync_status", "i");
                        cv.put("sid", jObj.getJSONObject("Result").getString("id"));

                        String WhereClause = "id_no = ?";
                        String[] WhereArgs = {"" + jObj.getJSONObject("Result").getString("nat_id")};

                        doa.db.update("drivers", cv, WhereClause, WhereArgs);

//                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                sendRecipes();
            break;

            case "jobAddDriverResponses":
                try {
                    Log.e("SEND DRIVERS ANSW. RESPONSE ", "" + jObj.toString());

                    if (jObj.getString("StatusCode").equals("200") || jObj.getString("StatusCode").equalsIgnoreCase("201")) {

                        String quizId = "";
                        String lessonId = "";
                        String driver_id = "";
                        JSONArray resultArray = jObj.getJSONArray("Result");

                        for(int i = 0; i < resultArray.length(); i++){
                            quizId = resultArray.getJSONObject(i).getString("quizId");
                            lessonId = resultArray.getJSONObject(i).getString("lessonId");
                            driver_id = resultArray.getJSONObject(i).getString("driver_id");
                        }

                        ContentValues cv = new ContentValues();
                        cv.put("sync_status", "i");

                        String WhereClause = "quiz_id = ? AND lessonId = ? AND driver_id = ?";
                        String[] WhereArgs = {"" +quizId, ""+lessonId, ""+driver_id};

                        doa.db.update("user_answers_given", cv, WhereClause, WhereArgs);

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            break;

        }
    }

    private void sendRecipes()  throws IOException, JSONException {

        int pending_registered_drivers = doa.countPendingRecipes();

        Log.v("=====>>>>>>", " pending_registered_drivers " + pending_registered_drivers);

        if (pending_registered_drivers > 0){

            JSONObject lpo = doa.recipesToSync();

            Log.v("=====>>>>>>", "REGISTERED DRIVERS OBJ: " + lpo);
            url = surl + "SafetyCapture/Mobileservices/AddDriver";
            HTTPjob = "jobSendRegisteredDrivers";
            Params = lpo.toString();

            if (lpo.length() > 0){
                postData(url, lpo.toString(), "jobSendRegisteredDrivers");
            }

        } else {
        }
    }

    public void endService(final String message) {
        deleteCache(getApplicationContext());
        Log.e("hehehehehehe", "jjjjjjjjjjjjjjj" + GlobalVariables.service_start_mode);

        if (GlobalVariables.service_start_mode == null) {

        } else if (GlobalVariables.service_start_mode.equalsIgnoreCase("manual")) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    MainActivity.stopPD(message);
                    GlobalVariables.the_service_start = "stop";

                }
            });


        } else if (GlobalVariables.service_start_mode.equalsIgnoreCase("manual_fetch_lessons")) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    ProfileFragment.stopPD2(message);
                    GlobalVariables.the_service_start = "stop";

                }
            });


        }

//        else if (GlobalVariables.service_start_mode.equalsIgnoreCase("manual_fetch_lessons_quizes")) {
//            new Handler(Looper.getMainLooper()).post(new Runnable() {
//                @Override
//                public void run() {
//                    DriversLessonsQuizesActivity.stopPD(message);
//                    GlobalVariables.the_service_start = "stop";
//
//                }
//            });
//
//
//        }
        stopSelf();
    }
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

}
