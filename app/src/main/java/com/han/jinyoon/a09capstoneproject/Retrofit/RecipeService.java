package com.han.jinyoon.a09capstoneproject.Retrofit;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Jin Yoon on 12/7/2016.
 */

public class RecipeService {

    public RecipeService(){
        super();
    }

    public interface RecipeApi{
        @GET("/api/search")
        Call<RecipeBody> getRecipe(
            @QueryMap Map<String, String> options
        );

    }
}
