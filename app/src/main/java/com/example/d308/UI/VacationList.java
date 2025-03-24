package com.example.d308.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
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
import com.google.type.Date;
import com.google.type.DateTime;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class VacationList extends AppCompatActivity {
private Repository repository;
private VacationAdapter vacationAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        repository=new Repository(getApplication());
        List<Vacation> allVacations=repository.getAllVacations();
        RecyclerView recyclerView=findViewById(R.id.recyclerview);

        vacationAdapter=new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);

        FloatingActionButton fab=findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent=new Intent(VacationList.this, VacationDetails.class);
                startActivity(intent);
            }
        });


        SearchView searchView = findViewById(R.id.search_text);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                vacationAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                vacationAdapter.getFilter().filter(newText);
                return false;
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }


    @Override
    protected void onResume() {

        super.onResume();
        List<Vacation> allVacations = repository.getAllVacations();
//        RecyclerView recyclerView = findViewById(R.id.recyclerview);
//        final VacationAdapter vacationAdapter = new VacationAdapter(this);
//        recyclerView.setAdapter(vacationAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.sample){
            repository=new Repository(getApplication());
            //Toast.makeText(VacationList.this, "put in sample data", Toast.LENGTH_LONG).show();
            Vacation vacation = new Vacation(0, "bahamas", "Mariott", "3/4/25", "4/4/25");
            repository.insert(vacation);
            vacation = new Vacation(0, "italy", "Hilton", "4/4/25", "5/4/25");
            repository.insert(vacation);
            Excursion excursion= new Excursion(0, "pizza tour", "4/7/25", 1);
            repository.insert(excursion);
            excursion=new Excursion(0, "bike ride", "5/1/25", 1);
            repository.insert(excursion);
            return true;
        }
        if(item.getItemId()==android.R.id.home){
            this.finish();//this tells backarrow to go back to previous screen, you can override with Intent
            return true;
        }
        if(item.getItemId()==R.id.createReport){
            repository=new Repository(getApplication());
            List<Vacation> allVacations=repository.getAllVacations();
            LogReport(allVacations);
            return true;
        }
        return true;
    }



    public void LogReport(List<Vacation> vacationsList){

        Log.d("VacationLog", "------ Vacation List Report ------");
        LocalDateTime now = LocalDateTime.now();
        String formatted = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Log.d("VacationLog", "Current time: " + formatted);
        for (Vacation vacation : vacationsList) {
            String logEntry = "ID: " + vacation.getVacationID() +
                    ", Name: " + vacation.getVacationName() +
                    ", Hotel: " + vacation.getHotel() +
                    ", Start: " + vacation.getStartDate() +
                    ", End: " + vacation.getEndDate();
            Log.d("VacationLog", logEntry);
        }
        Log.d("VacationLog", "------ End of Report ------");

    }

}