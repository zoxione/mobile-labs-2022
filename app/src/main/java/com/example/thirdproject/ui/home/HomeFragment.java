package com.example.thirdproject.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thirdproject.App;
import com.example.thirdproject.R;

import com.example.thirdproject.data.Town;
import com.example.thirdproject.database.AppDatabase;
import com.example.thirdproject.database.DbTown;
import com.example.thirdproject.database.DbTownDao;
import com.example.thirdproject.databinding.FragmentHomeBinding;
import com.example.thirdproject.data.TownService;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    private TextView textViewAlert;

    // Объявление RecyclerView
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Town> mTownsData = new ArrayList<>();
    private List<DbTown> mRecyclerData = new ArrayList<>();
    private RecyclerView.Adapter mAdapter = new HomeTownsRecyclerViewAdapter(mRecyclerData);

    // Объявление и инициализация базы данных
    private AppDatabase dbRoom = App.getInstance().getDatabase();
    private DbTownDao dbTownDao = dbRoom.dbTownDao();
    List<DbTown> dbTownsList = new ArrayList<>();
    //List<DbTown> dbTownsList = dbTownDao.getTowns();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        View root = binding.getRoot();

        // Инициализация textViewAlert
        textViewAlert = view.findViewById(R.id.textViewAlert);
        textViewAlert.setText("Загрузка данных...");

        // Создание RecyclerView
        mRecyclerView = root.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        //--------------- Получение данных с Api ---------------
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://raw.githubusercontent.com/Lpirskaya/JsonLab/master/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                TownService service = retrofit.create(TownService.class);
                Call<List<Town>> getTownsRequest = service.getTowns();

                // Выполение веб-запроса
                // .execute() - синхронный
                // .enqueue() - асинхронный
                getTownsRequest.enqueue(new Callback<List<Town>>() {
                    @Override
                    public void onResponse(Call<List<Town>> call, Response<List<Town>> response) {
                        if (response.isSuccessful()) {
                            // Обработка ответа
                            Log.i("[onResponse] call = ", call.toString());
                            Log.i("[onResponse] response = ", response.toString());
                            textViewAlert.setText("");

                            mTownsData = response.body();

                            // Заполнение БД
                            if (dbTownsList.size() == 0) {
                                for (Town townData : mTownsData) {
                                    DbTown town = new DbTown();
                                    town.country = townData.country;
                                    town.population = townData.population;
                                    town.language = townData.language;
                                    town.name = townData.name;
                                    town.square = townData.square;
                                    dbTownDao.insertTown(town);
                                }
                            }
                            else {
                                for (int i = 0; i < mTownsData.size(); i++) {
                                    for (int j = 0; j < dbTownsList.size(); j++) {
                                        if (dbTownsList.get(j).name.equals(mTownsData.get(i).name)) {
                                            break;
                                        }
                                        if (j == dbTownsList.size() - 1) {
                                            DbTown town = new DbTown();
                                            town.country = mTownsData.get(i).country;
                                            town.population = mTownsData.get(i).population;
                                            town.language = mTownsData.get(i).language;
                                            town.name = mTownsData.get(i).name;
                                            town.square = mTownsData.get(i).square;
                                            dbTownDao.insertTown(town);
                                        }
                                    }
                                }
                            }

                            dbTownsList = dbTownDao.getTowns();
                            for (DbTown town : dbTownsList) {
                                Log.i("DbTowns", town.name + " " + town.country + " " + town.population + " " + town.language + " " + town.square);
                            }

                            // Добавление объектов в RecyclerView
                            if (dbTownsList.size() != 0) {
                                mRecyclerData.clear();
                                mRecyclerData.addAll(dbTownsList);
                                mAdapter = new HomeTownsRecyclerViewAdapter(mRecyclerData);
                                mAdapter.notifyDataSetChanged();
                                mRecyclerView.setAdapter(mAdapter);
                            }
                            else {
                                Toast.makeText(getContext(), "Нет данных!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            ResponseBody errorBody = response.errorBody();
                            Toast.makeText(getContext(), "Ошибка получения данных", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Town>> call, Throwable t) {
                        // Обработка ошибок
                        Log.i("[onFailure] t = ", t.toString());
                        Toast.makeText(getContext(), "Ошибка получения данных!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        thread.start();
        //--------------- Получение данных с Api ---------------


        //Toast.makeText(getContext(), "Загрузка данных...", Toast.LENGTH_SHORT).show();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}