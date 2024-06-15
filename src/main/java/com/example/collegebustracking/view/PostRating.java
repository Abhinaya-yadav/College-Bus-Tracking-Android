package com.example.collegebustracking.view;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.collegebustracking.R;
import com.example.collegebustracking.form.Rating;
import com.example.collegebustracking.util.Session;
import com.example.collegebustracking.dao.DAO;
import com.example.collegebustracking.util.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class PostRating extends AppCompatActivity {

    EditText sendTextRating;

    Button postRatingSubmit;
    Button postRatingCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post_rating);

        final Session s = new Session(getApplicationContext());

        sendTextRating = (EditText) findViewById(R.id.sendTextRating);

        postRatingSubmit = (Button) findViewById(R.id.postRatingSubmit);
        postRatingCancel = (Button) findViewById(R.id.postRatingCancel);

        Intent i = getIntent();
        savedInstanceState = i.getExtras();
        final String vehicleid = savedInstanceState.getString("vehicleno");

        postRatingSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ratingText=sendTextRating.getText().toString();

                if (ratingText.length()==0) {
                    Toast.makeText(getApplicationContext(), "Please Enter rating", Toast.LENGTH_SHORT).show();
                } else {

                    final DAO dao=new DAO();
                    dao.getDBReference(Constants.RATING).addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            boolean isRated=false;
                            String rateid="";

                            for (DataSnapshot snapshotNode: dataSnapshot.getChildren()) {

                                String key=snapshotNode.getKey();
                                Rating rating=snapshotNode.getValue(Rating.class);

                                if(rating!=null)
                                {
                                    if(rating.getSender().equals(s.getusername()) && rating.getVehicleid().equals(vehicleid))
                                    {
                                        isRated=true;
                                        rateid=key;
                                    }
                                }
                            }

                            if(!isRated)
                            {
                                Rating rating=new Rating();

                                rating.setRatingId(dao.getUnicKey(Constants.RATING));
                                rating.setSender(s.getusername());
                                rating.setRating(ratingText);
                                rating.setVehicleid(vehicleid);

                                try {

                                    dao.addObject(Constants.RATING,rating, rating.getRatingId());

                                    Intent i = new Intent(getApplicationContext(),StudentHome.class);
                                    startActivity(i);

                                    Toast.makeText(getApplicationContext(), "Rating Sent Successfully", Toast.LENGTH_SHORT).show();

                                } catch (Exception ex) {
                                    Toast.makeText(getApplicationContext(), "Sending Failed", Toast.LENGTH_SHORT).show();
                                    Log.v("complaint failed", ex.toString());
                                    ex.printStackTrace();
                                }
                            }
                            else
                            {
                                DAO dao = new DAO();
                                dao.getDBReference(Constants.RATING).child(rateid).addListenerForSingleValueEvent(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        Rating rating = dataSnapshot.getValue(Rating.class);

                                        if (rating != null) {

                                            rating.setRating(ratingText);
                                            dao.addObject(Constants.RATING,rating,rating.getRatingId());

                                            Intent i = new Intent(getApplicationContext(),StudentHome.class);
                                            startActivity(i);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });
                }
            }
        });

        postRatingCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), StudentHome.class);
            }
        });
    }
}
