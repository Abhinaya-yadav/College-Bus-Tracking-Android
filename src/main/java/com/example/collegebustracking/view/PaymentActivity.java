package com.example.collegebustracking.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.cardform.view.CardForm;

import com.example.collegebustracking.R;
import com.example.collegebustracking.dao.DAO;
import com.example.collegebustracking.form.Student;
import com.example.collegebustracking.util.Constants;
import com.example.collegebustracking.util.Session;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class PaymentActivity extends AppCompatActivity {

    CardForm cardForm;
    Button buy;
    AlertDialog.Builder alertBuilder;
    TextView t1,t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        final Session s = new Session(getApplicationContext());

        t1 = (TextView) findViewById(R.id.totalamt);
        t2 = (TextView) findViewById(R.id.paymentaccno);

        final String cost = "30000";

        t1.setText("Cost:"+cost);
        t2.setText("College Account Number: 1111 2222 3333 4444");

        cardForm = findViewById(R.id.card_form);
        buy = findViewById(R.id.btnBuy);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS is required on this number")
                .setup(PaymentActivity.this);
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardForm.isValid()) {
                    alertBuilder = new AlertDialog.Builder(PaymentActivity.this);
                    alertBuilder.setTitle("Confirm before purchase");
                    alertBuilder.setMessage("Card number: " + cardForm.getCardNumber() + "\n" +
                            "Card expiry date: " + cardForm.getExpirationDateEditText().getText().toString() + "\n" +
                            "Card CVV: " + cardForm.getCvv() + "\n" +
                            "Postal code: " + cardForm.getPostalCode() + "\n" +
                            "Phone number: " + cardForm.getMobileNumber());
                    alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();

                            DAO d=new DAO();
                            d.getDBReference(Constants.STUDENT_DB).child(s.getusername()).addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    Student student=dataSnapshot.getValue(Student.class);

                                    if(student!=null)
                                    {
                                        if(student.getIsPaid().equals("no")) {

                                            student.setIsPaid("yes");

                                            d.addObject(Constants.STUDENT_DB, student, student.getRollnumber());
                                            Toast.makeText(getApplicationContext(), "Payment Done Successfully", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(getApplicationContext(), StudentHome.class);
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(), "Your all ready paid Fee", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                    alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), "Please complete the form", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}