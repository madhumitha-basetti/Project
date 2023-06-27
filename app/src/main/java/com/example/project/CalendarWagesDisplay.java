package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class CalendarWagesDisplay extends AppCompatActivity {

    TextView Date,textView,head;
    CalendarView calendarView;
    Button submit;

    int paid,pending,total,diesel;
    String d,data="",fuel="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_wages_display);
        // head=findViewById(R.id.headings);
        Date=findViewById(R.id.date);
        submit=findViewById(R.id.submit_but);
        textView=findViewById(R.id.txt_view);
        calendarView=findViewById(R.id.cal_view);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                d=i2+"-"+(i1+1)+"-"+i;
                Date.setText(d);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                data = "Name\t\t\t\tItem\t\t\t\tQuantity\t\t\tAmount\t\tStatus\n";
                paid=0;
                total=0;
                pending=0;
                diesel=0;
                //head.setText("Name\t\t\t\tItem\t\t\t\tQuantity\t\t\tAmount\t\t\tStatus\t\t\t");

                FirebaseFirestore.getInstance().collection(d+" worker").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException error) {

                        for (DocumentSnapshot snapshot : documentSnapshots) {

                            if (!documentSnapshots.isEmpty()) {
                                data = data + "\t" + snapshot.getString("Name") + "\t\t\t\t" + snapshot.getString("Item") + "\t\t\t\t" + snapshot.getString("Quantity") + "\t\t\t\t\t\t\t" + snapshot.getString("Amount") + "\t\t\t\t\t" + snapshot.getString("Status") + "\n\n";

                                if (snapshot.getString("Status").equals("Unpaid")) {
                                    pending = pending + Integer.parseInt(snapshot.getString("Amount"));
                                } else if (snapshot.getString("Status").equals("Paid")) {
                                    paid = paid + Integer.parseInt(snapshot.getString("Amount"));
                                }
                                total = total + Integer.parseInt(snapshot.getString("Amount"));

                            } else {
                                data = "";
                            }
                        }



                        if (total == 0) {
                            textView.setText("\n\n\n\n\n\t\t\t\t\t\t\tNo Data Found!");
                        } else {
                            FirebaseFirestore.getInstance().collection("Diesel").document(d).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    fuel= documentSnapshot.getString("Fuel");
                                    diesel=diesel+Integer.parseInt(fuel);
                                    textView.setText(data + "\n\n\tTotal Amount =  " + total + "\n\n\tPaid =  " + paid + "\n\n\tPending Amount =  " + pending + "\n\n\tFuel = " +diesel+ "\n\n");
                                }
                            });
                           // Toast.makeText(CalendarWagesDisplay.this,String.valueOf(diesel),Toast.LENGTH_SHORT).show();
                           // Toast.makeText(CalendarWagesDisplay.this, fuel, Toast.LENGTH_SHORT).show();
                           // textView.setText(data + "\n\n\tTotal Amount =  " + total + "\n\n\tPaid =  " + paid + "\n\n\tPending Amount =  " + pending + "\n\n\tFuel = " +diesel+ "\n\n");

                        }
                    }

                });
            }
        });

    }


}