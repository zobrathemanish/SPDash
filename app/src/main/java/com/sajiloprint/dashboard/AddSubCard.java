package com.sajiloprint.dashboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.CursorLoader;

import com.airbnb.lottie.model.layer.ImageLayer;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.sajiloprint.dashboard.models.ImageUploadModel;
import com.sajiloprint.dashboard.models.SubCardsmodel;
import com.sajiloprint.multipleimageselect.activities.AlbumSelectActivity;
import com.sajiloprint.multipleimageselect.helpers.Constants;
import com.sajiloprint.multipleimageselect.models.Image;
import com.sajiloprint.usersession.UserSession;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class AddSubCard extends AppCompatActivity {

    //created for firebaseui android tutorial by Vamsi Tallapudi

    private DatabaseReference mDatabaseReference;
    private TextInputEditText cardName;
    private TextInputEditText cardimage;
    private TextInputEditText carddesc;
    private TextInputEditText cardprice;
    private TextInputEditText bulkdescription;
    //    private TextInputEditText productid;
    private String card;

    private Button bSubmit;
    private String category;
    private TextView upload;
    private boolean uploadflag = false;
    private static final int RESULT_LOAD_IMAGE = 1;
    private List<String> firebaseImgAddresses;
    private List<String> ImagesLocationList;

    private String uploadimageid = "0";
    private String currdatetime;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference mStorage = storage.getReference();
    private Uri imageuri;
    private String image_url="null";
    private KProgressHUD progressDialog;
    private UserSession session;
    private String shopname;
    private String shopemail,shopmobile;




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
//        productid = findViewById(R.id.productid);
        bSubmit = findViewById(R.id.b_submit);
        upload = findViewById(R.id.uploadimages);
        bulkdescription = findViewById(R.id.bulkdescription);


        ImagesLocationList = new ArrayList<>();
        firebaseImgAddresses = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        card = bundle.getString("cardname");

        category = card;

        getValues();

//        if(!cardimage.getText().toString().equals("")){
//            uploadflag = true;
//        }

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                ImagesLocationList.clear();
                firebaseImgAddresses.clear();

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
                bSubmit.setClickable(true);  //was false

                hideKeyboard(AddSubCard.this);
                progressDialog = KProgressHUD.create(AddSubCard.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Please wait")
                        .setCancellable(false)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .show();
                if (view.getId() == R.id.b_submit) {
                    if (!isEmpty(cardName) && (uploadflag || !isEmpty(cardimage)) && !isEmpty(carddesc) && !isEmpty(cardprice)) {
                        uploadimageid = getordernumber();
                        if(uploadflag)
                        addImage();
                        else
                            myNewCard(cardName.getText().toString().trim(), cardimage.getText().toString(), carddesc.getText().toString(), Float.parseFloat(cardprice.getText().toString()), Integer.parseInt(uploadimageid),bulkdescription.getText().toString());






                    } else {
                        progressDialog.dismiss();
                        if (isEmpty(cardName)) {
                            Toast.makeText(getApplicationContext(), "Please enter a name!", Toast.LENGTH_SHORT).show();
                        } else if ( !uploadflag) {
                            Toast.makeText(getApplicationContext(), "Please specify a url or upload image", Toast.LENGTH_SHORT).show();
                        } else if (isEmpty(carddesc)) {
                            Toast.makeText(getApplicationContext(), "Please enter description", Toast.LENGTH_SHORT).show();
                        }
                        else if (isEmpty(cardprice)) {
                            Toast.makeText(getApplicationContext(), "Please enter price", Toast.LENGTH_SHORT).show();
                        }
                    }
                    //to remove current fragment
//                        getActivity().onBackPressed();
                }
            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        currdatetime = sdf.format(new Date());


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            Toasty.info(AddSubCard.this, images.size()+" Image Selected", Toast.LENGTH_SHORT, true).show();
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


    private void myNewCard( String name, String image, String desc, float price, int pid,String bulkdescription ) {

        //Creating a movie object with user defined variables
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        System.out.println("firebaseImgAddresses" + firebaseImgAddresses);
        SubCardsmodel movie = new SubCardsmodel(pid,name,image,desc,price,bulkdescription, mAuth.getCurrentUser().getEmail(),shopname,shopmobile,firebaseImgAddresses);
        //referring to movies node and setting the values from movie object to that location
        mDatabaseReference.child("Products").child(category).push().setValue(movie);

        progressDialog.dismiss();

        Intent intent = new Intent(AddSubCard.this, AddCardview.class);
        intent.putExtra("show",category);

        startActivity(intent);




    }

    private void addImage(){



        Uri[] uri = new Uri[ImagesLocationList.size()];
        for (int i = 0; i < ImagesLocationList.size(); i++) {
            uri[i] = Uri.parse("file://" + ImagesLocationList.get(i));
            final int finalI = i;
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

                        if(isEmpty(cardimage))
                            image_url = firebaseImgAddresses.get(0);
                        else
                            image_url = cardimage.getText().toString();

                        progressDialog.dismiss();

                        if (finalI==0) {
                            Toasty.success(AddSubCard.this, "Product Registered Succesfully", Toast.LENGTH_SHORT, true).show();

                        }
                        if (firebaseImgAddresses.size() == ImagesLocationList.size())
                            myNewCard(cardName.getText().toString().trim(), image_url, carddesc.getText().toString(), Float.parseFloat(cardprice.getText().toString()), Integer.parseInt(uploadimageid),bulkdescription.getText().toString());

//
//                        mDatabaseReference.child("ProductImages").child(uploadimageid).push().setValue((uploadimage(uploadimageid, image)));

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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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


    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void getValues() {

        //create new session object by passing application context
        session = new UserSession(getApplicationContext());

        //validating session
        session.isLoggedIn();

        //get User details if logged in
        HashMap<String, String> user = session.getUserDetails();

        shopname = user.get(UserSession.KEY_NAME);
        shopemail = user.get(UserSession.KEY_EMAIL);
        shopmobile = user.get(UserSession.KEY_MOBiLE);
        System.out.println("nameemailmobile " + shopname + shopemail + shopmobile);
    }






}