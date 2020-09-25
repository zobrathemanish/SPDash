package com.sajiloprint.dashboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.appcompat.widget.Toolbar;

import android.os.Parcelable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sajiloprint.dashboard.models.CardsModel;
import com.sajiloprint.dashboard.models.SearchAdapter;
import com.sajiloprint.dashboard.models.SubCardsmodel;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

public class DeleteSubCardView extends AppCompatActivity {

    //created for firebaseui android tutorial by Vamsi Tallapudi
    EditText search_edit_text;

    private FloatingActionButton sub;

    ScaleAnimation shrinkAnim;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private TextView tvNoMovies;
    private ArrayList<String> CardList;
    private ArrayList<String> ShowList;
    SearchAdapter searchAdapter;



    //Getting reference to Firebase Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();

    private String category;
    private static final String userId = "53";
    private String card;
    private String currentuser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delcards);

        Toolbar toolbar = findViewById(R.id.toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        CardList = new ArrayList<>();
        ShowList = new ArrayList<>();

//        CardList.add("Cards");
//        CardList.add("CorporateGifts");
//        CardList.add("Calendar");
//        CardList.add("WallDecors");
//        CardList.add("Wearables");
//        CardList.add("Photogifts");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
         currentuser = mAuth.getCurrentUser().getEmail();


            //Initializing our Recyclerview
        category = "Cards";
        mRecyclerView = findViewById(R.id.my_recycler_view);



        Bundle bundle = getIntent().getExtras();
        card = bundle.getString("cardname");

        category = card;
        System.out.println("The card is" + card);

        tvNoMovies = (TextView) findViewById(R.id.tv_no_cards);


        shrinkAnim = new ScaleAnimation(1.15f, 0f, 1.15f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        if (mRecyclerView != null) {
            //to enable optimization of recyclerview
            mRecyclerView.setHasFixedSize(true);
        }
        //using staggered grid pattern in recyclerview
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);



        refreshproducts();



//        spinner = findViewById(R.id.categoryspinner);

//
//        spinner.setItems(CardList);
//        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
//
//            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
//                category = item;
//
//                refreshproducts();
//            }
//        });
//
//        refreshproducts();






    }

    private void refreshproducts() {

        //Say Hello to our new FirebaseUI android Element, i.e., FirebaseRecyclerAdapter
        FirebaseRecyclerAdapter<SubCardsmodel,MovieViewHolder> adapter = new FirebaseRecyclerAdapter<SubCardsmodel, MovieViewHolder>(
                SubCardsmodel.class,
                R.layout.list_image_single,
                MovieViewHolder.class,
                //referencing the node where we want the database to store the data from our Object
                mDatabaseReference.child("Products").child(category).getRef()
        ) {
            @Override
            protected void populateViewHolder(final MovieViewHolder viewHolder, final SubCardsmodel model, final int position) {

                if(tvNoMovies.getVisibility()== View.VISIBLE){
                    tvNoMovies.setVisibility(View.GONE);
                }
                System.out.println("asdf" + model.getShopemail());
                if( model.getShopemail()!=null) {
                    if (model.getShopemail().equals(currentuser)) {
                        System.out.println("inside curent");

                        viewHolder.cardcategory.setText(model.getCardname());

                        Picasso.with(DeleteSubCardView.this).load(model.getCardimage()).config(Bitmap.Config.RGB_565).resize(800, 600).into(viewHolder.cardimage);

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(DeleteSubCardView.this, EditSubCard.class);
                                intent.putExtra("product",  getItem(position));
                                intent.putExtra("card",category);

                                startActivity(intent);
                            }

                        });
                        viewHolder.carddelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Toast.makeText(ImageRecyclerAdapter.this,getItem(position).getPrname(),Toast.LENGTH_SHORT).show();
                                removeAt(getItem(position));
                                //session.decreaseCartValue();
                                //mContext.startActivity(new Intent(ImageRecyclerAdapter.this,ImageRecyclerAdapter.this));
                            }
                        });
                    }

                    else{

                        viewHolder.cardcategory.setVisibility(View.GONE);
                        viewHolder.carddelete.setVisibility(View.GONE);
                        viewHolder.cardimage.setVisibility(View.GONE);
                        viewHolder.mView.setVisibility(View.GONE);
                    }
                }
                else{


                    viewHolder.cardcategory.setVisibility(View.GONE);
                    viewHolder.carddelete.setVisibility(View.GONE);
                    viewHolder.cardimage.setVisibility(View.GONE);
                    viewHolder.mView.setVisibility(View.GONE);
                }



            }
        };

        mRecyclerView.setAdapter(adapter);

    }



    //ViewHolder for our Firebase UI
    public static class MovieViewHolder extends RecyclerView.ViewHolder{

        TextView cardcategory;
        ImageView cardimage;
        ImageView carddelete;


        View mView;


        public MovieViewHolder(View v) {
            super(v);
            mView =v;
            cardcategory = (TextView) v.findViewById(R.id.cardcategory);
            cardimage = (ImageView) v.findViewById(R.id.cardimage);
            carddelete = v.findViewById(R.id.deletecard);

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void removeAt(final SubCardsmodel model) {
//        ImageList.remove(position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, ImageList.size());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference mStorage = storage.getReference();

        if (model.getProductimages()!=null) {

            for (String image : model.getProductimages()) {

                System.out.println("the image in productimages" + image);

                StorageReference photoRef = storage.getReferenceFromUrl(image);
                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("success photoref");
                        // File deleted successfully
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Uh-oh, an error occurred!
                        System.out.println("failure storagereference!");
                    }
                });

            }
        }

        mDatabaseReference.child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String parent = snapshot.getKey();

                    mDatabaseReference.child("Products").child(parent).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (final DataSnapshot datasnapshot: dataSnapshot.getChildren()){
                                String UID = datasnapshot.getKey();
                                System.out.println("UID is" + UID);
                                String card_name = datasnapshot.child("cardname").getValue(String.class);
                                System.out.println(card_name);
                                System.out.println("model.getcardname " + model.getCardname());

                                if (card_name!=null) {

                                    if (card_name.equals(model.getCardname())) {
                                        System.out.println("category is +" + category);
                                        mDatabaseReference.child("Products").child(category).child(UID).removeValue();

                                    }
                                }





                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}