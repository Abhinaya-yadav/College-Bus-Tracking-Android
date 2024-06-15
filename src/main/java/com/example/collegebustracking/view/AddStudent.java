package com.example.collegebustracking.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.collegebustracking.MainActivity;
import com.example.collegebustracking.R;
import com.example.collegebustracking.dao.DAO;
import com.example.collegebustracking.form.Student;
import com.example.collegebustracking.util.Constants;

public class AddStudent extends AppCompatActivity {

    EditText e1,e2,e3,e4,e5;
    RadioGroup rg1,rg2;
    RadioButton r1;

    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_student);

        e1 = (EditText) findViewById(R.id.studentRollNumber);
        e2 = (EditText) findViewById(R.id.studentPassword);
        e3 = (EditText) findViewById(R.id.studentConPassword);
        e4 = (EditText) findViewById(R.id.studentName);
        e5 = (EditText) findViewById(R.id.studentSection);

        rg1 = (RadioGroup) findViewById(R.id.radiogroupbranch);
        rg2 = (RadioGroup) findViewById(R.id.radiogroupyear);


        b1 = (Button) findViewById(R.id.registerButton);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String rollnumber = e1.getText().toString();
                String password = e2.getText().toString();
                String cpassword = e3.getText().toString();
                String name = e4.getText().toString();
                String section = e5.getText().toString();

                String branch = "";
                int selectedBranch = rg1.getCheckedRadioButtonId();
                r1 = (RadioButton) findViewById(selectedBranch);
                branch = r1.getText().toString();

                int year = 1;
                int selectedYear = rg2.getCheckedRadioButtonId();
                r1 = (RadioButton) findViewById(selectedYear);
                year = Integer.parseInt(r1.getText().toString());

                if (password.length() <= 7 || cpassword.length() <= 7 || name == "" || branch == "" || section=="" || rollnumber=="") {
                    Toast.makeText(getApplicationContext(), "Missing Inputs", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(cpassword)) {
                    Toast.makeText(getApplicationContext(), "Password Mismatch", Toast.LENGTH_SHORT).show();
                } else {

                    Student s = new Student();

                    s.setRollnumber(rollnumber);
                    s.setPassword(password);
                    s.setName(name);
                    s.setBranch(branch);
                    s.setYear(year+"");
                    s.setSection(section);
                    s.setIsPaid("no");

                    DAO d = new DAO();

                    try {

                        d.getDBReference(Constants.STUDENT_DB).child(s.getRollnumber()).setValue(s);

                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), "Register Error", Toast.LENGTH_SHORT).show();
                        Log.v("Student Registration Ex", ex.toString());
                        ex.printStackTrace();
                    }

                }
            }
        });
    }
}