package com.example.wear.data;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface TownService {
    @GET("City2022.json")
    Call<List<Town>> getTowns();
}
