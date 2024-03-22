package com.example.kulaapp.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.kulaapp.Models.users;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class DbHelper {

    @SuppressLint("StaticFieldLeak")
    static Context ctx;

    static Context act;
    public static boolean loadeddb = false;
    public static SQLiteDatabase db = null;
    private String user_id;

    private String RawText;


    public DbHelper(Context ct) {
        DbHelper.ctx = ct;
        net.sqlcipher.database.SQLiteDatabase.loadLibs(ctx.getApplicationContext());

        //try setting up the db and catch exception if otherwise
        if (!loadeddb) {
            try {
                setupdb();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SharedPreferences sharedPref = ct.getApplicationContext().getSharedPreferences("com.example.kulaapp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("version_code", "" + getAppVersion(ct));
        editor.apply();
        String dbpath = ctx.getExternalFilesDir(null).getAbsolutePath() + "/" + "Kula.db";

        SQLiteDatabase.loadLibs(ctx);
    }

    private int getAppVersion(Context cont) {


        try {
            PackageInfo pinfo = cont.getPackageManager().getPackageInfo(cont.getPackageName(), 0);
            return pinfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1; // Return default value in case of error
        }

        /**PackageInfo pinfo = null;
        try {
            pinfo = cont.getPackageManager().getPackageInfo(cont.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return pinfo.versionCode;**/
    }

    public DbHelper(Context ctx, Activity act) {
        String dbpath = ctx.getExternalFilesDir(null).getAbsolutePath() + "/" + "   Kula.db";
        SQLiteDatabase.loadLibs(ctx);
        act = act;
        if (db == null) {
            db = SQLiteDatabase.openOrCreateDatabase(dbpath, "", null);
        }
    }

    private void setupdb() {

        Log.d("in helper", "db setup: " + "success");
        String dbpath = ctx.getExternalFilesDir(null).getAbsolutePath() + "/" + "Kula.db";
        SQLiteDatabase.loadLibs(ctx);
        db = SQLiteDatabase.openOrCreateDatabase(dbpath, "", null);
        createtables();

    }

    public void updateDBAppVersion(String version_code) {
        ContentValues ctx = new ContentValues();
        ctx.put("version_code", version_code);
        ctx.put("version_name", version_code);
        db.insert("app_version", null, ctx);
        Log.d("in helper", "Version update: " + "success");
    }

    public String getVersionCode() {
        try {
            Cursor cursor1 = db.rawQuery("SELECT MAX(id), version_code FROM app_version", null);
            cursor1.moveToFirst();
            if (!cursor1.isAfterLast()) {
                do {
                    return cursor1.getString(1);
                } while (cursor1.moveToNext());
            }
            cursor1.close();
        } catch (Exception e) {

        }
        return "0";
    }

    private void createtables() {

        Log.e("in helper", "create tables: " + "success");
        //TO DO add columns

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS users (_id INTEGER PRIMARY KEY  NOT NULL ,displayname VARCHAR,password VARCHAR,about VARCHAR, pronouns VARCHAR,avatar image,agent_id INTEGER,status_id VARCHAR DEFAULT (null) ,sync_datetime LONG,email_address VARCHAR,phone_no VARCHAR,account_id INTEGER,employee_id INTEGER,account_name VARCHAR,rid INTEGER UNIQUE , branch VARCHAR, branch_id INTEGER, name VARCHAR, is_active VARCHAR)");
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS logs (_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , user_id INTEGER, sync_datetime LONG DEFAULT CURRENT_TIMESTAMP, description VARCHAR)");
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS app_version  (id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL, version_code VARCHAR, version_name VARCHAR, datetime VARCHAR, status VARCHAR)");
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS recipes  (id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL, recipeXml VARCHAR)");
    }


    public boolean editProfileInputData(String displayname, String password, String pronouns, String email_address, String about) {
        ContentValues ctx = new ContentValues();
        ctx.put("displayname", displayname);
        ctx.put("password", password);
        ctx.put("pronouns", pronouns);
        ctx.put("email_address", email_address);
        ctx.put("about", about);
        //ctx.put("avatar", avatar);
        long newRowId = db.insert("users", null, ctx);

        Log.d("in helper", "users form update: " + "success");
        return newRowId != -1;
    }

    public Boolean insertRecipe(String recipeXml) {
        ContentValues ctx = new ContentValues();
        ctx.put("recipe_xml", recipeXml);

        long newRowId = db.insert("recipe", null, ctx);

        Log.d("in helper", "recipe update: " + "success");

        return newRowId != -1;
    }

    public void log_event(Context act, String data) {
        try {
            String prefix = GlobalVariables.timeNow() + " : " + GlobalVariables.code;

            String root = Environment.getExternalStorageDirectory().toString() + "/School/.LOGS/";
            File file = new File(root);
            file.mkdirs();

            File gpxfile = new File(file, GlobalVariables.logDate() + "" + GlobalVariables.Log_file_name);
            FileWriter writer = new FileWriter(gpxfile, true);
            writer.append(prefix + data + "\n");
            writer.flush();
            writer.close();
        } catch (Exception ex) {

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String encrypt(String textRaw) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        String text = textRaw;
        // Change this to UTF-16 if needed
        md.update(text.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();
        String hex = String.format("%064x", new BigInteger(1, digest));
        String str = new String(digest, StandardCharsets.UTF_8);
        String s = new String(digest);
        return hex;
    }

    private void InsertLog(ContentValues cv) {

        try {
            db.insert("logs", null, cv);
        } catch (Exception e) {

        }
    }

    public void InsertRecipes(JSONArray jRecipes, Context applicationContext) throws JSONException {

        JSONArray jRecipe = jRecipes;
        int recipe_length = jRecipe.length();

        new Helper().ConsoleLog("RESULTS", "RecipesFromServer--------- length " +recipe_length );

        db.beginTransaction();

        String sql_insert = "insert into recipes (sid, phone_no, fullname, member_no, transacting_branch_id, comment, user_id, membership_sub_type_id, sync_datetime, memberShipTypeId, membership_type_id, id_no, reg_date, datecomparer, status, is_active, medical_status, drivers_licence_no, employee_category_id, member_type,sync_status) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String sql_update = "update recipes set phone_no=?, fullname=?, member_no=?, transacting_branch_id=?, comment=?, user_id=?, membership_sub_type_id=?, sync_datetime=?, memberShipTypeId=?, membership_type_id=?, id_no=?, reg_date=?, datecomparer=?, status=?, is_active=?, medical_status=?, drivers_licence_no=?, employee_category_id=?, member_type=?, sync_status=? where sid = ?";

        net.sqlcipher.database.SQLiteStatement stmt_insert = db.compileStatement(sql_insert);
        net.sqlcipher.database.SQLiteStatement stmt_update = db.compileStatement(sql_update);

//        {
//            "$id": "7",
//                "id": 21126,
//                "phone1": "11111",
//                "full_name": "John Test",
//                "member_no": null,
//                "departmentName": 0,
//                "transacting_branch_id": null,
//                "membership_sub_type_id": null,
//                "sync_datetime": "2023-09-28T17:13:43+03:00",
//                "memberShipTypeId": null,
//                "membership_type_id": 2,
//                "nat_id": "111111",
//                "reg_date": "2023-09-28T00:00:00",
//                "datecomparer": 16959212235611296,
//                "status": true,
//                "Is_active": false,
//                "medical_checked": false,
//                "dl_Number": "11111",
//                "employee_category_id": null,
//                "member_type": null,
//                "IsMedicallyAssessed": false
//        }

        for (int i = 0; i < jRecipe.length(); i++) {


            stmt_insert.bindString(1, jRecipe.getJSONObject(i).getString("id"));
            stmt_insert.bindString(2, jRecipe.getJSONObject(i).getString("phone1"));
            stmt_insert.bindString(3, jRecipe.getJSONObject(i).getString("full_name"));
            stmt_insert.bindString(4, jRecipe.getJSONObject(i).getString("member_no"));
            stmt_insert.bindString(5, jRecipe.getJSONObject(i).getString("transacting_branch_id"));
            stmt_insert.bindString(6, jRecipe.getJSONObject(i).getString("comment"));
            stmt_insert.bindString(7, jRecipe.getJSONObject(i).getString("user_id"));
            stmt_insert.bindString(8, jRecipe.getJSONObject(i).getString("membership_sub_type_id"));
            stmt_insert.bindString(9, jRecipe.getJSONObject(i).getString("sync_datetime"));
            stmt_insert.bindString(10, jRecipe.getJSONObject(i).getString("memberShipTypeId"));
            stmt_insert.bindString(11, jRecipe.getJSONObject(i).getString("membership_type_id"));
            stmt_insert.bindString(12, jRecipe.getJSONObject(i).getString("nat_id"));
            stmt_insert.bindString(13, jRecipe.getJSONObject(i).getString("reg_date"));
            stmt_insert.bindString(14, jRecipe.getJSONObject(i).getString("datecomparer"));
            stmt_insert.bindString(15, jRecipe.getJSONObject(i).getString("status"));
            stmt_insert.bindString(16, jRecipe.getJSONObject(i).getString("Is_active"));
            stmt_insert.bindString(17, jRecipes.getJSONObject(i).getString("medical_checked"));
            stmt_insert.bindString(18, jRecipe.getJSONObject(i).getString("dl_Number"));
            stmt_insert.bindString(19, jRecipe.getJSONObject(i).getString("employee_category_id"));
            stmt_insert.bindString(20, jRecipe.getJSONObject(i).getString("member_type"));
            stmt_insert.bindString(21, "i");

            stmt_update.bindString(1, jRecipe.getJSONObject(i).getString("phone1"));
            stmt_update.bindString(2, jRecipe.getJSONObject(i).getString("full_name"));
            stmt_update.bindString(3, jRecipe.getJSONObject(i).getString("member_no"));
            stmt_update.bindString(4, jRecipe.getJSONObject(i).getString("transacting_branch_id"));
            stmt_update.bindString(5, jRecipe.getJSONObject(i).getString("comment"));
            stmt_update.bindString(6, jRecipe.getJSONObject(i).getString("user_id"));
            stmt_update.bindString(7, jRecipe.getJSONObject(i).getString("membership_sub_type_id"));
            stmt_update.bindString(8, jRecipe.getJSONObject(i).getString("sync_datetime"));
            stmt_update.bindString(9, jRecipe.getJSONObject(i).getString("memberShipTypeId"));
            stmt_update.bindString(10, jRecipe.getJSONObject(i).getString("membership_type_id"));
            stmt_update.bindString(11, jRecipe.getJSONObject(i).getString("nat_id"));
            stmt_update.bindString(12, jRecipe.getJSONObject(i).getString("reg_date"));
            stmt_update.bindString(13, jRecipe.getJSONObject(i).getString("datecomparer"));
            stmt_update.bindString(14, jRecipe.getJSONObject(i).getString("status"));
            stmt_update.bindString(15, jRecipe.getJSONObject(i).getString("Is_active"));
            stmt_update.bindString(16, jRecipe.getJSONObject(i).getString("medical_checked"));
            stmt_update.bindString(17, jRecipe.getJSONObject(i).getString("dl_Number"));
            stmt_update.bindString(18, jRecipe.getJSONObject(i).getString("employee_category_id"));
            stmt_update.bindString(19, jRecipe.getJSONObject(i).getString("member_type"));
            stmt_update.bindString(20, "i");
            stmt_update.bindString(21, jRecipe.getJSONObject(i).getString("id"));

            String server_id = null;
            String[] whereArgs = {""+ jRecipe.getJSONObject(i).getString("id")};
            android.database.Cursor c = db.rawQuery("SELECT sid FROM recipes WHERE sid = ?", whereArgs);
            c.moveToFirst();
            if (!c.isAfterLast()){
                do {
                    server_id = c.getString(0);
                } while (c.moveToNext());
            }
            c.close();

            if (server_id == null){
                long entryID = stmt_insert.executeInsert();
            } else {
                long entryID = stmt_update.executeUpdateDelete();
            }

            stmt_insert.clearBindings();
            stmt_update.clearBindings();
        }

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public int countPendingRecipes() {

        int res = 0;
        android.database.Cursor c = db.rawQuery("SELECT count(*) FROM recipes WHERE sync_status = ?", new String[]{"new_recipe"});
        if (!c.isAfterLast()){
            c.moveToFirst();
            do {
                res = c.getInt(0);
            }while (c.moveToNext());
        }
        c.close();

        return res;
    }

    public JSONObject recipesToSync()  throws JSONException{


        JSONObject mainObject = new JSONObject();
        android.database.Cursor c = db.rawQuery("SELECT * FROM recipes WHERE sync_status = ? LIMIT 1", new String[]{"new_recipe"});
        if (!c.isAfterLast()){
            c.moveToFirst();

            do {
                mainObject.put("full_name", c.getString(c.getColumnIndexOrThrow("fullname")));
                mainObject.put("nat_id", c.getString(c.getColumnIndexOrThrow("id_no")));
                mainObject.put("phone1", c.getString(c.getColumnIndexOrThrow("phone_no")));
                mainObject.put("dl_Number", c.getString(c.getColumnIndexOrThrow("drivers_licence_no")));
            }while (c.moveToNext());
        }
        c.close();

        return mainObject;
    }

    public users getUserLoggedIn() {

        int p_id = 0;
        int refID = userLoggedIn();
        String[] columns = {"_id", "username", "password", "account_name", "branch"};
        String selection = "rid = ?";
        String[] args = {"" + refID};
        users uz = new users();
        android.database.Cursor c = db.query("users", columns, selection, args, null,
                null, null);
        if (c != null) {
            c.moveToFirst();
            if (!c.isAfterLast()) {
                do {

                    uz.setAccount_name(c.getString(3));
                    uz.setBranch(c.getString(4));
                    uz.setUsername(c.getString(1));

                } while (c.moveToNext());
            } else {
                uz.setAccount_name("");
                uz.setBranch("");
                uz.setUsername("");
            }
            c.close();

        }

        return uz;
    }

    private int userLoggedIn() {

        int p_id = 0;
        int refID = (int) getMaxId("logs");
        String[] columns = {"COALESCE(user_id, 0)"}; // columns to select
        String selection = "_id = ?";
        String[] args = {"" + refID}; // value added into where clause -->
        android.database.Cursor c = db.query("logs", columns, selection, args, null, null, null);
        if (c != null) {
            c.moveToFirst();
            if (!c.isAfterLast()) {
                do {
                    p_id = c.getInt(0);
                } while (c.moveToNext());
            }
            c.close();
        }
        return p_id;
    }

    private Object getMaxId(String logs) {
        int id = 0;
        String[] columns = {"MAX(_id)"}; // columns to select
        String selection = "_id = ?";
        android.database.Cursor cursor = db.query("logs", columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return id;
    }

    public String EncryptTextMD5(String password1) {

        String EncText = "";
        byte[] keyArray = new byte[24];
        byte[] temporaryKey;

        byte[] toEncryptArray = null;

        try {

            toEncryptArray = RawText.getBytes(StandardCharsets.UTF_8);
            MessageDigest m = MessageDigest.getInstance("MD5");
            temporaryKey = m.digest(getkey().getBytes(StandardCharsets.UTF_8));
            Cipher c = Cipher.getInstance("DESede/ECB/PKCS7Padding");
            KeyGenerator k = KeyGenerator.getInstance("DESede");
            SecretKey key1;
            key1 = new SecretKeySpec(temporaryKey, k.getAlgorithm());
            c.init(Cipher.ENCRYPT_MODE, key1);
            byte[] encrypted = c.doFinal(toEncryptArray);
            EncText = encodeBase64String(encrypted);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
                 | InvalidKeyException | BadPaddingException NoEx) {
            Log.v("##################", NoEx.getMessage());
        }

        return EncText;
    }

    private String encodeBase64String(byte[] encrypted) {

        return encodeBase64String(encrypted);
    }


    private String getkey() {
        return act.getSharedPreferences("com.example.safety_cap", Context.MODE_PRIVATE).getString("move",
                "");
    }

    public Boolean loginUser(String username, String passcode) {

        boolean login_success = false;
        try {
            android.database.Cursor UsersCursor = db
                    .query("users",
                            new String[]{"_id", "username", "password",
                                    "account_name", "branch", "rid"},
                            "username = ? AND password = ? ",
                            new String[]{username.trim(), passcode.trim()},
                            null, null, "_id");
            UsersCursor.moveToFirst();

            if (!UsersCursor.isAfterLast()) {
                do {
                    String name = UsersCursor.getString(1);
                    login_success = true;
                    ContentValues cv = new ContentValues();
                    cv.put("user_id", UsersCursor.getInt(5));
                    InsertLog(cv);
                    GlobalVariables.userid = UsersCursor.getString(0);
                } while (UsersCursor.moveToNext());
            } else {
                login_success = false;
            }
            UsersCursor.close();

            return login_success;
        } catch (Exception e) {
            e.printStackTrace();
            login_success = false;
        }

        return login_success;
    }

    public void updateUser(ContentValues memberMap, int updatingID, ContentValues logMap) {

        int counter = 0;
        android.database.Cursor user = db.rawQuery("SELECT count(*) FROM users WHERE rid = ?", new String[]{"" + user_id});
        user.moveToFirst();
        if (!user.isAfterLast()) {
            do {
                counter = user.getInt(0);
            } while (user.moveToNext());
        }
        user.close();
        if (counter <= 0) {
            Log.v("LLLLLLLLLL", "KKKKKKKKKKKKKKKKK inserting");
            try {
                db.insert("users", null, memberMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String WhereClause = "rid = ?";
            String[] WhereArgs = {"" + user_id};
            try {
                Log.v("LLLLLLLLLL", "KKKKKKKKKKKKKKKKK updating");
                db.update("users", memberMap, WhereClause, WhereArgs);
            } catch (Exception ep) {
                ep.printStackTrace();
            }
        }
        // UPDATE LOG TBL
        try {
            db.insert("logs", null, logMap);
            // userModel.onCreate(null);
        } catch (Exception e) {
            Log.v("%%%%%%%%%%%% ", "" + e);
        }
    }
}
