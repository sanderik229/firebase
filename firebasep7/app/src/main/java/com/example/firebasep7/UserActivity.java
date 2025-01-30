package com.example.firebasep7;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserActivity extends AppCompatActivity {

    private ListView servicesListView;

    private Button bookServices;

    private FirebaseFirestore db;

    private  List<String> servicesList;

    private List<String> idServices;

    private ArrayAdapter<String> adapter;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);

        db = FirebaseFirestore.getInstance();
        servicesListView=findViewById(R.id.servicesListView);
        bookServices=findViewById(R.id.bookServiceButton);
        servicesList= new ArrayList<>();
        idServices= new ArrayList<>();
        adapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, servicesList);
        servicesListView.setAdapter(adapter);
        calendar=Calendar.getInstance();

        servicesListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        loadServices();

        bookServices.setOnClickListener(view -> {
            int selectedPosition=servicesListView.getCheckedItemPosition();
            if(selectedPosition !=ListView.INVALID_POSITION){
                String servicedIds=idServices.get(selectedPosition);
                showDateTimePickerDialog(servicedIds);
            }else {
                Toast.makeText(UserActivity.this, "Пожаоуйста выберите услугу", Toast.LENGTH_SHORT).show();
            }
        });

    }



    private void loadServices() {

        db.collection("services").get()
                .addOnSuccessListener(queryDocumentSnapshots ->  {
                    servicesList.clear();
                    idServices.clear();

                    for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                        String serviceName=documentSnapshot.getString("servicesName");
                        String serviceId=documentSnapshot.getId();
                        servicesList.add(serviceName);
                        servicesList.add(serviceId);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Ошибка загрузки услуг", Toast.LENGTH_SHORT).show();

    });


}
    private void showDateTimePickerDialog(String servicedIds) {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Выбирите дату и время");
        View view = getLayoutInflater().inflate(R.layout.dialog_date_time_picker, null);
        builder.setView(view);
        TextView datafield=view.findViewById(R.id.dateField);
        TextView timefield=view.findViewById(R.id.timeField);
        datafield.setOnClickListener(view1 -> showDatePickerDialog(datafield));
        timefield.setOnClickListener(view1 -> showTimePickerDialog(timefield));

        builder.setPositiveButton("Записаться",(dialog, which) -> {
            String date=datafield.getText().toString().trim();
            String time=timefield.getText().toString().trim();
            if(date.isEmpty() || time.isEmpty()){
                Toast.makeText(UserActivity.this, "Заполните дату и время", Toast.LENGTH_SHORT).show();
                return;
            }
            bookServices(servicedIds,date,time);
        });
        builder.setNegativeButton("Отмена",(dialog, which) -> dialog.dismiss());

        builder.create().show();


    }



    private void showTimePickerDialog(TextView timefield) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);

                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }



    private void showDatePickerDialog(TextView datafield) {
        DatePickerDialog datePickerDialog=new DatePickerDialog(this,
                (view,year,month,dayOfMonth)-> {
                    calendar.set(Calendar.YEAR,year);
                    calendar.set(Calendar.MONTH,month);
                    calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();

    }

    private  void bookServices(String servicedIds, String date, String time){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            String clientName=user.getEmail();
            db.collection("services").document(servicedIds).get().addOnSuccessListener(
                    documentSnapshot -> {
                        if (documentSnapshot.exists()){
                            String serviceName=documentSnapshot.getString("serviceName");
                            Map<String, Object> appoinment= new HashMap<>();
                            appoinment.put("clientId",user.getUid());
                            appoinment.put("clientNameId",user.getUid());
                            appoinment.put("serviceId",user.getUid());
                            appoinment.put("serviceName",user.getUid());
                            appoinment.put("date",user.getUid());
                            appoinment.put("time",user.getUid());
                        }

                    });

        }

    }



}


