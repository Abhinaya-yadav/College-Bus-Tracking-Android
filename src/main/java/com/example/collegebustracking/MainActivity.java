package com.example.collegebustracking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.collegebustracking.R;
import com.example.collegebustracking.view.AddStudent;
import com.example.collegebustracking.view.LoginActivity;


//voidmain.vehicletracking@gmail.com

public class MainActivity extends AppCompatActivity {

    Button b1;
    Button b2;

    Button b3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button) findViewById(R.id.loginbutton);
        b2 = (Button) findViewById(R.id.studentloginbutton);
        b3 = (Button) findViewById(R.id.studentregisterbutton);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.putExtra("ltype","admin");
                startActivity(i);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.putExtra("ltype","student");
                startActivity(i);
            }
        });

        b3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(getApplicationContext(), AddStudent.class);
                startActivity(i);
            }
        });
    }
}
