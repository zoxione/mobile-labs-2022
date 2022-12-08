package com.example.thirdproject.ui.saves;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thirdproject.App;
import com.example.thirdproject.R;
import com.example.thirdproject.data.Session;
import com.example.thirdproject.database.AppDatabase;
import com.example.thirdproject.database.DbSaveTown;
import com.example.thirdproject.database.DbTown;
import com.example.thirdproject.database.DbSaveTownDao;
import com.example.thirdproject.database.DbTownDao;
import com.example.thirdproject.databinding.FragmentSavesBinding;

import java.util.ArrayList;
import java.util.List;

public class SavesFragment extends Fragment {
    private FragmentSavesBinding binding;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<DbTown> mRecyclerData = new ArrayList<>();
    private RecyclerView.Adapter mAdapter = new SaveTownsRecyclerViewAdapter(mRecyclerData);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSavesBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.fragment_saves, container, false);
        View root = binding.getRoot();

        // Создание RecyclerView
        mRecyclerView = root.findViewById(R.id.saves_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        // Получение данных из БД
        Session session = Session.getInstance();
        AppDatabase dbRoom = App.getInstance().getDatabase();
        DbSaveTownDao dbSaveTownDao = dbRoom.dbSaveTownDao();

        List<DbSaveTown> dbSaveTowns = dbSaveTownDao.getSaveTownsByUserId(session.getId());
        DbTownDao dbTownDao = dbRoom.dbTownDao();
        for (DbSaveTown dbSaveTown : dbSaveTowns) {
            DbTown dbTown = dbTownDao.getTownById(dbSaveTown.town_id);
            mRecyclerData.add(dbTown);
        }
        mAdapter = new SaveTownsRecyclerViewAdapter(mRecyclerData);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}