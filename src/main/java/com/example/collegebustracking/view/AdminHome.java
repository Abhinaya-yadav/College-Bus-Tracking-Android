package com.example.collegebustracking.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.collegebustracking.MainActivity;
import com.example.collegebustracking.R;
import com.example.collegebustracking.dao.DAO;
import com.example.collegebustracking.form.Rating;
import com.example.collegebustracking.form.Student;
import com.example.collegebustracking.util.Constants;
import com.example.collegebustracking.util.Session;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class AdminHome extends AppCompatActivity {

    private Session session;

    Button adminLogout;
    Button addvehicles;
    Button viewvehicles;

//    Button addStudent;
    Button viewStudents;

    Button adminrestfees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        viewvehicles=(Button) findViewById(R.id.adminviewvehicles);
        addvehicles=(Button)findViewById(R.id.adminaddvehicle);
        adminrestfees=(Button)findViewById(R.id.adminrestfees);
        adminLogout=(Button) findViewById(R.id.adminlogout);

//        addStudent=(Button) findViewById(R.id.addstudent);
        viewStudents=(Button) findViewById(R.id.viewstudents);

        final Session s = new Session(getApplicationContext());

        viewvehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ListVehicles.class);
                startActivity(i);
            }
        });

        addvehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),AddVehicle.class);
                startActivity(i);
            }
        });

//        addStudent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(),AddStudent.class);
//                startActivity(i);
//            }
//        });

        viewStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),ListStudents.class);
                startActivity(i);
            }
        });


        viewStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),ListStudents.class);
                startActivity(i);
            }
        });

        adminrestfees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DAO dao=new DAO();
                dao.getDBReference(Constants.STUDENT_DB).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshotNode: dataSnapshot.getChildren()) {

                            Student student =snapshotNode.getValue(Student.class);

                            if(student!=null)
                            {
                                student.setIsPaid("no");
                                dao.getDBReference(Constants.STUDENT_DB).child(student.getRollnumber()).setValue(student);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            }
        });

        adminLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                s.loggingOut();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
    }
}