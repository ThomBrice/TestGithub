/*
 * Created by Thomas Brice
 * Copyright (c) 2017.
 */

package brice.testgithub.Model;


import com.google.gson.annotations.SerializedName;

public class Repository {
    private int id;
    private String name;

    @SerializedName("private")
    private boolean confidential;

    public Repository (){
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isConfidential() {
        return confidential;
    }
}
