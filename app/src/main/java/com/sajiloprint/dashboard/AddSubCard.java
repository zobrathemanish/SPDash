package com.sajiloprint.dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.CursorLoader;

import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sajiloprint.dashboard.models.ImageUploadModel;
import com.sajiloprint.dashboard.models.SubCardsmodel;
import com.sajiloprint.multipleimageselect.activities.AlbumSelectActivity;
import com.sajiloprint.multipleimageselect.helpers.Constants;
import com.sajiloprint.multipleimageselect.models.Image;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AddSubCard extends AppCompatActivity {

    //created for firebaseui android tutorial by Vamsi Tallapudi

    private DatabaseReference mDatabaseReference;
    private TextInputEditText cardName;
    private TextInputEditText cardimage;
    private TextInputEditText carddesc;
    private TextInputEditText cardprice;
    private TextInputEditText productid;
    private String card;

    private Button bSubmit;
    private String category;
    private TextView upload;
    private boolean uploadflag;
    private static final int RESULT_LOAD_IMAGE = 1;
    private List<String> firebaseImgAddresses;
    private List<String> ImagesLocationList;

    private String uploadimageid = "0";
    private String currdatetime;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference mStorage = storage.getReference();
    private Uri imageuri;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_card_fragment);

        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        cardName = findViewById(R.id.tiet_movie_name);
        cardimage = findViewById(R.id.tiet_movie_logo);
        carddesc = findViewById(R.id.description);
        cardprice = findViewById(R.id.price);
        productid = findViewById(R.id.productid);
        bSubmit = findViewById(R.id.b_submit);
        upload = findViewById(R.id.uploadimages);

        ImagesLocationList = new ArrayList<>();
        firebaseImgAddresses = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        card = bundle.getString("cardname");

        category = card;

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                uploadflag = true;
                Intent intent = new Intent(AddSubCard.this, AlbumSelectActivity.class);
                intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 4);
                startActivityForResult(intent, Constants.REQUEST_CODE);

            }
        });



        //initializing database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        //bSubmit.setOnClickListener(this);
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.b_submit) {
                    if (!isEmpty(cardName) && (!isEmpty(cardimage) || uploadflag) && !isEmpty(carddesc) && !isEmpty(cardprice) && !isEmpty(productid)) {
                        addImage();
                        myNewCard(cardName.getText().toString().trim(), cardimage.getText().toString(), carddesc.getText().toString(), Float.parseFloat(cardprice.getText().toString()), Integer.parseInt(productid.getText().toString()));
                    } else {
                        if (isEmpty(cardName)) {
                            Toast.makeText(getApplicationContext(), "Please enter a name!", Toast.LENGTH_SHORT).show();
                        } else if (isEmpty(cardimage)) {
                            Toast.makeText(getApplicationContext(), "Please specify a url or upload image", Toast.LENGTH_SHORT).show();
                        } else if (isEmpty(carddesc)) {
                            Toast.makeText(getApplicationContext(), "Please enter description", Toast.LENGTH_SHORT).show();
                        }
                    }
                    //to remove current fragment
//                        getActivity().onBackPressed();
                }
            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy-HH-mm");
        currdatetime = sdf.format(new Date());


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            StringBuilder stringBuffer = new StringBuilder();
            for (int i = 0, l = images.size(); i < l; i++) {
                System.out.println("imagessss" + images.get(i).path);

                stringBuffer.append(images.get(i).path + "\n");
                ImagesLocationList.add(images.get(i).path);

                System.out.println("stringbuffer is " + stringBuffer);
            }

//            StringBuffer stringBuffer = new StringBuffer();
//            for (int i = 0, l = images.size(); i < l; i++) {
//                stringBuffer.append(images.get(i).path + "\n");
//            }
//            textView.setText(stringBuffer.toString());
        }
    }

    private boolean isEmpty(TextInputEditText textInputEditText) {
        return textInputEditText.getText().toString().trim().length() <= 0;
    }


    private void myNewCard( String name, String image, String desc, float price, int pid ) {

        //Creating a movie object with user defined variables
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        SubCardsmodel movie = new SubCardsmodel(pid,name,image,desc,price, mAuth.getCurrentUser().getEmail(),uploadimageid);
        //referring to movies node and setting the values from movie object to that location
        mDatabaseReference.child("Products").child(category).push().setValue(movie);
        Intent intent = new Intent(AddSubCard.this, AddCardview.class);
        startActivity(intent);



    }

    private void addImage(){

        uploadimageid = getordernumber();


        Uri[] uri = new Uri[ImagesLocationList.size()];
        for (int i = 0; i < ImagesLocationList.size(); i++) {
            uri[i] = Uri.parse("file://" + ImagesLocationList.get(i));
//                Uri.fromFile(new File("/sdcard/sample.jpg"))
            String fileName = getFileName(uri[i]);
            final StorageReference fileToUpload = mStorage.child("Products").child(fileName);
            fileToUpload.putFile(uri[i]).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }
                    return fileToUpload.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddSubCard.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                        imageuri = task.getResult();
                        String image = imageuri.toString();
                        System.out.println("image uri is "+image);

                        firebaseImgAddresses.add(image);
                        mDatabaseReference.child("ProductImages").push().setValue((uploadimage(uploadimageid, image)));

                    } else {
                        Toast.makeText(AddSubCard.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            });

        }


    }

    private ImageUploadModel uploadimage(String uploadimageid, String downloadUrl) {

        return new ImageUploadModel(uploadimageid, downloadUrl);

    }




    public String getordernumber() {

        return currdatetime.replaceAll("-","");
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;

    }

    @SuppressLint("ObsoleteSdkInt")
    public String getPathFromURI(Uri uri){
        String realPath="";
// SDK < API11
        if (Build.VERSION.SDK_INT < 11) {
            String[] proj = { MediaStore.Images.Media.DATA };
            @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
            int column_index = 0;
            String result="";
            if (cursor != null) {
                column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                realPath=cursor.getString(column_index);
            }
        }
        // SDK >= 11 && SDK < 19
        else if (Build.VERSION.SDK_INT < 19){
            String[] proj = { MediaStore.Images.Media.DATA };
            CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();
            if(cursor != null){
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                realPath = cursor.getString(column_index);
            }
        }
        // SDK > 19 (Android 4.4)
        else{
            String wholeID = DocumentsContract.getDocumentId(uri);
            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];
            String[] column = { MediaStore.Images.Media.DATA };
            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel, new String[]{ id }, null);
            int columnIndex = 0;
            if (cursor != null) {
                columnIndex = cursor.getColumnIndex(column[0]);
                if (cursor.moveToFirst()) {
                    realPath = cursor.getString(columnIndex);
                }
                cursor.close();
            }
        }
        return realPath;
    }







}