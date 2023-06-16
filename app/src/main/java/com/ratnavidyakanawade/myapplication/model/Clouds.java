package com.ratnavidyakanawade.myapplication.model;

import com.google.gson.annotations.SerializedName;

class Clouds {
    @SerializedName("all")
    public float all;

    public float getAll() {
        return all;
    }

    public void setAll(float all) {
        this.all = all;
    }
}

