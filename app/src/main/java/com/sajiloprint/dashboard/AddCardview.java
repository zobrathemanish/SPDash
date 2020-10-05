package com.sajiloprint.dashboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.sajiloprint.dashboard.models.CardsModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sajiloprint.dashboard.models.Orderbydateadapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AddCardview extends AppCompatActivity {


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
    Orderbydateadapter searchAdapter;



    //Getting reference to Firebase Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();

    private String category;
    private static final String userId = "53";
    private String card;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        if (bundle!=null)
            card = bundle.getString("show");

        tvNoMovies = (TextView) findViewById(R.id.tv_no_cards);




        if (card!=null)
            category=card;

        shrinkAnim = new ScaleAnimation(1.15f, 0f, 1.15f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        if (mRecyclerView != null) {
            //to enable optimization of recyclerview
            mRecyclerView.setHasFixedSize(true);
        }
        //using staggered grid pattern in recyclerview
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentuser = mAuth.getCurrentUser().getEmail();
        if(currentuser.equals("sajiloprint@gmail.com") || currentuser.equals("manishofficial4378@gmail.com") || currentuser.equals("opensoft.tech110@gmail.com") ){
                fab.setVisibility(View.VISIBLE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, new addCardFragement(category))
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
                R.layout.cards_cardview_layout,
                MovieViewHolder.class,
                //referencing the node where we want the database to store the data from our Object
                mDatabaseReference.child("Products").child(category).getRef()
        ) {
            @Override
            protected void populateViewHolder(MovieViewHolder viewHolder, final CardsModel model, final int position) {
                if(tvNoMovies.getVisibility()== View.VISIBLE){
                    tvNoMovies.setVisibility(View.GONE);
                }
//                if (model.getShopEmail)
                viewHolder.cardcategory.setText(model.getCardname());
                Picasso.with(AddCardview.this).load(model.getCardimage()).config(Bitmap.Config.RGB_565).resize(800, 600).into(viewHolder.cardimage);

                if (model.getCardname().equals("Photo prints")||model.getCardname().equals("Photobooks") || model.getCardname().equals("Framed prints")
                || model.getCardname().equals("Magnets") || model.getCardname().equals("Wood Prints") || model.getCardname().equals("Posters")){
                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(AddCardview.this, AddPhotoSubCard.class);
                            intent.putExtra("cardname", model.getCardname());

                            startActivity(intent);
                        }
                    });

                }
                else {

                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(AddCardview.this, AddSubCard.class);
                            intent.putExtra("cardname", model.getCardname());

                            startActivity(intent);
                        }
                    });
                }
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


        View mView;


        public MovieViewHolder(View v) {
            super(v);
            mView =v;
            cardcategory = (TextView) v.findViewById(R.id.cardcategory);
            cardimage = (ImageView) v.findViewById(R.id.cardimage);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}