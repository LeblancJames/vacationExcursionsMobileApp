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

import com.example.d308.R;
import com.example.d308.database.Repository;
import com.example.d308.entities.Excursion;
import com.example.d308.entities.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {
    String excursionName;
    String excursionDate;
    EditText editExcursionTitleText;
    EditText editExcursionDateText;
    int excursionID;
    int vacationID;
    Repository repository;

    DatePickerDialog.OnDateSetListener myStartDate;
    final Calendar myCalender=Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursion_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        excursionName = getIntent().getStringExtra("name");
        excursionDate=getIntent().getStringExtra("excursion date");
        repository = new Repository(getApplication());
        excursionID=getIntent().getIntExtra("excursionID",-1);
        vacationID=getIntent().getIntExtra("id",-1);
        editExcursionTitleText = findViewById(R.id.excursionTitleText);
        editExcursionDateText = findViewById(R.id.excursionDateText);
        editExcursionTitleText.setText(excursionName);
        editExcursionDateText.setText(excursionDate);


        String myFormat =  "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editExcursionDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date;
                String info=editExcursionDateText.getText().toString();
                try{
                    myCalender.setTime(sdf.parse(info));
                } catch (ParseException e){
                    e.printStackTrace();
                }
                new DatePickerDialog(ExcursionDetails.this,myStartDate,myCalender.get(Calendar.YEAR),
                        myCalender.get(Calendar.MONTH), myCalender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        myStartDate=new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                try {
//                    String excursionDate = editExcursionDateText.getText().toString();
                    Vacation currentVacation = null;
                    for (Vacation vacation : repository.getAllVacations()) {
                        if (vacation.getVacationID() == vacationID) {
                            currentVacation = vacation;
                        }
                    }
                    System.out.println(repository.getAllVacations());
                    String startDateInfo = currentVacation.getStartDate();
                    String endDateInfo = currentVacation.getEndDate();


                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(Calendar.YEAR, year);
                    selectedDate.set(Calendar.MONTH, month);
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String excursionDate = sdf.format(selectedDate.getTime());



                    if (sdf.parse(startDateInfo).before(sdf.parse(excursionDate)) && sdf.parse(excursionDate).before(sdf.parse(endDateInfo))) {
                        myCalender.set(Calendar.YEAR, year);
                        myCalender.set(Calendar.MONTH, month);
                        myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateStartLabel();
                    } else {
                        Toast.makeText(view.getContext(), "Excursion date cannot be outside of vacation dates!", Toast.LENGTH_LONG).show();
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

        editExcursionDateText.setText(sdf.format(myCalender.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursiondetails, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        if(item.getItemId()==R.id.excursionSave){
            Excursion excursion;
            if(excursionID==-1){
                if(repository.getAllExcursions().size() == 0) {

                    excursionID = 1;
                }
                else {
                    excursionID = repository.getAllExcursions().get(repository.getAllExcursions().size() - 1).getExcursionID() + 1;
                }
                excursion = new Excursion(excursionID, editExcursionTitleText.getText().toString(), editExcursionDateText.getText().toString(), vacationID);
                repository.insert(excursion);
                this.finish();
            }
            else{
                System.out.println(excursionID);
                System.out.println(vacationID);
                excursion = new Excursion(excursionID, editExcursionTitleText.getText().toString(), editExcursionDateText.getText().toString(), vacationID);
                repository.insert(excursion);
                repository.update(excursion);

            }
        }

        if(item.getItemId()==R.id.excursionDelete){

            Excursion currentExcursion = null;
            for (Excursion excursion : repository.getAllExcursions()) {
                if (excursion.getExcursionID() == excursionID) {
                    currentExcursion = excursion;
                }
            }

            repository.delete(currentExcursion);
            Toast.makeText(ExcursionDetails.this, currentExcursion.getExcursionName() + " was deleted", Toast.LENGTH_LONG).show();
            finish();


        }

        if(item.getItemId()== R.id.excursionNotify) {

            String excursionTitle = editExcursionTitleText.getText().toString();

            String myFormat = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(excursionDate);
//                System.out.println(myDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            try{
                Long trigger = myDate.getTime();
                Intent intent = new Intent(ExcursionDetails.this, MyReceiver2.class);
                intent.putExtra("title", excursionTitle);

                PendingIntent sender = PendingIntent.getBroadcast(ExcursionDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);


            }
            catch (Exception e){
                throw new RuntimeException(e);
            }
            return true;
        }

        return true;
    };
}