package com.sajiloprint.dashboard.models;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.airbnb.lottie.LottieAnimationView;
//import com.beingdev.magicprint.models.GenericProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sajiloprint.dashboard.R;
import java.util.ArrayList;

public class SearchBarActivity extends AppCompatActivity {
    EditText search_edit_text;
    RecyclerView recyclerView;
    DatabaseReference databasereference;
    FirebaseUser firebaseUser;
    ArrayList<String> cardNameList;
   // ArrayList<GenericProductModel> ItemList;
    SearchAdapter searchAdapter;
    private LottieAnimationView tv_no_item;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_edit_text = (EditText)findViewById(R.id.search_product_name);
        recyclerView = (RecyclerView)findViewById(R.id.search_list);
        databasereference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (recyclerView != null) {
            //to enable optimization of recyclerview
            recyclerView.setHasFixedSize(true);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        cardNameList = new ArrayList<>();

        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()){
                    setAdapter(editable.toString());
                } else{
                    cardNameList.clear();
                    recyclerView.removeAllViews();
                }
            }
        });
    }

    private void setAdapter(final String searchedString) {


        final int[] counter = {0};

        databasereference.child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cardNameList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String parent = snapshot.getKey();
                    cardNameList.add(parent);

                    if (parent.toLowerCase().contains(searchedString.toLowerCase())) {
//                                if (card_id == null && card_price == null) {
//                                    card_id = 4378;
//                                    card_price = 4378 / (float) card_id;
//                                }
//                                CardsModel model = new CardsModel(card_id, card_name, card_image, card_desc, card_price);
//                                cardNameList.add(card_name);
//                                cardDescList.add(card_desc);
//                                cardImageList.add(card_image);
//                                ItemList.add(model);
                        cardNameList.add(parent);
                        System.out.println("is this the card name? " + parent);



                        counter[0]++;

                    }
                    if (counter[0] == 15)
                        break;

                }

                searchAdapter = new SearchAdapter(SearchBarActivity.this,cardNameList);
                recyclerView.setAdapter(searchAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
    }
}
