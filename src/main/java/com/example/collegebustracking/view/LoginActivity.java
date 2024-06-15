package com.example.collegebustracking.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.collegebustracking.form.Student;
import com.example.collegebustracking.form.Vehicle;
import com.example.collegebustracking.dao.DAO;
import com.example.collegebustracking.util.Constants;
import com.example.collegebustracking.util.Session;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import com.example.collegebustracking.R;

public class LoginActivity extends AppCompatActivity {

    private Session session;
    EditText e1,e2;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(getApplicationContext());

        setContentView(R.layout.activity_login);

        e1=(EditText)findViewById(R.id.loginid);
        e2=(EditText)findViewById(R.id.loginPass);
        b1=(Button)findViewById(R.id.loginConfirm);

        Intent i=getIntent();
        savedInstanceState=i.getExtras();

        final String ltype=savedInstanceState.getString("ltype");

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String username=e1.getText().toString();
                final String password=e2.getText().toString();

                if(username==null|| password==null || username.length()<=0|| password.length()<=0)
                {
                    Toast.makeText(getApplicationContext(),"Please Enter UserName and Password",Toast.LENGTH_SHORT).show();
                }
                else {

                    if(ltype.equals("admin"))
                    {
                        if (username.equals("admin") && password.equals("admin")) {

                            session.setusername("admin");
                            session.setRole("admin");

                            Intent i = new Intent(getApplicationContext(), AdminHome.class);
                            startActivity(i);

                        } else {

                            DAO d = new DAO();
                            d.getDBReference(Constants.VEHICLE_DB).child(username).addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    Vehicle vehicle=(Vehicle) dataSnapshot.getValue(Vehicle.class);

                                    if (vehicle == null) {
                                        Toast.makeText(getApplicationContext(), "Invalid UserName ", Toast.LENGTH_SHORT).show();
                                    } else if (vehicle != null && vehicle.getPassword().equals(password)) {

                                        session.setusername(vehicle.getVehicleno());
                                        session.setRole("vehicle");

                                        Intent i = new Intent(getApplicationContext(),LocationUpdater.class);
                                        startActivity(i);

                                    } else {
                                        Toast.makeText(getApplicationContext(), "In valid Password", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                    else if (ltype.equals("student"))
                    {
                        DAO d = new DAO();
                        d.getDBReference(Constants.STUDENT_DB).child(username).addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                Student student =(Student) dataSnapshot.getValue(Student.class);

                                if (student == null) {
                                    Toast.makeText(getApplicationContext(), "Invalid UserName ", Toast.LENGTH_SHORT).show();
                                } else if (student != null && student.getPassword().equals(password)) {

                                    session.setusername(username);
                                    session.setRole("student");

                                    Intent i = new Intent(getApplicationContext(),StudentHome.class);
                                    startActivity(i);

                                } else {
                                    Toast.makeText(getApplicationContext(), "In valid Password", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
        });
    }
}
