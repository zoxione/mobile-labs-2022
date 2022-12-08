package com.example.thirdproject.data;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

import com.example.thirdproject.data.Town;

public interface TownService {
    @GET("City2022.json")
    Call<List<Town>> getTowns();
}
