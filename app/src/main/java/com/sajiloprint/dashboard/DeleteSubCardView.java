package com.sajiloprint.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sajiloprint.dashboard.models.CardsModel;
import com.sajiloprint.dashboard.models.SearchAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DeleteSubCardView extends AppCompatActivity {

    //created for firebaseui android tutorial by Vamsi Tallapudi
    EditText search_edit_text;

    private FloatingActionButton fab;
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

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, new deleteCardFragment())
                        .addToBackStack(null)
                        .commit();
                //animation being used to make floating actionbar disappear
                shrinkAnim.setDuration(400);
                fab.setAnimation(shrinkAnim);
                shrinkAnim.start();
                shrinkAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //changing floating actionbar visibility to gone on animation end
                        fab.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {


                    }
                });
            }
        });

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
        FirebaseRecyclerAdapter<CardsModel,MovieViewHolder> adapter = new FirebaseRecyclerAdapter<CardsModel, MovieViewHolder>(
                CardsModel.class,
                R.layout.list_image_single,
                MovieViewHolder.class,
                //referencing the node where we want the database to store the data from our Object
                mDatabaseReference.child("Products").child(category).getRef()
        ) {
            @Override
            protected void populateViewHolder(final MovieViewHolder viewHolder, final CardsModel model, final int position) {

                if(tvNoMovies.getVisibility()== View.VISIBLE){
                    tvNoMovies.setVisibility(View.GONE);
                }
                viewHolder.cardcategory.setText(model.getCardname());
                Picasso.with(DeleteSubCardView.this).load(model.getCardimage()).into(viewHolder.cardimage);

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DeleteSubCardView.this, DeleteSubCard.class);
                        intent.putExtra("cardname",model.getCardname());

                        startActivity(intent);
                    }

            });
                viewHolder.carddelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(ImageRecyclerAdapter.this,getItem(position).getPrname(),Toast.LENGTH_SHORT).show();
                        removeAt(model.getCardname());
                        //session.decreaseCartValue();
                        //mContext.startActivity(new Intent(ImageRecyclerAdapter.this,ImageRecyclerAdapter.this));
                    }
                });



            }
        };

        mRecyclerView.setAdapter(adapter);

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fab.getVisibility() == View.GONE)
            fab.setVisibility(View.VISIBLE);
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

    public void removeAt(final String cardname) {
//        ImageList.remove(position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, ImageList.size());

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
                                String card_name = datasnapshot.child("cardname").getValue(String.class);
                                System.out.println(card_name);
                                if(card_name.toLowerCase().contains(cardname.toLowerCase())) {
                                    System.out.println("category is +"+ category);
                                    System.out.println("UID is" + UID);
                                    mDatabaseReference.child("Products").child(category).child(UID).removeValue();

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