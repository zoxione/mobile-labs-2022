package com.example.thirdproject.ui.town;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thirdproject.App;
import com.example.thirdproject.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.thirdproject.database.AppDatabase;
import com.example.thirdproject.database.DbTown;
import com.example.thirdproject.database.DbTownDao;
import com.example.thirdproject.database.DbUserDao;
import com.example.thirdproject.databinding.FragmentTownBinding;

public class TownFragment extends Fragment {
    private FragmentTownBinding binding;
    private String townName;
    private String townCountry;
    private Integer townPopulation;
    private String townLanguage;
    private Integer townSquare;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        townName = getArguments().getString("town_name");
        townCountry = getArguments().getString("town_country");
        townPopulation = getArguments().getInt("town_population");
        townLanguage = getArguments().getString("town_language");
        townSquare = getArguments().getInt("town_square");

        if (townName.equals("nan")) {
            if (savedInstanceState != null) {
                townName = savedInstanceState.getString("town_name");
                townCountry = savedInstanceState.getString("town_country");
                townPopulation = savedInstanceState.getInt("town_population");
                townLanguage = savedInstanceState.getString("town_language");
                townSquare = savedInstanceState.getInt("town_square");
            } else {
                AppDatabase dbRoom = App.getInstance().getDatabase();
                DbTownDao dbTownDao = dbRoom.dbTownDao();
                DbTown dbTown = dbTownDao.getTownById(1);
                townName = dbTown.name;
                townCountry = dbTown.country;
                townPopulation = dbTown.population;
                townLanguage = dbTown.language;
                townSquare = dbTown.square;
            }
        }

        Bundle bundle = new Bundle();
        bundle.putString("town_name", townName);
        bundle.putString("town_country", townCountry);
        bundle.putInt("town_population", townPopulation);
        bundle.putString("town_language", townLanguage);
        bundle.putInt("town_square", townSquare);
        savedInstanceState = bundle;

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TownViewModel townViewModel = new ViewModelProvider(this).get(TownViewModel.class);

        binding = FragmentTownBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView textViewName = (TextView) getView().findViewById(R.id.textViewTownName);
        TextView textViewCountry = (TextView) getView().findViewById(R.id.textViewTownCountry);
        TextView textViewPopulation = (TextView) getView().findViewById(R.id.textViewTownPopulation);
        TextView textViewLanguage = (TextView) getView().findViewById(R.id.textViewTownLanguage);
        TextView textViewSquare = (TextView) getView().findViewById(R.id.textViewTownSquare);

        textViewName.setText(townName);
        textViewCountry.setText(townCountry);
        textViewPopulation.setText(townPopulation.toString());
        textViewLanguage.setText(townLanguage);
        textViewSquare.setText(townSquare.toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}