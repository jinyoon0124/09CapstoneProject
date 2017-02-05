package com.han.jinyoon.a09capstoneproject.Retrofit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jin Yoon on 12/7/2016.
 */

public class RecipeBody {

    private List<Recipes> recipes = new ArrayList<>();

    public List<Recipes> getRecipes(){
        return recipes;
    }

    public void setRecipes(List<Recipes> recipeDetails){
        this.recipes = recipeDetails;
    }

    @Override
    public String toString() {
        return "RecipeBody{" +
                "recipes=" + recipes +
                '}';
    }
}
