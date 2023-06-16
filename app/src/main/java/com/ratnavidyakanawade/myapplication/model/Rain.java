package com.ratnavidyakanawade.myapplication.model;

import com.google.gson.annotations.SerializedName;

class Rain {
    @SerializedName("3h")
    public float h3;

    public float getH3() {
        return h3;
    }

    public void setH3(float h3) {
        this.h3 = h3;
    }
}

