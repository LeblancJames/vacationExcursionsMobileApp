package com.example.d308.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308.R;
import com.example.d308.entities.Vacation;

import java.util.ArrayList;
import java.util.List;


public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> implements Filterable {


    public class VacationViewHolder extends RecyclerView.ViewHolder{
        private final TextView vacationItemView;

        public VacationViewHolder(View itemView) {
            super(itemView);
            vacationItemView = itemView.findViewById(R.id.textView2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    final Vacation current=searchingVacations.get(position);
                    Intent intent=new Intent(context,VacationDetails.class);
                    intent.putExtra("id",current.getVacationID());
                    intent.putExtra("name",current.getVacationName());
                    intent.putExtra("hotel",current.getHotel());
                    intent.putExtra("start date",current.getStartDate());
                    intent.putExtra("end date",current.getEndDate());
                    context.startActivity(intent);


                }
            });
        }
    }

    private List<Vacation> searchingVacations;
    private List<Vacation> mVacations; //full list

    private final Context context;

    private final LayoutInflater mInflater;

    public VacationAdapter(Context context){
        mInflater= LayoutInflater.from(context);
        this.context=context;
    }

    @NonNull
    @Override
    public VacationAdapter.VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=mInflater.inflate(R.layout.vacation_list_item,parent,false);
        return new VacationViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull VacationAdapter.VacationViewHolder holder, int position) {
        if(searchingVacations!=null){
            Vacation current =searchingVacations.get(position);
            String name=current.getVacationName();
            holder.vacationItemView.setText(name);
        }
        else{
            holder.vacationItemView.setText("No vacation name");
        }

    }

    @Override
    public int getItemCount() {
        if (searchingVacations!=null){
            return searchingVacations.size();
        }
        else{
            return 0;
        }
    }


    public void setVacations(List<Vacation> vacations) {
        this.mVacations = new ArrayList<>(vacations);
        this.searchingVacations = new ArrayList<>(vacations);
        notifyDataSetChanged();
    }


    @Override
    public Filter getFilter() {
        return vacationFilter;
    }

    private final Filter vacationFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Vacation> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mVacations);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Vacation vacation : mVacations) {
                    if (vacation.getVacationName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(vacation);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            searchingVacations.clear();
            searchingVacations.addAll((List<Vacation>) results.values);
            notifyDataSetChanged();
        }
    };


}
