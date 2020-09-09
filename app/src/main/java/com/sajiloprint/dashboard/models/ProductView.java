package com.sajiloprint.dashboard.models;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.sajiloprint.dashboard.AddCardview;
import com.sajiloprint.dashboard.DeleteCardView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sajiloprint.dashboard.R;

public class ProductView extends AppCompatActivity {

    //created for firebaseui android tutorial by Vamsi Tallapudi

    private FloatingActionButton fab;

    ScaleAnimation shrinkAnim;
    private StaggeredGridLayoutManager mLayoutManager;
    private TextView tvNoMovies;

    //Getting reference to Firebase Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();

    private String category;
    private MaterialSpinner spinner;
    private TextView cattv;
    private static final String userId = "53";
    private Button bSubmit;
    private String addordelete;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productview);

        Toolbar toolbar = findViewById(R.id.toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null)
            addordelete = bundle.getString("value");
        System.out.println("The card is" + addordelete);

        //Initializing our Recyclerview
        cattv = findViewById(R.id.cattext);
        tvNoMovies = (TextView) findViewById(R.id.tv_no_cards);
        category = "Cards";
        spinner = findViewById(R.id.categoryspinner);
        bSubmit = findViewById(R.id.b_submit);

        spinner.setItems("Cards", "CorporateGifts", "Calendar", "Stationary", "WallDecors", "Wearables","Photogifts","Awards");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                category = item;

            }
        });


        //scale animation to shrink floating actionbar
        shrinkAnim = new ScaleAnimation(1.15f, 0f, 1.15f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.b_submit) {
                    myNewCard();
                }
            }
        });




    }

    private void myNewCard() {
        //Creating a movie object with user defined variables
        //referring to movies node and setting the values from movie object to that location
        if(addordelete!=null){
            Intent intent = new Intent(ProductView.this, AddCardview.class);
            System.out.println("The category here is" + category);
            intent.putExtra("show", category);
            startActivity(intent);

        }
        else {

            Intent intent = new Intent(ProductView.this, DeleteCardView.class);
            intent.putExtra("show", category);
            startActivity(intent);
        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if (fab.getVisibility() == View.GONE)
//            fab.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}