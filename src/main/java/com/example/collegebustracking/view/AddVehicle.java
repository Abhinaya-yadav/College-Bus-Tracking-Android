package com.example.collegebustracking.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.collegebustracking.R;
import com.example.collegebustracking.dao.DAO;
import com.example.collegebustracking.form.Vehicle;
import com.example.collegebustracking.util.Constants;
import com.example.collegebustracking.util.Session;

public class AddVehicle extends AppCompatActivity {

    EditText e1,e2,e3,e4,e5,e6;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_vehicle);

        final Session s=new Session(getApplicationContext());

        e1=(EditText)findViewById(R.id.addvehicleno);
        e2=(EditText)findViewById(R.id.addvehicleusername);
        e3=(EditText)findViewById(R.id.addvehiclepassword);
        e4=(EditText)findViewById(R.id.addvehiclemobilenumber);

        e5=(EditText)findViewById(R.id.addvehiclesource);
        e6=(EditText)findViewById(R.id.addvehicledestination);

        b1=(Button)findViewById(R.id.addVehicleButton);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String vehicleno=e1.getText().toString();
                final String username=e2.getText().toString();
                final String password=e3.getText().toString();
                final String mobileno=e4.getText().toString();
                final String source=e5.getText().toString();
                final String destination=e6.getText().toString();

                if(vehicleno==null|| username==null|| password==null|| mobileno==null || source==null||destination==null) {
                    Toast.makeText(getApplicationContext(), "Please Enter Valid Data", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Vehicle vehicle=new Vehicle();
                    vehicle.setVehicleno(vehicleno);
                    vehicle.setUsername(username);
                    vehicle.setPassword(password);
                    vehicle.setMobile(mobileno);
                    vehicle.setSource(source);
                    vehicle.setDestination(destination);

                    DAO dao=new DAO();

                    try
                    {
                        dao.addObject(Constants.VEHICLE_DB,vehicle,vehicle.getVehicleno());
                        Toast.makeText(getApplicationContext(),"Vehicle Added Success",Toast.LENGTH_SHORT).show();

                        Intent i=new Intent(getApplicationContext(),AdminHome.class);
                        startActivity(i);
                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(getApplicationContext(),"Vehicle Adding Error",Toast.LENGTH_SHORT).show();
                        Log.v("Vehicle Adding  Ex", ex.toString());
                        ex.printStackTrace();
                    }

                }
            }
        });
    }
}
