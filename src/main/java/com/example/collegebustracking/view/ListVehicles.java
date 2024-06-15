package com.example.collegebustracking.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.collegebustracking.R;
import com.example.collegebustracking.dao.DAO;
import com.example.collegebustracking.form.Vehicle;
import com.example.collegebustracking.util.Constants;
import com.example.collegebustracking.util.Session;

public class ListVehicles extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_vehicles);

        listView = (ListView) findViewById(R.id.VehiclesList);

        final Session s = new Session(getApplicationContext());

        DAO dao = new DAO();
        dao.setDataToAdapterList(listView, Vehicle.class, Constants.VEHICLE_DB);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String item = listView.getItemAtPosition(i).toString();
                Intent intent = new Intent(getApplicationContext(), ViewVehicle.class);
                intent.putExtra("vehicleno", item);
                startActivity(intent);
            }
        });
    }
}
