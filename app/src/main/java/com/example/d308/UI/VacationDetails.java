package com.example.d308.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.util.ArrayList;
import java.util.List;

public class VacationDetails extends AppCompatActivity {


    String name;
    double price;
    int vacationID;
    EditText editName;
    EditText editPrice;
    Repository repository;
    Vacation currentVacation;
    int numExcursions;
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
        editPrice=findViewById(R.id.pricetext);
        name=getIntent().getStringExtra("name");
        price=getIntent().getDoubleExtra("price",0.0);
        editName.setText(name);
        editPrice.setText(Double.toString(price));
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
                vacation = new Vacation(vacationID, editName.getText().toString(), Double.parseDouble(editPrice.getText().toString()));
                repository.insert(vacation);
                this.finish();
            }
            else{
                    vacation = new Vacation(vacationID, editName.getText().toString(), Double.parseDouble(editPrice.getText().toString()));
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