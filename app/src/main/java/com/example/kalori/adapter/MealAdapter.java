package com.example.kalori.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kalori.R;
import com.example.kalori.realm.addMealTable;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.myViewHolder> {

    List<addMealTable> mealTables;
    LayoutInflater inflater;
    Realm realm =Realm.getDefaultInstance();


    public MealAdapter(List<addMealTable> mealTables, Context context) {
        this.mealTables = mealTables;
        inflater =LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.meal, parent, false);
        myViewHolder holder = new myViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        addMealTable selectedMeal = mealTables.get(position);
        holder.setData(selectedMeal,position);

    }

    @Override
    public int getItemCount() {
        return mealTables.size();

    }

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textCaloriDetail,textMealNameDetail,textMealMakro;
        ImageView deleteimage;
        public myViewHolder(View view) {
            super(view);
            textMealNameDetail = (TextView)  view.findViewById(R.id.listMealName);
            textMealMakro = (TextView) view.findViewById(R.id.listMakroDetail);
            textCaloriDetail = (TextView) view.findViewById(R.id.listCalorieDetail);
            deleteimage = (ImageView) view.findViewById(R.id.deleteproduct);
            deleteimage.setOnClickListener(this);
        }

        public void setData(addMealTable selectMeal,int position){
            String makro=selectMeal.getMealAmount()+"gram\n"+
                    selectMeal.getCarbonhydrat()+"g karbonhidrat\n"+
                         selectMeal.getProtein()+"g protein\n"+
                         selectMeal.getFat()+"g yağ\n";
            this.textMealNameDetail.setText(selectMeal.getMealName());
            this.textCaloriDetail.setText(selectMeal.getCalorie()+" kcal");
            this.textMealMakro.setText(makro);




        }
        @Override
        public void onClick(View v){
            if(v==deleteimage){
                deleteimage(getLayoutPosition());
            }
        }

        private void deleteimage(int position){
            RealmResults<addMealTable> mealTablesRealm = realm.where(addMealTable.class).findAll();
            realm.beginTransaction();
            mealTablesRealm.deleteFromRealm(position);
            realm.commitTransaction();
            mealTables.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,mealTables.size());
        }
    }
}
