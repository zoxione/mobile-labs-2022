package com.example.thirdproject.ui.saves;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thirdproject.App;
import com.example.thirdproject.R;
import com.example.thirdproject.database.AppDatabase;
import com.example.thirdproject.database.DbTown;
import com.example.thirdproject.database.DbTownDao;

import java.util.List;

public class SaveTownsRecyclerViewAdapter extends RecyclerView.Adapter<SaveTownsRecyclerViewAdapter.MyViewHolder> {
    private static List<DbTown> mDataset; // Список городов для RecyclerView
    private Context context; // Контекст

    // Внутренний класс, предназначенный для хранения информации о View-элементах списка
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewName;
        public TextView mTextViewCountry;
        public TextView mTextViewPopulation;
        public MyViewHolder(View v) {
            super(v);
            mTextViewName = v.findViewById(R.id.textViewName);
            mTextViewCountry = v.findViewById(R.id.textViewCountry);
            mTextViewPopulation = v.findViewById(R.id.textViewPopulation);

            // Обработчик нажатия на элемент списка
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle myBundle = new Bundle();

                    AppDatabase dbRoom = App.getInstance().getDatabase();
                    DbTownDao dbTownDao = dbRoom.dbTownDao();
                    DbTown selectedTown = dbTownDao.getTownByName(mTextViewName.getText().toString());

                    myBundle.putString("town_name", selectedTown.name);
                    myBundle.putString("town_country", selectedTown.country);
                    myBundle.putInt("town_population", selectedTown.population);
                    myBundle.putString("town_language", selectedTown.language);
                    myBundle.putInt("town_square", selectedTown.square);

                    Navigation.findNavController(view).navigate(R.id.nav_town, myBundle, new NavOptions.Builder()
                            .setPopUpTo(R.id.nav_saves, true)
                            .build()
                    );
                }
            });
        }
    }

    public SaveTownsRecyclerViewAdapter(List<DbTown> myDataset) { mDataset = myDataset; }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        context = parent.getContext();
        return  vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTextViewName.setText(mDataset.get(position).name);
        holder.mTextViewCountry.setText(mDataset.get(position).country);
        holder.mTextViewPopulation.setText(mDataset.get(position).population.toString());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
