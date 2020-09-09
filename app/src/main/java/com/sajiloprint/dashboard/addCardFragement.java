package com.sajiloprint.dashboard;

/**
 * Created by kshitij on 16/1/18.
 */

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.sajiloprint.dashboard.models.CardsModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class addCardFragement extends Fragment implements View.OnClickListener {

    private DatabaseReference mDatabaseReference;
    private TextInputEditText cardName;
    private TextInputEditText cardimage;
    private TextInputEditText carddesc;


    private Button bSubmit;
    private String category;

    public addCardFragement(String category) {
        this.category=category;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.card_fragment,container,false);

        cardName = v.findViewById(R.id.tiet_movie_name);
        cardimage = v.findViewById(R.id.tiet_movie_logo);
        carddesc = v.findViewById(R.id.description);
        bSubmit = v.findViewById(R.id.b_submit);
//        category = "Cards";


        //initializing database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        bSubmit.setOnClickListener(this);

        return v;
    }



    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.b_submit:
                if(!isEmpty(cardName) && !isEmpty(cardimage) && !isEmpty(carddesc)){
                    myNewCard(cardName.getText().toString().trim(),cardimage.getText().toString(),carddesc.getText().toString());
                }else{
                    if(isEmpty(cardName)){
                        Toast.makeText(getContext(), "Please enter a name!", Toast.LENGTH_SHORT).show();
                    }else if(isEmpty(cardimage)){
                        Toast.makeText(getContext(), "Please specify a url for the image", Toast.LENGTH_SHORT).show();
                    }else if(isEmpty(carddesc)){
                        Toast.makeText(getContext(), "Please enter description", Toast.LENGTH_SHORT).show();
                    }
                }
                //to remove current fragment
                getActivity().onBackPressed();
                break;
        }
    }

    private void myNewCard(String name, String image, String desc) {
        //Creating a movie object with user defined variables
        CardsModel movie = new CardsModel(name,image,desc);
        //referring to movies node and setting the values from movie object to that location
        mDatabaseReference.child("Products").child(category).push().setValue(movie);
    }

    //check if edittext is empty
    private boolean isEmpty(TextInputEditText textInputEditText) {
        if (textInputEditText.getText().toString().trim().length() > 0)
            return false;
        return true;
    }
}