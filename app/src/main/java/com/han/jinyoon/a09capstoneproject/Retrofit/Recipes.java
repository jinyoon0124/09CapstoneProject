package com.han.jinyoon.a09capstoneproject.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 *POJO for RecipeBody retrofit
 */

public class Recipes implements Serializable {

    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("source_url")
    @Expose
    private String sourceUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }


}