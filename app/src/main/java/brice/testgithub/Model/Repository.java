/*
 * Created by Thomas Brice
 * Copyright (c) 2017.
 */

package brice.testgithub.Model;


import com.google.gson.annotations.SerializedName;

public class Repository {

    public Repository(String name) {
        this.name = name;
    }

    @SerializedName("id")

    private int id;

    @SerializedName("name")
    private String name;

    private String full_name;

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

    public String getFull_name() {
        return full_name;
    }

    public boolean isConfidential() {
        return confidential;
    }
}
