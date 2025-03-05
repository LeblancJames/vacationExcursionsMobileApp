package com.example.d308.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308.R;
import com.example.d308.database.Repository;
import com.example.d308.entities.Excursion;
import com.example.d308.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationDetails extends AppCompatActivity {


    String name;
    String hotel;

    String startDate;
    String endDate;

    int vacationID;
    EditText editName;
    EditText editHotel;
    EditText editStartDate;
    EditText editEndDate;

    Repository repository;
    Vacation currentVacation;
    int numExcursions;

    DatePickerDialog.OnDateSetListener myStartDate;
    DatePickerDialog.OnDateSetListener myEndDate;

    final Calendar myCalender=Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FloatingActionButton fab=findViewById(R.id.floatingActionButton4);

        editName=findViewById(R.id.titletext);
        editHotel=findViewById(R.id.hoteltext);
        editStartDate=findViewById(R.id.startdatetext);
        editEndDate=findViewById(R.id.enddatetext);
        name=getIntent().getStringExtra("name");
        hotel=getIntent().getStringExtra("hotel");
        startDate=getIntent().getStringExtra("start date");
        endDate=getIntent().getStringExtra("end date");
        editName.setText(name);
        editHotel.setText(hotel);
        editStartDate.setText(startDate);
        editEndDate.setText(endDate);
        vacationID=getIntent().getIntExtra("id",-1);


        RecyclerView recyclerView = findViewById(R.id.excursionRecyclerView);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e :repository.getAllExcursions()){
            if(e.getVacationID() == vacationID) filteredExcursions.add(e);
        }
        excursionAdapter.setExcursions(filteredExcursions);

        fab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent=new Intent(VacationDetails.this, ExcursionDetails.class);
                intent.putExtra("id", vacationID);
                startActivity(intent);
            }
        });
        String myFormat =  "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date;
                String info=editStartDate.getText().toString();
                try{
                    myCalender.setTime(sdf.parse(info));
                } catch (ParseException e){
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this,myStartDate,myCalender.get(Calendar.YEAR),
                        myCalender.get(Calendar.MONTH), myCalender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        myStartDate=new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                try {
                    String startDateInfo=editStartDate.getText().toString();
                    String endDateInfo=editEndDate.getText().toString();
                    if (sdf.parse(startDateInfo).before(sdf.parse(endDateInfo))) {
                        myCalender.set(Calendar.YEAR, year);
                        myCalender.set(Calendar.MONTH, month);
                        myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateStartLabel();
                    } else {
                        Toast.makeText(view.getContext(), "Start date cannot be after end date!", Toast.LENGTH_LONG).show();
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date;
                String info=editEndDate.getText().toString();
                try{
                    myCalender.setTime(sdf.parse(info));
                } catch (ParseException e){
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this,myEndDate,myCalender.get(Calendar.YEAR),
                        myCalender.get(Calendar.MONTH), myCalender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        myEndDate=new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                try {
                    String startDateInfo=editStartDate.getText().toString();
                    String endDateInfo=editEndDate.getText().toString();
                    if (sdf.parse(startDateInfo).before(sdf.parse(endDateInfo))) {
                        myCalender.set(Calendar.YEAR, year);
                        myCalender.set(Calendar.MONTH, month);
                        myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateEndLabel();
                    } else {
                        Toast.makeText(view.getContext(), "Start date cannot be after end date!", Toast.LENGTH_LONG).show();
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        };

    }
    private void updateStartLabel(){
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editStartDate.setText(sdf.format(myCalender.getTime()));
    }

    private void updateEndLabel(){
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editEndDate.setText(sdf.format(myCalender.getTime()));
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacationdetails, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()== android.R.id.home){
            this.finish();
            return true;}
        if(item.getItemId()==R.id.vacationSave){
            Vacation vacation;
            if(vacationID==-1){
                if(repository.getAllVacations().size() == 0) {

                    vacationID = 1;
                }
                else {
                    vacationID = repository.getAllVacations().get(repository.getAllVacations().size() - 1).getVacationID() + 1;
                }
                vacation = new Vacation(vacationID, editName.getText().toString(), editHotel.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
                repository.insert(vacation);
                this.finish();
            }
            else{
                vacation = new Vacation(vacationID, editName.getText().toString(), editHotel.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
                repository.insert(vacation);
                repository.update(vacation);

            }
        }
        if(item.getItemId()==R.id.vacationDelete){

            for (Vacation vacation : repository.getAllVacations()) {
//                System.out.println(vacation.getVacationID());
//                System.out.println(vacationID);
                if (vacation.getVacationID() == vacationID) {
                    currentVacation = vacation;
                }
            }

            numExcursions = 0;
            for (Excursion excursion : repository.getAllExcursions()) {
                if (excursion.getVacationID() == vacationID) {
                    ++numExcursions;
                }
            }

            if (numExcursions == 0) {
                repository.delete(currentVacation);
                Toast.makeText(VacationDetails.this, currentVacation.getVacationName() + " was deleted", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(VacationDetails.this, "Can't delete a product with parts", Toast.LENGTH_LONG).show();
            }
            return true;

        }

        if(item.getItemId()== R.id.vacationNotify) {
            String dateFromScreen = editStartDate.getText().toString();
            String myFormat = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try{
                Long trigger = myDate.getTime();
                Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
                intent.putExtra("key", "vacation starting");
                PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);}
            catch (Exception e){
                throw new RuntimeException(e);
            }
            return true;
        }

        return true;
    }

    @Override
    protected void onResume() {

        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.excursionRecyclerView);
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredParts = new ArrayList<>();
        for (Excursion e : repository.getAllExcursions()) {
            if (e.getVacationID() == vacationID) filteredParts.add(e);
        }
        excursionAdapter.setExcursions(filteredParts);

    }


}