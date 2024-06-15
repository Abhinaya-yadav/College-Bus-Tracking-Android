package com.example.collegebustracking.view;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import com.example.collegebustracking.R;
import com.example.collegebustracking.dao.DAO;
import com.example.collegebustracking.form.Student;
import com.example.collegebustracking.util.Constants;
import com.example.collegebustracking.util.Session;

public class ViewStudent extends AppCompatActivity {

    TextView t1,t2,t3,t4,t5,t6;
    Button cancel;
    Button delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);

        cancel=(Button) findViewById(R.id.studentviewback);
        delete=(Button) findViewById(R.id.viewStudentDelete);

        final Session session=new Session(getApplicationContext());
        final String role=session.getRole();

        Intent i=getIntent();
        savedInstanceState=i.getExtras();

        final String studentId=savedInstanceState.getString("studentid");

        t1=(TextView) findViewById(R.id.studentviewrollnumber);
        t2=(TextView)findViewById(R.id.studentviewname);
        t3=(TextView)findViewById(R.id.studentviewbranch);
        t4=(TextView)findViewById(R.id.studentviewyear);
        t5=(TextView)findViewById(R.id.studentviewsection);
        t6=(TextView)findViewById(R.id.studentviewispaid);

        DAO d=new DAO();
        d.getDBReference(Constants.STUDENT_DB).child(studentId).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Student student=dataSnapshot.getValue(Student.class);

                if(student!=null)
                {
                    t1.setText("Roll Number: "+student.getRollnumber());
                    t2.setText("Name: "+student.getName());
                    t3.setText("Branch: "+student.getBranch());
                    t4.setText("Year: "+student.getYear());
                    t5.setText("Section: "+student.getSection());
                    t6.setText("is paid ?: "+student.getIsPaid());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DAO d=new DAO();
                d.deleteObject(Constants.STUDENT_DB,studentId);

                Intent i=new Intent(getApplicationContext(),AdminHome.class);
                startActivity(i);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),AdminHome.class);
                startActivity(i);
            }
        });
    }
}
