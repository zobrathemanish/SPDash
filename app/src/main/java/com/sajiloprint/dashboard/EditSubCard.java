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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

public class EditSubCard extends AppCompatActivity {

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
    private static final int RESULT_LOAD_IMAGE = 1;
    private List<String> firebaseImgAddresses;
    private List<String> ImagesLocationList;

    private String uploadimageid = "0";
    private String currdatetime;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference mStorage = storage.getReference();
    private Uri imageuri;
    private String image_url;
    private KProgressHUD progressDialog;
    private SubCardsmodel model;
    private List<String> productimages;
    private UserSession session;
    private String shopname;
    private String shopemail,shopmobile;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_card_fragment);

        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        model = (SubCardsmodel) getIntent().getSerializableExtra("product");


        cardName = findViewById(R.id.tiet_movie_name);
        cardimage = findViewById(R.id.tiet_movie_logo);
        carddesc = findViewById(R.id.description);
        cardprice = findViewById(R.id.price);
//        productid = findViewById(R.id.productid);
        bSubmit = findViewById(R.id.b_submit);
        bulkdescription = findViewById(R.id.bulkdescription);

        cardName.setText(model.getCardname());
        carddesc.setText(model.getCarddiscription());
        cardprice.setText(String.valueOf(model.getCardprice()));
        bulkdescription.setText(model.getBulkdescription());
        cardimage.setText(model.getCardimage());

        Bundle bundle = getIntent().getExtras();
        card = bundle.getString("card");




        ImagesLocationList = new ArrayList<>();
        ImagesLocationList = model.getProductimages();
        firebaseImgAddresses = new ArrayList<>();
        productimages = new ArrayList<>();

        //initializing database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        //bSubmit.setOnClickListener(this);
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bSubmit.setClickable(true);  //was false

                hideKeyboard(EditSubCard.this);
                progressDialog = KProgressHUD.create(EditSubCard.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Please wait")
                        .setCancellable(false)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .show();
                if (view.getId() == R.id.b_submit) {
                    if (!isEmpty(cardName) && !isEmpty(carddesc) && !isEmpty(cardprice)) {
                        editAt(model.getCardname());

                    } else {
                        progressDialog.dismiss();
                        if (isEmpty(cardName)) {
                            Toast.makeText(getApplicationContext(), "Please enter a name!", Toast.LENGTH_SHORT).show();
                        }  else if (isEmpty(carddesc)) {
                            Toast.makeText(getApplicationContext(), "Please enter description", Toast.LENGTH_SHORT).show();
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


    private boolean isEmpty(TextInputEditText textInputEditText) {
        return textInputEditText.getText().toString().trim().length() <= 0;
    }


    private void myNewCard( String name, String image, String desc, float price, int pid,String bulkdescription ) {

        //Creating a movie object with user defined variables

            firebaseImgAddresses = model.getProductimages();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        SubCardsmodel movie = new SubCardsmodel(pid,name,image,desc,price,bulkdescription, mAuth.getCurrentUser().getEmail(),shopname,shopmobile,firebaseImgAddresses);
        //referring to movies node and setting the values from movie object to that location
        System.out.println("card and model " + card + movie.getCardname());
        mDatabaseReference.child("Products").child(card).push().setValue(movie);
        progressDialog.dismiss();

//        editAt(model.getCardname());


        Intent intent = new Intent(EditSubCard.this, DeleteSubCardView.class);
        intent.putExtra("cardname",card);

        startActivity(intent);




    }

    private ImageUploadModel uploadimage(String uploadimageid, String downloadUrl) {

        return new ImageUploadModel(uploadimageid, downloadUrl);

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

    public void editAt(final String cardname) {
//        ImageList.remove(position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, ImageList.size());


        productimages = model.getProductimages();

        mDatabaseReference.child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String parent = snapshot.getKey();

                    mDatabaseReference.child("Products").child(card).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (final DataSnapshot datasnapshot: dataSnapshot.getChildren()){
                                String UID = datasnapshot.getKey();
                                String card_name = datasnapshot.child("cardname").getValue(String.class);
                                System.out.println("The card_name is " + card_name);
                                if(card_name.toLowerCase().contains(cardname.toLowerCase())) {
                                    System.out.println("UID here is" + UID);
                                    System.out.println("bulkdescription " + bulkdescription.getText().toString());


                                    mDatabaseReference.child("Products").child(card).child(UID).removeValue();
//                                    mDatabaseReference.child("Products").child(category).child(UID).child("cardname").setValue(cardName.getText().toString());
//                                    mDatabaseReference.child("Products").child(category).child(UID).child("carddiscription").setValue(carddesc.getText().toString());
//                                    mDatabaseReference.child("Products").child(category).child(UID).child("bulkdescription").setValue(bulkdescription.getText().toString());
//                                    mDatabaseReference.child("Products").child(category).child(UID).child("cardimage").setValue(cardimage.getText().toString());
//                                    mDatabaseReference.child("Products").child(category).child(UID).child("cardprice").setValue(cardprice.getText().toString());
//                                    if (changeflag)
//                                        mDatabaseReference.child("Products").child(category).child(UID).child("productimages").setValue(firebaseImgAddresses);
//


                                }





                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                myNewCard(cardName.getText().toString().trim(), cardimage.getText().toString(), carddesc.getText().toString(), Float.parseFloat(cardprice.getText().toString()), model.getCardid(),bulkdescription.getText().toString());



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }








}