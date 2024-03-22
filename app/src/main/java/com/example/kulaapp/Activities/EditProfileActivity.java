package com.example.kulaapp.Activities;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.kulaapp.R;
import com.example.kulaapp.Utils.DbHelper;
import com.example.kulaapp.Utils.GlobalVariables;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditProfileActivity extends AppCompatActivity {


    Button save_button;

    EditText display_name_edt, email_edt,password_edt, about_edt;
    String displayName , password, about, pronouns, email;
    Boolean editProfileData, saved;

    Activity act;
    Context ctx;
    String storeFilename;
    String currentPhotoPath;

    TextView pronouns_edt;

    String ba1 = "";
    String trans_no;
    private DbHelper dao;

    Spinner pronounsSpinner;

    private static final int PERMISSION_REQUEST_CODE = 200;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    public static SQLiteDatabase db = null;

    ImageView pfp_img;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        save_button = findViewById(R.id.save_button);
        display_name_edt = findViewById(R.id.display_name_edt);
        pronouns_edt = findViewById(R.id.pronouns_edt);
        email_edt = findViewById(R.id.email_edt);
        password_edt = findViewById(R.id.password_edt);
        about_edt = findViewById(R.id.about_edt);
        pronounsSpinner = findViewById(R.id.pronounsSpinner);
        pfp_img = findViewById(R.id.pfp_img);

        dao = new DbHelper(this);

        save_button.setOnClickListener(v -> {

            //TODO code to submit form entries to database.

            displayName = display_name_edt.getText().toString();
            password = password_edt.getText().toString();
            pronouns = pronounsSpinner.getSelectedItem().toString();
            email = email_edt.getText().toString();
            about = about_edt.getText().toString();


            if (displayName.equals("") || password.equals("") || pronouns.equals("") || email.equals("") || about.equals("")) { //checks if any of the edt are empty
                Toast.makeText(EditProfileActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
            } else {
                editProfileData = dao.editProfileInputData(displayName, password, pronouns, email, about);
                if (editProfileData) {//if the new details are able to be inserted
                    Toast.makeText(EditProfileActivity.this, "changes saved", Toast.LENGTH_SHORT).show();
                } else {// changes not saved
                    Toast.makeText(EditProfileActivity.this, "failed", Toast.LENGTH_SHORT).show();
                }
            }
        });


        pfp_img.setOnClickListener(v -> {

            //check for permissions on clicking the photo

            Log.e("TAG", "onClick: "+"CLICKED" );

            try {

                if(!checkPermission()){
                    //request for permissions if they are not granted
                    requestPermission();
                } else {
                    //if permissions are given then take the picture
                    takePicture(ctx);
                }
                //code to catch the error
            }catch(Exception e){
                e.printStackTrace();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            File f = new File(currentPhotoPath);
            Bitmap bitmap = getDownsampledBitmap(ctx, Uri.fromFile(f), 720, 1024);
            pfp_img.setImageBitmap(bitmap);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            ba1 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                    byteArray.length);

            saved = saveImageData(ba1);
            if (saved) {
                Toast.makeText(this, "Image saved to database", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to save image to database", Toast.LENGTH_SHORT).show();
            }

            // Save the bitmap image to file
            saveImage(bitmap, "PFP_IMG_" + trans_no + ".jpg");
        }
    }

    private Boolean saveImageData(String ba1) {
        ContentValues ctx = new ContentValues();
        ctx.put("pfp_img", ba1);

        long newRowId = db.insert("users", null, ctx);

        return newRowId != -1;
    }

    private void saveImage(Bitmap finalBitmap, String fi_name) {

        String root = Environment.getExternalStorageDirectory().toString();

        File myDir = new File(GlobalVariables.profile_pics);

        myDir.mkdirs();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File file = new File(myDir, fi_name);
        if (file.exists()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void takePicture(Context ctx) {
        try {
            dispatchTakePictureIntent();
        } catch (Exception e){
            Log.e("Not easy",">>>>>>>>>>>"+e.getMessage());
        }
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        Log.e("nonononono",">>>>>>>>>>>>>>>>>>> "+takePictureIntent.resolveActivity(getPackageManager()));

        if (takePictureIntent.resolveActivity(getPackageManager()) != null){

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            Log.e("picha file",">>>>>>>>>>>"+photoFile);

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.kulaapp.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

        }
    }
    private File createImageFile() throws IOException{
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        storeFilename = imageFileName;
        Log.e("TAG", imageFileName+"createImageFile: "+currentPhotoPath );
        Log.e("TAG", imageFileName+"storeFilename: "+storeFilename );
        return image;
    }

    private Bitmap getDownsampledBitmap(Context ctx, Uri uri, int targetWidth, int targetHeight) {
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options outDimens = getBitmapDimensions(uri);

            int sampleSize = calculateSampleSize(outDimens.outWidth, outDimens.outHeight, targetWidth, targetHeight);

            bitmap = downsampleBitmap(uri, sampleSize);

        } catch (Exception e) {
            //handle the exception(s)
        }

        return bitmap;
    }

    private BitmapFactory.Options getBitmapDimensions(Uri uri) throws FileNotFoundException, IOException {
        BitmapFactory.Options outDimens = new BitmapFactory.Options();
        outDimens.inJustDecodeBounds = true; // the decoder will return null (no bitmap)

        InputStream is= getContentResolver().openInputStream(uri);
        // if Options requested only the size will be returned
        BitmapFactory.decodeStream(is, null, outDimens);
        is.close();

        return outDimens;
    }

    private int calculateSampleSize(int width, int height, int targetWidth, int targetHeight) {
        int inSampleSize = 1;

        if (height > targetHeight || width > targetWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) targetHeight);
            final int widthRatio = Math.round((float) width / (float) targetWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee.
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    private Bitmap downsampleBitmap(Uri uri, int sampleSize) throws FileNotFoundException, IOException {
        Bitmap resizedBitmap;
        BitmapFactory.Options outBitmap = new BitmapFactory.Options();
        outBitmap.inJustDecodeBounds = false; // the decoder will return a bitmap
        outBitmap.inSampleSize = sampleSize;

        InputStream is = getContentResolver().openInputStream(uri);
        resizedBitmap = BitmapFactory.decodeStream(is, null, outBitmap);
        is.close();

        return resizedBitmap;
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, Manifest.permission.CAMERA,WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}