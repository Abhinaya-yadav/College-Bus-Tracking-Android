package com.example.collegebustracking.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.collegebustracking.R;
import com.example.collegebustracking.dao.DAO;
import com.example.collegebustracking.form.Rating;
import com.example.collegebustracking.form.Vehicle;
import com.example.collegebustracking.form.VehicleLocation;
import com.example.collegebustracking.util.Constants;
import com.example.collegebustracking.util.Session;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ViewVehicle extends AppCompatActivity {

    TextView t1,t2,t3,t4,t5,t6,t7;

    Button back;
    Button delete;
    Button rating;
    Button vehicleLocation;

    String source;
    String destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vehicle);

        back=(Button) findViewById(R.id.viewvehicleback);
        delete=(Button) findViewById(R.id.viewVehicleDelete);
        rating=(Button) findViewById(R.id.viewVehiclePostRating);
        vehicleLocation=(Button) findViewById(R.id.viewVehicleLocation);

        final Session session=new Session(getApplicationContext());
        final String role=session.getRole();

        Intent i=getIntent();
        savedInstanceState=i.getExtras();

        final String vehicleno=savedInstanceState.getString("vehicleno");

        t1=(TextView) findViewById(R.id.viewvehicleno);
        t2=(TextView)findViewById(R.id.viewvehicleusername);


        t3=(TextView) findViewById(R.id.viewvehiclepassword);
        t4=(TextView)findViewById(R.id.viewvehiclemobile);
        t5=(TextView)findViewById(R.id.viewvehiclerating);
        t6=(TextView)findViewById(R.id.viewvehiclesource);
        t7=(TextView)findViewById(R.id.viewvehicledestination);

        if(session.getRole().equals("admin"))
        {
            rating.setEnabled(false);
        }

        if(session.getRole().equals("student"))
        {
            delete.setEnabled(false);
        }

        DAO d=new DAO();
        d.getDBReference(Constants.VEHICLE_DB).child(vehicleno).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Vehicle vehicle=dataSnapshot.getValue(Vehicle.class);

                if(vehicle!=null)
                {
                    t1.setText("Vehicle NO :"+vehicle.getVehicleno());
                    t2.setText("Driver UserName :"+vehicle.getUsername());


                    if(session.getRole().equals("admin"))
                    {
                        t3.setText("Driver Password :"+vehicle.getPassword());
                    }

                    t4.setText("Driver Mobile NO :"+vehicle.getMobile());
                    t6.setText("Source :"+vehicle.getSource());
                    t7.setText("Destination :"+vehicle.getDestination());
                    source=vehicle.getSource();
                    destination=vehicle.getDestination();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final DAO dao=new DAO();
        dao.getDBReference(Constants.RATING).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int count=0;
                int sum=0;

                for (DataSnapshot snapshotNode: dataSnapshot.getChildren()) {

                    Rating rating =snapshotNode.getValue(Rating.class);

                    if(rating!=null)
                    {
                        if(rating.getVehicleid().equals(vehicleno))
                        {
                            count=count+1;
                            sum=sum+Integer.parseInt(rating.getRating());
                        }
                    }
                }

                if(sum!=0 && count!=0)
                {
                    t5.setText("Rating (out of 10 is ):"+(sum/count));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(session.getRole().equals("admin"))
                {
                    Intent i=new Intent(getApplicationContext(),AdminHome.class);
                    startActivity(i);
                }
                else if (session.getRole().equals("student"))
                {
                    Intent i=new Intent(getApplicationContext(),StudentHome.class);
                    startActivity(i);
                }
            }
        });

        vehicleLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DAO d=new DAO();
                d.getDBReference(Constants.LOCATION_DB).child(vehicleno).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        VehicleLocation vehicleLocation=dataSnapshot.getValue(VehicleLocation.class);

                        if(vehicleLocation!=null)
                        {
                            String vlat=vehicleLocation.getLatitude();
                            String vlong=vehicleLocation.getLongitude();

                            Toast.makeText(getApplicationContext(),source,Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(),destination,Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(),vlat+","+vlong,Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(getApplicationContext(),DirectionsActivity.class);
                            intent.putExtra("source",source);
                            intent.putExtra("destination",destination);
                            intent.putExtra("buslocation",vlat+","+vlong);

                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(getApplicationContext(),PostRating.class);
                i.putExtra("vehicleno", vehicleno);
                startActivity(i);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DAO d=new DAO();
                d.deleteObject(Constants.VEHICLE_DB,vehicleno);

                Intent i=new Intent(getApplicationContext(),AdminHome.class);
                startActivity(i);
            }
        });
    }
}
