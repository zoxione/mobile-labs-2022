package com.example.thirdproject.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thirdproject.App;
import com.example.thirdproject.R;
import com.example.thirdproject.data.Session;
import com.example.thirdproject.database.AppDatabase;
import com.example.thirdproject.database.DbSaveTown;
import com.example.thirdproject.database.DbTown;
import com.example.thirdproject.database.DbSaveTownDao;
import com.example.thirdproject.database.DbTownDao;

import java.util.List;

public class HomeTownsRecyclerViewAdapter extends RecyclerView.Adapter<HomeTownsRecyclerViewAdapter.MyViewHolder> {
    private static List<DbTown> mDataset;   // Список городов для RecyclerView
    private Context context;                // Контекст

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
                            .setPopUpTo(R.id.nav_home, true)
                            .build()
                    );
                }
            });

            // Обработчик долгого нажатия на элемент списка
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.i("MyRecyclerViewAdapter", "onLongClick");

                    // Создание диалогового окна
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(true)
                            .setIcon(R.drawable.ic_baseline_home_24)
                            .setMessage("Вы действительно хотите сохранить город " + mTextViewName.getText() + "?")
                            .setTitle("Добавление города")
                            .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i("AlertDialog", "Да");

                                    Session session = Session.getInstance();

                                    AppDatabase dbRoom = App.getInstance().getDatabase();
                                    DbTownDao dbTownDao = dbRoom.dbTownDao();
                                    DbSaveTownDao dbSaveTownDao = dbRoom.dbSaveTownDao();

                                    //saveTownDao.deleteSaveTowns();

                                    DbTown selectedTown = dbTownDao.getTownByName(mTextViewName.getText().toString());
                                    List<DbSaveTown> userDbSaveTowns = dbSaveTownDao.getSaveTownsByUserId(session.getId());

                                    for (int i = 0; i < userDbSaveTowns.size(); i++) {
                                        if (userDbSaveTowns.get(i).town_id == selectedTown.id) {
                                            Toast.makeText(context, "Город уже сохранен", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }

                                    DbSaveTown dbSaveTown = new DbSaveTown();
                                    dbSaveTown.town_id = selectedTown.id;
                                    dbSaveTown.user_id = session.getId();
                                    dbSaveTownDao.insertSaveTown(dbSaveTown);

                                    Toast.makeText(context, "Город добавлен в сохраненные", Toast.LENGTH_SHORT).show();

                                    List<DbSaveTown> st = dbSaveTownDao.getSaveTowns();
                                    for (int i = 0; i < st.size(); i++) {
                                        Log.i("SaveTown", st.get(i).id + " " + st.get(i).town_id + " " + st.get(i).user_id);
                                    }
                                }
                            })
                            .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i("AlertDialog", "Нет");
                                }
                            });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    return true;
                }
            });
        }
    }

    public HomeTownsRecyclerViewAdapter(List<DbTown> myDataset) { mDataset = myDataset; }

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
