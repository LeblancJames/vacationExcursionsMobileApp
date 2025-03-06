package com.example.d308.UI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d308.R;
import com.example.d308.database.Repository;
import com.example.d308.entities.Excursion;
import com.example.d308.entities.Vacation;

public class ExcursionDetails extends AppCompatActivity {
    int excursionID;
    Repository repository;
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
        repository = new Repository(getApplication());
        excursionID=getIntent().getIntExtra("id",-1);
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

//        if(item.getItemId()==R.id.excursionSave){
//            Excursion excursion;
//            if(excursionID==-1){
//                if(repository.getAllVacations().size() == 0) {
//
//                    excursionID = 1;
//                }
//                else {
//                    excursionID = repository.getAllExcursions().get(repository.getAllExcursions().size() - 1).getExcursionID() + 1;
//                }
//                excursion = new Excursion(excursionID, editName.getText().toString(), editHotel.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
//                repository.insert(vacation);
//                this.finish();
//            }
//            else{
//                vacation = new Vacation(vacationID, editName.getText().toString(), editHotel.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
//                repository.insert(vacation);
//                repository.update(vacation);
//
//            }
//        }
        return true;
    };
}